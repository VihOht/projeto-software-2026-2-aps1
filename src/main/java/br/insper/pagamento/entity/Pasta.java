package br.insper.pagamento.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.text.Normalizer;
import java.util.Locale;

public enum Pasta {
	FAVORITO("favorito"),
	LISTA_DE_COMPRAS("lista de compras"),
	QUERO_JOGAR("quero jogar");

	private final String descricao;

	Pasta(String descricao) {
		this.descricao = descricao;
	}

	@JsonValue
	public String getDescricao() {
		return descricao;
	}

	@JsonCreator
	public static Pasta fromValue(String valor) {
		if (valor == null) {
			return null;
		}

		String normalizado = Normalizer.normalize(valor, Normalizer.Form.NFD)
				.replaceAll("\\p{M}", "")
				.trim()
				.toLowerCase(Locale.ROOT)
				.replace('_', ' ')
				.replace('-', ' ')
				.replaceAll("\\s+", " ");

		for (Pasta pasta : values()) {
			if (pasta.descricao.equals(normalizado)) {
				return pasta;
			}
		}

		throw new IllegalArgumentException("Pasta desconhecida: " + valor);
	}
}
