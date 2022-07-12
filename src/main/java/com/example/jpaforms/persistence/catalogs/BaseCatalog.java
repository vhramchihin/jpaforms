package com.example.jpaforms.persistence.catalogs;

import javax.persistence.MappedSuperclass;

import com.example.jpaforms.persistence.BaseEntity;

@MappedSuperclass
public abstract class BaseCatalog extends BaseEntity {
	
	protected String title;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return title;
	}

}
