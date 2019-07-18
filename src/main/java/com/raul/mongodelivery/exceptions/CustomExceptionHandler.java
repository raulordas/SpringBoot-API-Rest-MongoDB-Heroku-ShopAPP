package com.raul.mongodelivery.exceptions;

import java.util.Date;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@PropertySource("classpath:exceptions.properties")
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
	Logger logger = org.slf4j.LoggerFactory.getLogger(CustomExceptionHandler.class);
	
	@Autowired
	private CustomExceptionModel customExceptionModel;
	
	@Value(value="${exception.code.401}")
	private String UNAUTHORIZED_401;
	
	@Value(value="${exception.code.404}")
	private String NOT_FOUND_404;
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		customExceptionModel.setFecha(new Date());
		customExceptionModel.setEstado(status.value());
		customExceptionModel.setError(ex.getMessage());
		customExceptionModel.setMensaje(ex.getLocalizedMessage());
		
		return new ResponseEntity<Object>(customExceptionModel, status);
	}
	
	@ExceptionHandler({AccessDeniedException.class})
	public ResponseEntity<Object> handleAccessDenied(Exception ex, WebRequest request) {
		fillException(HttpStatus.UNAUTHORIZED.value(), UNAUTHORIZED_401, ex);
		return new ResponseEntity<Object>(customExceptionModel, HttpStatus.UNAUTHORIZED);
	}
	
	@ExceptionHandler({ResponseStatusException.class})
	public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
		
		String status = ex.getLocalizedMessage();
		HttpStatus code = null;
		
		if (status.equals(HttpStatus.UNAUTHORIZED.toString())) {
			code = HttpStatus.UNAUTHORIZED;
			fillException(HttpStatus.UNAUTHORIZED.value(), UNAUTHORIZED_401, ex);
		
		} else if (status.equals(HttpStatus.NOT_FOUND.toString())) {
			code = HttpStatus.NOT_FOUND;
			fillException(HttpStatus.NOT_FOUND.value(), NOT_FOUND_404, ex);
		}
		
		return new ResponseEntity<Object>(customExceptionModel, code);
	}
	
	private void fillException(int estado, String mensaje, Exception exception) {
		customExceptionModel.setEstado(estado);
		customExceptionModel.setMensaje(mensaje);
		customExceptionModel.setFecha(new Date());
		customExceptionModel.setError(exception.getMessage());
	}
	
}
