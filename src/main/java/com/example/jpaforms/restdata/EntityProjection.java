package com.example.jpaforms.restdata;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import com.example.jpaforms.persistence.BaseEntity;

@Projection(
		  name = "customCatalog", 
		  types = { BaseEntity.class }) 
public interface EntityProjection {
	
	@Value("#{target.toString()}")	
	String getRepresentation();

}