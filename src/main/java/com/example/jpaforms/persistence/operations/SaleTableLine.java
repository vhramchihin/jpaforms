package com.example.jpaforms.persistence.operations;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.example.jpaforms.persistence.catalogs.Product;

@Entity
@Table(name="sale_table_line")
public class SaleTableLine extends BaseOperationTableLine {
	
	@ManyToOne
	private Product product;
	private int quantity;
	private BigDecimal amount;
	
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

}
