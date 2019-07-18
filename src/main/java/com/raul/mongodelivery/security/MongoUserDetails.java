package com.raul.mongodelivery.security;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import com.raul.mongodelivery.entities.Usuario;
import com.raul.mongodelivery.services.ServiceUsuarios;

@Component
public class MongoUserDetails implements UserDetailsService {
	
	@Autowired
	private ServiceUsuarios serviceUsuarios;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		Usuario usuario = serviceUsuarios.findUsuarioByUsername(username);
		usuario.getAuthorities().stream().forEach(rol -> authorities.add(new SimpleGrantedAuthority(rol.getNombre())));
		
		return new User(usuario.getUsername(), usuario.getPassword(), authorities);
	}
}
