package pokesal;

/**
 * Classe principal responsável por iniciar e controlar o menu do jogo PokeSal.
 */
public final class Main {

    private static final int OPCAO_SAIR = 0;
    private static final int OPCAO_INICIAR = 1;
    private static final int OPCAO_MOSTRAR = 2;
    private static final int PRIMEIRO_POKESAL = 1;
    private static final int ULTIMO_POKESAL = 6;
    private static final int PRIMEIRO_TERRENO = 1;
    private static final int SEGUNDO_TERRENO = 2;
    private static final int ULTIMO_TERRENO = 3;
    private static final int SPD_BULBASAL = 50;
    private static final int SPD_CHARSAL = 70;
    private static final int SPD_SQUIRTSAL = 40;
    private static final int SPD_CHIKOSAL = 60;
    private static final int SPD_CYNDASAL = 80;
    private static final int SPD_TOTOSAL = 50;

    private Main() {
        throw new IllegalStateException("Classe utilitária.");
    }

    /**
     * Executa o menu principal do jogo.
     *
     * @param args argumentos da linha de comando
     */
    public static void main(String[] args) {
        int opcao = -1;

        while (opcao != OPCAO_SAIR) {
            System.out.println("Jogo de batalha POKESAL:");

            System.out.println("1: Iniciar batalha");
            System.out.println("2: Ver Pokesais disponíveis");
            System.out.println("0: Sair");

            opcao = Entrada.lerInteiro("Escolha uma opção: ", OPCAO_SAIR, OPCAO_MOSTRAR);

            if (opcao == OPCAO_INICIAR) {
                iniciarBatalha();
            } else if (opcao == OPCAO_MOSTRAR) {
                mostrarPokesais();
            }
        }

        System.out.println("Ate a próxima!");
    }

    /**
     * Solicita os dados dos treinadores e inicia uma nova batalha.
     */
    public static void iniciarBatalha() {

        String nome1 = Entrada.lerTexto("\nNome do Treinador 1: ");
        if (nome1.trim().equals("")) {
            nome1 = "Treinador 1";
        }
        mostrarPokesais();
        int escolha1 = Entrada.lerInteiro(
                nome1 + ", escolha seu Pokesal (1 a 6): ",
                PRIMEIRO_POKESAL, ULTIMO_POKESAL);
        Pokesal pokesal1 = criarPokesal(escolha1);

        String nome2 = Entrada.lerTexto("\nNome do Treinador 2: ");
        if (nome2.trim().equals("")) {
            nome2 = "Treinador 2";
        }
        mostrarPokesais();
        int escolha2 = Entrada.lerInteiro(
                nome2 + ", escolha seu Pokesal (1 a 6): ",
                PRIMEIRO_POKESAL, ULTIMO_POKESAL);
        Pokesal pokesal2 = criarPokesal(escolha2);

        System.out.println("\nTerrenos do Estacionamento da UCSal:");
        System.out.println("1: Asfalto Quente");
        System.out.println("2: Poça de Chuva");
        System.out.println("3: Canteiro Central");
        int escolhaTerreno = Entrada.lerInteiro(
                "Escolha o terreno: ", PRIMEIRO_TERRENO, ULTIMO_TERRENO);

        String terreno = "";
        if (escolhaTerreno == PRIMEIRO_TERRENO) {
            terreno = "Asfalto Quente";
        } else if (escolhaTerreno == SEGUNDO_TERRENO) {
            terreno = "Poça de Chuva";
        } else {
            terreno = "Canteiro Central";
        }

        Treinador treinador1 = new Treinador(nome1, pokesal1);
        Treinador treinador2 = new Treinador(nome2, pokesal2);
        Batalha batalha = new Batalha(treinador1, treinador2, terreno);

        batalha.iniciar();
    }

    /**
     * Cria um Pokésal de acordo com a opção selecionada.
     *
     * @param opcao opção correspondente ao Pokésal escolhido
     * @return Pokésal criado
     */
    public static Pokesal criarPokesal(int opcao) {
        switch (opcao) {
        case 1:
            return new Pokesal("BulbaSal", "Planta", SPD_BULBASAL);
        case 2:
            return new Pokesal("CharSal", "Fogo", SPD_CHARSAL);
        case 3:
            return new Pokesal("SquirtSal", "Agua", SPD_SQUIRTSAL);
        case 4:
            return new Pokesal("ChikoSal", "Planta", SPD_CHIKOSAL);
        case 5:
            return new Pokesal("CyndaSal", "Fogo", SPD_CYNDASAL);
        default:
            return new Pokesal("TotoSal", "Agua", SPD_TOTOSAL);
        }
    }

    /**
     * Exibe no console os Pokésais disponíveis para seleção.
     */
    public static void mostrarPokesais() {
        System.out.println("\nPokesais disponíveis:");
        System.out.println("1: BulbaSal  | Planta | SPD 50");
        System.out.println("2: CharSal   |Fogo   | SPD 70");
        System.out.println("3: SquirtSal | Agua   | SPD 40");
        System.out.println("4: ChikoSal  | Planta | SPD 60");
        System.out.println("5: CyndaSal  | Fogo   | SPD 80");
        System.out.println("6: TotoSal   | Agua   | SPD 50");
    }

}
