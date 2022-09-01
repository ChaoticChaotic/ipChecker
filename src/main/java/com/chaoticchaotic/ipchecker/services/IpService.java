package com.chaoticchaotic.ipchecker.services;

import org.springframework.web.multipart.MultipartFile;

public interface IpService {
    String getCheck(MultipartFile file);
}
