package com.example.jpaforms.persistence.operations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.example.jpaforms.persistence.catalogs.Warehouse;

@Entity
@Table(name="sale")
public class Sale extends BaseOperation {
	
	@ManyToOne
	private Warehouse warehouse;
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "operation_id")
	private List<SaleTableLine> commodities = new ArrayList<>();

	@Override
	public List<? extends BaseOperationTableLine> tableByName(String tableName) {
		if(tableName.equals("commodities")) return commodities;
		else throw new IllegalArgumentException("No such table in this operation");
	}

	@Override
	public Map<String, List<? extends BaseOperationTableLine>> allTables() {
		Map<String, List<? extends BaseOperationTableLine>> result = new HashMap<String, List<? extends BaseOperationTableLine>>();
		result.put("commodities", commodities);
		return result;
	}

	@Override
	public BaseOperationTableLine addTableLine(String tableName) {
		if(tableName.equals("commodities")) {
			SaleTableLine newLine = new SaleTableLine();
			commodities.add(newLine);
			return newLine;
		}
		else {
			throw new IllegalArgumentException("No such table in this operation " + tableName);
		}
	}

	public Warehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}

	public List<SaleTableLine> getCommodities() {
		return commodities;
	}

	public void setCommodities(List<SaleTableLine> commodities) {
		this.commodities = commodities;
	}

}
