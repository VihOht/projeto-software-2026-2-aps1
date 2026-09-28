package br.insper.pagamento.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MusicasDto {
	private Long jogoId;
	private String titulo;
	private String artista;
	private String album;
	private LocalDate dataLancamento;
	private Integer duracaoSegundos;
	private String genero;
}
