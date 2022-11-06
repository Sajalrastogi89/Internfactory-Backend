package com.OurInternfactory.Services;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public interface FileServices {
    String uploadImage(String path, MultipartFile file) throws IOException;
    InputStream getImage(String path, String fileName) throws FileNotFoundException;
}
