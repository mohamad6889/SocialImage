package com.social.image.socialimage.controller;


import com.social.image.socialimage.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class ImageController {

    private static final String IMAGES_PATH = "/images";
    private static final String FILE_NAME = "{filename:.+";

    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }


    @RequestMapping(method = RequestMethod.GET, value = IMAGES_PATH + "/" + FILE_NAME + "/raw")
    @ResponseBody
    public ResponseEntity<?> oneRawImage(@PathVariable String filename) {
        Resource file = imageService.findOneImageResource(filename);
        try {
            return ResponseEntity.ok()
                    .contentLength(file.contentLength())
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(new InputStreamResource(file.getInputStream()));
        } catch (IOException e) {
            return ResponseEntity.badRequest()
                    .body("Could not find" + filename + " : " + e.getMessage());
        }
    }

    public ResponseEntity<?> postFile(@RequestParam("file")MultipartFile file, HttpRequest request) {
        try {
            imageService.saveImage(file);
            return ResponseEntity.created(request.getURI().resolve(file.getOriginalFilename() + "/raw"))
                    .body(file.getOriginalFilename() + "Posted Successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to Post" + file.getOriginalFilename() + " : " + e.getMessage());
        }

    }

}
