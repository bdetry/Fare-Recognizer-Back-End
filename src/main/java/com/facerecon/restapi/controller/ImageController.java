package com.facerecon.restapi.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.facerecon.restapi.exception.ResourceNotFoundException;
import com.facerecon.restapi.model.Image;
import com.facerecon.restapi.repository.ImageRepository;
import com.facerecon.restapi.repository.UserRepository;

@CrossOrigin
@RestController
@RequestMapping(path = "/image")
public class ImageController {

	@Autowired
	ImageRepository imageRepository;
	
	@Autowired
	UserRepository userRepository;
	
	/**
	 * Save image of a user, assign it to the userId in the Database
	 * @param photo
	 * @param userId
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="/uploadImage/{userId}", method=RequestMethod.POST)
	public String saveUserImage(@RequestParam("photo") MultipartFile photo, @PathVariable Long userId ) throws IOException {
		
		byte[] imageByte = photo.getBytes();
		Image image = new Image();
		image.setImage(imageByte);
			
		return userRepository.findById(userId).map(user->{			
			user.setImage(image);
			userRepository.save(user);
			
			return ("user saved!");
		}).orElseThrow(() -> new ResourceNotFoundException("userId " + userId + " not found"));

	}
	
	
}
