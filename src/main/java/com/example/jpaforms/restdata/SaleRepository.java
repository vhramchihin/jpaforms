package com.example.jpaforms.restdata;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.example.jpaforms.persistence.operations.Sale;

@RepositoryRestResource(excerptProjection = EntityProjection.class)
public interface SaleRepository extends CrudRepository<Sale, Long> {

}