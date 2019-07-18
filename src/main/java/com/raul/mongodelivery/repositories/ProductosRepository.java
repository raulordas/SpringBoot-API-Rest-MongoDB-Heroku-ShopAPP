package com.raul.mongodelivery.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.raul.mongodelivery.entities.Producto;
import java.lang.String;
import java.util.List;

public interface ProductosRepository extends MongoRepository<Producto, Integer> {
	
	List<Producto> findByComercioId(String comercioid);
	
	List<Producto> findBy_id(String _id);
	
}
