package com.temporal.Practice.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import com.temporal.Practice.Starter.TravelWorkflowStarter;
import com.temporal.Practice.dtos.TravelRequest;
import com.temporal.Practice.dtos.WorkflowStatusResponse;

@RestController
@RequestMapping("/api/travel-workflows")
public class TravelWorkflowController {

    private final TravelWorkflowStarter travelWorkflowStarter;

    public TravelWorkflowController(TravelWorkflowStarter travelWorkflowStarter)
    {
        this.travelWorkflowStarter = travelWorkflowStarter;
    }

	@GetMapping
	public String getTravelWorkflows() {
		return "Travel workflows endpoint is running.";
	}

    @PostMapping("/book") 
    public ResponseEntity<String> runWorkflow(@RequestBody TravelRequest travelRequest)
    {
        travelWorkflowStarter.startWorkflow(travelRequest);

        return ResponseEntity.ok("Workflow started");
    }

    @PostMapping("/book-sync")
    public ResponseEntity<String> runWorkflowAndWait(@RequestBody TravelRequest travelRequest)
    {
        return ResponseEntity.ok(
                travelWorkflowStarter.startWorkflowAndWait(travelRequest)
        );
    }

    @GetMapping("/{userId}/status")
    public ResponseEntity<WorkflowStatusResponse> getWorkflowStatus(@PathVariable Long userId)
    {
        return ResponseEntity.ok(travelWorkflowStarter.getWorkflowStatus(userId));
    }

    @PostMapping("/{userId}/hotel-approval")
    public ResponseEntity<String> setHotelApproval(
            @PathVariable Long userId,
            @RequestBody boolean approved)
    {
        travelWorkflowStarter.setHotelApproval(userId, approved);

        return ResponseEntity.ok("Hotel approval signal sent");
    }
}
