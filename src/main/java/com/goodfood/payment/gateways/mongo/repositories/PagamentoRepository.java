package com.goodfood.payment.gateways.mongo.repositories;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.goodfood.payment.gateways.mongo.documents.PagamentoDocument;

public interface PagamentoRepository extends MongoRepository<PagamentoDocument, String> {

  Optional<PagamentoDocument> findByIdPedido(String idPedido);
  
}
