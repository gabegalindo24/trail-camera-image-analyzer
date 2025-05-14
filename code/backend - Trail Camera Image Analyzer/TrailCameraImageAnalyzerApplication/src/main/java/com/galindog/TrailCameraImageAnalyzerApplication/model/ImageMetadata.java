// Gabe Galindo
// CYBR 408: Principles of Programming Languages & Automata
// Final Project - Trail Camera Image Analyzer
// 05/15/2025

// Image Metadata class
package com.galindog.TrailCameraImageAnalyzerApplication.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ImageMetadata {
    private Date creationDate;

    // constructors
    public ImageMetadata() {
        this.setCreationDate(null);
    }

    public ImageMetadata(Date creationDate) {
        this.setCreationDate(creationDate);
    }

    // helpers
    public String formatDate() {
        String formatDate = null;
        SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        SimpleDateFormat outputFormat = new SimpleDateFormat("MM-dd-yyyy");

        try {
            Date date = inputFormat.parse(this.getCreationDate().toString());
            formatDate = outputFormat.format(date);
        } catch (ParseException e) {
            System.err.println("Failed to parse date: " + this.getCreationDate().toString());
            throw new RuntimeException(e);
        }

        return formatDate;
    }

    // getters
    public Date getCreationDate() {
        return this.creationDate;
    }

    // setters
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    // to string
    public String toString() {
        String temp;
        temp = "Image Metadata Object:\n\tcreation date = {" + this.getCreationDate() + "}\n";
        return temp;
    }
}
