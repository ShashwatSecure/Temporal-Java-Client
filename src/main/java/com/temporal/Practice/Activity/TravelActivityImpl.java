package com.temporal.Practice.Activity;

import com.temporal.Practice.dtos.TravelRequest;

import io.temporal.spring.boot.ActivityImpl;
import lombok.extern.slf4j.Slf4j;

@ActivityImpl 
@Slf4j 
public class TravelActivityImpl implements TravelActivity {
    
    public void bookFlight(TravelRequest travelRequest)
    {
        log.info("Booking flight for : {} to {} on {}", travelRequest.getUserId(), travelRequest.getPlace(), travelRequest.getDate());
    }

    public void bookHotel(TravelRequest travelRequest)
    {
        log.info("Booking hotel for : {} to {} on {}", travelRequest.getUserId(), travelRequest.getPlace(), travelRequest.getDate());
    }

    public void bookCar(TravelRequest travelRequest)
    {
        log.info("Booking car for : {} to {} on {}", travelRequest.getUserId(), travelRequest.getPlace(), travelRequest.getDate());
    }
}

