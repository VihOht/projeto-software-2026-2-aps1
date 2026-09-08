package br.insper.pagamento.service;

import br.insper.pagamento.dto.PagamentoDto;
import br.insper.pagamento.entity.Pagamento;
import br.insper.pagamento.entity.TipoPagamento;
import br.insper.pagamento.repository.PagamentoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class PagamentoServiceTests {

    @InjectMocks
    private PagamentoService pagamentoService;

    @Mock
    private PagamentoRepository pagamentoRepository;


    @Test
    public void test_shouldReturnTwoPaymentsWhenListarTodos() {
        List<Pagamento> pagamentos = new ArrayList<>();
        pagamentos.add(new Pagamento());
        pagamentos.add(new Pagamento());

        // cria os mocks
        Mockito.when(pagamentoRepository.findAll())
                .thenReturn(pagamentos);

        // chama o metodo testado
        List<Pagamento> response = pagamentoService.listarTodos();

        // asserts
        Assertions.assertEquals(2, response.size());
    }



    @Test
    public void test_shouldCreatePaymentWhenTipoPagamentoIsPix() {
        // mocks
        PagamentoDto dto = new PagamentoDto();
        dto.setTipo(TipoPagamento.PIX);
        dto.setValor(new BigDecimal(1000));
        dto.setDataCompra(LocalDate.now());
        dto.setChaveOrigem("123");
        dto.setChaveDestino("234");

        Pagamento pagamento = Pagamento.fromDto(dto);

        Mockito.when(pagamentoRepository.save(Mockito.any()))
                .thenReturn(pagamento);

        // chamada
        Pagamento response = pagamentoService.criar(dto);

        // asserts
        Assertions.assertEquals(TipoPagamento.PIX, response.getTipo());
        Assertions.assertEquals("123", response.getChaveOrigem());
    }


}