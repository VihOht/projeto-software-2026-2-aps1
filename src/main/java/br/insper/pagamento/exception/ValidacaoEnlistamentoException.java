package br.insper.pagamento.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ValidacaoEnlistamentoException extends RuntimeException {
	public ValidacaoEnlistamentoException(String mensagem) {
		super(mensagem);
	}
}
