package com.social.image.socialimage.service;

import com.social.image.socialimage.domain.Image;
import com.social.image.socialimage.repo.ImageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class ImageService {

    private static String UPLOAD_ROOT = "upload_directory";

    private final ImageRepo repo;
    private final ResourceLoader resourceLoader;

    @Autowired
    public ImageService(ImageRepo repo, ResourceLoader resourceLoader) {
        this.repo = repo;
        this.resourceLoader = resourceLoader;
    }

    public Resource findOneImageResource(String fileName) {
        return resourceLoader.getResource("file:" + UPLOAD_ROOT + "/" + fileName);
    }

    public void saveImage(MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            Files.copy(file.getInputStream(), Paths.get(UPLOAD_ROOT, file.getOriginalFilename()));
            repo.save(new Image(file.getOriginalFilename()));
        }
    }

    public void deleteImage(String name) throws IOException {
        final Image fileByName = repo.findByName(name);
        repo.delete(fileByName);
        Files.deleteIfExists(Paths.get(UPLOAD_ROOT, name));
    }
}
