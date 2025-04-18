package com.futechsoft.gis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.futechsoft.framework.component.RestTemplateFactory;


@Service
public class GeocodingService {
	
	@Autowired
	RestTemplateFactory restTemplateFactory;
	
	@Value("${geocode.key}")
	private String geocodeKey;

	
	public String getCoordinates(String address) {
		RestTemplate restTemplate = restTemplateFactory.getRestTemplate();
        String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + address + "&key="+geocodeKey;
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        
        
        double[] latLng = extractLatLng(response.getBody());
        
        System.out.println("위도: " + latLng[0]);
        System.out.println("경도: " + latLng[1]);
        
        return  latLng[0]+","+latLng[1];
    }

	
	 public static double[] extractLatLng(String json) {
	        try {
	            ObjectMapper mapper = new ObjectMapper();
	            JsonNode root = mapper.readTree(json);

	            JsonNode results = root.path("results");
	            if (results.isArray() && results.size() > 0) {
	                JsonNode location = results.get(0).path("geometry").path("location");
	                double lat = location.path("lat").asDouble();
	                double lng = location.path("lng").asDouble();
	                return new double[]{lat, lng};
	            } else {
	                throw new RuntimeException("결과가 없습니다.");
	            }
	        } catch (Exception e) {
	            throw new RuntimeException("JSON 파싱 중 오류 발생", e);
	        }
	    }

}
