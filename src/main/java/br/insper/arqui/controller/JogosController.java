package br.insper.arqui.controller;

import br.insper.arqui.dto.JogosDto;
import br.insper.arqui.entity.Jogos;
import br.insper.arqui.entity.Pasta;
import br.insper.arqui.service.JogosService;
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
@RequestMapping("/api/jogos")
public class JogosController {

	private final JogosService jogosService;

	public JogosController(JogosService jogosService) {
		this.jogosService = jogosService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Jogos criar(@RequestBody JogosDto dto) {
		return jogosService.criar(dto);
	}

	@GetMapping
	public List<Jogos> listar(@RequestParam(required = false) Pasta pasta) {
		if (pasta == null) {
			return jogosService.listarTodos();
		}
		return jogosService.listarPorPasta(pasta);
	}

	@GetMapping("/{id}")
	public Jogos obter(@PathVariable Long id) {
		return jogosService.obterPorId(id);
	}

	@PutMapping("/{id}")
	public Jogos atualizar(@PathVariable Long id, @RequestBody JogosDto dto) {
		return jogosService.atualizar(id, dto);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deletar(@PathVariable Long id) {
		jogosService.deletar(id);
	}
}
