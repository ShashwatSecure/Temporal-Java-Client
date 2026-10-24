package com.temporal.Practice.Workflow;

import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.failure.ApplicationFailure;
import io.temporal.failure.ActivityFailure;
import io.temporal.workflow.Workflow;
import java.time.Duration;
import io.temporal.spring.boot.WorkflowImpl;
import lombok.extern.slf4j.Slf4j;
import com.temporal.Practice.dtos.TravelRequest;
import com.temporal.Practice.Activity.TravelActivity;

@WorkflowImpl 
@Slf4j 
public class TravelWorkflowImpl implements TravelWorkflow {

    private boolean hotelApprovalReceived;
    private boolean hotelApproved;
    
    private final TravelActivity travelActivity = Workflow.newActivityStub(
            TravelActivity.class,
            ActivityOptions.newBuilder()
                    .setStartToCloseTimeout(Duration.ofMinutes(1))
                    .setRetryOptions(
                        RetryOptions.newBuilder()
                            .setInitialInterval(Duration.ofSeconds(30))
                            .setBackoffCoefficient(1.0)
                            .setMaximumAttempts(3)
                            .build()   
                    )
                    .build()
        );

    @Override 
    public void bookTrip(TravelRequest travelRequest)
    {
        log.info("Travel Request registered for {} to {} on {}",travelRequest.getUserId(), travelRequest.getPlace(), travelRequest.getDate());
        travelActivity.bookFlight(travelRequest);

        boolean approvalReceived = Workflow.await(
            Duration.ofMinutes(2),
            () -> hotelApprovalReceived
        );

        if (!approvalReceived || !hotelApproved) {
            throw ApplicationFailure.newFailure(
                "Hotel booking was not approved within two minutes",
                "HOTEL_APPROVAL_REQUIRED"
            );
        }

        try{
            travelActivity.bookHotel(travelRequest);
        } catch(ActivityFailure exception)
        {
            log.error("Hotel booking failed after retries",exception);
        }
        travelActivity.bookCar(travelRequest);

        log.info("Travel Request completed for {} to {} on {}",travelRequest.getUserId(),travelRequest.getPlace(), travelRequest.getDate());
    }

    @Override
    public void setHotelApproval(boolean approved) {
        hotelApprovalReceived = true;
        hotelApproved = approved;
    }
}
