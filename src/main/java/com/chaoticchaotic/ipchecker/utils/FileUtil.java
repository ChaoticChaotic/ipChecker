package com.chaoticchaotic.ipchecker.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

@Component
public class FileUtil {

    @Value("${files.folder}")
    private String folderPath;

    public File uploadImage(MultipartFile file) throws IOException, NullPointerException {
        String fileName = file.getOriginalFilename();
        String fileSystemPath = folderPath + fileName;
        File toCheck = new File(fileSystemPath);
        BufferedOutputStream buffStream =
                new BufferedOutputStream(new FileOutputStream(fileSystemPath));
        byte[] bytes = file.getBytes();
        buffStream.write(bytes);
        buffStream.close();
        return toCheck;
    }
}
