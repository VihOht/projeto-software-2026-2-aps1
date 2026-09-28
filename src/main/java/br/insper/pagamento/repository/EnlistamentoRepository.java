package br.insper.pagamento.repository;

import br.insper.pagamento.entity.Enlistamento;
import br.insper.pagamento.entity.Pasta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnlistamentoRepository extends JpaRepository<Enlistamento, Long> {
	List<Enlistamento> findAllByPasta(Pasta pasta);

	boolean existsByJogoIdAndPasta(Long jogoId, Pasta pasta);
}
