package com.facerecon.restapi.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.facerecon.restapi.exception.ResourceNotFoundException;
import com.facerecon.restapi.model.Image;
import com.facerecon.restapi.model.User;
import com.facerecon.restapi.repository.ImageRepository;
import com.facerecon.restapi.repository.UserRepository;


@CrossOrigin(
		origins = "*", 
		methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS, RequestMethod.DELETE}, 
		allowedHeaders = "*")
@RestController
@RequestMapping(path = "/user")
public class UserController {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ImageRepository imageRepository;
	
	@PostMapping("/addUser")
	public User createuser(@Valid @RequestBody User user) {
		return userRepository.save(user);
	}
	
	@GetMapping("/getUsers")
	public List<User> getUsers(){
		List<User> response = new ArrayList<User>();
		
		for (User user : userRepository.findAll()) {
			
			User userAdded = new User();
			userAdded.setId(user.getId());
			userAdded.setNom(user.getNom());
			userAdded.setPrenom(user.getPrenom());
			userAdded.setImage(null);
			
			response.add(userAdded);
		}
		
		System.out.println("RESPONSE : " + response);
		
		return response;
	}

	@GetMapping("/{userId}")
	public User getUserById(@PathVariable Long userId) {
		
		User userReturn = new User();
		userReturn.setNom(userRepository.findById(userId).get().getNom());
		userReturn.setPrenom(userRepository.findById(userId).get().getPrenom());
		userReturn.setId(userRepository.findById(userId).get().getId());
		
		Image image = new Image();
		image.setId(userRepository.findById(userId).get().getImage().getId());
//		image.setImage(userRepository.findById(userId).get().getImage().getImage());
		
		userReturn.setImage(image);
		
		return userReturn;
	}
	
	
	@RequestMapping(value="/{userId}/image", method=RequestMethod.GET, produces = MediaType.IMAGE_PNG_VALUE)
	public byte[] getUserImage(@PathVariable Long userId) {
		byte[] image = (imageRepository.findImageWithUserId(userId)).getImage();

	        return image;
	}
	

	// ne fontionne pas...
	@RequestMapping(value="/{userId}/getUserAndImage", method=RequestMethod.GET, produces = MediaType.IMAGE_PNG_VALUE)
	public User getUserAndImage(@PathVariable Long userId) {
		
		User userReturn = new User();
		userReturn.setNom(userRepository.findById(userId).get().getNom());
		userReturn.setPrenom(userRepository.findById(userId).get().getPrenom());
		userReturn.setId(userRepository.findById(userId).get().getId());
		
		Image image = new Image();
		image.setId(userRepository.findById(userId).get().getImage().getId());
		image.setImage(userRepository.findById(userId).get().getImage().getImage());
		image.setUser(userReturn);
		
		userReturn.setImage(image);
		
		return userReturn;
	}
	
	//manque le mapping
	public User updateUser(@PathVariable Long userId, @Valid @RequestBody User userRequest) {
		
		return userRepository.findById(userId).map(user->{
			user.setNom(userRequest.getNom());
			user.setPrenom(userRequest.getPrenom());
			return userRepository.save(user);
		}).orElseThrow(() -> new ResourceNotFoundException("userId " + userId + " not found"));
		
	}
	
	@DeleteMapping("/user/{userId}")
	public ResponseEntity<?> deleteuser(@PathVariable Long userId) {
		return userRepository.findById(userId).map(user ->{
			userRepository.delete(user);
			return ResponseEntity.ok().build();
		}).orElseThrow(() -> new ResourceNotFoundException("userId " + userId + " not found"));
	}
	
}
