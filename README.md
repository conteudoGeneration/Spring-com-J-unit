# Implementando testes com o Spring Testing 
Desenvolvido por Rafael

Nesta atividade iremos implementar os testes nas Camadas Model, Repository e Controller no projeto Agenda que possui as Classes: Contato, ContatoRepository e ContatoController. 

## Boas Práticas

1. <a href="#prj">Implemmete as Classes das 3 Camadas do Projeto Agenda
2. <a href="#dep">Configure as Dependências no arquivo pom.xml</a>
3. <a href="#dtb">Configure o Banco de Dados de testes</a>
4. <a href="#pac">Prepare a estrutura de packages para testes</a>
5. <a href="#mod">Crie a Classe de testes na Camada Model: ContatoTest</a>
6. <a href="#rep">Crie a Classe de testes na Camada Repository: ContatoRepositoryTest</a>
7. <a href="#ctr">Crie a Classe de testes na Camada Controller: ContatoControllerTest</a>
8. <a href="#run">Execute os testes</a>
9. <a href="#ref">Referências</a>



<h2 id="prj">O Projeto Agenda

### Banco de Dados (application.properties)

```properties
spring.jpa.hibernate.ddl-auto=update
spring.datasource.url=jdbc:mysql://localhost/db_agenda?createDatabaseIfNotExist=true&serverTimezone=UTC&useSSl=false
spring.datasource.username=root
spring.datasource.password=root
spring.jpa.show-sql=true
```

### Classe Contato

```java
package com.generation.agenda.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;

@Entity
public class Contato {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@NotEmpty(message="O DDD deve ser preenchido")
	private String ddd;

	@NotEmpty(message="O Telefone deve ser preenchido")
	private String telefone;

	@NotEmpty(message="O Nome deve ser preenchido")
	private String nome;

	public Contato() {}

	public Contato(Long id, String nome, String ddd, String telefone) {
		this.id = id;
		this.nome = nome;
		this.ddd = ddd;
		this.telefone = telefone;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDdd() {
		return ddd;
	}

	public void setDdd(String ddd) {
		this.ddd = ddd;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

}

```

### Classe ContatoRepository

```java
package com.generation.agenda.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.generation.agenda.model.Contato;

@Repository
public interface ContatoRepository extends JpaRepository<Contato, Long> {

	public List<Contato> findAllByNomeIgnoreCaseContaining(String nome);
	public Contato findByNome(String nome);
}
```

### Classe ContatoController

```java
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

```



  <h2 id="dep">Dependências</h2>


No arquivo, **pom.xml**, vamos alterar a linha:

```xml
    <dependency>
    	<groupId>org.springframework.boot</groupId>
    	<artifactId>spring-boot-starter-test</artifactId>
    	<scope>test</scope>
    </dependency>
```

Para:

```xml
    <dependency>
    	<groupId>org.springframework.boot</groupId>
    	<artifactId>spring-boot-starter-test</artifactId>
    	<scope>test</scope>
    	<exclusions>
    		<exclusion>
    			<groupId>org.junit.vintage</groupId>
    			<artifactId>junit-vintage-engine</artifactId>
    		</exclusion>
    	</exclusions>
    </dependency>
```

*Essa alteração irá ignorar as versões anteriores ao **JUnit 5** (vintage).



<h2 id="dtb">Banco de Dados de teste</h2>


1)  Crie a **Source Folder resources** em **src/test**

2) Na **Source Folder resources**, crie o arquivo **application.properties**, para configurarmos a conexão com o Banco de Dados de testes

3) Insira neste arquivo as seguintes linhas:

```properties
spring.jpa.hibernate.ddl-auto=update
spring.datasource.url=jdbc:mysql://localhost/db_testagenda?createDatabaseIfNotExist=true&serverTimezone=UTC&useSSl=false
spring.datasource.username=root
spring.datasource.password=root
spring.jpa.show-sql=true
```

Observe que na **linha spring.datasource.url**, o nome do Banco de dados possui a palavra **test** para indicar que será apenas para a execução dos testes.

Na **linha spring.datasource.password**, configure a senha do usuário root criada na instalação do MySQL na sua máquina local



<h2 id="pac">Estrutura de Testes</h2>


Na Source Folder de Testes (**src/test/java**) , observe que existe uma estrutura de pacotes idêntica a Source Folder Main (**src/main/java**). Crie na Source Folder de Testes as packages Model, Repository e Controller. 

O Processo de criação dos arquivos é o mesmo do código principal, exceto o nome dos arquivos que deverão ser iguais aos arquivos da Source Folder Main (**src/main/java**) acrescentando a palavra Test no final. 

<b>Exemplo: </b>
<b>UsuarioRepository -> UsuarioRepositoryTest</b>.



<h2 id="mod">## Classe ContatoModelTest</h2>


Crie a classe ContatoModelTest na package **model**, na Source Folder de Testes (**src/test/java**) 

```java
package com.generation.agenda.model;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ContatoModelTest {

	public Contato contato;
	
	/* Injeta um Objeto da Classe Validator, responsável pela Validação dos Atributos da Model*/
	
	@Autowired
	private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
	
	@BeforeEach
	public void start() {
		contato = new Contato(null, "Gabriel", "011y", "9xxxxxxx9");
	    
	}
	
	@Test
    public void testValidationAtributos(){
       
		contato.setNome("João");
		contato.setDdd("011");
		contato.setTelefone("21837559");
        
		//Armazena a lista de Mensagens de Erros de Validação da Model
		Set<ConstraintViolation<Contato>> violations = validator.validate(contato);
        
		//Exibe as Mensagens de Erro se existirem
		System.out.println(violations.toString());
        
        //O Teste só passará se a Lista de Erros estiver vazia!
        assertTrue(violations.isEmpty());
                
    }

}

```



