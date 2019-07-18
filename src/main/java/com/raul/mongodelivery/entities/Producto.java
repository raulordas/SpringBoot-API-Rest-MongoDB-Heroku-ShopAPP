package com.raul.mongodelivery.entities;

import java.util.Date;
import javax.validation.constraints.NotNull;
import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="productos")
public class Producto {
	
	@Id
	private ObjectId _id;
	
	@NotNull
	private String nombre;
	
	@NotNull
	private String descripcion;
	
	@NotNull
	private double precio;
	
	@NotNull
	private boolean activo;
	
	private Binary imagen;
	
	private int stock;
	
	@CreatedDate
	private Date createdAt;
	
	private String comercioId;

	public Producto() {}
	
	public Producto(ObjectId _id, @NotNull String nombre, @NotNull String descripcion, @NotNull double precio,
			boolean activo, Binary imagen, int stock, Date createdAt, String comercioId) {
		this._id = _id;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.precio = precio;
		this.activo = activo;
		this.imagen = imagen;
		this.stock = stock;
		this.createdAt = createdAt;
		this.comercioId = comercioId;
	}

	public String get_id() {
		return _id.toHexString();
	}

	public void set_id(ObjectId _id) {
		this._id = _id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public Binary getImagen() {
		return imagen;
	}

	public void setImagen(Binary imagen) {
		this.imagen = imagen;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	
	public String getComercioId() {
		return comercioId;
	}

	public void setComercioId(String comercioId) {
		this.comercioId = comercioId;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}
}
