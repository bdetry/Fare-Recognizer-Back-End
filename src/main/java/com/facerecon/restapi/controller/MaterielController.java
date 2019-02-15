package com.facerecon.restapi.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.facerecon.restapi.exception.ResourceNotFoundException;
import com.facerecon.restapi.model.Materiel;
import com.facerecon.restapi.model.Stock;
import com.facerecon.restapi.model.User;
import com.facerecon.restapi.repository.MaterielRepository;
import com.facerecon.restapi.repository.StockRepository;
import com.facerecon.restapi.repository.UserRepository;

@CrossOrigin
@RestController
public class MaterielController {

	@Autowired
	private MaterielRepository materielRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private StockRepository stockRepository;
	
	
	@GetMapping("materiel/all")
	public List<Stock> getAll(){
		
		return stockRepository.findAll();
	}
	
	//get the equipement of a user
	@GetMapping("user/{userId}/equipement")
	public List<Materiel> getUserMaterielList(@PathVariable (value="userId") Long userId){
		
		return materielRepository.findMaterielByUserId(userId);
	}
	
	//add a materiel
	@GetMapping("user/{userId}/materiel")
	public Materiel addMateriel(@PathVariable(value="userId") Long userId, @RequestParam Long stockId) {
					
		Optional<Stock> stockReturned = stockRepository.findById( stockId );
		int quantityLeft = stockReturned.get().getQuantity();
		
		if (quantityLeft > 0){
			Stock stock = new Stock();
			
			stock.setId(stockId);
			stock.setName( stockReturned.get().getName() );
			stock.setQuantity((stockReturned.get().getQuantity()) - 1);
			stock.setInitialQuantity(stockReturned.get().getInitialQuantity());
				
			stockRepository.save(stock);

			Optional<User> user = userRepository.findById(userId);
			User userToSave = new User();
			
			userToSave.setId(userId);
			userToSave.setNom(user.get().getNom());
			userToSave.setPrenom( user.get().getPrenom() );
			
			
			Materiel materiel = new Materiel();
			materiel.setStock(stock);
			materiel.setNom(stock.getName());
			materiel.setUser(userToSave);
			materiel.setQuantity(1);
			
			materielRepository.save(materiel);
			
			return materiel;
			
		}else {
			
			throw new ResourceNotFoundException("Stock insuffisant");
			
		}
		

	}
	
	
	@RequestMapping(value = "/user/{userId}/equipement/{materielId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteMateriel(@PathVariable (value = "userId") Long userId,
                              @PathVariable (value = "materielId") Long materielId) {
		
		Optional<Materiel> materielReturned = materielRepository.findById(materielId);
		
		Stock stock = materielReturned.get().getStock();
		stock.setQuantity((stock.getQuantity()) + 1);

			
		stockRepository.save(stock);

		
        return materielRepository.findById(materielId).map(materiel -> {
        	materielRepository.delete(materiel);
             return ResponseEntity.ok().build();
             
        }).orElseThrow(() -> new ResourceNotFoundException("materielId " + materielId + " not found"));
        
    }
	
	
}
