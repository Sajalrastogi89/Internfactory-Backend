package com.OurInternfactory.Controllers;

import com.OurInternfactory.Payloads.FileDto;
import com.OurInternfactory.Services.FileServices;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/file")
public class FileController {
    private final FileServices fileServices;
    @Value("${project.image}")
    private String path;
    public FileController(FileServices fileServices){
        this.fileServices = fileServices;
    }


    //Upload FILE API
    @PostMapping("/upload")
    public ResponseEntity<FileDto> fileUpload(
            @RequestParam("image") MultipartFile image
    ){
        String filename = null;
        try {
            filename = this.fileServices.uploadImage(path, image);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(new FileDto(filename, "Image not uploaded, Server error !!!"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(new FileDto(filename, "Image is Successfully Uploaded !!!"), HttpStatus.OK);
    }


    //Serving API not to be used

//    @GetMapping(value  = "/images/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
//    public void downloadImage(@PathVariable("imageName") String imageName, HttpServletResponse response) throws IOException {
//        InputStream resource = this.fileServices.getImage(path, imageName);
//        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
//        StreamUtils.copy(resource, response.getOutputStream());
//    }
    // localhost:8080/files/abc.png
}
