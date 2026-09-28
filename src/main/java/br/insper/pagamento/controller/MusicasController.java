package br.insper.pagamento.controller;

import br.insper.pagamento.dto.MusicasDto;
import br.insper.pagamento.entity.Musicas;
import br.insper.pagamento.service.MusicasService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/musicas")
public class MusicasController {

	private final MusicasService musicasService;

	public MusicasController(MusicasService musicasService) {
		this.musicasService = musicasService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Musicas criar(@RequestBody MusicasDto dto) {
		return musicasService.criar(dto);
	}

	@GetMapping
	public List<Musicas> listar(@RequestParam(required = false) Long jogoId) {
		if (jogoId == null) {
			return musicasService.listarTodos();
		}
		return musicasService.listarPorJogo(jogoId);
	}

	@GetMapping("/{id}")
	public Musicas obter(@PathVariable Long id) {
		return musicasService.obterPorId(id);
	}

	@PutMapping("/{id}")
	public Musicas atualizar(@PathVariable Long id, @RequestBody MusicasDto dto) {
		return musicasService.atualizar(id, dto);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deletar(@PathVariable Long id) {
		musicasService.deletar(id);
	}
}
