package com.facerecon.restapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.facerecon.restapi.model.Materiel;


@Repository
public interface MaterielRepository extends JpaRepository<Materiel, Long>{

	 @Query(nativeQuery=true, value="SELECT * FROM materiel m where m.user_id = :userId") 
	  List<Materiel> findMaterielByUserId(@Param("userId") Long userId);
	
}
