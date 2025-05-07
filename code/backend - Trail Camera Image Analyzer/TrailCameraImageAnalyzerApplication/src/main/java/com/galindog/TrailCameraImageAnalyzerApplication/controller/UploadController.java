// Gabe Galindo
// CYBR 408: Principles of Programming Languages & Automata
// Final Project - Trail Camera Image Analyzer
// 05/14/2025

// UploadController class

package com.galindog.TrailCameraImageAnalyzerApplication.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

// UploadController class - handles uploading the trail camera images, keeps track of them,
// and controls which page to show in browser
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
                try {
                    String fileName = uploadedImages.get(i).getOriginalFilename();
                    Path uploadPath = Paths.get("src/main/resources/static/uploads/" + fileName);
                    Files.createDirectories(uploadPath.getParent());
                    uploadedImages.get(i).transferTo(uploadPath);
//                    uploadedImages.add(uploadedImages.get(i));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Image " + (i+1) + ": " + uploadedImages.get(i).getOriginalFilename());
            }

            model.addAttribute("currIndex", 0);
            model.addAttribute("imageName", uploadedImages.getFirst().getOriginalFilename());
        }

        return redirect;
    }

    @GetMapping("/label")
    public String showLabelPage(Model model) {
        String redirect = "label";

        if (uploadedImages.isEmpty()) {
            redirect = "error";
        }

        MultipartFile firstImage = uploadedImages.getFirst();
        String fileName = firstImage.getOriginalFilename();

        model.addAttribute("currIndex", 0);
        model.addAttribute("imageName", "/uploads/" + fileName);

        return redirect;
    }


}
