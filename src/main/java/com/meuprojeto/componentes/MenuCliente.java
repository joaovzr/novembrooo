package com.meuprojeto.componentes;

import com.meuprojeto.dao.ClienteDao;
import com.meuprojeto.dominio.Cliente;
import com.meuprojeto.dominio.Endereco;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public final class MenuCliente {

    private static final ClienteDao clienteDao = new ClienteDao();
    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private MenuCliente() {
    }

    public static void exibir(Scanner scanner) {
        int opcao;

        do {
            System.out.println("\n===== MENU CLIENTE =====");
            System.out.println("1 - Cadastrar cliente");
            System.out.println("2 - Listar clientes");
            System.out.println("3 - Buscar cliente");
            System.out.println("4 - Atualizar cliente");
            System.out.println("5 - Deletar cliente");
            System.out.println("0 - Voltar");
            System.out.print("Escolha uma opção: ");
            opcao = MenuGeral.lerOpcao(scanner);

            switch (opcao) {
                case 1 -> cadastrarCliente(scanner);
                case 2 -> listarClientes();
                case 3 -> buscarCliente(scanner);
                case 4 -> atualizarCliente(scanner);
                case 5 -> deletarCliente(scanner);
                case 0 -> System.out.println("Voltando ao menu geral...");
                default -> System.out.println("Opção inválida!");
            }
        } while (opcao != 0);
    }

    private static void cadastrarCliente(Scanner scanner) {
        System.out.println("\n--- Cadastrar cliente ---");

        System.out.print("Nome: ");
        String nome = scanner.nextLine().trim();

        LocalDate dataNascimento = MenuGeral.lerData(scanner, "Data de nascimento (dd/MM/yyyy): ");

        System.out.print("E-mail: ");
        String email = scanner.nextLine().trim();

        System.out.print("Telefone: ");
        String telefone = scanner.nextLine().trim();

        System.out.print("Gênero: ");
        String genero = scanner.nextLine().trim();

        System.out.print("CPF: ");
        String cpf = scanner.nextLine().trim();

        System.out.println("-- Endereço --");
        System.out.print("Rua: ");
        String rua = scanner.nextLine().trim();

        int numero = MenuGeral.lerInteiro(scanner, "Número: ");

        System.out.print("Cidade: ");
        String cidade = scanner.nextLine().trim();

        Endereco endereco = new Endereco(rua, numero, cidade);

        Cliente cliente = new Cliente(nome, dataNascimento, email, telefone, genero, cpf,
                endereco, LocalDate.now(), "sistema");

        clienteDao.salvar(cliente);
        System.out.println("Cliente cadastrado com sucesso!");
    }

    private static void listarClientes() {
        System.out.println("\n--- Lista de clientes ---");
        List<Cliente> clientes = clienteDao.listar();

        if (clientes.isEmpty()) {
            System.out.println("Nenhum cliente cadastrado.");
            return;
        }

        for (int i = 0; i < clientes.size(); i++) {
            System.out.println("[" + (i + 1) + "] " + clientes.get(i).getNome()
                    + " | CPF: " + clientes.get(i).getCpf()
                    + " | E-mail: " + clientes.get(i).getEmail());
        }
    }

    private static void buscarCliente(Scanner scanner) {
        System.out.println("\n--- Buscar cliente ---");

        if (clienteDao.listar().isEmpty()) {
            System.out.println("Nenhum cliente cadastrado.");
            return;
        }

        listarClientes();
        int indice = MenuGeral.lerIndice(scanner, "Digite o número do cliente: ", clienteDao.listar().size());

        if (indice == -1) {
            System.out.println("Cliente não encontrado!");
            return;
        }

        exibirDetalhes(clienteDao.buscar(indice));
    }

    private static void exibirDetalhes(Cliente c) {
        System.out.println("\nNome: " + c.getNome());
        System.out.println("Data de nascimento: " + c.getDataNascimento().format(FORMATO_DATA));
        System.out.println("E-mail: " + c.getEmail());
        System.out.println("Telefone: " + c.getTelefone());
        System.out.println("Gênero: " + c.getGenero());
        System.out.println("CPF: " + c.getCpf());
        System.out.println("Endereço: " + c.getEndereco().getRua() + ", " + c.getEndereco().getNumero()
                + " - " + c.getEndereco().getCidade());
    }

    private static void atualizarCliente(Scanner scanner) {
        System.out.println("\n--- Atualizar cliente ---");

        if (clienteDao.listar().isEmpty()) {
            System.out.println("Nenhum cliente cadastrado.");
            return;
        }

        listarClientes();
        int indice = MenuGeral.lerIndice(scanner, "Digite o número do cliente: ", clienteDao.listar().size());

        if (indice == -1) {
            System.out.println("Cliente não encontrado!");
            return;
        }

        Cliente cliente = clienteDao.buscar(indice);
        System.out.println("Pressione Enter para manter o valor atual.");

        cliente.setNome(MenuGeral.lerTexto(scanner, "Nome", cliente.getNome()));
        cliente.setDataNascimento(MenuGeral.lerDataOuManter(scanner, "Data de nascimento", cliente.getDataNascimento()));
        cliente.setEmail(MenuGeral.lerTexto(scanner, "E-mail", cliente.getEmail()));
        cliente.setTelefone(MenuGeral.lerTexto(scanner, "Telefone", cliente.getTelefone()));
        cliente.setGenero(MenuGeral.lerTexto(scanner, "Gênero", cliente.getGenero()));
        cliente.setCpf(MenuGeral.lerTexto(scanner, "CPF", cliente.getCpf()));

        Endereco endereco = cliente.getEndereco();
        System.out.println("-- Endereço --");
        endereco.setRua(MenuGeral.lerTexto(scanner, "Rua", endereco.getRua()));
        endereco.setNumero(MenuGeral.lerInteiroOuManter(scanner, "Número", endereco.getNumero()));
        endereco.setCidade(MenuGeral.lerTexto(scanner, "Cidade", endereco.getCidade()));

        clienteDao.atualizar(indice, cliente);
        System.out.println("Cliente atualizado com sucesso!");
    }

    private static void deletarCliente(Scanner scanner) {
        System.out.println("\n--- Deletar cliente ---");

        if (clienteDao.listar().isEmpty()) {
            System.out.println("Nenhum cliente cadastrado.");
            return;
        }

        listarClientes();
        int indice = MenuGeral.lerIndice(scanner, "Digite o número do cliente: ", clienteDao.listar().size());

        if (indice == -1) {
            System.out.println("Cliente não encontrado!");
            return;
        }

        String nome = clienteDao.buscar(indice).getNome();
        if (MenuGeral.confirmar(scanner, "Deseja realmente deletar o cliente " + nome + "?")) {
            clienteDao.deletar(indice);
            System.out.println("Cliente deletado com sucesso!");
        } else {
            System.out.println("Operação cancelada.");
        }
    }
}
