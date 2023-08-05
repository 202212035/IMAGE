package com.springboot.imagedemo.controller;

import java.io.IOException;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
import org.springframework.http.*;

import java.nio.file.Files;
import java.util.List;
import java.util.Optional;
import java.io.File;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ContentDisposition;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.imagedemo.repository.ImageRepository;
import com.springboot.imagedemo.service.ImageService;
import com.springboot.imagedemo.model.Image;


@RestController
@CrossOrigin("*")
@RequestMapping("/Image")
public class ImageController
{
//	private final ImageRepository imageRepository;
	private ImageRepository imageRepository;
	private ImageService imageService;
	
	@Value("${project.image}")
	private String path;
	
	@Autowired
	public ImageController(ImageService imageService)
	{
		this.imageService = imageService;
	}
	
	@PostMapping("/upload")
	public ResponseEntity<String> uploadImage(@RequestParam("image") MultipartFile file) throws IOException
//	public ResponseEntity<Image> uploadImage(@RequestParam("image") MultipartFile file) throws IOException
	{
		String fileName = null;
		try 
		{
			fileName = this.imageService.uploadImage(path, file);
//			fileName = this.fileService.uploadImage(path, image);
		}
		catch (IOException e)
		{
			e.printStackTrace();
//			return new ResponseEntity<>(new Image(null, "Image NOT Uploaded !!!"),HttpStatus.INTERNAL_SERVER_ERROR);
			return new ResponseEntity<String>("Image NOT UPLOADED",null);
		}
//		return new ResponseEntity<>(new FileResponse(fileName, "Image Uploaded !!!"),HttpStatus.OK);
//		imageService.uploadImage(fileName, file);
		return ResponseEntity.ok("Image Uploaded");
	}
	
    @GetMapping("/getall")
    public ResponseEntity<List<Image>> getAllImages()
    {
        List<Image> images = imageService.getAllImages();
        return ResponseEntity.ok(images);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> getImageById(@PathVariable("id") int id)
//    public ResponseEntity<byte[]> getImageById(@PathVariable() int id)
    {
    	Optional<Image> imageOptional = imageService.getImageById(id);
    	if(imageOptional.isPresent())
    	{
    		Image image = imageOptional.get();
	        // Retrieve the image from the service
	    	// You can modify the file path or storage mechanism as per your implementation
	    	File imageFile = new File(path + image.getFileName());
	    	
	    	try
	    	{
	    		// Read the image file into a byte array
	    		byte[] imageData = Files.readAllBytes(imageFile.toPath());
	    		// Determine the appropriate content type based on the file extension
                String contentType = Files.probeContentType(imageFile.toPath());
                // Set the Content-Disposition header to trigger the file download
                HttpHeaders headers = new HttpHeaders();
                headers.setContentDisposition(ContentDisposition.builder("attachment").filename(image.getFileName()).build());
                headers.setContentType(MediaType.parseMediaType(contentType));
                return ResponseEntity.ok().headers(headers).body(imageData);
	    	}
	    	catch (IOException e)
	    	{
	    		// Handle the error if the image file cannot be read
//				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	    		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	    	}
	    		
    	
        // Convert it to byte array
        // Set the appropriate content type (e.g., "image/jpeg", "image/png")
        // Return ResponseEntity with byte array and headers
    	}

        return ResponseEntity.notFound().build();
    }
}