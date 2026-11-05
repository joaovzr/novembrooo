package com.meuprojeto.componentes;

import com.meuprojeto.dao.ProdutoDao;
import com.meuprojeto.dominio.Eletrodomestrico;
import com.meuprojeto.dominio.Moveis;
import com.meuprojeto.dominio.Produto;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public final class MenuProduto {

    private static final ProdutoDao produtoDao = new ProdutoDao();

    private MenuProduto() {
    }

    public static void exibir(Scanner scanner) {
        int opcao;

        do {
            System.out.println("\n===== MENU PRODUTO =====");
            System.out.println("1 - Cadastrar produto");
            System.out.println("2 - Listar produtos");
            System.out.println("3 - Buscar produto");
            System.out.println("4 - Atualizar produto");
            System.out.println("5 - Deletar produto");
            System.out.println("0 - Voltar");
            System.out.print("Escolha uma opção: ");
            opcao = MenuGeral.lerOpcao(scanner);

            switch (opcao) {
                case 1 -> cadastrarProduto(scanner);
                case 2 -> listarProdutos();
                case 3 -> buscarProduto(scanner);
                case 4 -> atualizarProduto(scanner);
                case 5 -> deletarProduto(scanner);
                case 0 -> System.out.println("Voltando ao menu geral...");
                default -> System.out.println("Opção inválida!");
            }
        } while (opcao != 0);
    }


    private static void cadastrarProduto(Scanner scanner) {
        System.out.println("\n--- Cadastrar produto ---");
        System.out.println("Tipo do produto:");
        System.out.println("1 - Móveis");
        System.out.println("2 - Eletrodoméstico");
        System.out.print("Escolha o tipo: ");
        int tipo = MenuGeral.lerOpcao(scanner);

        if (tipo != 1 && tipo != 2) {
            System.out.println("Tipo inválido! Cadastro cancelado.");
            return;
        }

        System.out.print("Marca: ");
        String marca = scanner.nextLine().trim();

        System.out.print("Nome: ");
        String nome = scanner.nextLine().trim();

        System.out.print("Modelo: ");
        String modelo = scanner.nextLine().trim();

        int quantidade = MenuGeral.lerInteiro(scanner, "Quantidade: ");

        System.out.print("Descrição: ");
        String descricao = scanner.nextLine().trim();

        LocalDate dataCriacao = LocalDate.now();
        String criadoPor = "sistema";

        if (tipo == 1) {
            System.out.print("Composição: ");
            String composicao = scanner.nextLine().trim();

            System.out.print("Materiais: ");
            String materiais = scanner.nextLine().trim();

            int qtdItens = MenuGeral.lerInteiro(scanner, "Quantidade de itens: ");

            produtoDao.salvar(new Moveis(marca, nome, modelo, quantidade, descricao,
                    dataCriacao, criadoPor, composicao, materiais, qtdItens));
        } else {
            int voltagem = MenuGeral.lerInteiro(scanner, "Voltagem: ");
            int potencial = MenuGeral.lerInteiro(scanner, "Potência: ");

            System.out.print("Especificações: ");
            String especificacoes = scanner.nextLine().trim();

            produtoDao.salvar(new Eletrodomestrico(marca, nome, modelo, quantidade, descricao,
                    dataCriacao, criadoPor, voltagem, potencial, especificacoes));
        }

        System.out.println("Produto cadastrado com sucesso!");
    }

    private static void listarProdutos() {
        System.out.println("\n--- Lista de produtos ---");
        List<Produto> produtos = produtoDao.listar();

        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto cadastrado.");
            return;
        }

        for (int i = 0; i < produtos.size(); i++) {
            Produto p = produtos.get(i);
            System.out.println("[" + (i + 1) + "] " + p.getNome()
                    + " | Marca: " + p.getMarca()
                    + " | Modelo: " + p.getModelo()
                    + " | Qtd: " + p.getQuantidade()
                    + " | Tipo: " + tipoDoProduto(p));
        }
    }

    private static void buscarProduto(Scanner scanner) {
        System.out.println("\n--- Buscar produto ---");

        if (produtoDao.listar().isEmpty()) {
            System.out.println("Nenhum produto cadastrado.");
            return;
        }

        listarProdutos();
        int indice = MenuGeral.lerIndice(scanner, "Digite o número do produto: ", produtoDao.listar().size());

        if (indice == -1) {
            System.out.println("Produto não encontrado!");
            return;
        }

        exibirDetalhes(produtoDao.buscar(indice));
    }

    private static void exibirDetalhes(Produto p) {
        System.out.println("\nTipo: " + tipoDoProduto(p));
        System.out.println("Marca: " + p.getMarca());
        System.out.println("Nome: " + p.getNome());
        System.out.println("Modelo: " + p.getModelo());
        System.out.println("Quantidade: " + p.getQuantidade());
        System.out.println("Descrição: " + p.getDescricacao());

        if (p instanceof Moveis m) {
            System.out.println("Composição: " + m.getComposicao());
            System.out.println("Materiais: " + m.getMateriais());
            System.out.println("Quantidade de itens: " + m.getQtdItens());
        } else if (p instanceof Eletrodomestrico e) {
            System.out.println("Voltagem: " + e.getVoltagem());
            System.out.println("Potência: " + e.getPotencial());
            System.out.println("Especificações: " + e.getEspecificacoes());
        }
    }

    private static String tipoDoProduto(Produto p) {
        if (p instanceof Moveis) {
            return "Móveis";
        } else if (p instanceof Eletrodomestrico) {
            return "Eletrodoméstico";
        }
        return "Produto";
    }

    private static void atualizarProduto(Scanner scanner) {
        System.out.println("\n--- Atualizar produto ---");

        if (produtoDao.listar().isEmpty()) {
            System.out.println("Nenhum produto cadastrado.");
            return;
        }

        listarProdutos();
        int indice = MenuGeral.lerIndice(scanner, "Digite o número do produto: ", produtoDao.listar().size());

        if (indice == -1) {
            System.out.println("Produto não encontrado!");
            return;
        }

        Produto produto = produtoDao.buscar(indice);
        System.out.println("Pressione Enter para manter o valor atual.");

        produto.setMarca(MenuGeral.lerTexto(scanner, "Marca", produto.getMarca()));
        produto.setNome(MenuGeral.lerTexto(scanner, "Nome", produto.getNome()));
        produto.setModelo(MenuGeral.lerTexto(scanner, "Modelo", produto.getModelo()));
        produto.setQuantidade(MenuGeral.lerInteiroOuManter(scanner, "Quantidade", produto.getQuantidade()));
        produto.setDescricacao(MenuGeral.lerTexto(scanner, "Descrição", produto.getDescricacao()));

        if (produto instanceof Moveis m) {
            m.setComposicao(MenuGeral.lerTexto(scanner, "Composição", m.getComposicao()));
            m.setMateriais(MenuGeral.lerTexto(scanner, "Materiais", m.getMateriais()));
            m.setQtdItens(MenuGeral.lerInteiroOuManter(scanner, "Quantidade de itens", m.getQtdItens()));
        } else if (produto instanceof Eletrodomestrico e) {
            e.setVoltagem(MenuGeral.lerInteiroOuManter(scanner, "Voltagem", e.getVoltagem()));
            e.setPotencial(MenuGeral.lerInteiroOuManter(scanner, "Potência", e.getPotencial()));
            e.setEspecificacoes(MenuGeral.lerTexto(scanner, "Especificações", e.getEspecificacoes()));
        }

        produtoDao.atualizar(indice, produto);
        System.out.println("Produto atualizado com sucesso!");
    }

    private static void deletarProduto(Scanner scanner) {
        System.out.println("\n--- Deletar produto ---");

        if (produtoDao.listar().isEmpty()) {
            System.out.println("Nenhum produto cadastrado.");
            return;
        }

        listarProdutos();
        int indice = MenuGeral.lerIndice(scanner, "Digite o número do produto: ", produtoDao.listar().size());

        if (indice == -1) {
            System.out.println("Produto não encontrado!");
            return;
        }

        String nome = produtoDao.buscar(indice).getNome();
        if (MenuGeral.confirmar(scanner, "Deseja realmente deletar o produto " + nome + "?")) {
            produtoDao.deletar(indice);
            System.out.println("Produto deletado com sucesso!");
        } else {
            System.out.println("Operação cancelada.");
        }
    }
}
