package br.insper.pagamento.service;

import br.insper.pagamento.dto.MusicasDto;
import br.insper.pagamento.entity.Musicas;
import br.insper.pagamento.exception.MusicasNaoEncontradaException;
import br.insper.pagamento.exception.ValidacaoMusicasException;
import br.insper.pagamento.repository.MusicasRepository;
import br.insper.pagamento.validator.ValidadorMusicas;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MusicasService {

	private final MusicasRepository musicasRepository;
	private final ValidadorMusicas validadorMusicas;

	public MusicasService(MusicasRepository musicasRepository, ValidadorMusicas validadorMusicas) {
		this.musicasRepository = musicasRepository;
		this.validadorMusicas = validadorMusicas;
	}

	public Musicas criar(MusicasDto dto) {
		validadorMusicas.validar(dto);
		validarDuplicidade(dto, null);
		return musicasRepository.save(Musicas.fromDto(dto));
	}

	public List<Musicas> listarTodos() {
		return musicasRepository.findAll();
	}

	public List<Musicas> listarPorJogo(Long jogoId) {
		if (jogoId == null || jogoId <= 0) {
			throw new ValidacaoMusicasException("ID do jogo deve ser maior que zero");
		}
		return musicasRepository.findAllByJogoId(jogoId);
	}

	public Musicas obterPorId(Long id) {
		return musicasRepository.findById(id)
				.orElseThrow(() -> new MusicasNaoEncontradaException(id));
	}

	public Musicas atualizar(Long id, MusicasDto dto) {
		validadorMusicas.validar(dto);
		Musicas musica = obterPorId(id);
		validarDuplicidade(dto, musica);
		musica.setJogoId(dto.getJogoId());
		musica.setTitulo(dto.getTitulo());
		musica.setArtista(dto.getArtista());
		musica.setAlbum(dto.getAlbum());
		musica.setDataLancamento(dto.getDataLancamento());
		musica.setDuracaoSegundos(dto.getDuracaoSegundos());
		musica.setGenero(dto.getGenero());
		return musicasRepository.save(musica);
	}

	public void deletar(Long id) {
		Musicas musica = obterPorId(id);
		musicasRepository.delete(musica);
	}

	private void validarDuplicidade(MusicasDto dto, Musicas atual) {
		boolean dadosNaoMudaram = atual != null
				&& atual.getJogoId().equals(dto.getJogoId())
				&& atual.getTitulo().equals(dto.getTitulo())
				&& atual.getArtista().equals(dto.getArtista());

		if (!dadosNaoMudaram && musicasRepository.existsByJogoIdAndTituloAndArtista(
				dto.getJogoId(), dto.getTitulo(), dto.getArtista())) {
			throw new ValidacaoMusicasException("A música já está cadastrada para esse jogo");
		}
	}
}
