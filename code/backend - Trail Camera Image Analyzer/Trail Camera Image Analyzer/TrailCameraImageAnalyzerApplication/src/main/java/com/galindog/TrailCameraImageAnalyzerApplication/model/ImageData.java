// Gabe Galindo
// CYBR 408: Principles of Programming Languages & Automata
// Final Project - Trail Camera Image Analyzer
// 05/15/2025
package com.galindog.TrailCameraImageAnalyzerApplication.model;
import java.time.*;

// ImageData class
public class ImageData {
    private String species;
    private int quantity;
    private LocalDate date;
    private LocalTime time;

    // constructors
    public ImageData() {
        this.setSpecies("\0");
        this.setQuantity(-1);
        this.setDate(null);
        this.setTime(null);
    }

    public ImageData(String specie, int quantity, LocalDate date, LocalTime time) {
        this.setSpecies(specie);
        this.setQuantity(quantity);
        this.setTime(time);
        this.setDate(date);
    }

    // getters
    public String getSpecies() {
        return this.species;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public LocalTime getTime() {
        return this.time;
    }

    // setters
    public void setSpecies(String species) {
        this.species = species;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    // to string
    public String toString() {
        String temp;
        temp = "Image Data Object:\n\tSpecie: " + this.getSpecies() + "\n" +
                "\n\tQuantity: " + this.getQuantity() + "\n" +
                "\n\tDate: " + this.getDate() + "\n" +
                "\n\tTime: " + this.getTime() + "\n";
        return temp;
    }
}
