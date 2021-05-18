package com.generation.agenda.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.generation.agenda.model.Contato;
import com.generation.agenda.repository.ContatoRepository;

@RestController
@RequestMapping("/contatos")
public class ContatoController {

	@Autowired
	private ContatoRepository contatoRepository;

	@GetMapping
	public ResponseEntity<List<Contato>> getAll() {
		List<Contato> contatos = contatoRepository.findAll();
		return ResponseEntity.ok(contatos);
	}

	@GetMapping("/contato/{id}")
	public ResponseEntity<Contato> getById(@PathVariable Long id) {
		return contatoRepository.findById(id)
				.map(resp -> ResponseEntity.ok(resp))
				.orElse(ResponseEntity.badRequest().build());
	}

	@PostMapping("/inserir")
	public ResponseEntity<Contato> post(@RequestBody Contato contato) {
		contato = contatoRepository.save(contato);
		return ResponseEntity.status(HttpStatus.CREATED).body(contato);
	}

	@PutMapping("/alterar")
	public ResponseEntity<Contato> put(@RequestBody Contato contato) {
		contato = contatoRepository.save(contato);
		return ResponseEntity.status(HttpStatus.OK).body(contato);
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		contatoRepository.deleteById(id);		
	}

}
