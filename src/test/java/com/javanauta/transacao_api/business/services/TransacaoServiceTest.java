package com.javanauta.transacao_api.business.services;

import com.javanauta.transacao_api.controller.dtos.TransacaoRequestDTO;
import com.javanauta.transacao_api.infrastructure.exceptions.UnprocessableEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TransacaoServiceTest {
    @InjectMocks
    TransacaoService transacaoService;

    TransacaoRequestDTO transacao;

    @BeforeEach
    void setup() {
        transacao = new TransacaoRequestDTO(20.0, OffsetDateTime.now());
    }

    @Test
    void deveAdicionarTransacaoComSucesso() {
        transacaoService.adicionarTransacoes(transacao);
        List<TransacaoRequestDTO> transacoes = transacaoService.buscarTransacoes(5000);
        assertTrue(transacoes.contains(transacao));
    }

    @Test
    void deveLancarExcecaoCasoDataOuHoraMaiorQueAtual() {
        UnprocessableEntity exception = assertThrows(UnprocessableEntity.class, () ->
                transacaoService.adicionarTransacoes(new TransacaoRequestDTO(20.0, OffsetDateTime.now().plusDays(1))));
        assertEquals("Data e hora maiores que a data e hora atual", exception.getMessage());
    }


    @Test
    void deveLancarExcecaoCasoValorSejaNegativo() {
        UnprocessableEntity exception = assertThrows(UnprocessableEntity.class, () ->
                transacaoService.adicionarTransacoes(new TransacaoRequestDTO(-20.0, OffsetDateTime.now())));
        assertEquals("Valor não pode ser menor que 0", exception.getMessage());
    }

    @Test
    void deveLimparTransacaoComSucesso() {
        transacaoService.limparTransacoes();
        List<TransacaoRequestDTO> transacoes = transacaoService.buscarTransacoes(5000);
        assertTrue(transacoes.isEmpty());
    }

    @Test
    void deveBuscarTransacaoDentroDoIntervalo() {
        TransacaoRequestDTO dto = new TransacaoRequestDTO(10.0, OffsetDateTime.now().minusHours(1));
        transacaoService.adicionarTransacoes(transacao);
        transacaoService.adicionarTransacoes(dto);
        List<TransacaoRequestDTO> transacoes = transacaoService.buscarTransacoes(60);
        assertTrue(transacoes.contains(transacao));
        assertFalse(transacoes.contains(dto));
    }
}