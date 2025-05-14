package com.galindog.TrailCameraImageAnalyzerApplication.service;

import com.drew.imaging.ImageProcessingException;
import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.galindog.TrailCameraImageAnalyzerApplication.model.ImageMetadata;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class MetadataService {

    public ImageMetadata extractMetadata(String filePath) {
        ImageMetadata imageMetadata;

        try {
            File imageFile = new File(filePath);
            Metadata metadata = ImageMetadataReader.readMetadata(imageFile);

            ExifSubIFDDirectory exifDirectory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);

            Date creationDate = null;
            if (exifDirectory != null) {
                creationDate = exifDirectory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
            }

            imageMetadata = new ImageMetadata(creationDate);

        } catch (ImageProcessingException | IOException e) {
            throw new RuntimeException("Failed to extract metadata from image: " + filePath, e);
        }

        return imageMetadata;
    }
}
