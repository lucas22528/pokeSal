

/**
 * Representa um Pokésal, contendo seus atributos, tipo, vida e efeitos de status.
 */
public class Pokesal {

	private String nome;
	private String tipo;
	private int hp;
	private int hpMaximo;
	private int atk;
	private int spd;
	private String status;
	private boolean statusAtivo;

	/**
     * Cria um Pokésal com nome, tipo e velocidade definidos.
     *
     * @param nome nome do Pokésal
     * @param tipo tipo elemental do Pokésal
     * @param spd velocidade base do Pokésal
     */
	public Pokesal(String nome, String tipo, int spd) {
		this.nome = nome;
		this.tipo = tipo;
		this.hpMaximo = 100;
		this.hp = 100;
		this.atk = 10;
		this.spd = spd;
		this.status = "Nenhum";
		this.statusAtivo = false;
	}

	/**
     * Retorna o nome do Pokésal.
     *
     * @return nome do Pokésal
     */
	public String getNome() {
		return nome;
	}

	/**
     * Retorna o tipo elemental do Pokésal.
     *
     * @return tipo elemental
     */
	public String getTipo() {
		return tipo;
	}

	/**
     * Retorna os pontos de vida atuais.
     *
     * @return pontos de vida atuais
     */
	public int getHp() {
		return hp;
	}

	/**
     * Atualiza os pontos de vida, mantendo-os entre zero e o máximo permitido.
     *
     * @param hp novos pontos de vida
     */
	public void setHp(int hp) {
		if (hp < 0) {
			hp = 0;
		}
		if (hp > hpMaximo) {
			hp = hpMaximo;
		}
		this.hp = hp;
	}

	/**
     * Retorna o máximo de pontos de vida.
     *
     * @return pontos de vida máximos
     */
	public int getHpMaximo() {
		return hpMaximo;
	}

	/**
     * Retorna o ataque base do Pokésal.
     *
     * @return ataque base
     */
	public int getAtk() {
		return atk;
	}

	/**
     * Retorna a velocidade base do Pokésal.
     *
     * @return velocidade base
     */
	public int getSpd() {
		return spd;
	}

	/**
     * Retorna o status atual do Pokésal.
     *
     * @return status atual
     */
	public String getStatus() {
		return status;
	}

	/**
     * Aplica dano ao Pokésal.
     *
     * @param dano quantidade de dano recebida
     */
	public void receberDano(int dano) {
		setHp(hp - dano);
	}

	/**
     * Recupera pontos de vida do Pokésal.
     *
     * @param quantidade quantidade de vida a recuperar
     */
	public void curar(int quantidade) {
		setHp(hp + quantidade);
	}

	/**
     * Verifica se o Pokésal foi derrotado.
     *
     * @return true quando os pontos de vida são zero
     */
	public boolean estaDerrotado() {
		return hp == 0;
	}

	/**
     * Verifica se o Pokésal está em estado crítico.
     *
     * @return true quando os pontos de vida são menores que 30
     */
	public boolean estaCritico() {
		return hp < 30;
	}

	/**
     * Aplica um novo status ao Pokésal caso ele não possua outro status.
     *
     * @param novoStatus status que será aplicado
     * @return true se o status foi aplicado
     */
	public boolean aplicarStatus(String novoStatus) {
		if (status.equals("Nenhum")) {
			status = novoStatus;
			statusAtivo = false;
			return true;
		}
		return false;
	}

	/**
     * Remove o status atual do Pokésal.
     */
	public void removerStatus() {
		status = "Nenhum";
		statusAtivo = false;
	}

	/**
     * Verifica se o Pokésal está sofrendo do status Queimado.
     *
     * @return true quando o status Queimado está ativo
     */
	public boolean estaQueimado() {
		return status.equals("Queimado") && statusAtivo;
	}

	/**
     * Verifica se o Pokésal está sofrendo do status Paralisado.
     *
     * @return true quando o status Paralisado está ativo
     */
	public boolean estaParalisado() {
		return status.equals("Paralisado") && statusAtivo;
	}

