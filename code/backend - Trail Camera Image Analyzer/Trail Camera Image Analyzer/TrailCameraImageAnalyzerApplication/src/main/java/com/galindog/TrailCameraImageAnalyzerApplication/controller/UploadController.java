// Gabe Galindo
// CYBR 408: Principles of Programming Languages & Automata
// Final Project - Trail Camera Image Analyzer
// 05/15/2025
package com.galindog.TrailCameraImageAnalyzerApplication.controller;
import com.galindog.TrailCameraImageAnalyzerApplication.model.ImageData;
import com.galindog.TrailCameraImageAnalyzerApplication.model.ImageMetadata;
import com.galindog.TrailCameraImageAnalyzerApplication.service.MetadataService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

// UploadController class - controller class handles uploading trail camera images, labeling, organizing, and analytics
@Controller
public class UploadController {
    // store uploaded images in List
    private final List<MultipartFile> uploadedImages = new ArrayList<>();

    // handles folder upload, saves images to directory path "code/backend - Trail Camera Image Analyzer/uploads"
    @PostMapping("/upload")
    public String handleFolderUpload(@RequestParam("images") List<MultipartFile> images, Model model) {
        String redirect = "redirect:/label?currIndex=0";
        if (images == null || images.isEmpty()) {
            System.out.println("No images uploaded!!!");
            redirect = "error";
        } else {
            System.out.println("\nUploading... " + (images.size() - 1) + " images");
            uploadedImages.clear();
            for (MultipartFile img : images) {
                String fileName = img.getOriginalFilename();
                if (fileName != null && fileName.contains(".JPG")) {
                    try {
                        Path uploadPath = Paths.get("uploads/" + fileName);
                        Files.createDirectories(uploadPath.getParent());
                        img.transferTo(uploadPath);
                        uploadedImages.add(img);
                        System.out.println("Saved Image: " + fileName);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            System.out.println();
        }
        return redirect;
    }

    // shows the labeling page for current image in folder
    @GetMapping("/label")
    public String showLabelPage(@RequestParam("currIndex") int currIndex, Model model) {
        String redirect = "label";
        if (uploadedImages.isEmpty() || currIndex >= uploadedImages.size()) {
            model.addAttribute("message", "Labeling images complete!");
            redirect = "redirect:/done";
        } else {
            MultipartFile nextImage = uploadedImages.get(currIndex);
            String fileName = nextImage.getOriginalFilename();
            // pass data to view
            model.addAttribute("currIndex", currIndex);
            model.addAttribute("imageName", "/" + fileName);
        }
        return redirect;
    }

    // processes submitted label data, renames image file name, creates a species folder and moves it into species folder
    @PostMapping("/label/next")
    public String handleFormData(@RequestParam("currIndex") int currIndex,
                                 @RequestParam("species") String species,
                                 @RequestParam("quantity") int quantity) {
        String originalFileName = "uploads/" + uploadedImages.get(currIndex).getOriginalFilename();
        String[] parts = originalFileName.split("/");
        String folderName = parts[1];
        // extract creation date from image metadata
        MetadataService metadataService = new MetadataService();
        ImageMetadata imageMetadata = metadataService.extractMetadata(originalFileName);
        String date = imageMetadata.formatDate();
        // rename file based on label info and metadata
        String newFileName = species + "_" + quantity + "_" + date + ".JPG";
        // create species folder if it does not exist
        Path specieFolderPath = Paths.get("uploads/"+ folderName + "/" + species);
        if (!Files.exists(specieFolderPath)) {
            try {
                Files.createDirectories(specieFolderPath); // Create the folder
                System.out.println("\nCreated folder for species: " + species + "\n");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        // move file with new name to species folder
        String originalPath = Paths.get(originalFileName).toString();
        String newPath = specieFolderPath.resolve(newFileName).toString();
        try {
            Files.move(Path.of(originalPath), Path.of(newPath));
            System.out.println("File path changed from '" + originalPath + "' => '" + newPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "redirect:/label?currIndex=" + (currIndex + 1);
    }

    // shows the done page with images and analytics
    @GetMapping("/done")
    public String showDonePage(@RequestParam(value = "species", required = false, defaultValue = "all") String species, Model model) {
        List<String> imagePaths = new ArrayList<>();
        List<ImageData> analyticsList = new ArrayList<>();
        if (!uploadedImages.isEmpty()) {
            String[] temp = uploadedImages.getFirst().getOriginalFilename().split("/");
            String uploadPath = "uploads/" + temp[0] + "/";
            File baseDir = new File(uploadPath);
            // load images based on selected species
            if (species.equals("all")) {
                for (File speciesFolder : baseDir.listFiles()) {
                    if (speciesFolder.isDirectory()) {
                        for (File img : Objects.requireNonNull(speciesFolder.listFiles())) {
                            String fileName = img.getName();
                            imagePaths.add("/" + temp[0] + "/" + speciesFolder.getName() + "/" + fileName);
                            parseFileNameToData(fileName, analyticsList);
                        }
                    }
                }
            } else {
                File speciesFolder = new File(baseDir, species);
                if (speciesFolder.exists() && speciesFolder.isDirectory()) {
                    for (File img : Objects.requireNonNull(speciesFolder.listFiles())) {
                        String fileName = img.getName();
                        imagePaths.add("/" + temp[0] + "/" + species + "/" + fileName);
                        parseFileNameToData(fileName, analyticsList);
                    }
                }
            }
        }
        // create analytics maps
        Map<String, Integer> speciesCount = new HashMap<>();
        Map<String, Integer> speciesQuantity = new HashMap<>();
        Map<LocalDate, Integer> dailyCount = new TreeMap<>(); // Keeps dates sorted
        Map<String, Integer> hourlyCount = new HashMap<>();
        for (ImageData data : analyticsList) {
            speciesCount.merge(data.getSpecies(), 1, Integer::sum);
            speciesQuantity.merge(data.getSpecies(), data.getQuantity(), Integer::sum);
            dailyCount.merge(data.getDate(), data.getQuantity(), Integer::sum);
        }
        // parse filenames for hourly activity
        for (String path : imagePaths) {
            try {
                String filename = new File(path).getName();
                filename = filename.replace(".JPG", "").replace(".jpg", "");
                String[] parts = filename.split("_");

                if (parts.length >= 5) {
                    int quantity = Integer.parseInt(parts[1]);
                    String time = parts[3];
                    String amPm = parts[4];
                    String hour = time.split(":")[0];
                    String hourLabel = hour + " " + amPm;

                    hourlyCount.put(hourLabel, hourlyCount.getOrDefault(hourLabel, 0) + quantity);
                }
            } catch (Exception e) {
                System.out.println("Error parsing filename: " + path);
                throw new RuntimeException(e);
            }
        }
        // get top 3 most active hours
        List<Map.Entry<String, Integer>> topHours = hourlyCount.entrySet()
                .stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .limit(3)
                .collect(Collectors.toList());
        // pass data to view
        model.addAttribute("selectedSpecies", species);
        model.addAttribute("images", imagePaths);
        model.addAttribute("speciesCount", speciesCount);
        model.addAttribute("speciesQuantity", speciesQuantity);
        model.addAttribute("dailyCount", dailyCount);
        model.addAttribute("topHours", topHours);
        return "done";
    }

    // handles species filtering on the done page
    @PostMapping("/done")
    public String handleSpeciesFilter(@RequestParam("species") String species, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("species", species);
        return "redirect:/done";
    }

    // helper method to parse filename into ImageData object
    private void parseFileNameToData(String fileName, List<ImageData> list) {
        try {
            String name = fileName.replace(".JPG", "").replace(".jpg", "");
            String[] parts = name.split("_");
            String species = parts[0];
            int quantity = Integer.parseInt(parts[1]);
            String dateStr = parts[2];
            String timeStr = parts[3] + "_" + parts[4];
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM-dd-yyyy");
            DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("hh:mm:ss_a");
            LocalDate date = LocalDate.parse(dateStr, dateFormat);
            LocalTime time = LocalTime.parse(timeStr, timeFormat);
            list.add(new ImageData(species, quantity, date, time));
        } catch (Exception e) {
            System.out.println("Error parsing file: " + fileName + " – " + e.getMessage());
        }
    }
}