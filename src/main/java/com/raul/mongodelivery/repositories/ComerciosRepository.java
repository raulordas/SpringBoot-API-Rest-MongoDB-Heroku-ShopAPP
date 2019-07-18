package com.raul.mongodelivery.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.raul.mongodelivery.entities.Comercio;
import java.lang.String;
import java.util.List;

public interface ComerciosRepository extends MongoRepository<Comercio, Integer> {
	
	List<Comercio> findBy_id(String _id);
	
}
