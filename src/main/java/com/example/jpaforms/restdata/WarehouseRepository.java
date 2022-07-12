package com.example.jpaforms.restdata;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.example.jpaforms.persistence.catalogs.Warehouse;

@RepositoryRestResource(excerptProjection = EntityProjection.class)
public interface WarehouseRepository extends CrudRepository<Warehouse, Long> {

}
