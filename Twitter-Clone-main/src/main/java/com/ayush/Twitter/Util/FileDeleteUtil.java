package com.ayush.Twitter.Util;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileDeleteUtil {
    public static void deleteFile(String uploadDir) throws IOException {
        Path uploadPath = Paths.get(uploadDir);
        Files.delete(uploadPath);
    }
}
