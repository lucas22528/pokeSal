

/**
 * Classe principal responsável por iniciar e controlar o menu do jogo PokeSal.
 */
public class Main {

	/**
     * Executa o menu principal do jogo.
     *
     * @param args argumentos da linha de comando
     */
	public static void main(String[] args) {
		int opcao = -1;

		while (opcao != 0) {
			System.out.println("Jogo de batalha POKESAL:");

			System.out.println("1: Iniciar batalha");
			System.out.println("2: Ver Pokesais disponíveis");
			System.out.println("0: Sair");

			opcao = Entrada.lerInteiro("Escolha uma opção: ", 0, 2);

			if (opcao == 1) {
				iniciarBatalha();
			} else if (opcao == 2) {
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
		int escolha1 = Entrada.lerInteiro(nome1 + ", escolha seu Pokesal (1 a 6): ", 1, 6);
		Pokesal pokesal1 = criarPokesal(escolha1);

		String nome2 = Entrada.lerTexto("\nNome do Treinador 2: ");
		if (nome2.trim().equals("")) {
			nome2 = "Treinador 2";
		}
		mostrarPokesais();
		int escolha2 = Entrada.lerInteiro(nome2 + ", escolha seu Pokesal (1 a 6): ", 1, 6);
		Pokesal pokesal2 = criarPokesal(escolha2);

		System.out.println("\nTerrenos do Estacionamento da UCSal:");
		System.out.println("1: Asfalto Quente");
		System.out.println("2: Poça de Chuva");
		System.out.println("3: Canteiro Central");
		int escolhaTerreno = Entrada.lerInteiro("Escolha o terreno: ", 1, 3);

		String terreno = "";
		if (escolhaTerreno == 1) {
			terreno = "Asfalto Quente";
		} else if (escolhaTerreno == 2) {
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
			return new Pokesal("BulbaSal", "Planta", 50);
		case 2:
			return new Pokesal("CharSal", "Fogo", 70);
		case 3:
			return new Pokesal("SquirtSal", "Agua", 40);
		case 4:
			return new Pokesal("ChikoSal", "Planta", 60);
		case 5:
			return new Pokesal("CyndaSal", "Fogo", 80);
		default:
			return new Pokesal("TotoSal", "Agua", 50);
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
