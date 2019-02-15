package com.facerecon.restapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.facerecon.restapi.model.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long>{

	@Query(value = "SELECT * FROM images WHERE user_id = :userId ", nativeQuery=true )
	Image findImageWithUserId(@Param("userId") Long userId);

}
