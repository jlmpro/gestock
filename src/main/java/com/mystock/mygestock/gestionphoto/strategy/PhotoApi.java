package com.mystock.mygestock.gestionphoto.strategy;

import com.flickr4java.flickr.FlickrException;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Tag(name = "Photo Api")
public interface PhotoApi {
    @PostMapping(value = "photos/{id}/{title}/{context}")
    Object savePhoto(@PathVariable Long id, @PathVariable String title,@PathVariable String  context, @RequestPart("file") MultipartFile photo) throws IOException, FlickrException;
}
