package br.insper.arqui.repository;

import br.insper.arqui.entity.Jogos;
import br.insper.arqui.entity.Pasta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JogosRepository extends JpaRepository<Jogos, Long> {
	List<Jogos> findAllByPasta(Pasta pasta);

	boolean existsByTituloAndPlataforma(String titulo, String plataforma);
}
