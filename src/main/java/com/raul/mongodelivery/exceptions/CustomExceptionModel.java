package com.raul.mongodelivery.exceptions;

import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class CustomExceptionModel {
	private Date fecha;
	private int estado;
	private String error;
	private String mensaje;
	
	public CustomExceptionModel() {}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public int getEstado() {
		return estado;
	}

	public void setEstado(int estado) {
		this.estado = estado;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
}
