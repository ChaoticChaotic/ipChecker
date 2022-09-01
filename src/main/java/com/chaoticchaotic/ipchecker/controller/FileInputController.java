package com.chaoticchaotic.ipchecker.controller;

import com.chaoticchaotic.ipchecker.services.IpService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/file")
public class FileInputController {

    private final IpService ipService;

    public FileInputController(IpService ipService) {
        this.ipService = ipService;
    }

    @PostMapping
    public ResponseEntity<String> getCheckResult(@RequestParam(value = "file") MultipartFile file) {
        return ResponseEntity.ok().body(ipService.getCheck(file));
    }
}

