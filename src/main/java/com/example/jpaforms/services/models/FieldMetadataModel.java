package com.example.jpaforms.services.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FieldMetadataModel {

	private String name;
	private FieldControlType controlType;
	private String typeRel;
	
}
