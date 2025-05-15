// Gabe Galindo
// CYBR 408: Principles of Programming Languages & Automata
// Final Project - Trail Camera Image Analyzer
// 05/15/2025
package com.galindog.TrailCameraImageAnalyzerApplication.service;
import com.drew.imaging.ImageProcessingException;
import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.galindog.TrailCameraImageAnalyzerApplication.model.ImageMetadata;
import java.io.File;
import java.io.IOException;
import java.util.Date;

// MetadataService class - service class to extract metadata from image files
public class MetadataService {

    // Extracts creation date metadata from the given image file path
    public ImageMetadata extractMetadata(String filePath) {
        ImageMetadata imageMetadata;
        try {
            File imageFile = new File(filePath);
            Metadata metadata = ImageMetadataReader.readMetadata(imageFile);
            // get EXIF dir that has the date the image was created
            ExifSubIFDDirectory exifDirectory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
            Date creationDate = null;
            if (exifDirectory != null) {
                creationDate = exifDirectory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
            }
            // wrap metadata into ImageMetaData object
            imageMetadata = new ImageMetadata(creationDate);
        } catch (ImageProcessingException | IOException e) {
            throw new RuntimeException("Failed to extract metadata from image: " + filePath, e);
        }
        return imageMetadata; // return ImageMetadata object
    }
}
