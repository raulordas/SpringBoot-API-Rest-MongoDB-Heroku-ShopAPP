package com.raul.mongodelivery.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.raul.mongodelivery.entities.Producto;
import com.raul.mongodelivery.services.ServiceProductos;

@RestController
@RequestMapping(path="/productos")
@CrossOrigin
public class ProductosController {
	
	@Autowired
	private ServiceProductos serviceProductos;
	
	@GetMapping
	public ResponseEntity<List<Producto>> findAllProductos() {
		return new ResponseEntity<List<Producto>>(serviceProductos.findAllProductos(), HttpStatus.OK);
	}
}
