package com.futechsoft.gis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.futechsoft.gis.service.TileService;

@RestController
@RequestMapping("/tiles")
public class TileController {

	@Autowired
    private  TileService tileService;

  
/*
    @GetMapping("/{zoom}/{x}/{y}.png")
    public ResponseEntity<byte[]> getTile(@PathVariable int zoom, @PathVariable int x, @PathVariable int y) {
        // B 서버에서 타일을 가져옵니다.
        byte[] tile = tileService.getTile(zoom, x, y);

        // 타일 이미지 파일을 클라이언트에게 반환합니다.
        return ResponseEntity.ok()
                             .header("Content-Type", "image/png")
                             .body(tile);
    }
    */
    
    @GetMapping("/{zoom}/{x}/{y}.png")
    public ResponseEntity<byte[]> getTile(@PathVariable int zoom, @PathVariable int x, @PathVariable int y) {
        // B 서버에서 타일을 가져옵니다.
        byte[] tile = tileService.getMap("c",zoom, x, y);

        // 타일 이미지 파일을 클라이언트에게 반환합니다.
        return ResponseEntity.ok()
                             .header("Content-Type", "image/png")
                             .body(tile);
    }
    
}