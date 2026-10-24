# GameLibrary — Auth0

## Estrutura e autenticação

- `src/main`: serviço de jogos (8080), incluindo catálogo RAWG.
- `musicas-service`: serviço de trilhas (8082), com banco próprio.
- `enlistamento-service`: serviço de coleções (8083), com banco próprio.
- Cada serviço possui seu próprio `pom.xml`, configuração e testes; não há agregador Maven.
- `../frontend/index.html` carrega `app.js`; os arquivos React `main.jsx` e `Home.jsx` são referências sem projeto React completo.
- `../WebSecurityConfig.java` é o exemplo fornecido, fora do build e com outro pacote. A adaptação compilada fica em `src/main/java/br/insper/arqui/config/WebSecurityConfig.java` de cada serviço.
- `src/resources/application.properties` é um arquivo legado fora do caminho padrão do Maven. A configuração ativa fica em `src/main/resources/application.properties`.

Todos os endpoints exigem `Authorization: Bearer <access_token>`. Os serviços validam assinatura, emissor, expiração e audiência do JWT. A aplicação não cria sessão HTTP. CORS permite os endereços configurados e preflight sem token. Músicas e enlistamentos encaminham o JWT autenticado apenas ao serviço de jogos; ele não é enviado ao RAWG.

A política segue o exemplo fornecido: qualquer usuário autenticado pode acessar os dados compartilhados. Não foram criadas regras de administrador ou bibliotecas privadas por usuário.

## Configuração

Os três serviços usam estes valores padrão, com sobrescrita por ambiente:

```properties
spring.security.oauth2.resourceserver.jwt.issuer-uri=${AUTH0_ISSUER_URI:https://dev-bpbgrel3zl2chj7s.us.auth0.com/}
spring.security.oauth2.resourceserver.jwt.audiences=${AUTH0_AUDIENCE:https://dev-bpbgrel3zl2chj7s.us.auth0.com/api/v2/}
app.cors.allowed-origins=${CORS_ALLOWED_ORIGINS:http://localhost:5500,http://127.0.0.1:5500}
```

`audiences` é a propriedade suportada pelo Spring Boot; `audience` no singular não habilita essa validação. O espaço final do valor original foi removido.

A audiência fornecida identifica a **Auth0 Management API**. Foi preservada conforme solicitado. Para uma API própria, registre uma API GameLibrary no Auth0 com RS256 e configure seu Identifier em `AUTH0_AUDIENCE` e em `frontend/auth.js`. Não solicite permissões administrativas da Management API para usar a biblioteca.

Para executar via Docker Compose, copie `.env.example` para `.env`, configure `RAWG_API_KEY` e execute `docker compose up --build` nesta pasta. O Compose encaminha as variáveis Auth0/CORS para todos os serviços. Execução direta com Maven requer exportar as variáveis no shell; o Spring não lê `.env` automaticamente.

Veja `../frontend/README.md` para configurar a aplicação SPA no painel Auth0. Nenhum client secret é necessário no frontend ou nos resource servers.

## Verificação

Requer JDK 25 (incluindo `javac`). Nesta pasta:

```bash
bash ./mvnw test
bash ./mvnw -f musicas-service/pom.xml test
bash ./mvnw -f enlistamento-service/pom.xml test
node --test ../frontend/tests/auth.test.mjs
```

Os testes de segurança usam chaves RSA e um endpoint JWKS local, sem banco ou conta Auth0. Cobrem tokens válidos, ausência de token, assinatura inválida, expiração, emissor/audiência incorretos, CORS e ausência de sessão. Os clientes internos têm testes de encaminhamento e isolamento do token entre chamadas.

Com os serviços em execução:

```bash
curl -i http://localhost:8080/api/jogos
# Esperado: 401
curl -i -H "Authorization: Bearer $ACCESS_TOKEN" http://localhost:8080/api/jogos
# Esperado: 200 com um access token válido para o emissor e audiência configurados.
```

As configurações do tenant, callback e emissão real de tokens devem ser verificadas com uma sessão no Auth0. O código não altera o painel Auth0.

---

# Aula 02/09

## Adicionar configuração do JaCoCo

