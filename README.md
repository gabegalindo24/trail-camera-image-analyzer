# Trail Camera Image Analyzer

## Project Summary  
The **Trail Camera Image Analyzer** is a Java Spring Boot web application built to help users process and analyze wildlife photos taken from a trail cameras. This application tool would be useful to researchers, landowners, hunters, and wildlife enthusiasts who want to draw some insights from their trail camera images.

### Project Tech Stack
**Backend:** [Java Spring Boot](https://spring.io/projects/spring-boot)  
**Frontend:** [Thymeleaf & HTML](https://www.thymeleaf.org/)

#### Project Scope
### Project Scope
- **Image Organization**: Classify and organize images into folders by species based on the filename.
- **Analytics**: Find useful analytics from filenames and image metadata:
  - Total number of images per species
  - Total quantity of animals seen per species
  - Animal Activity by date
  - Top hours of animal activity (based on image creation date)
- **Web UI**: Simple Thymeleaf-based frontend that allows users to:
  - View uploaded and categorized images
  - Filter by species
  - View generated statistics 

### Limitations
- No machine learning — this project relies entirely on filenames for classification.
- Everything runs off the image filename. So filenames have to follow this format: **Example: `deer_1_11-07-2022_08:42:54_PM.JPG`**
- Images must be manually labeled ahead of time with the correct species and animal count in the filename.
- Analytics are not saved or exportable — everything is generated on the fly each time the page loads.

### Explanations of Use
1. Run the Driver.java file.
2. Open your browser and go to http://localhost:8080/ to launch the app.
3. Upload a folder of trail camera images.
4. Label each image with the correct species type and number of animals.
5. After labeling the last image, you'll be taken to the results page automatically. 
  - Browse uploaded images by species
  - View stats like total images per species, total animal counts, daily activity, and top hours of animal movement

NOTE: If you want to upload a new folder of images, make sure to delete or clear the existing contents of the /uploads/ directory first.
Path: code/backend - Trail Camera Image Analyzer/uploads/

## Links
* [Documentation of Effort](https://github.com/gabegalindo24/CYBR_408_Final_Project/blob/main/docs/DOCUMENTATION.md)
* [Standards and Practices (S&P)](https://github.com/gabegalindo24/CYBR_408_Final_Project/blob/main/docs/S%26P.md)
* [Java Spring Boot Backend in Project](https://github.com/gabegalindo24/CYBR_408_Final_Project/tree/221525425a04254d22cf8ec6745f1468e29d1dbd/code/backend%20-%20Trail%20Camera%20Image%20Analyzer/Trail%20Camera%20Image%20Analyzer/TrailCameraImageAnalyzerApplication/src/main/java/com/galindog/TrailCameraImageAnalyzerApplication)
* [Thymeleaf & HTML Frontend](https://github.com/gabegalindo24/CYBR_408_Final_Project/tree/91ed817145ef3dcb24d37ac4b1e2f962da0f6866/code/backend%20-%20Trail%20Camera%20Image%20Analyzer/Trail%20Camera%20Image%20Analyzer/TrailCameraImageAnalyzerApplication/src/main/resources/templates)
