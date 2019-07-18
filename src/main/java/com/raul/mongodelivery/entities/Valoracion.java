package com.raul.mongodelivery.entities;

import java.util.ArrayList;

public class Valoracion extends Comercio {
	
	private double valoracion = -1;;

	public Valoracion() {}

	public Valoracion(double valoracion) {
		super();
		this.valoracion = valoracion;
	}

	public double getValoracion() {
		return valoracion;
	}

	public void setValoracion(double valoracion) {
		this.valoracion = valoracion;
	}
	
	public void calcularValoracionTotal(ArrayList<Pedido> pedidos) {
		double acumulado = 0;
		
		for (Pedido aux : pedidos) {
			acumulado += aux.getValoracion();
		}
		
		this.valoracion = acumulado / pedidos.size();
	}
}
