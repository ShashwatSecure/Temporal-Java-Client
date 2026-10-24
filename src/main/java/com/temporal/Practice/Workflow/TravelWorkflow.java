package com.temporal.Practice.Workflow;

import com.temporal.Practice.dtos.TravelRequest;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;
import io.temporal.workflow.SignalMethod;

@WorkflowInterface 
public interface TravelWorkflow {

    @WorkflowMethod 
    public void bookTrip(TravelRequest travelRequest);

    @SignalMethod
    void setHotelApproval(boolean approved);
    
} 
