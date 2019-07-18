package com.raul.mongodelivery.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.raul.mongodelivery.entities.Comercio;
import com.raul.mongodelivery.repositories.ComerciosRepository;

@Service
public class ServiceComercios {
	
	@Autowired
	private ComerciosRepository comerciosRepository;
	
	public Comercio insertComercio(Comercio comercio) {
		return comerciosRepository.insert(comercio);
	}
	
	public Comercio updateComercio(Comercio comercio) {
		
		
		return comerciosRepository.save(comercio);
	}
	
	public Comercio findComercioById(String id) {

		List<Comercio> comercio = comerciosRepository.findBy_id(id);
		
		if (comercio.size() > 0) {
			return comercio.get(0);
		} else {
			return null;
		}
	}
	
	public List<Comercio> findAllComercios() {
		return comerciosRepository.findAll();
	}
}
