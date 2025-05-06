package com.galindog.TrailCameraImageAnalyzerApplication.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Controller
public class UploadController {

    private final List<MultipartFile> uploadedImages = new ArrayList<>();

    @PostMapping("/upload")
    public String handleFolderUpload(@RequestParam("images") List<MultipartFile> images, Model model) {
        String redirect = "redirect:/label";

        if (images == null || images.isEmpty()) {
            System.out.println("No images uploaded!!!");
            redirect = "error";
        } else {
            System.out.println("Uploading... " + images.size() + " images");
            uploadedImages.clear();
            uploadedImages.addAll(images);
            for (int i = 0; i < uploadedImages.size(); i++) {
                System.out.println("Image " + (i+1) + ": " + uploadedImages.get(i).getOriginalFilename());
            }

            model.addAttribute("currIndex", 0);
            model.addAttribute("imageName", uploadedImages.getFirst().getOriginalFilename());
        }

        return redirect;
    }

}
