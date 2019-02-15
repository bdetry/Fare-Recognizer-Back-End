package com.facerecon.restapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.facerecon.restapi.model.Stock;

public interface StockRepository extends JpaRepository <Stock, Long>{

	
}
