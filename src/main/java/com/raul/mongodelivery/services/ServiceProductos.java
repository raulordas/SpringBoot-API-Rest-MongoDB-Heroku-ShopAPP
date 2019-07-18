package com.raul.mongodelivery.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.raul.mongodelivery.entities.Producto;
import com.raul.mongodelivery.repositories.ProductosRepository;

@Service
public class ServiceProductos {
	
	@Autowired
	private ProductosRepository productosRepository;

	public List<Producto> findAllProductos() {
		return productosRepository.findAll();
	}
	
	public Producto findProductoByStringId(String _id) {
		List<Producto> productos =  productosRepository.findBy_id(_id);
		
		if(productos.size() > 0) {
			return productos.get(0);
		} else {
			return null;
		}
	}
	
	public Producto findProductoById(Integer id) {
		Optional<Producto> producto = productosRepository.findById(id);
		
		if (producto.isPresent()) {
			return producto.get();
		} else {
			return null;
		}
	}
	
	public Producto saveProducto(Producto producto) {
		Producto res = productosRepository.save(producto);
		return res;
	}
	
	public void deleteProducto(Integer id) {
		productosRepository.deleteById(id);
	}
	
	public List<Producto> findAllProductosFromComercio(String comercioid) {
		return productosRepository.findByComercioId(comercioid);
	}
}
