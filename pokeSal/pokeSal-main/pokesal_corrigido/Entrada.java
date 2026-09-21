
import java.util.Scanner;

/**
 * Fornece métodos utilitários para leitura e validação de entradas do usuário.
 */
public class Entrada {

    private static Scanner scanner = new Scanner(System.in);

        /**
     * Lê um número inteiro dentro do intervalo informado.
     *
     * @param mensagem mensagem exibida ao usuário
     * @param min valor mínimo permitido
     * @param max valor máximo permitido
     * @return número inteiro informado pelo usuário
     */
    public static int lerInteiro(String mensagem, int min, int max) {
        while (true) {
            System.out.print(mensagem);
            String texto = scanner.nextLine();

            try {
                int numero = Integer.parseInt(texto.trim());
                if (numero >= min && numero <= max) {
                    return numero;
                }
                System.out.println("Opção inválida: Digite um numero entre " + min + " e " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida! Digite apenas numeros.");
            }
        }
    }

    /**
     * Lê uma linha de texto informada pelo usuário.
     *
     * @param mensagem mensagem exibida ao usuário
     * @return texto informado pelo usuário
     */
    public static String lerTexto(String mensagem) {
        System.out.print(mensagem);
        return scanner.nextLine();
    }
}
