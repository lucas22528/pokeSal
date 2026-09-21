/*
 * Classe que representa um treinador.
 * Cada treinador tem um nome, um Pokésal e um contador de itens usados.
 */
public class Treinador {

    // Constante: máximo de itens que cada treinador pode usar por batalha
    public static final int MAX_ITENS = 2;

    private String nome;
    private Pokesal pokesal;
    private int itensUsados;

    public Treinador(String nome, Pokesal pokesal) {
        this.nome = nome;
        this.pokesal = pokesal;
        this.itensUsados = 0;
    }

    // ---------- Getters ----------
    public String getNome() {
        return nome;
    }

    public Pokesal getPokesal() {
        return pokesal;
    }

    public int getItensUsados() {
        return itensUsados;
    }

    // ---------- Controle de itens ----------
    public boolean podeUsarItem() {
        return itensUsados < MAX_ITENS;
    }

    // Chamado no início de cada nova batalha
    public void reiniciarItens() {
        itensUsados = 0;
    }

    // Cada método de item devolve uma mensagem para ser mostrada na tela.

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
        
        return nome + " usou Super Potion! " + pokesal.getNome() + " recuperou " + recuperado + " HP.";


    }

    public String usarAntidote() {
        itensUsados++;
        if (pokesal.getStatus().equals("Envenenado")) {
            pokesal.removerStatus();
            return nome + " usou Antidote! " + pokesal.getNome() + " foi curado do veneno.";
        }
        return nome + " usou Antidote, mas " + pokesal.getNome() + " não estava envenenado. Nada aconteceu.";
    }
}
