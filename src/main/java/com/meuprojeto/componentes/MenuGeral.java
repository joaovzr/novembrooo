package com.meuprojeto.componentes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Scanner;

public final class MenuGeral {

    private MenuGeral() {
    }

    public static void exibir() {
        Scanner scanner = new Scanner(System.in);
        int opcao;

        do {
            System.out.println("\n===== MENU GERAL =====");
            System.out.println("1 - Clientes");
            System.out.println("2 - Produtos");
            System.out.println("0 - Sair");
            System.out.print("Escolha uma opção: ");
            opcao = lerOpcao(scanner);

            switch (opcao) {
                case 1 -> MenuCliente.exibir(scanner);
                case 2 -> MenuProduto.exibir(scanner);
                case 0 -> System.out.println("Saindo do sistema...");
                default -> System.out.println("Opção inválida!");
            }
        } while (opcao != 0);

        scanner.close();
    }

    static int lerOpcao(Scanner scanner) {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    static int lerInteiro(Scanner scanner, String mensagem) {
        while (true) {
            System.out.print(mensagem);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Digite apenas números inteiros.");
            }
        }
    }

    static LocalDate lerData(Scanner scanner, String mensagem) {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);
        while (true) {
            System.out.print(mensagem);
            try {
                return LocalDate.parse(scanner.nextLine().trim(), formato);
            } catch (DateTimeParseException e) {
                System.out.println("Data inválida! Use o formato dd/MM/yyyy.");
            }
        }
    }


    static int lerIndice(Scanner scanner, String mensagem, int tamanhoLista) {
        int numero = lerInteiro(scanner, mensagem);
        if (numero < 1 || numero > tamanhoLista) {
            return -1;
        }
        return numero - 1;
    }

    static String lerTexto(Scanner scanner, String mensagem, String atual) {
        System.out.print(mensagem + " [" + atual + "]: ");
        String entrada = scanner.nextLine().trim();
        return entrada.isEmpty() ? atual : entrada;
    }

    static int lerInteiroOuManter(Scanner scanner, String mensagem, int atual) {
        while (true) {
            System.out.print(mensagem + " [" + atual + "]: ");
            String entrada = scanner.nextLine().trim();
            if (entrada.isEmpty()) {
                return atual;
            }
            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Digite apenas números inteiros.");
            }
        }
    }


    static LocalDate lerDataOuManter(Scanner scanner, String mensagem, LocalDate atual) {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);
        while (true) {
            System.out.print(mensagem + " [" + atual.format(formato) + "]: ");
            String entrada = scanner.nextLine().trim();
            if (entrada.isEmpty()) {
                return atual;
            }
            try {
                return LocalDate.parse(entrada, formato);
            } catch (DateTimeParseException e) {
                System.out.println("Data inválida! Use o formato dd/MM/yyyy.");
            }
        }
    }

    static boolean confirmar(Scanner scanner, String mensagem) {
        System.out.print(mensagem + " (s/n): ");
        String resposta = scanner.nextLine().trim();
        return resposta.equalsIgnoreCase("s") || resposta.equalsIgnoreCase("sim");
    }
}
