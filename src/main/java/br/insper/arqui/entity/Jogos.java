package br.insper.arqui.entity;

import br.insper.arqui.dto.JogosDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(
		name = "jogos",
		uniqueConstraints = @UniqueConstraint(columnNames = {"titulo", "plataforma"})
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Jogos {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String titulo;

	@Column(name = "data_lancamento", nullable = false)
	private LocalDate dataLancamento;

	@Column(name = "duracao_horas", nullable = false)
	private Integer duracaoHoras;

	@Column(name = "tipo_jogo", nullable = false)
	private String tipoJogo;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Pasta pasta;

	@Column(name = "trilha_sonora", nullable = false)
	private String trilhaSonora;

	@Column(nullable = false)
	private String desenvolvedora;

	@Column(nullable = false)
	private String plataforma;

	public static Jogos fromDto(JogosDto dto) {
		Jogos jogo = new Jogos();
		jogo.setTitulo(dto.getTitulo());
		jogo.setDataLancamento(dto.getDataLancamento());
		jogo.setDuracaoHoras(dto.getDuracaoHoras());
		jogo.setTipoJogo(dto.getTipoJogo());
		jogo.setPasta(dto.getPasta());
		jogo.setTrilhaSonora(dto.getTrilhaSonora());
		jogo.setDesenvolvedora(dto.getDesenvolvedora());
		jogo.setPlataforma(dto.getPlataforma());
		return jogo;
	}
}
