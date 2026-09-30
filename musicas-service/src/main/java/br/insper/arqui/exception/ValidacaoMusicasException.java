package br.insper.arqui.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ValidacaoMusicasException extends RuntimeException {
	public ValidacaoMusicasException(String mensagem) {
		super(mensagem);
	}
}
