package com.springboot.imagedemo.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.imagedemo.model.Image;
import com.springboot.imagedemo.repository.ImageRepository;


@Service
public class ImageService
{
//	private final ImageRepository imageRepository;
	private ImageRepository imageRepository;
	
	@Autowired
	public ImageService(ImageRepository imageRepository)
	{
		this.imageRepository = imageRepository;
	}
	
	@Value("${project.image}")
	private String path;
	
	
	public String uploadImage(String path,MultipartFile file) throws IOException
	{
		String name = file.getOriginalFilename();
		String filePath = path + File.separator + name;
		Image image = new Image();
		File f = new File(path);
		if(!f.exists())
			f.mkdir();
		
		Files.copy(file.getInputStream(), Paths.get(filePath));
		
		image.setFileName(file.getOriginalFilename());
		imageRepository.save(image);
//		return "";
		return "Welcome!!";
	}
	
	public List<Image> getAllImages()
	{
		return imageRepository.findAll();
	}
	
	public Optional<Image> getImageById(int id)
	{
		return imageRepository.findById(id);
	}

}
