package com.raul.mongodelivery.controllers;

import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import com.raul.mongodelivery.entities.Comercio;
import com.raul.mongodelivery.entities.LineaPedido;
import com.raul.mongodelivery.entities.Pedido;
import com.raul.mongodelivery.entities.Producto;
import com.raul.mongodelivery.entities.Usuario;
import com.raul.mongodelivery.services.ServiceComercios;
import com.raul.mongodelivery.services.ServicePedidos;
import com.raul.mongodelivery.services.ServiceProductos;
import com.raul.mongodelivery.services.ServiceUsuarios;

@RestController
@CrossOrigin
public class PedidosController {
	
	private static final String ROLE_USER = "ROLE_USER";
	
	private static final String ROLE_ADMIN = "ROLE_ADMIN";
	
	private static final String ROLE_COMERCIO = "ROLE_COMERCIO";
	
	@Autowired
	private ServiceUsuarios serviceUsuarios;
	
	@Autowired
	private ServicePedidos servicePedidos;
	
	@Autowired
	private ServiceComercios serviceComercios;
	
	@Autowired
	private ServiceProductos serviceProductos;
	
	@Secured(ROLE_USER)
	@PostMapping(path="/usuarios/{id_usuario}/comercios/{id_comercio}/pedidos")
	public ResponseEntity<Pedido> insertPedido(@PathVariable String id_usuario, @PathVariable String id_comercio,
			@RequestBody @Valid Pedido pedido) {
		
		//Comprobamos el usuario autenticado en la sesion
		String username = checkUsername();
		
		if (username == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		}
		
		//Comprobamos si el usuario sobre el que se realiza solicitud existe en la base de datos
		Usuario usuario = serviceUsuarios.findUsuarioByUsername(username);
		
		if (usuario == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		
		//Comprobamos si el usuario recuperado el el mismo que  el autenticado
		if (!usuario.get_id().equals(id_usuario)) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		}
		
		//Comprobamos si existe el comercio facilitado en el pathVariable
		Comercio comercio = serviceComercios.findComercioById(id_comercio);
		
		if(comercio == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		
		//Comprobamos si el pedido tiene contenido
		if (pedido.getLineas_pedido().size() == 0) {
			throw new ResponseStatusException(HttpStatus.NO_CONTENT);
		}
		
		ArrayList<Producto> listaProductos = new ArrayList<>();
		
		//Comprobamos si las lineas de pedido corresponden a productos con stock y que corresponden al comercio facilitado
		for (LineaPedido lineaPedido: pedido.getLineas_pedido()) {
			Producto producto = serviceProductos.findProductoByStringId(lineaPedido.getProducto_id());
			
			if (producto == null) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND);
			}
			
			if (!producto.getComercioId().equals(comercio.get_id())) {
				throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE);
			}
			
			if (producto.getStock() - lineaPedido.getCantidad() < 0 ) {
				throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE);
			}
			
			listaProductos.add(producto);
		}
		
		//Aseguramos el stock y realizamos los calculos
		int subtotal  = 0;
		
		for (LineaPedido lineaPedido: pedido.getLineas_pedido()) {
			for (int i = 0; i < listaProductos.size(); i++) {	
				if(listaProductos.get(i).get_id().equals(lineaPedido.getProducto_id())) {
					listaProductos.get(i).setStock(listaProductos.get(i).getStock() - lineaPedido.getCantidad());
					subtotal += listaProductos.get(i).getPrecio() * lineaPedido.getCantidad(); 
				}
			}
		}
	
		//Persistimos la rebaja de stock en los productos
		for (Producto aux: listaProductos) {
			serviceProductos.saveProducto(aux);
		}
		
		//Aportamos al pedido la información necesaria
		pedido.setClienteId(usuario.get_id());
		pedido.setComercioId(comercio.get_id());
		pedido.setIva(1.21);
		pedido.setSubtotal(subtotal);
		pedido.setTotal(pedido.getSubtotal() * pedido.getIva());
		
		//Persistimos el pedido
		Pedido pedidoRes = servicePedidos.insertPedido(pedido);
		
		return new ResponseEntity<Pedido>(pedidoRes, HttpStatus.OK);
	}
	
	@GetMapping(path="/pedidos")
	@Secured(ROLE_ADMIN)
	public ResponseEntity<List<Pedido>> findAllPedidos() {
		return new ResponseEntity<List<Pedido>>(servicePedidos.findAllPedidos(), HttpStatus.OK);
	}
	
	@GetMapping(path="usuarios/{id_usuario}/pedidos")
	@Secured(ROLE_USER)
	public ResponseEntity<List<Pedido>> findAllPedidosFromUsuario(@PathVariable String id_usuario) {
		String username = checkUsername();
		
		Usuario user = serviceUsuarios.findUsuarioByUsername(username);
		
		if (user == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		
		if (!user.get_id().equals(id_usuario)) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		}
		
		return new ResponseEntity<List<Pedido>>(servicePedidos.findAllPedidosFromUsuario(id_usuario), HttpStatus.OK);
	}
	
	@GetMapping(path="usuarios/{id_usuario}/comercios/{id_comercio}/pedidos")
	@Secured(ROLE_COMERCIO)
	public ResponseEntity<List<Pedido>> findAllPedidosFromComercio(@PathVariable String id_usuario, @PathVariable String id_comercio) {
		String username = checkUsername();
		
		Usuario user = serviceUsuarios.findUsuarioByUsername(username);
		
		if (user == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		
		if (!user.get_id().equals(id_usuario)) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		}
		
		
		return new ResponseEntity<List<Pedido>>(servicePedidos.findAllPedidosFromComercio(id_comercio), HttpStatus.OK);
	}
	
	@Secured(ROLE_USER)
	@PutMapping(path="/usuarios/{id_usuario}/pedidos/{id_pedido}")
	public ResponseEntity<Pedido> valorarPedido(@PathVariable String id_usuario, @PathVariable String id_pedido, @RequestParam(required=true) int valoracion) {
		String username = checkUsername();
		
		Usuario userAuth = serviceUsuarios.findUsuarioByUsername(username);
		
		if (userAuth == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		}
		
		
		if (!userAuth.get_id().equals(id_usuario)) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		}
		
		if (valoracion < 0 || valoracion > 10) {
			throw new ResponseStatusException(HttpStatus.CONFLICT);
		}
		
		List<Pedido> pedidos = servicePedidos.findOnePedido(id_pedido);
		Pedido pedido = null;
		
		if (pedidos.size() == 0) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		} else {
			pedido = pedidos.get(0);
		}
		
		if (!pedido.getClienteId().equals(id_usuario)) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		} else {
			pedido.setValoracion(valoracion);
			servicePedidos.updatePedido(pedido);
		}
		
		return new ResponseEntity<Pedido>(pedido, HttpStatus.OK);
	}
	
	private String checkUsername() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = null;
		
		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
		} 
		return username;
	}
}
