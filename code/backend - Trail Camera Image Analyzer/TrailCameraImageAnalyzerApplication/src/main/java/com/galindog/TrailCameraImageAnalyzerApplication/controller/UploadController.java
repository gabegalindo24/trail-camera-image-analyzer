// Gabe Galindo
// CYBR 408: Principles of Programming Languages & Automata
// Final Project - Trail Camera Image Analyzer
// 05/15/2025

// Upload Controller class - handles uploading the trail camera images, keeps track of them,
// and controls which page to show in browser
package com.galindog.TrailCameraImageAnalyzerApplication.controller;

import com.galindog.TrailCameraImageAnalyzerApplication.model.ImageMetadata;
import com.galindog.TrailCameraImageAnalyzerApplication.service.MetadataService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Controller
public class UploadController {

    private final List<MultipartFile> uploadedImages = new ArrayList<>();

    @PostMapping("/upload")
    public String handleFolderUpload(@RequestParam("images") List<MultipartFile> images, Model model) {
        String redirect = "redirect:/label?currIndex=0";

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
                        Path uploadPath = Paths.get("uploads/" + fileName);
                        Files.createDirectories(uploadPath.getParent());
                        images.get(i).transferTo(uploadPath);

                        uploadedImages.add(images.get(i));
                        System.out.println("Saved Image " + (i+1) + ": " + fileName);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            System.out.println();
        }

        return redirect;
    }

    @GetMapping("/label")
    public String showLabelPage(@RequestParam("currIndex") int currIndex, Model model) {
        String redirect = "label";

        if (uploadedImages.isEmpty()) {
            redirect = "error";
        } else if (currIndex >= uploadedImages.size()) {
            model.addAttribute("message", "Labeling images complete!");
            redirect = "done";
        } else {
            MultipartFile nextImage = uploadedImages.get(currIndex);
            // metadata from jpeg
            String fileName = nextImage.getOriginalFilename();

            model.addAttribute("currIndex", currIndex);
            model.addAttribute("imageName", "/" + fileName);
        }

        return redirect;
    }

    @PostMapping("/label/next")
    public String handleFormData(@RequestParam("currIndex") int currIndex,
                                 @RequestParam("animal") String animal,
                                 @RequestParam("quantity") int quantity) {

        String currFileName = "uploads/" + uploadedImages.get(currIndex).getOriginalFilename();
        MetadataService metadataService = new MetadataService();
        ImageMetadata imageMetadata = metadataService.extractMetadata(currFileName);
        String date = imageMetadata.formatDate();
        String newFileName = "uploads/trailCameraImages/" + animal + "_" + quantity + "_" + date + ".JPG";

        try {
            Files.move(Path.of(currFileName), Path.of(newFileName));
            System.out.println("File path changed from '" + currFileName + "' => '" + newFileName + "'\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return "redirect:/label?currIndex=" + (currIndex + 1);
    }

}
