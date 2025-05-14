// Gabe Galindo
// CYBR 408: Principles of Programming Languages & Automata
// Final Project - Trail Camera Image Analyzer
// 05/15/2025

// Driver class
package com.galindog.TrailCameraImageAnalyzerApplication;

import com.galindog.TrailCameraImageAnalyzerApplication.model.ImageMetadata;
import com.galindog.TrailCameraImageAnalyzerApplication.service.MetadataService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Driver {
	// main method
	public static void main(String[] args) {
		SpringApplication.run(Driver.class, args);
		System.out.println("\nSPRING BOOT RUNNING...\n");
	}

}
