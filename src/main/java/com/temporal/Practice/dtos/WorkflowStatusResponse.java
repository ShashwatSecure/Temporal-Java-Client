package com.temporal.Practice.dtos;

public record WorkflowStatusResponse(
        String startTime,
        String status
) {
}