package com.raul.mongodelivery.entities;

import javax.validation.constraints.NotNull;

public class LineaPedido {
	
	@NotNull
	private String producto_id;
	
	@NotNull
	private int cantidad;
	
	public LineaPedido() {}

	public LineaPedido(String producto_id, int cantidad) {
		this.producto_id = producto_id;
		this.cantidad = cantidad;
	}

	public String getProducto_id() {
		return producto_id;
	}

	public void setProducto_id(String producto_id) {
		this.producto_id = producto_id;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
}
