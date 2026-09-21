import java.util.Scanner;


public class Entrada {

    private static Scanner scanner = new Scanner(System.in);

        public static int lerInteiro(String mensagem, int min, int max) {
        while (true) {
            System.out.print(mensagem);
            String texto = scanner.nextLine();

            try {
                int numero = Integer.parseInt(texto.trim());
                if (numero >= min && numero <= max) {
                    return numero;
                }
                System.out.println("Opção inválida: Digite um número entre " + min + " e " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida! Digite apenas números.");
            }
        }
    }

    public static String lerTexto(String mensagem) {
        System.out.print(mensagem);
        return scanner.nextLine();
    }
}
