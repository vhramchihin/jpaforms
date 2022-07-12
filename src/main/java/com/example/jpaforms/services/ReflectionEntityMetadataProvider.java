package com.example.jpaforms.services;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.example.jpaforms.services.models.FieldControlType;
import com.example.jpaforms.services.models.FieldMetadataModel;
import com.example.jpaforms.services.models.TableMetadataModel;

@Service
@Primary
public class ReflectionEntityMetadataProvider implements EntityMetadataProvider {

	@Override
	public List<FieldMetadataModel> determineFieldsMetadata(Class<?> entityClass, Map<Class<?>, String> classRelMap) {

		List<FieldMetadataModel> result = new ArrayList<>();
		
		List<Field> fields = getAllFields(new ArrayList<>(), entityClass);
		
		for(Field field: fields) {
			
			if(field.getName().equals("class") || field.getName().equals("id")) continue;

			Class<?> fieldClass = field.getType();
			if(List.class.isAssignableFrom(fieldClass)) continue;
				
			FieldMetadataModel fieldMetadataModel = new FieldMetadataModel();
			fieldMetadataModel.setName(field.getName());
			if(fieldClass.equals(String.class)) {
				fieldMetadataModel.setControlType(FieldControlType.STRING);
			}
			else if(fieldClass.equals(Integer.class)
				|| fieldClass.equals(Long.class)
				|| fieldClass.equals(BigInteger.class)) {
				fieldMetadataModel.setControlType(FieldControlType.NUMBER);
			}
			else if(fieldClass.equals(Integer.TYPE)
				|| fieldClass.equals(Long.TYPE)) {
				fieldMetadataModel.setControlType(FieldControlType.NUMBER);
			}
			else if(fieldClass.equals(BigDecimal.class)) {
				fieldMetadataModel.setControlType(FieldControlType.NUMBER);
			}
			else if(fieldClass.equals(Date.class)) {
				fieldMetadataModel.setControlType(FieldControlType.DATE);
			}
			else if(classRelMap.containsKey(fieldClass)) {
				fieldMetadataModel.setControlType(FieldControlType.REFERENCE);
				fieldMetadataModel.setTypeRel(classRelMap.get(fieldClass));
			}
			result.add(fieldMetadataModel);			
			
		}
		
		return result;
		
	}

	@Override
	public List<TableMetadataModel> determineTablesMetadata(Class<?> entityClass, Map<Class<?>, String> classRelMap) {

		List<TableMetadataModel> result = new ArrayList<>(); 
		
		List<Field> fields = getAllFields(new ArrayList<>(), entityClass);
		
		for(Field field: fields) {
			
			Class<?> fieldClass = field.getType();
			if(List.class.isAssignableFrom(fieldClass)) {
				
				ParameterizedType listType = (ParameterizedType) field.getGenericType();
		        Class<?> listClass = (Class<?>) listType.getActualTypeArguments()[0];
		        
		        TableMetadataModel tableMetadata = new TableMetadataModel();
		        tableMetadata.setName(field.getName());
		        tableMetadata.setFieldsMetadata(determineFieldsMetadata(listClass, classRelMap));
				result.add(tableMetadata);
				
			}
			
		}
		
		return result;
		
	}
	
	public static List<Field> getAllFields(List<Field> fields, Class<?> type) {
		
		if (type.getSuperclass() != null) {
	        getAllFields(fields, type.getSuperclass());
	    }
		
	    fields.addAll(Arrays.asList(type.getDeclaredFields()));

	    return fields;
	}
	
	

}

