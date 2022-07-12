package com.example.jpaforms.persistence.operations;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.MappedSuperclass;

import com.example.jpaforms.persistence.BaseEntity;

@MappedSuperclass
public abstract class BaseOperation extends BaseEntity {
		
		protected Date date;
		
		public Date getDate() {
			return date;
		}

		public void setDate(Date date) {
			this.date = date;
		}
		
		@Override
		public String toString() {
			return this.getClass().getSimpleName() + " " + new SimpleDateFormat("dd-MM-yyyy").format(date);
		}

		public abstract List<? extends BaseOperationTableLine> tableByName(String tableName);
		public abstract Map<String, List<? extends BaseOperationTableLine>> allTables();
		public abstract BaseOperationTableLine addTableLine(String tableName);
		
	}
