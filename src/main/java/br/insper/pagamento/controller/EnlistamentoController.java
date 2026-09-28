package br.insper.pagamento.controller;

import br.insper.pagamento.dto.EnlistamentoDto;
import br.insper.pagamento.entity.Enlistamento;
import br.insper.pagamento.entity.Pasta;
import br.insper.pagamento.service.EnlistamentoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enlistamentos")
public class EnlistamentoController {

	private final EnlistamentoService enlistamentoService;

	public EnlistamentoController(EnlistamentoService enlistamentoService) {
		this.enlistamentoService = enlistamentoService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Enlistamento criar(@RequestBody EnlistamentoDto dto) {
		return enlistamentoService.criar(dto);
	}

	@GetMapping
	public List<Enlistamento> listar(@RequestParam(required = false) Pasta pasta) {
		if (pasta == null) {
			return enlistamentoService.listarTodos();
		}
		return enlistamentoService.listarPorPasta(pasta);
	}

	@GetMapping("/{id}")
	public Enlistamento obter(@PathVariable Long id) {
		return enlistamentoService.obterPorId(id);
	}

	@PutMapping("/{id}")
	public Enlistamento atualizar(@PathVariable Long id, @RequestBody EnlistamentoDto dto) {
		return enlistamentoService.atualizar(id, dto);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deletar(@PathVariable Long id) {
		enlistamentoService.deletar(id);
	}
}
