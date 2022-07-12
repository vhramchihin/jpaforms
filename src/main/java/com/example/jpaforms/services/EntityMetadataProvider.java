package com.example.jpaforms.services;

import java.util.List;
import java.util.Map;

import com.example.jpaforms.services.models.FieldMetadataModel;
import com.example.jpaforms.services.models.TableMetadataModel;

public interface EntityMetadataProvider {

	public List<FieldMetadataModel> determineFieldsMetadata(Class<?> entityClass, Map<Class<?>, String> classRelMap);
	
	public List<TableMetadataModel> determineTablesMetadata(Class<?> entityClass, Map<Class<?>, String> classRelMap);
	
}