	/**
     * Determina o status provocado pelo tipo elemental do Pokésal.
     *
     * @return status correspondente ao tipo elemental
     */
	public String statusQueProvoca() {
		if (tipo.equals("Fogo")) {
			return "Queimado";
		} else if (tipo.equals("Planta")) {
			return "Envenenado";
		} else {
			return "Paralisado";
		}
	}

	/**
     * Processa o efeito do status atual no final do turno.
     *
     * @return mensagem descrevendo o efeito processado
     */
	public String processarStatusNoFimDoTurno() {
		if (status.equals("Nenhum")) {
			return "";
		}

		if (!statusAtivo) {
			statusAtivo = true;
			if (status.equals("Queimado") || status.equals("Envenenado")) {
				receberDano(3);
				return nome + " sofre 3 de dano por estar " + status + "!";
			}
			return nome + " está " + status + " e ficará mais lento na proxima rodada.";
		}

		String statusAntigo = status;
		removerStatus();
		return nome + " não está mais " + statusAntigo + ".";
	}

	/**
     * Calcula a velocidade efetiva considerando estados que alteram a velocidade.
     *
     * @return velocidade efetiva
     */
	public double getSpdEfetiva() {
		double velocidade = spd;

		if (estaCritico()) {
			velocidade = velocidade * 1.25;
		}
		if (estaParalisado()) {
			velocidade = velocidade * 0.5;
		}
		return velocidade;
	}

	/**
     * Calcula o multiplicador de dano entre os tipos do atacante e defensor.
     *
     * @param defensor Pokésal que receberá o ataque
     * @return multiplicador de dano por vantagem ou desvantagem de tipo
     */
	public double multiplicadorDeTipo(Pokesal defensor) {
		String tipoDefensor = defensor.getTipo();

		if (tipo.equals("Fogo") && tipoDefensor.equals("Planta")) {
			return 2.0;
		}
		if (tipo.equals("Fogo") && tipoDefensor.equals("Água")) {
			return 0.5;
		}
		if (tipo.equals("Água") && tipoDefensor.equals("Fogo")) {
			return 2.0;
		}
		if (tipo.equals("Água") && tipoDefensor.equals("Planta")) {
			return 0.5;
		}
		if (tipo.equals("Planta") && tipoDefensor.equals("Água")) {
			return 2.0;
		}
		if (tipo.equals("Planta") && tipoDefensor.equals("Fogo")) {
			return 0.5;
		}
		return 1.0;
	}

	/**
     * Calcula o dano causado ao defensor considerando regras da batalha.
     *
     * @param defensor Pokésal que receberá o dano
     * @param terreno terreno atual da batalha
     * @param acertoCritico indica se o ataque foi crítico
     * @return dano calculado antes do arredondamento final
     */
	public double calcularDano(Pokesal defensor, String terreno, boolean acertoCritico) {
		double dano = atk;

		if (acertoCritico) {
			dano = dano + 5;
		}

		double multiplicador = multiplicadorDeTipo(defensor);
		if (multiplicador == 2.0) {
			dano = dano + 10;
		} else if (multiplicador == 0.5) {
			dano = dano - 5;
		}

		if (terreno.equals("Asfalto Quente") && tipo.equals("Fogo")) {
			dano = dano + 1.5;
		}
		if (terreno.equals("Poça de Chuva") && tipo.equals("Água")) {
			dano = dano + 1;
		}

		if (estaCritico()) {
			dano = dano * 1.25;
		}
		if (estaQueimado()) {
			dano = dano * 0.5;
		}

		if (dano < 0) {
			dano = 0;
		}
		return dano;
	}

	/**
     * Monta um resumo dos principais dados do Pokésal.
     *
     * @return texto com nome, tipo, vida e status
     */
	public String getResumo() {
		String texto = nome + " (" + tipo + ") - HP: " + hp + "/" + hpMaximo + " - Status: " + status;
		if (estaCritico() && !estaDerrotado()) {
			texto = texto + " - ESTADO CRÍTICO";
		}
		return texto;
	}
}
