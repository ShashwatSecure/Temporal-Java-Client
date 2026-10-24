package com.temporal.Practice.Activity;

import com.temporal.Practice.dtos.TravelRequest;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface 
public interface TravelActivity {
    
    @ActivityMethod 
    public void bookFlight(TravelRequest travelRequest);

    @ActivityMethod 
    public void bookHotel(TravelRequest travelRequest);

    @ActivityMethod 
    public void bookCar(TravelRequest travelRequest);
}
