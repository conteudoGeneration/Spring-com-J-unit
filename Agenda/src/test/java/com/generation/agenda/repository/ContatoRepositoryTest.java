package com.generation.agenda.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.generation.agenda.model.Contato;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ContatoRepositoryTest {

	@Autowired
	private ContatoRepository contatoRepository;

	@BeforeAll
	public void start() {
		Contato contato = new Contato(null, "Chefe", "0y", "9xxxxxxx9");
		if (contatoRepository.findFirstByNome(contato.getNome()) == null)
			contatoRepository.save(contato);

		contato = new Contato(null, "Novo Chefe", "0y", "8xxxxxxx8");
		if (contatoRepository.findFirstByNome(contato.getNome()) == null)
			contatoRepository.save(contato);

		contato = new Contato(null, "chefe Mais Antigo", "0y", "7xxxxxxx7");
		if (contatoRepository.findFirstByNome(contato.getNome()) == null)
			contatoRepository.save(contato);

		contato = new Contato(null, "Amigo", "0z", "5xxxxxxx5");
		if (contatoRepository.findFirstByNome(contato.getNome()) == null)
			contatoRepository.save(contato);
	}

	@Test
	public void findByNomeRetornaContato() throws Exception {

		Contato contato = contatoRepository.findFirstByNome("Chefe");

		assertTrue(contato.getNome().equals("Chefe"));
	}

	@Test
	public void findAllByNomeIgnoreCaseRetornaTresContato() {

		List<Contato> contatos = contatoRepository.findAllByNomeIgnoreCaseContaining("chefe");

		assertEquals(3, contatos.size());
	}

	@AfterAll
	public void end() {
		contatoRepository.deleteAll();
	}
	
}