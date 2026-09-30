package br.insper.arqui.dto;

import br.insper.arqui.entity.Pasta;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnlistamentoDto {
	private Long jogoId;
	private Pasta pasta;
}
