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

            for (int i = 0; i < images.size(); i++) {
                String fileName = images.get(i).getOriginalFilename();
                if (fileName != null && fileName.contains(".JPG")) {
                    try {
                        Path uploadPath = Paths.get("src/main/resources/static/uploads/" + fileName);
                        Files.createDirectories(uploadPath.getParent());
                        images.get(i).transferTo(uploadPath);

                        uploadedImages.add(images.get(i));
                        System.out.println("Saved Image " + (i+1) + ": " + fileName);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
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

    @PostMapping("/label/next")
    public String showNextImage(@RequestParam("currIndex") int currIndex, Model model) {
        int nextIndex = currIndex + 1;
        String redirect = "label";

        if (nextIndex >= uploadedImages.size()) {
            model.addAttribute("message", "Labeling images complete!");
            redirect = "done";
        } else {
            MultipartFile nextImage = uploadedImages.get(nextIndex);
            String fileName = nextImage.getOriginalFilename();

            model.addAttribute("currIndex", nextIndex);
            model.addAttribute("imageName", "/uploads/" + fileName);
        }

        return redirect;
    }

}
