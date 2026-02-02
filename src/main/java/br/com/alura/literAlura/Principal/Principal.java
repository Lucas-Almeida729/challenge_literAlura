package br.com.alura.literAlura.principal;

import br.com.alura.literAlura.model.*;
import br.com.alura.literAlura.repository.AutorRepository;
import br.com.alura.literAlura.repository.LivroRepository;
import br.com.alura.literAlura.service.ConsumoApi;
import br.com.alura.literAlura.service.ConverteDados;
import java.util.List;
import java.util.Scanner;

public class Principal {
    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://gutendex.com/books/";

    private LivroRepository livroRepository;
    private AutorRepository autorRepository;

    public Principal(LivroRepository livroRepository, AutorRepository autorRepository) {
        this.livroRepository = livroRepository;
        this.autorRepository = autorRepository;
    }

    public void exibeMenu() {
        var opcao = -1;
        while (opcao != 0) {
            var menu = """
                    ------------------------------------
                    Escolha o número de sua opção:
                    1- buscar livro pelo título
                    2- listar livros registrados
                    3- listar autores registrados
                    4- listar autores vivos em um determinado ano
                    5- listar livros em um determinado idioma
                    0- sair
                    ------------------------------------
                    """;

            System.out.println(menu);
            try {
                opcao = leitura.nextInt();
                leitura.nextLine();

                switch (opcao) {
                    case 1 -> buscarLivroWeb();
                    case 2 -> listarLivrosRegistrados();
                    case 3 -> listarAutoresRegistrados();
                    case 4 -> listarAutoresVivosPorAno();
                    case 5 -> listarLivrosPorIdioma();
                    case 0 -> System.out.println("Saindo...");
                    default -> System.out.println("Opção inválida");
                }
            } catch (Exception e) {
                System.out.println("Erro: Digite apenas números.");
                leitura.nextLine();
            }
        }
    }

    private void buscarLivroWeb() {
        DadosResposta dados = getDadosLivro();

        if (dados != null && !dados.resultado().isEmpty()) {
            DadosLivro primeiroLivro = dados.resultado().get(0);

            var livroOptional = livroRepository.findByTituloContainingIgnoreCase(primeiroLivro.titulo());

            if (livroOptional.isPresent()) {
                System.out.println("Aviso: O livro '" + primeiroLivro.titulo() + "' já está no catálogo.");
            } else {
                Livro livro = new Livro(primeiroLivro);

                DadosAutor dadosAutor = primeiroLivro.autor().get(0);
                Autor autor = autorRepository.findByNomeContainingIgnoreCase(dadosAutor.nome())
                        .orElseGet(() -> autorRepository.save(new Autor(dadosAutor)));

                livro.setAutor(autor);

                livroRepository.save(livro);
                System.out.println("\nSucesso! Livro adicionado: " + livro.getTitulo());
            }
        } else {
            System.out.println("Livro não encontrado na API.");
        }
    }

    private void listarLivrosRegistrados() {
        List<Livro> livros = livroRepository.findAll();
        if (livros.isEmpty()) {
            System.out.println("Seu catálogo está vazio.");
        } else {
            livros.forEach(System.out::println);
        }
    }

    private void listarAutoresRegistrados() {
        List<Autor> autores = autorRepository.findAll();
        autores.forEach(System.out::println);
    }

    private void listarAutoresVivosPorAno() {
        System.out.println("Digite o ano:");
        var ano = leitura.nextInt();
        leitura.nextLine();
        autorRepository.autoresVivosNoAno(ano).forEach(System.out::println);
    }

    private void listarLivrosPorIdioma() {
        System.out.println("Digite o idioma (pt, en, es):");
        var idioma = leitura.nextLine();
        livroRepository.findByIdioma(idioma).forEach(System.out::println);
    }

    private DadosResposta getDadosLivro() {
        System.out.println("Digite o nome do livro:");
        var nomeLivro = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + "?search=" + nomeLivro.replace(" ", "%20"));
        return conversor.obterDados(json, DadosResposta.class);
    }
}