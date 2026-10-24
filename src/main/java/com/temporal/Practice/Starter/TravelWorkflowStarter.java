package com.temporal.Practice.Starter;

import org.springframework.stereotype.Component;

import com.temporal.Practice.Workflow.TravelWorkflow;
import com.temporal.Practice.dtos.TravelRequest;
import com.temporal.Practice.dtos.WorkflowStatusResponse;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.client.WorkflowStub;
import io.temporal.api.common.v1.WorkflowExecution;
import io.temporal.api.workflowservice.v1.DescribeWorkflowExecutionRequest;
import io.temporal.serviceclient.WorkflowServiceStubs;

@Component 
public class TravelWorkflowStarter {
    
    private WorkflowServiceStubs workflowServiceStubs;

    public TravelWorkflowStarter(WorkflowServiceStubs workflowServiceStubs)
    {
        this.workflowServiceStubs = workflowServiceStubs;
    }

    public void startWorkflow(TravelRequest travelRequest)
    {
        WorkflowClient workflowClient = WorkflowClient.newInstance(workflowServiceStubs);

        TravelWorkflow travelWorkflow = workflowClient.newWorkflowStub(TravelWorkflow.class,
                                                                        WorkflowOptions.newBuilder()
                                                                            .setTaskQueue("TRAVEL_TASK_QUEUE")
                                                                            .setWorkflowId("travel_"+travelRequest.getUserId())
                                                                            .build()
        );

        WorkflowClient.start(travelWorkflow::bookTrip,travelRequest);
    }

    public String startWorkflowAndWait(TravelRequest travelRequest)
    {
        WorkflowClient workflowClient = WorkflowClient.newInstance(workflowServiceStubs);

        TravelWorkflow travelWorkflow = workflowClient.newWorkflowStub(
            TravelWorkflow.class,
            WorkflowOptions.newBuilder()
                .setTaskQueue("TRAVEL_TASK_QUEUE")
                .setWorkflowId("travel_" + travelRequest.getUserId())
                .build()
        );

        WorkflowExecution execution = WorkflowClient.start(
            travelWorkflow::bookTrip,
            travelRequest
        );

        // This is suitable for a short workflow: the gRPC request waits for completion.
        WorkflowStub.fromTyped(travelWorkflow).getResult(Void.class);

        return "Workflow completed: " + execution.getWorkflowId();
    }

    public WorkflowStatusResponse getWorkflowStatus(Long userId)
    {
        WorkflowClient workflowClient = WorkflowClient.newInstance(workflowServiceStubs);
        WorkflowExecution execution = WorkflowExecution.newBuilder()
            .setWorkflowId("travel_" + userId)
            .build();

        // This asks Temporal for the current status and returns immediately.
        var workflowInfo = workflowClient.getWorkflowServiceStubs().blockingStub()
            .describeWorkflowExecution(
                DescribeWorkflowExecutionRequest.newBuilder()
                    .setNamespace("default")
                    .setExecution(execution)
                    .build()
            )
            .getWorkflowExecutionInfo();

        return new WorkflowStatusResponse(
                workflowInfo.getStartTime().toString(),
                workflowInfo.getStatus().name()
        );
    }

    public void setHotelApproval(Long userId, boolean approved)
    {
        WorkflowClient workflowClient = WorkflowClient.newInstance(workflowServiceStubs);
        TravelWorkflow travelWorkflow = workflowClient.newWorkflowStub(
                TravelWorkflow.class,
                "travel_" + userId
        );

        travelWorkflow.setHotelApproval(approved);
    }
}
