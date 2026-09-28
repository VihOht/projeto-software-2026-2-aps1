package br.insper.pagamento.entity;

import br.insper.pagamento.dto.EnlistamentoDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
		name = "enlistamentos",
		uniqueConstraints = @UniqueConstraint(columnNames = {"jogo_id", "pasta"})
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Enlistamento {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "jogo_id", nullable = false)
	private Long jogoId;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Pasta pasta;

	public static Enlistamento fromDto(EnlistamentoDto dto) {
		Enlistamento enlistamento = new Enlistamento();
		enlistamento.setJogoId(dto.getJogoId());
		enlistamento.setPasta(dto.getPasta());
		return enlistamento;
	}
}
