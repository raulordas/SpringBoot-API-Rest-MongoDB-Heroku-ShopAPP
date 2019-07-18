package com.raul.mongodelivery.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.raul.mongodelivery.entities.Pedido;
import java.lang.String;
import java.util.List;

public interface PedidosRepository extends MongoRepository<Pedido, Integer> {
	
	List<Pedido> findByClienteId(String clienteId);
	List<Pedido> findByComercioId(String comercioId);
	List<Pedido> findBy_id(String _id);
}
