package com.OurInternfactory.Services.Impl;

import com.OurInternfactory.Services.FileServices;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServicesImpl implements FileServices {
    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException {
        //file name
        String  filename = file.getOriginalFilename();
        //random name generator for file
        String randomID = UUID.randomUUID().toString();
        assert filename != null;
        String newFileName =  randomID.concat(filename.substring(filename.lastIndexOf(".")));
        //Fillpath
        String filepath = path + File.separator + newFileName;
        //create folder if not present in the server
        File newFile = new File(path);
        if(!newFile.exists()){
            newFile.mkdir();
        }
        //file upload    i.e copying the file from the argument and save the Filepath
        Files.copy(file.getInputStream(), Paths.get(filepath));
        return newFileName;
    }

    @Override
    public InputStream getImage(String path, String fileName) throws FileNotFoundException {
        String fullPath = path +File.separator + fileName;
        InputStream is = new FileInputStream(fullPath);
        return is;
    }
}
