package com.raul.mongodelivery.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.raul.mongodelivery.entities.Usuario;
import com.raul.mongodelivery.repositories.UsuariosRepository;

@Service
public class ServiceUsuarios {
	
	@Autowired
	private UsuariosRepository usuariosRepository;
	
	
	public List<Usuario> findAllUsuarios() {
		return usuariosRepository.findAll();
	}
	
	public Usuario insertUsuario(Usuario usuario) {
		return usuariosRepository.insert(usuario);
	}
	
	public Usuario findUsuarioById(String id) {
		List<Usuario> usuarios = usuariosRepository.findBy_id(id);
		
		if (usuarios.isEmpty()) {
			return null;
		} else {
			return usuarios.get(0);
		}
	}
	
	public Usuario findUsuarioByUsername(String username) {
		
		List<Usuario> usuario = usuariosRepository.findByUsername(username);
		
		if (usuario.isEmpty()) {
			return null;
		} else {
			return usuario.get(0);
		}
	}
	
	public Usuario updateUsuarioById(Usuario usuario) {
		Usuario user = usuariosRepository.save(usuario);
		return user;
	}
	
}
