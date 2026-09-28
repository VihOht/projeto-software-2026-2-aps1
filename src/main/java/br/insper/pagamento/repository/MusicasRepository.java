package br.insper.pagamento.repository;

import br.insper.pagamento.entity.Musicas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MusicasRepository extends JpaRepository<Musicas, Long> {
	List<Musicas> findAllByJogoId(Long jogoId);

	boolean existsByJogoIdAndTituloAndArtista(Long jogoId, String titulo, String artista);
}
