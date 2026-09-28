package br.insper.arqui.dto;

import br.insper.arqui.entity.Pasta;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JogosDto {
	private String titulo;
	private LocalDate dataLancamento;
	private Integer duracaoHoras;
	private String tipoJogo;
	private Pasta pasta;
	private String trilhaSonora;
	private String desenvolvedora;
	private String plataforma;
}