O JaCoCo é a biblioteca que mostra a cobertura de teste que existe no projeto.

```
            <plugin>
				<groupId>org.jacoco</groupId>
				<artifactId>jacoco-maven-plugin</artifactId>
				<version>0.8.15</version>
				<executions>
					<execution>
						<goals>
							<goal>prepare-agent</goal>
						</goals>
					</execution>
					<execution>
						<id>report</id>
						<phase>test</phase>
						<goals>
							<goal>report</goal>
						</goals>
						<configuration>
							<outputDirectory>${project.basedir}/tests</outputDirectory>
						</configuration>
					</execution>
				</executions>
			</plugin>
```

## Substituir bibliotecas de teste

Remover as dependências spring-boot-starter-data-jpa-test e spring-boot-starter-webmvc-test

Adicionar essa:

```
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
```

## Remover arquivo de teste default do Spring

Remover o arquivo src/test/br.insper.pagameto.PagamentoApplicationTests

O spring sempre adiciona esse arquivo quando o projeto é criado pelo Spring Initializr

## Criar a classe de Teste

Criar o arquivo src/test/br.insper.arqui.service.PagamentoServiceTest

Copiar o seguinte trecho de código nele
```

package br.insper.arqui.service;

@ExtendWith(MockitoExtension.class)
public class PagamentoServiceTest {

	@InjectMocks
	private PagamentoService pagamentoService;

	@Mock
	private PagamentoRepository pagamentoRepository;

}	
	
```

## Adicionar o primeiro caso de teste 

```
    @Test
	public void testCriarPagamentoComSucesso() {

		PagamentoDto dto = createPagamentoDTO();
		Pagamento entity = createPagamento();
		
		when(pagamentoRepository.save(any(Pagamento.class))).thenReturn(entity);

		Pagamento resultado = pagamentoService.criar(dto);

		assertNotNull(resultado);
		assertEquals(1L, resultado.getId());
		assertEquals(TipoPagamento.PIX, resultado.getTipo());
		assertEquals(new BigDecimal("100.00"), resultado.getValor());
		assertEquals("pendente", resultado.getStatus());
	}
	
	private PagamentoDto createPagamentoDTO() {
		PagamentoDto pagamentoDtoValido = new PagamentoDto();
		pagamentoDtoValido.setTipo(TipoPagamento.PIX);
		pagamentoDtoValido.setValor(new BigDecimal("100.00"));
		pagamentoDtoValido.setParcelas(1);
		pagamentoDtoValido.setDataCompra(LocalDate.now());
		pagamentoDtoValido.setChaveOrigem("chave@pix.com");
		pagamentoDtoValido.setChaveDestino("destino@pix.com");
		return pagamentoDtoValido;
	}
	
	private Pagamento createPagamento() {
		Pagamento pagamentoEsperado = new Pagamento();
		pagamentoEsperado.setId(1L);
		pagamentoEsperado.setTipo(TipoPagamento.PIX);
		pagamentoEsperado.setValor(new BigDecimal("100.00"));
		pagamentoEsperado.setParcelas(1);
		pagamentoEsperado.setDataCompra(LocalDate.now());
		pagamentoEsperado.setChaveOrigem("chave@pix.com");
		pagamentoEsperado.setChaveDestino("destino@pix.com");
		pagamentoEsperado.setStatus("pendente");
		return pagamentoEsperado;
	}
```

## Pipeline de verificação de testes

```
name: Java CI with Maven

permissions:
  contents: read
  pull-requests: write

on:
  pull_request:
    branches: [ "main" ]

jobs:
  build:

    runs-on: ubuntu-latest

    steps:
    - uses: actions/checkout@v4
    - name: Set up JDK 21
      uses: actions/setup-java@v4
      with:
        java-version: '21'
        distribution: 'temurin'
        cache: maven
        
    - name: Build with Maven
      run: mvn clean install

    - name: Check JaCoCo coverage
      uses: madrapps/jacoco-report@v1.7.2
      with:
        paths: |
          jacoco/jacoco.xml
        token: ${{ secrets.GITHUB_TOKEN }}
        min-coverage-overall: 80
        min-coverage-changed-files: 80
        fail-build: true
```
# zambomAps1




