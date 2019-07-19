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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import com.raul.mongodelivery.entities.Authorities;
import com.raul.mongodelivery.entities.Usuario;
import com.raul.mongodelivery.services.ServiceUsuarios;

@RestController
@RequestMapping(path="/usuarios")
//@CrossOrigin
public class UsuariosController {
	
	private static final String ROLE_USER = "ROLE_USER";
	private static final String ROLE_ADMIN = "ROLE_ADMIN";
	private static final String ROLE_COMERCIO = "ROLE_COMERCIO";
	
	@Autowired
	private ServiceUsuarios serviceUsuarios;
	
	@Secured(ROLE_ADMIN)
	@GetMapping
	public ResponseEntity<List<Usuario>> findAllUsuarios() {		
		return new ResponseEntity<List<Usuario>>(serviceUsuarios.findAllUsuarios(), HttpStatus.OK);
	}
	
	@Secured(ROLE_ADMIN)
	@PostMapping
	public ResponseEntity<Usuario> insertUsuario(@RequestBody @Valid Usuario usuario) {
		Usuario userResult = serviceUsuarios.insertUsuario(usuario);
		
		if (userResult == null) {
			throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED);
		}
		
		return new ResponseEntity<Usuario>(userResult, HttpStatus.OK);	
	}
	
	@Secured({ROLE_COMERCIO, ROLE_USER})
	@GetMapping(path="/login")
	public ResponseEntity<Usuario> loginCliente() {
		String username = checkUsername();
		
		return new ResponseEntity<Usuario>(serviceUsuarios.findUsuarioByUsername(username), HttpStatus.OK);
	}
	
	@PostMapping(path="/signup")
	public ResponseEntity<Usuario> signUpCliente(@RequestBody @Valid Usuario usuario, @RequestParam(required=true) String rol) {
		usuario.setAuthorities(null);
		List<Authorities> authorities = new ArrayList<>();
		Authorities auth = null;
		boolean rolSuccess = false;
		
		if (rol.equals("ROLE_COMERCIO")) {
			auth = new Authorities();
			auth.setNombre(ROLE_COMERCIO);
			authorities.add(auth);
			usuario.setAuthorities(authorities);
			rolSuccess = true;
		}
		
		if (rol.equals("ROLE_USER")) {
			auth = new Authorities();
			auth.setNombre(ROLE_USER);
			authorities.add(auth);
			usuario.setAuthorities(authorities);
			rolSuccess = true;
		}
		
		if (!rolSuccess) {
			throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED);
		}
		
		Usuario userResult = serviceUsuarios.insertUsuario(usuario);
		
		if (userResult == null) {
			throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED);
		}
		
		return new ResponseEntity<Usuario>(userResult, HttpStatus.OK);
	}
	
	
	@Secured({ROLE_USER, ROLE_COMERCIO})
	@GetMapping(path="/{id}")
	public ResponseEntity<Usuario> findUsuarioById(@PathVariable String id) {
		
		//Se recupera el usuario del contexto de autenticación
		String username = checkUsername();
		
		/*
		 * Si se ha recuperado el usuario correctamente 
		 * invocamos al método que recupera el usuario 
		 * con el id solicitado en la petición
		 */
		if (username != null) {
			Usuario usuario = serviceUsuarios.findUsuarioById(id);
			
			/*
			 * Si se ha encontrado un cliente con dicho identificador
			 * comprobamos si coincide con el del usuario autenticado
			 */
			if (usuario != null) {
				
				if (usuario.getUsername().equals(username)) {
					return new ResponseEntity<Usuario>(usuario, HttpStatus.OK);
				
				} else {
					throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
				}
				
			} else {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND);
			}
			
		} else {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		}

	}
	
	@Secured({ROLE_COMERCIO, ROLE_USER})
	@PutMapping(path="/update/{id_usuario}")
	public ResponseEntity<Usuario> updateUsuarioByIdNoAdmin(@PathVariable String id_usuario, @RequestBody  @Valid Usuario userupdate) {
		String username = checkUsername();
		
		Usuario user = serviceUsuarios.findUsuarioByUsername(username);
		
		if (user == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		
		userupdate.setCreatedAt(user.getCreatedAt());
		userupdate.setAuthorities(user.getAuthorities());
		
		if (!user.get_id().equals(id_usuario)) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		}
		
		Usuario userResult = serviceUsuarios.updateUsuarioById(userupdate);
		
		if (userResult == null) {
			throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED);
		}
		
		return new ResponseEntity<Usuario>(userResult, HttpStatus.OK);
	}
	
	@Secured(ROLE_ADMIN)
	@PutMapping(path="/{id_usuario}")
	public ResponseEntity<Usuario> updateUsuarioById(@PathVariable String id_usuario, @RequestBody  @Valid Usuario userupdate) {
		String username = checkUsername();
		
		Usuario user = serviceUsuarios.findUsuarioByUsername(username);
		
		if (user == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		
		Usuario userFind = serviceUsuarios.findUsuarioById(id_usuario);
		
		if (userFind == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		
		userupdate.setCreatedAt(userFind.getCreatedAt());
		
		Usuario userResult = serviceUsuarios.updateUsuarioById(userupdate);
		
		if (userResult == null) {
			throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED);
		}
		
		return new ResponseEntity<Usuario>(userResult, HttpStatus.OK);
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
