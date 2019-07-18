package com.raul.mongodelivery.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.raul.mongodelivery.entities.Pedido;
import com.raul.mongodelivery.repositories.PedidosRepository;

@Service
public class ServicePedidos {
	
	@Autowired
	private PedidosRepository pedidosRepository;
	
	public Pedido insertPedido(Pedido pedido) {
		return pedidosRepository.insert(pedido);
	}
	
	public List<Pedido> findAllPedidos() {
		return pedidosRepository.findAll();
	}
	
	public List<Pedido> findAllPedidosFromComercio(String comercioId) {
		return pedidosRepository.findByComercioId(comercioId);
	}
	
	public List<Pedido> findAllPedidosFromUsuario(String usuarioId) {
		return pedidosRepository.findByClienteId(usuarioId);
	}
	
	public List<Pedido> findOnePedido(String pedidoId) {
		return pedidosRepository.findBy_id(pedidoId);
	}
	
	public Pedido updatePedido(Pedido pedido) {
		return pedidosRepository.save(pedido);
	}
	
}
