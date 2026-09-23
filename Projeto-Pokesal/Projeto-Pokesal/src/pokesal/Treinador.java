package pokesal;

/**
 * Representa um treinador e o Pokésal escolhido para participar da batalha.
 */
public class Treinador {

    public static final int MAX_ITENS = 2;
    private static final int CURA_POTION = 20;
    private static final int CURA_SUPER_POTION = 40;

    private String nome;
    private Pokesal pokesal;
    private int itensUsados;

    /**
     * Cria um treinador associado a um Pokésal.
     *
     * @param nome nome do treinador
     * @param pokesal Pokésal escolhido pelo treinador
     */
    public Treinador(String nome, Pokesal pokesal) {
        this.nome = nome;
        this.pokesal = pokesal;
        this.itensUsados = 0;
    }

    /**
     * Retorna o nome do treinador.
     *
     * @return nome do treinador
     */
    public String getNome() {
        return nome;
    }

    /**
     * Retorna o Pokésal do treinador.
     *
     * @return Pokésal associado ao treinador
     */
    public Pokesal getPokesal() {
        return pokesal;
    }

    /**
     * Retorna a quantidade de itens usados na batalha atual.
     *
     * @return quantidade de itens usados
     */
    public int getItensUsados() {
        return itensUsados;
    }

    /**
     * Verifica se o treinador ainda pode usar itens na batalha.
     *
     * @return true quando ainda há itens disponíveis
     */
    public boolean podeUsarItem() {
        return itensUsados < MAX_ITENS;
    }

    /**
     * Reinicia a contagem de itens usados pelo treinador.
     */
    public void reiniciarItens() {
        itensUsados = 0;
    }

    /**
     * Usa uma Potion para recuperar pontos de vida do Pokésal.
     *
     * @return mensagem descrevendo o uso do item
     */
    public String usarPotion() {
        itensUsados++;
        int hpAntes = pokesal.getHp();
        pokesal.curar(CURA_POTION);
        int recuperado = pokesal.getHp() - hpAntes;
        return nome + " usou Potion e " + pokesal.getNome() + " recuperou " + recuperado + " HP.";
    }

    /**
     * Usa uma Super Potion para recuperar pontos de vida do Pokésal.
     *
     * @return mensagem descrevendo o uso do item
     */
    public String usarSuperPotion() {
        itensUsados++;
        int hpAntes = pokesal.getHp();
        pokesal.curar(CURA_SUPER_POTION);
        int recuperado = pokesal.getHp() - hpAntes;

        return nome + " usou Super Potion e " + pokesal.getNome()
                + " recuperou " + recuperado + " HP.";

    }

    /**
     * Usa um Antidote para remover o status de envenenamento.
     *
     * @return mensagem descrevendo o uso do item
     */
    public String usarAntidote() {
        itensUsados++;
        if (pokesal.getStatus().equals("Envenenado")) {
            pokesal.removerStatus();
            return nome + " usou Antidote e " + pokesal.getNome() + " foi curado do veneno.";
        }
        return nome + " usou Antidote, mas " + pokesal.getNome() + " não estava envenenado.";
    }
}
