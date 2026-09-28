package br.insper.pagamento.validator;

import br.insper.pagamento.dto.EnlistamentoDto;
import br.insper.pagamento.exception.ValidacaoEnlistamentoException;
import org.springframework.stereotype.Component;

@Component
public class ValidadorEnlistamento {

	public void validar(EnlistamentoDto dto) {
		if (dto == null) {
			throw new ValidacaoEnlistamentoException("Os dados do enlistamento são obrigatórios");
		}
		if (dto.getJogoId() == null || dto.getJogoId() <= 0) {
			throw new ValidacaoEnlistamentoException("ID do jogo deve ser maior que zero");
		}
		if (dto.getPasta() == null) {
			throw new ValidacaoEnlistamentoException("Pasta é obrigatória");
		}
	}
}
