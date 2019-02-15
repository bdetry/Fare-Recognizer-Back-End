package com.facerecon.restapi.controller;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.BoundingBox;
import com.amazonaws.services.rekognition.model.CompareFacesMatch;
import com.amazonaws.services.rekognition.model.CompareFacesRequest;
import com.amazonaws.services.rekognition.model.CompareFacesResult;
import com.amazonaws.services.rekognition.model.ComparedFace;
import com.amazonaws.services.rekognition.model.Image;
import com.facerecon.restapi.model.User;
import com.facerecon.restapi.repository.ImageRepository;
import com.facerecon.restapi.repository.UserRepository;

@CrossOrigin(origins = "*", 
			methods = {RequestMethod.POST, RequestMethod.OPTIONS}, 
			allowedHeaders = "*")
@RestController
@RequestMapping(path = "/rekognition")
public class RekognitionController {
	
   	@Autowired
	ImageRepository imageRepository;
   	
   	@Autowired
   	UserRepository userRepository;
   	
	// renvoyer l'id du gars
	@PostMapping("/compareFaces")
	public User compareFaces(@RequestPart("image") MultipartFile sourceImage, @RequestParam("id") Long userId) throws IOException, SQLException {
		

		boolean result = false;		
		
	       AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder
	    		   .standard()
	    		   .withRegion("us-east-1") // The first region to try your request against
                   .build();

			
	       Float similarityThreshold = 70F;
	       
	       ByteBuffer sourceImageBytes=null;
	       ByteBuffer targetImageBytes=null;     	       


       try {
	    	   
	     
	      byte[] userImage = imageRepository.findImageWithUserId(userId).getImage();
	      
          sourceImageBytes = ByteBuffer.wrap(sourceImage.getBytes());
          targetImageBytes = ByteBuffer.wrap(userImage);

	       Image source=new Image()
	            .withBytes(sourceImageBytes);
	       Image target=new Image()
	            .withBytes(targetImageBytes);

	       CompareFacesRequest request = new CompareFacesRequest()
	               .withSourceImage(source)
	               .withTargetImage(target)
	               .withSimilarityThreshold(similarityThreshold);

	       // Call operation
	       CompareFacesResult compareFacesResult=rekognitionClient.compareFaces(request);

	       // Display results
	       List<CompareFacesMatch> faceDetails = compareFacesResult.getFaceMatches();
	       for (CompareFacesMatch match: faceDetails){
	         ComparedFace face= match.getFace();
	         BoundingBox position = face.getBoundingBox();
	         System.out.println("Face at " + position.getLeft().toString()
	               + " " + position.getTop()
	               + " matches with " + face.getConfidence().toString()
	               + "% confidence.");
	         if(face.getConfidence()>96.5) {
	  	       System.out.println(face.getConfidence());
	        	 result = true;
	         }
	         System.out.println(face.getConfidence());

	       }
	       
       }catch(Exception e){
    	   System.out.println("[ERROR]" + e.getMessage());
       }
		
       if(result == true) {
    	   Optional<User> user = userRepository.findById(userId);
    	   
    	   User userResponse = new User();
    	   userResponse.setId(user.get().getId());
    	   userResponse.setNom(user.get().getNom());
    	   userResponse.setPrenom(user.get().getPrenom());
    	   userResponse.setImage(user.get().getImage());
    	   
    	   return userResponse;
    	   
       }else {
           System.out.println("same person : " + result);
    	   User userResponse = new User();
   		  
    	   return userResponse;
   			
       }

	
	}
	
}
