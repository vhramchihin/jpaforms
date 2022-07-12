package com.example.jpaforms.services.models;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TableMetadataModel {
	
	private String name;
	private List<FieldMetadataModel> fieldsMetadata;

}
