package com.temporal.Practice.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.temporal.Practice.Activity.TravelActivityImpl;
import com.temporal.Practice.Workflow.TravelWorkflowImpl;

import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;

@Configuration 
public class TemporalConfig {
    
    @Bean
    public WorkerFactory workerFactory(WorkflowServiceStubs serviceStubs) {
        WorkflowClient workflowClient = WorkflowClient.newInstance(serviceStubs);
        WorkerFactory workerFactory = WorkerFactory.newInstance(workflowClient);

        Worker worker = workerFactory.newWorker("TRAVEL_TASK_QUEUE");

        worker.registerWorkflowImplementationTypes(TravelWorkflowImpl.class);
        worker.registerActivitiesImplementations(new TravelActivityImpl());

        workerFactory.start();

        return workerFactory;
    }
}
