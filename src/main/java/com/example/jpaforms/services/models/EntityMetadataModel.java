package com.example.jpaforms.services.models;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EntityMetadataModel {

	private String rel;
	private List<FieldMetadataModel> fieldsMetadata;
	private List<TableMetadataModel> tablesMetadata;
	
}
