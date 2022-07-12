package com.example.jpaforms.services;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.jpaforms.services.models.FieldControlType;
import com.example.jpaforms.services.models.FieldMetadataModel;
import com.example.jpaforms.services.models.TableMetadataModel;

@Service
public class IntrospectionEntityMetadataProvider implements EntityMetadataProvider {

	@Override
	public List<FieldMetadataModel> determineFieldsMetadata(Class<?> entityClass, Map<Class<?>, String> classRelMap) {

		List<FieldMetadataModel> result = new ArrayList<>();
		
		BeanInfo beanInfo;
		try {
			beanInfo = Introspector.getBeanInfo(entityClass);
			PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
			for(PropertyDescriptor descriptor: descriptors) {
				
				if(descriptor.getName().equals("class") || descriptor.getName().equals("id")) continue;

				Class<?> fieldClass = descriptor.getPropertyType();
				if(List.class.isAssignableFrom(fieldClass)) continue;
					
				FieldMetadataModel fieldMetadataModel = new FieldMetadataModel();
				fieldMetadataModel.setName(descriptor.getName());
				if(fieldClass.equals(String.class)) {
					fieldMetadataModel.setControlType(FieldControlType.STRING);
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
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}

		return result;
		
	}

	@Override
	public List<TableMetadataModel> determineTablesMetadata(Class<?> entityClass, Map<Class<?>, String> classRelMap) {

		List<TableMetadataModel> result = new ArrayList<>(); 
		
		BeanInfo beanInfo;
		try {
			beanInfo = Introspector.getBeanInfo(entityClass);
			PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
			for(PropertyDescriptor descriptor: descriptors) {
				
				Class<?> fieldClass = descriptor.getPropertyType();
				if(List.class.isAssignableFrom(fieldClass)) {
					ParameterizedType listType = (ParameterizedType) descriptor.getReadMethod().getGenericReturnType();
			        Class<?> listClass = (Class<?>) listType.getActualTypeArguments()[0];
			        
			        TableMetadataModel tableMetadata = new TableMetadataModel();
			        tableMetadata.setName(descriptor.getName());
			        tableMetadata.setFieldsMetadata(determineFieldsMetadata(listClass, classRelMap));
					result.add(tableMetadata);
				}

			}
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}

		return result;
		
	}

}