<h2 id="rep">Classe ContatoRepositoryTest</h2>


Crie a classe ContatoRepositoryTest na package **repository**, na Source Folder de Testes (**src/test/java**) 

```java
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
		if (contatoRepository.findByNome(contato.getNome()) == null)
			contatoRepository.save(contato);

		contato = new Contato(null, "Novo Chefe", "0y", "8xxxxxxx8");
		if (contatoRepository.findByNome(contato.getNome()) == null)
			contatoRepository.save(contato);

		contato = new Contato(null, "chefe Mais Antigo", "0y", "7xxxxxxx7");
		if (contatoRepository.findByNome(contato.getNome()) == null)
			contatoRepository.save(contato);

		contato = new Contato(null, "Amigo", "0z", "5xxxxxxx5");
		if (contatoRepository.findByNome(contato.getNome()) == null)
			contatoRepository.save(contato);
	}

	@Test
	public void findByNomeRetornaContato() throws Exception {

		Contato contato = contatoRepository.findByNome("Chefe");

		assertTrue(contato.getNome().equals("Chefe"));
	}

	@Test
	public void findAllByNomeIgnoreCaseRetornaTresContatos() {

		List<Contato> contatos = contatoRepository.findAllByNomeIgnoreCaseContaining("chefe");

		assertEquals(3, contatos.size());
	}

	@AfterAll
	public void end() {
		contatoRepository.deleteAll();
	}
	
}
```



<h2 id="ctr">Classe ContatoControllerTest</h2>


Crie a classe ContatoControllerTest na package **controller**, na Source Folder de Testes (**src/test/java**) 

```java
package com.generation.agenda.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.generation.agenda.model.Contato;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ContatoControllerTest {

	@Autowired
	private TestRestTemplate testRestTemplate;
	
	private Contato contato;
	private Contato contatoupd;

	
	@BeforeAll
	public void start() {
		contato = new Contato(null, "Maria", "21", "44451198");
		contatoupd = new Contato(2L,"Maria da Silva", "23", "995467892");
	}

	@Test
	public void deveRealizarPostContatos() {

		
		/*
		 * Criando um objeto do tipo HttpEntity para enviar como terceiro
		 * parâmentro do método exchange. (Enviando um objeto contato via body)
		 * 
		 * */
		HttpEntity<Contato> request = new HttpEntity<Contato>(contato);

		ResponseEntity<Contato> resposta = testRestTemplate.exchange("/contatos/inserir", HttpMethod.POST, request, Contato.class);
		assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
	}
	

	@Test
	public void deveMostrarTodosContatos() {
		ResponseEntity<String> resposta = testRestTemplate.exchange("/contatos/", HttpMethod.GET, null, String.class);
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
	}
	
	
	@Test
	public void deveRealizarPutContatos() {

		HttpEntity<Contato> request = new HttpEntity<Contato>(contatoupd);

		ResponseEntity<Contato> resposta = testRestTemplate.exchange("/contatos/alterar", HttpMethod.PUT, request, Contato.class);
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		
	}
	
	@Test
	@DisplayName("😱")
	public void deveRealizarDeleteContatos() {

		/*
		 * O Contato com Id 3 será apagado somente ele existir no Banco.
		 * Caso contrário, o teste irá falhar!
		 * 
		 * */
		ResponseEntity<String> resposta = testRestTemplate.exchange("/contatos/3", HttpMethod.DELETE, null, String.class);
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		
	}
	
}
```



<h2 id="run">Executando os testes no Eclipse e no STS</h2>


1) No lado esquerdo superior, na Guia **Project**, na Package **src/test/java**, clique com o botão direito do mouse sobre um dos testes e clique na opção **Run As->JUnit Test**.

<a href="https://imgur.com/HJN6A6p"><img src="https://i.imgur.com/HJN6A6p.jpg" title="source: imgur.com" /></a>


2) Para acompanhar os testes, ao lado da Guia **Project**, clique na Guia **JUnit**.

<a href="https://imgur.com/vdLvg6o"><img src="https://i.imgur.com/vdLvg6o.jpg" title="source: imgur.com" /></a>

 3) Se todos os testes passarem, a Guia do JUnit ficará com uma faixa verde (janela 01). Caso algum teste não passe, a Guia do JUnit ficará com uma faixa vermelha (janela 02). Neste caso, observe o item <b>Failure Trace</b> para identificar o (s) erro (s).

<div align="center">
<table width=100%>
	<tr>
		<td width=50%><div align="center"><a href="https://imgur.com/fVb4UAc"><img  src="https://i.imgur.com/fVb4UAc.png" title="source: imgur.com" /></a>
		<td width=50%><div align="center"><a href="https://imgur.com/xB1obRN"><img src="https://i.imgur.com/xB1obRN.png" title="source: imgur.com" /></a>
	</tr>
	<tr>
		<td><div align="center">Janela 01: <i> Testes aprovados.
		<td><div align="center">Janela 02: <i> Testes reprovados.
	</tr>
</table>
</div>



Ao escrever testes, sempre verifique se a importação dos pacotes do JUnit na Classe de testes estão corretos. O JUnit 5 tem como pacote base <b><i>org.junit.jupiter.api</i></b>.

<h2 id="ref">Referências</h2>


<a href="https://docs.spring.io/spring-framework/docs/current/reference/html/testing.html#testing-introduction" target="_blank">Documentação Oficial do Spring Testing</a>

<a href="https://junit.org/junit5/" target="_blank">Página Oficial do JUnit5</a>

<a href="https://junit.org/junit5/docs/current/user-guide/" target="_blank">Documentação Oficial do JUnit 5</a>


