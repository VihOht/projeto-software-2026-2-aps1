package br.insper.pagamento.entity;

import br.insper.pagamento.dto.MusicasDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
		name = "musicas",
		uniqueConstraints = @UniqueConstraint(columnNames = {"jogo_id", "titulo", "artista"})
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Musicas {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "jogo_id", nullable = false)
	private Long jogoId;

	@Column(nullable = false)
	private String titulo;

	@Column(nullable = false)
	private String artista;

	@Column
	private String album;

	@Column(name = "data_lancamento", nullable = false)
	private LocalDate dataLancamento;

	@Column(name = "duracao_segundos", nullable = false)
	private Integer duracaoSegundos;

	@Column
	private String genero;

	public static Musicas fromDto(MusicasDto dto) {
		Musicas musica = new Musicas();
		musica.setJogoId(dto.getJogoId());
		musica.setTitulo(dto.getTitulo());
		musica.setArtista(dto.getArtista());
		musica.setAlbum(dto.getAlbum());
		musica.setDataLancamento(dto.getDataLancamento());
		musica.setDuracaoSegundos(dto.getDuracaoSegundos());
		musica.setGenero(dto.getGenero());
		return musica;
	}
}
