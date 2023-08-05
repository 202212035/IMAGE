package com.springboot.imagedemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.springboot.imagedemo.model.Image;

public interface ImageRepository extends JpaRepository<Image, Integer>
{
	
}