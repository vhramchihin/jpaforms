package com.example.jpaforms.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.jpaforms.services.EntityMetadataService;
import com.example.jpaforms.services.models.EntityMetadataModel;

@RestController
public class MetadataController {
	
	@Autowired
	EntityMetadataService entityMetadataService;

	@GetMapping("/api/entity_metadata/{rel}")
	EntityMetadataModel getEntityMetadata(@PathVariable String rel) {
		return entityMetadataService.getEntityMetadata(rel);
	}
	
}
