package com.raul.mongodelivery.entities;

import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotNull;

import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Document(collection="usuarios")
public class Usuario{
	
	@Id
	private ObjectId _id;
	
	@NotNull
	private String nombre;
	
	@NotNull
	private String apellidos;
	
	@NotNull
	@Indexed(unique=true)
	private String email;
	
	@NotNull
	@Indexed(unique=true)
	private String username;
	
	@NotNull
	@BsonIgnore
	private String password;
	
	private List<Authorities> authorities;
	
	@CreatedDate
	private Date createdAt;
	
	public Usuario() {}

	public Usuario(ObjectId _id, @NotNull String nombre, @NotNull String apellidos, @NotNull String email,
			@NotNull String username, @NotNull String password, List<Authorities> authorities, Date createdAt) {
		this._id = _id;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.email = email;
		this.username = username;
		this.password = password;
		this.authorities = authorities;
		this.createdAt = createdAt;
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

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = new BCryptPasswordEncoder().encode(password);
	}

	public List<Authorities> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(List<Authorities> authorities) {
		this.authorities = authorities;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
}
