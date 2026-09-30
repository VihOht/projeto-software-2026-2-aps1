package br.insper.arqui.service;

import br.insper.arqui.client.JogosClient;
import br.insper.arqui.dto.MusicasDto;
import br.insper.arqui.entity.Musicas;
import br.insper.arqui.exception.MusicasNaoEncontradaException;
import br.insper.arqui.exception.ValidacaoMusicasException;
import br.insper.arqui.repository.MusicasRepository;
import br.insper.arqui.validator.ValidadorMusicas;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MusicasService {

	private final MusicasRepository musicasRepository;
	private final ValidadorMusicas validadorMusicas;
	private final JogosClient jogosClient;

	public MusicasService(
			MusicasRepository musicasRepository,
			ValidadorMusicas validadorMusicas,
			JogosClient jogosClient
	) {
		this.musicasRepository = musicasRepository;
		this.validadorMusicas = validadorMusicas;
		this.jogosClient = jogosClient;
	}

	public Musicas criar(MusicasDto dto) {
		validadorMusicas.validar(dto);
		validarJogo(dto.getJogoId());
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
		validarJogo(dto.getJogoId());

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

	private void validarJogo(Long jogoId) {
		if (!jogosClient.jogoExiste(jogoId)) {
			throw new ValidacaoMusicasException("Jogo não encontrado");
		}
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