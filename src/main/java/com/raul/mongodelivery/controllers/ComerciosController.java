package com.raul.mongodelivery.controllers;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.raul.mongodelivery.entities.Comercio;
import com.raul.mongodelivery.entities.Pedido;
import com.raul.mongodelivery.entities.Producto;
import com.raul.mongodelivery.entities.Usuario;
import com.raul.mongodelivery.entities.Valoracion;
import com.raul.mongodelivery.services.ServiceComercios;
import com.raul.mongodelivery.services.ServicePedidos;
import com.raul.mongodelivery.services.ServiceProductos;
import com.raul.mongodelivery.services.ServiceUsuarios;

@RestController
@RequestMapping(path="/comercios")
public class ComerciosController {

	@Autowired
	private ServiceUsuarios serviceUsuarios;

	@Autowired
	private ServiceProductos serviceProductos;

	@Autowired
	private ServiceComercios serviceComercios;
	
	@Autowired
	private ServicePedidos servicePedidos;

	private static final String ROLE_COMERCIO = "ROLE_COMERCIO";
	
	/*
	 * Peticiones PUT Y POST relacionadas con la creación 
	 * y modificación de comercios y productos en el Api
	 */
	
	@Secured({ROLE_COMERCIO})
	@PostMapping(consumes={"multipart/form-data"})
	public ResponseEntity<Object> saveComercio(@RequestPart("comercio") @Valid Comercio comercio, @RequestPart("imagen") MultipartFile imagen) throws IOException {
		Binary img = new Binary(BsonBinarySubType.BINARY, imagen.getBytes());
		comercio.setImagen(img);
		
		Usuario usuario = serviceUsuarios.findUsuarioByUsername(checkUsername());
		
		if (usuario != null) {
			comercio.setAdmin_restaurante(usuario.get_id());
		} else {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		}
		
		Comercio comercioResult = serviceComercios.insertComercio(comercio);
		
		if (comercioResult != null) {
			URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
					.path("/comercios/{id_restaurante}")
					.buildAndExpand(comercio.get_id()).toUri();
			
			return ResponseEntity.created(uri).build();
		
		} else {
			throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED);
		}
	}
	
	@Secured({ROLE_COMERCIO})
	@PutMapping(path="/{id_comercio}", consumes={"multipart/form-data"})
	public ResponseEntity<Comercio> updateComercio(@PathVariable String id_comercio, @RequestPart("comercio") @Valid Comercio comercio, @RequestPart(name="imagen", required=false) MultipartFile imagen ) throws IOException {
		
		if (comercioExistsAndUserIsAdmin(id_comercio)) {
			comercio.set_id(new ObjectId(id_comercio));
			
			Usuario usuario = serviceUsuarios.findUsuarioByUsername(checkUsername());
			
			if (usuario != null) {
				comercio.setAdmin_restaurante(usuario.get_id());
				comercio.setImagen(new Binary(BsonBinarySubType.BINARY, imagen.getBytes()));
				
			} else {
				throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
			}
			
			Comercio comercioResult = serviceComercios.updateComercio(comercio);
			
			return new ResponseEntity<Comercio>(comercioResult, HttpStatus.OK);
			
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}
	
	@Secured(ROLE_COMERCIO)
	@PostMapping(path = "/{id_comercio}/productos", consumes={"multipart/form-data"})
	public ResponseEntity<Object> saveProducto(@PathVariable String id_comercio,
			@RequestPart("producto") @Valid Producto producto, @RequestPart(required=false, name="imagen") MultipartFile imagen) throws URISyntaxException, IOException {
		URI uri = null;

		if (comercioExistsAndUserIsAdmin(id_comercio)) {

			// Adjuntamos al objeto producto el id del restaurante asociado
			producto.setComercioId(id_comercio);
			producto.setImagen(new Binary(BsonBinarySubType.BINARY, imagen.getBytes()));
			Producto productoResult = serviceProductos.saveProducto(producto);

			if (productoResult != null) {

				// Generamos la uri con la localizacion del producto
				uri = ServletUriComponentsBuilder.fromCurrentContextPath()
						.path("/comercios/{id_comercio}/productos/{id_producto}")
						.buildAndExpand(id_comercio, productoResult.get_id()).toUri();
			}

			return ResponseEntity.created(uri).build();
		} else {
			
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}
	
	@Secured(ROLE_COMERCIO)
	@PutMapping(path = "/{id_comercio}/productos/{id_producto}", consumes={"multipart/form-data"})
	public ResponseEntity<Producto> updateProducto(@PathVariable String id_comercio, @PathVariable String id_producto,
			@RequestPart("producto") @Valid Producto producto, @RequestPart(required=false, name="imagen") MultipartFile imagen) throws URISyntaxException, IOException {
		

		if (!comercioExistsAndUserIsAdmin(id_comercio)) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		}
		
		Producto productQuery = serviceProductos.findProductoByStringId(id_producto);
		
		if (productQuery == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		
		if (!productQuery.getComercioId().equals(id_comercio)) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		}
		
		producto.set_id(new ObjectId(productQuery.get_id()));
		producto.setComercioId(productQuery.getComercioId());
		producto.setCreatedAt(productQuery.getCreatedAt());
		producto.setImagen(new Binary(BsonBinarySubType.BINARY, imagen.getBytes()));
		
		Producto prodResult = serviceProductos.saveProducto(producto);
		
		return new ResponseEntity<Producto>(prodResult, HttpStatus.OK);
	}
	
	
	
	/*
	 * Peticiones GET relacionadas con la recuperación
	 * de Comercios y Productos
	 */
	
	@GetMapping()
	public ResponseEntity<List<Comercio>> findAllComercios() {
		return new ResponseEntity<List<Comercio>>(serviceComercios.findAllComercios(), HttpStatus.OK);
	}
	
	
	@GetMapping(path="/{id_comercio}")
	public ResponseEntity<Comercio> findComercioById(@PathVariable String id_comercio) {
		
		Comercio comercio = serviceComercios.findComercioById(id_comercio);
		
		if (comercio != null) {
			return new ResponseEntity<Comercio>(serviceComercios.findComercioById(id_comercio), HttpStatus.OK);
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping(path="/{id_comercio}/productos")
	public ResponseEntity<List<Producto>> findAllProductosFromComercio(@PathVariable String id_comercio) {
		Comercio comercio = serviceComercios.findComercioById(id_comercio);
		
		// Si existe el comercio comprobamos que corresponde al usuario autenticado
		if (comercio != null) {
			List<Producto> productos = serviceProductos.findAllProductosFromComercio(comercio.get_id());
			return new ResponseEntity<List<Producto>>(productos, HttpStatus.OK);
		
		} else { 
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}
	
	private boolean comercioExistsAndUserIsAdmin(String id_comercio) throws ResponseStatusException {
		// Comprobamos que existe el comercio en el id del path
		Comercio comercio = serviceComercios.findComercioById(id_comercio);

		// Si existe el comercio comprobamos que corresponde al usuario autenticado
		if (comercio != null) {
			String username = checkUsername();

			// Recuperamos el usuario de la base de datos
			Usuario user = serviceUsuarios.findUsuarioByUsername(username);

			// Si el usuario existe comprobamos sus authorities
			if (user != null) {

				// Si el usuario autenticado es el administrador del restaurante
				if (user.get_id().equals(comercio.getAdmin_restaurante())) {
					return true;
				} else {
					throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
				}
			} else {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND);
			}
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}
	
	@Secured(ROLE_COMERCIO)
	@PostMapping(path="/{id_comercio}/productos/{id_producto}/stock")
	public ResponseEntity<Producto> insertStockInProducto(@PathVariable String id_comercio, @PathVariable String id_producto, @RequestParam int stock) {
		String username = checkUsername();
		
		Comercio comercio = serviceComercios.findComercioById(id_comercio);
		
		if (comercio == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		
		Usuario user = serviceUsuarios.findUsuarioByUsername(username);
		
		if (user == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		
		if (!user.get_id().equals(comercio.getAdmin_restaurante())) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		}
		
		Producto producto = serviceProductos.findProductoByStringId(id_producto);
		
		if (producto == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		
		producto.setStock(producto.getStock() + stock);
		
		Producto productoResult = serviceProductos.saveProducto(producto);
		
		return new ResponseEntity<Producto>(productoResult, HttpStatus.OK);
	}
	
	@GetMapping(path="/valoraciones")
	public ResponseEntity<ArrayList<Valoracion>> findAllValoracionesOfComercios() {
		
		//Obtenemos todos los pedidos del sistema
		List<Pedido> pedidos = servicePedidos.findAllPedidos();
		
		//Generamos un hashmap para organizar los pedidos por comercio
		HashMap<String, ArrayList<Pedido>> map = new HashMap<>();
		
		for (Pedido aux: pedidos) {
			
			if (map.containsKey(aux.getComercioId())) {
				ArrayList<Pedido> auxPedidos = map.get(aux.getComercioId());
				
				if (aux.getValoracion() >= 0) {
					auxPedidos.add(aux);
					map.put(aux.getComercioId(), auxPedidos);
				}
				
				
			} else {
				ArrayList<Pedido> newListPedidos = new ArrayList<>();
				
				if (aux.getValoracion() >= 0) {
					newListPedidos.add(aux);
					map.put(aux.getComercioId(), newListPedidos);
				}
			}
		}
		
		//Generamos un array para guardar los comercios con su valoracion
		ArrayList<Valoracion> valoraciones = new ArrayList<>();
		
		//Iteramos el mapa accediendo a los pedidos de cada comercio y calculamos su valoracion final
		for(Map.Entry<String, ArrayList<Pedido>> element : map.entrySet() ) {
			Comercio com = serviceComercios.findComercioById(element.getKey());
			Valoracion val = new Valoracion();
			val.set_id(new ObjectId(com.get_id()));
			val.setAdmin_restaurante(com.getAdmin_restaurante());
			val.setCif(com.getCif());
			val.setCod_postal(com.getCod_postal());
			val.setDescripcion(com.getDescripcion());
			val.setDireccion(com.getDireccion());
			val.setImagen(com.getImagen());
			val.setMunicipio(com.getMunicipio());
			val.setNombre(com.getNombre());
			val.setPagina_web(com.getPagina_web());
			val.setPais(com.getPais());
			val.setTelefono(com.getTelefono());
			val.calcularValoracionTotal(element.getValue());
			
			if (val.getValoracion() >= 0) {
				valoraciones.add(val);
			}
		}
		
		return new ResponseEntity<ArrayList<Valoracion>>(valoraciones, HttpStatus.OK);
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
