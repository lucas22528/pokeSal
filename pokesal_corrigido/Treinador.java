
public class Treinador {

	public static final int MAX_ITENS = 2;

	private String nome;
	private Pokesal pokesal;
	private int itensUsados;

	public Treinador(String nome, Pokesal pokesal) {
		this.nome = nome;
		this.pokesal = pokesal;
		this.itensUsados = 0;
	}

	public String getNome() {
		return nome;
	}

	public Pokesal getPokesal() {
		return pokesal;
	}

	public int getItensUsados() {
		return itensUsados;
	}

	public boolean podeUsarItem() {
		return itensUsados < MAX_ITENS;
	}

	public void reiniciarItens() {
		itensUsados = 0;
	}

	public String usarPotion() {
		itensUsados++;
		int hpAntes = pokesal.getHp();
		pokesal.curar(20);
		int recuperado = pokesal.getHp() - hpAntes;
		return nome + " usou Potion! " + pokesal.getNome() + " recuperou " + recuperado + " HP.";
	}

	public String usarSuperPotion() {
		itensUsados++;
		int hpAntes = pokesal.getHp();
		pokesal.curar(40);
		int recuperado = pokesal.getHp() - hpAntes;

		return nome + " usou Super Potion e " + pokesal.getNome() + " recuperou " + recuperado + " HP.";

	}

	public String usarAntidote() {
		itensUsados++;
		if (pokesal.getStatus().equals("Envenenado")) {
			pokesal.removerStatus();
			return nome + " usou Antidote e " + pokesal.getNome() + " foi curado do veneno.";
		}
		return nome + " usou Antidote, mas " + pokesal.getNome() + " não estava envenenado.";
	}
}
