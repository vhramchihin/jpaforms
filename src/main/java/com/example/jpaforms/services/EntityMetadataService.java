package com.example.jpaforms.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.repository.support.Repositories;
import org.springframework.data.rest.core.mapping.RepositoryResourceMappings;
import org.springframework.data.rest.core.mapping.ResourceMetadata;
import org.springframework.stereotype.Service;

import com.example.jpaforms.services.models.EntityMetadataModel;

@Service
public class EntityMetadataService {
	
	@Autowired
	private ApplicationContext applicationContext;
	@Autowired	
	private EntityMetadataProvider entityMetadataProvider; 

	public EntityMetadataModel getEntityMetadata(String rel) {
		
		EntityMetadataModel result = new EntityMetadataModel();
		result.setRel(rel);
		
		Map<Class<?>, String> classRelMap = new HashMap<>();
		Class<?> entityClass = null;
		
        Repositories repositories = applicationContext.getBean(Repositories.class);
        RepositoryResourceMappings resourceMappings = applicationContext.getBean(RepositoryResourceMappings.class);
        for(Class<?> currentClass:repositories) {
        	
        	ResourceMetadata metadata = resourceMappings.getMetadataFor(currentClass);
        	classRelMap.put(currentClass, metadata.getRel().value());
            
            if(metadata.getRel().value().equals(rel)) {
            	entityClass = currentClass;
            }
            
        }
        
        result.setFieldsMetadata(entityMetadataProvider.determineFieldsMetadata(entityClass, classRelMap));
        result.setTablesMetadata(entityMetadataProvider.determineTablesMetadata(entityClass, classRelMap));
		
		return result;
		
	}
	
}
