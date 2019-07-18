package com.raul.mongodelivery.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.raul.mongodelivery.entities.Usuario;
import java.lang.String;
import java.util.List;

public interface UsuariosRepository extends MongoRepository<Usuario, Integer> {
	
	List<Usuario> findByUsername(String username);
	
	List<Usuario> findBy_id(String _id);

}
