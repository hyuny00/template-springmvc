package com.futechsoft.gis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.futechsoft.gis.service.GeocodingService;

@RestController
public class GeocodingController {

	@Autowired
    private  GeocodingService geocodingService;

   
    @GetMapping("/geocode")
    public String geocode(@RequestParam String address) {
        return geocodingService.getCoordinates(address);
    }
}
