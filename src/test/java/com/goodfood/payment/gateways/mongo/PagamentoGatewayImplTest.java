package com.goodfood.payment.gateways.mongo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.goodfood.payment.domain.Pagamento;
import com.goodfood.payment.domain.expcetions.PagamentoNaoEncontradoException;
import com.goodfood.payment.gateways.mongo.documents.PagamentoDocument;
import com.goodfood.payment.gateways.mongo.repositories.PagamentoRepository;

@ExtendWith(MockitoExtension.class)
class PagamentoGatewayImplTest {

    @InjectMocks
    private PagamentoGatewayImpl pagamentoGateway;

    @Mock
    private PagamentoRepository pagamentoRepository;

    @Test
    void deveSalvarPagamentoComSucesso() {
        Pagamento pagamento = Pagamento.builder()
                .idPedido("idPedido")
                .qrCode("qrCode")
                .valor(BigDecimal.TEN)
                .build();
        PagamentoDocument document = new PagamentoDocument(pagamento);
        when(pagamentoRepository.save(document)).thenReturn(document);

        Pagamento savedPagamento = pagamentoGateway.salvar(pagamento);

        assertNotNull(savedPagamento);
        assertEquals(pagamento.getIdPedido(), savedPagamento.getIdPedido());
        assertEquals(pagamento.getQrCode(), savedPagamento.getQrCode());
        assertEquals(pagamento.getValor(), savedPagamento.getValor());
    }

    @Test
    void deveObterPagamentoComSucesso() {
        String idPedido = "idPedido";
        PagamentoDocument document = new PagamentoDocument();
        document.setIdPedido(idPedido);
        when(pagamentoRepository.findByIdPedido(idPedido)).thenReturn(Optional.of(document));

        Pagamento pagamento = pagamentoGateway.obterPagamento(idPedido);

        assertNotNull(pagamento);
        assertEquals(idPedido, pagamento.getIdPedido());
    }

    @Test
    void deveLancarExcecaoQuandoPagamentoNaoForEncontrado() {
        String idPedido = "idPedido";
        when(pagamentoRepository.findByIdPedido(idPedido)).thenReturn(Optional.empty());

        assertThrows(PagamentoNaoEncontradoException.class, () -> pagamentoGateway.obterPagamento(idPedido));
    }
}
