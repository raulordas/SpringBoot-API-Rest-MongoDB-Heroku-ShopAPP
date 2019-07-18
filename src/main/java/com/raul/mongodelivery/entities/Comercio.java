package com.raul.mongodelivery.entities;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="comercios")
public class Comercio {
	
	@Id
	private ObjectId _id;
	
	@NotNull
	private String nombre;
	
	private String descripcion;
	
	@NotNull
	private String cif;
	
	@NotNull
	@Indexed(unique=true)
	private String direccion;
	
	@NotNull
	private int cod_postal;
	
	@NotNull
	private String municipio;
	
	@NotNull
	private String pais;
	
	@NotNull
	private int telefono;
	
	private String pagina_web;
	
	private Binary imagen;
	
	
	private String admin_restaurante;
	
	public Comercio() {}

	public Comercio(ObjectId _id, @NotNull String nombre, String descripcion, @NotNull String cif, String direccion,
			@NotNull @Size(max = 5, min = 5) int cod_postal, @NotNull String municipio, @NotNull String pais,
			@NotNull int telefono, String pagina_web, Binary imagen, @NotNull String admin_restaurante) {
		this._id = _id;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.cif = cif;
		this.direccion = direccion;
		this.cod_postal = cod_postal;
		this.municipio = municipio;
		this.pais = pais;
		this.telefono = telefono;
		this.pagina_web = pagina_web;
		this.imagen = imagen;
		this.admin_restaurante = admin_restaurante;
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

	public String getCif() {
		return cif;
	}

	public void setCif(String cif) {
		this.cif = cif;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public int getCod_postal() {
		return cod_postal;
	}

	public void setCod_postal(int cod_postal) {
		this.cod_postal = cod_postal;
	}

	public String getMunicipio() {
		return municipio;
	}

	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public int getTelefono() {
		return telefono;
	}

	public void setTelefono(int telefono) {
		this.telefono = telefono;
	}

	public String getPagina_web() {
		return pagina_web;
	}

	public void setPagina_web(String pagina_web) {
		this.pagina_web = pagina_web;
	}

	public Binary getImagen() {
		return imagen;
	}

	public void setImagen(Binary imagen) {
		this.imagen = imagen;
	}

	public String getAdmin_restaurante() {
		return admin_restaurante;
	}

	public void setAdmin_restaurante(String admin_restaurante) {
		this.admin_restaurante = admin_restaurante;
	}
}
