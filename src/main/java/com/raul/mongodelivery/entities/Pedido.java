package com.raul.mongodelivery.entities;

import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;

public class Pedido {
	private ObjectId _id;
	
	@CreatedDate
	private Date fecha;
	
	private double total;

	private double subtotal;
	
	private double iva;
	
	private String clienteId;
	
	private String comercioId;
	
	private int valoracion = -1;
	
	@NotNull
	private List<LineaPedido> lineas_pedido;
	
	public Pedido() {}

	public Pedido(ObjectId _id, Date fecha, double total, double subtotal, double iva, String clienteId, String comercioId,
			int valoracion, @NotNull List<LineaPedido> lineas_pedido) {
		super();
		this._id = _id;
		this.fecha = fecha;
		this.total = total;
		this.subtotal = subtotal;
		this.iva = iva;
		this.clienteId = clienteId;
		this.comercioId = comercioId;
		this.valoracion = valoracion;
		this.lineas_pedido = lineas_pedido;
	}

	public String get_id() {
		return _id.toHexString();
	}

	public void set_id(ObjectId _id) {
		this._id = _id;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public double getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}

	public double getIva() {
		return iva;
	}

	public void setIva(double iva) {
		this.iva = iva;
	}

	public String getClienteId() {
		return clienteId;
	}

	public void setClienteId(String clienteId) {
		this.clienteId = clienteId;
	}

	public String getComercioId() {
		return comercioId;
	}

	public void setComercioId(String comercioId) {
		this.comercioId = comercioId;
	}

	public int getValoracion() {
		return valoracion;
	}

	public void setValoracion(int valoracion) {
		this.valoracion = valoracion;
	}

	public List<LineaPedido> getLineas_pedido() {
		return lineas_pedido;
	}

	public void setLineasPedido(List<LineaPedido> lineas_pedido) {
		this.lineas_pedido = lineas_pedido;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public void setLineas_pedido(List<LineaPedido> lineas_pedido) {
		this.lineas_pedido = lineas_pedido;
	}
	
}
