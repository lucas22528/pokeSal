/*
 * Classe principal do sistema Pokésal.
 * Mostra o menu no console e monta a batalha.
 */
public class Main {

    public static void main(String[] args) {
        int opcao = -1;

        while (opcao != 0) {
            System.out.println("\n=================================");
            System.out.println("          POKÉSAL");
            System.out.println("=================================");
            System.out.println("1 - Iniciar batalha");
            System.out.println("2 - Ver Pokésais disponíveis");
            System.out.println("3 - Ver regras resumidas");
            System.out.println("0 - Sair");

            opcao = Entrada.lerInteiro("Escolha uma opção: ", 0, 3);

            if (opcao == 1) {
                iniciarBatalha();
            } else if (opcao == 2) {
                mostrarPokesais();
            } else if (opcao == 3) {
                mostrarRegras();
            }
        }

        System.out.println("Até a próxima!");
    }

    // ---------- Monta e inicia uma batalha ----------
    public static void iniciarBatalha() {
        // Treinador 1
        String nome1 = Entrada.lerTexto("\nNome do Treinador 1: ");
        if (nome1.trim().equals("")) {
            nome1 = "Treinador 1";
        }
        mostrarPokesais();
        int escolha1 = Entrada.lerInteiro(nome1 + ", escolha seu Pokésal (1 a 6): ", 1, 6);
        Pokesal pokesal1 = criarPokesal(escolha1);

        // Treinador 2
        String nome2 = Entrada.lerTexto("\nNome do Treinador 2: ");
        if (nome2.trim().equals("")) {
            nome2 = "Treinador 2";
        }
        mostrarPokesais();
        int escolha2 = Entrada.lerInteiro(nome2 + ", escolha seu Pokésal (1 a 6): ", 1, 6);
        Pokesal pokesal2 = criarPokesal(escolha2);

        // Terreno
        System.out.println("\nTerrenos do Estacionamento da UCSal:");
        System.out.println("1 - Asfalto Quente (golpes de Fogo causam +15% de dano)");
        System.out.println("2 - Poça de Chuva (golpes de Água causam +10% de dano)");
        System.out.println("3 - Canteiro Central (Pokésais de Planta recuperam 5% do HP no fim do round)");
        int escolhaTerreno = Entrada.lerInteiro("Escolha o terreno: ", 1, 3);

        String terreno = "";
        if (escolhaTerreno == 1) {
            terreno = "Asfalto Quente";
        } else if (escolhaTerreno == 2) {
            terreno = "Poça de Chuva";
        } else {
            terreno = "Canteiro Central";
        }

        // Cria os treinadores e a batalha
        Treinador treinador1 = new Treinador(nome1, pokesal1);
        Treinador treinador2 = new Treinador(nome2, pokesal2);
        Batalha batalha = new Batalha(treinador1, treinador2, terreno);

        batalha.iniciar();
    }

    // ---------- Cria um Pokésal novo de acordo com a opção escolhida ----------
    public static Pokesal criarPokesal(int opcao) {
        switch (opcao) {
            case 1:
                return new Pokesal("BulbaSal", "Planta", 50);
            case 2:
                return new Pokesal("CharSal", "Fogo", 70);
            case 3:
                return new Pokesal("SquirtSal", "Água", 40);
            case 4:
                return new Pokesal("ChikoSal", "Planta", 60);
            case 5:
                return new Pokesal("CyndaSal", "Fogo", 80);
            default:
                return new Pokesal("TotoSal", "Água", 50);
        }
    }

    // ---------- Telas de informação ----------
    public static void mostrarPokesais() {
        System.out.println("\nPokésais disponíveis (todos com HP 100 e ATK 10):");
        System.out.println("1 - BulbaSal  | Planta | SPD 50");
        System.out.println("2 - CharSal   | Fogo   | SPD 70");
        System.out.println("3 - SquirtSal | Água   | SPD 40");
        System.out.println("4 - ChikoSal  | Planta | SPD 60");
        System.out.println("5 - CyndaSal  | Fogo   | SPD 80");
        System.out.println("6 - TotoSal   | Água   | SPD 50");
    }

    public static void mostrarRegras() {
        System.out.println("\n----- REGRAS RESUMIDAS -----");
        System.out.println("- Dano base: 10. Modificadores somados: crítico +5, super efetivo +10,");
        System.out.println("  pouco efetivo -5, terreno (+1,5 Fogo / +1 Água), estado crítico +2,5.");
        System.out.println("- O dano é arredondado para o inteiro mais próximo.");
        System.out.println("- Acerto crítico: 10% de chance.");
        System.out.println("- Estado crítico: HP menor que 30 (ATK e SPD x1,25).");
        System.out.println("- Quem tem maior SPD (com bônus/penalidades) age primeiro.");
        System.out.println("- Cada treinador pode usar no máximo 2 itens por batalha.");
        System.out.println("- Depois do 3º round é possível desistir.");
        System.out.println("- Vence quem levar o HP do adversário a 0.");
    }
}
