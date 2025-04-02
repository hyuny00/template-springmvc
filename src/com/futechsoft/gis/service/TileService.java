package com.futechsoft.gis.service;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.futechsoft.framework.component.RestTemplateFactory;

@Service
public class TileService {

	@Autowired
	RestTemplateFactory restTemplateFactory;


    public byte[] getTile(int zoom, int x, int y) {
    	
    	HttpHeaders headers = new HttpHeaders();
    	headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/135.0.0.0 Safari/537.36");
    	HttpEntity<String> entity = new HttpEntity<>(headers);
    	
    	RestTemplate  restTemplate = restTemplateFactory.getRestTemplate();
    	
    	String[] servers = {"a", "b", "c"};
    	String server = servers[new Random().nextInt(servers.length)];
    	String tileUrl = String.format("https://%s.tile.openstreetmap.org/%d/%d/%d.png", server, zoom, x, y);
        	
		ResponseEntity<byte[]> response = restTemplate.exchange(tileUrl, HttpMethod.GET, entity, byte[].class);
        
		return response.getBody();
    }
}