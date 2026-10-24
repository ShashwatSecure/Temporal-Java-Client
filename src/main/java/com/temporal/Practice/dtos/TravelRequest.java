package com.temporal.Practice.dtos;

import lombok.Data;

@Data 
public class TravelRequest {
    
    Long userId;

    String place;

    String date;
}
