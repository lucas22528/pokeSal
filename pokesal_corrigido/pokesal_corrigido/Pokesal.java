
public class Pokesal {

	private String nome;
	private String tipo;
	private int hp;
	private int hpMaximo;
	private int atk;
	private int spd;
	private String status;
	private boolean statusAtivo;

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

	public String getNome() {
		return nome;
	}

	public String getTipo() {
		return tipo;
	}

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		if (hp < 0) {
			hp = 0;
		}
		if (hp > hpMaximo) {
			hp = hpMaximo;
		}
		this.hp = hp;
	}

	public int getHpMaximo() {
		return hpMaximo;
	}

	public int getAtk() {
		return atk;
	}

	public int getSpd() {
		return spd;
	}

	public String getStatus() {
		return status;
	}

	public void receberDano(int dano) {
		setHp(hp - dano);
	}

	public void curar(int quantidade) {
		setHp(hp + quantidade);
	}

	public boolean estaDerrotado() {
		return hp == 0;
	}

	public boolean estaCritico() {
		return hp < 30;
	}

	public boolean aplicarStatus(String novoStatus) {
		if (status.equals("Nenhum")) {
			status = novoStatus;
			statusAtivo = false;
			return true;
		}
		return false;
	}

	public void removerStatus() {
		status = "Nenhum";
		statusAtivo = false;
	}

	public boolean estaQueimado() {
		return status.equals("Queimado") && statusAtivo;
	}

	public boolean estaParalisado() {
		return status.equals("Paralisado") && statusAtivo;
	}

	public String statusQueProvoca() {
		if (tipo.equals("Fogo")) {
			return "Queimado";
		} else if (tipo.equals("Planta")) {
			return "Envenenado";
		} else {
			return "Paralisado";
		}
	}

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

	public String getResumo() {
		String texto = nome + " (" + tipo + ") - HP: " + hp + "/" + hpMaximo + " - Status: " + status;
		if (estaCritico() && !estaDerrotado()) {
			texto = texto + " - ESTADO CRÍTICO";
		}
		return texto;
	}
}
