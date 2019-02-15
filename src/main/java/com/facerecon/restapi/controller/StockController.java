package com.facerecon.restapi.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.facerecon.restapi.model.Stock;
import com.facerecon.restapi.repository.StockRepository;

@CrossOrigin
@RestController
@RequestMapping(path = "/stock")
public class StockController {
	
	@Autowired
	public StockRepository stockRepository;
	
	@GetMapping("/getStock")
	public List<Stock> getStock() {
		return stockRepository.findAll();
	}
	
	@PostMapping("/initilizeStock")
	public List<Stock> addStock(@Valid @RequestBody List<Stock> stock){
		
		return stockRepository.saveAll(stock);
	}

}
