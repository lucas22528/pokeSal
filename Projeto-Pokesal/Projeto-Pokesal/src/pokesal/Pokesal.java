package pokesal;

/**
 * Representa um Pokésal, contendo seus atributos, tipo, vida e efeitos de status.
 */
public class Pokesal {

    private static final int HP_MAXIMO_PADRAO = 100;
    private static final int ATAQUE_PADRAO = 10;
    private static final int HP_CRITICO = 30;
    private static final int DANO_STATUS = 3;
    private static final double MULTIPLICADOR_SUPER_EFETIVO = 2.0;
    private static final double MULTIPLICADOR_POUCO_EFETIVO = 0.5;
    private static final double MULTIPLICADOR_NORMAL = 1.0;
    private static final double BONUS_CRITICO = 1.25;
    private static final double PENALIDADE_PARALISIA = 0.5;
    private static final int BONUS_ACERTO_CRITICO = 5;
    private static final int BONUS_SUPER_EFETIVO = 10;
    private static final int PENALIDADE_POUCO_EFETIVO = 5;
    private static final double BONUS_ASFALTO = 1.5;
    private static final int BONUS_POCA = 1;

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
        this.hpMaximo = HP_MAXIMO_PADRAO;
        this.hp = HP_MAXIMO_PADRAO;
        this.atk = ATAQUE_PADRAO;
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
        return hp < HP_CRITICO;
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
                receberDano(DANO_STATUS);
                return nome + " sofre " + DANO_STATUS + " de dano por estar " + status + "!";
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
            velocidade = velocidade * BONUS_CRITICO;
        }
        if (estaParalisado()) {
            velocidade = velocidade * PENALIDADE_PARALISIA;
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
            return MULTIPLICADOR_SUPER_EFETIVO;
        }
        if (tipo.equals("Fogo") && tipoDefensor.equals("Água")) {
            return MULTIPLICADOR_POUCO_EFETIVO;
        }
        if (tipo.equals("Água") && tipoDefensor.equals("Fogo")) {
            return MULTIPLICADOR_SUPER_EFETIVO;
        }
        if (tipo.equals("Água") && tipoDefensor.equals("Planta")) {
            return MULTIPLICADOR_POUCO_EFETIVO;
        }
        if (tipo.equals("Planta") && tipoDefensor.equals("Água")) {
            return MULTIPLICADOR_SUPER_EFETIVO;
        }
        if (tipo.equals("Planta") && tipoDefensor.equals("Fogo")) {
            return MULTIPLICADOR_POUCO_EFETIVO;
        }
        return MULTIPLICADOR_NORMAL;
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
            dano = dano + BONUS_ACERTO_CRITICO;
        }

        double multiplicador = multiplicadorDeTipo(defensor);
        if (multiplicador == MULTIPLICADOR_SUPER_EFETIVO) {
            dano = dano + BONUS_SUPER_EFETIVO;
        } else if (multiplicador == MULTIPLICADOR_POUCO_EFETIVO) {
            dano = dano - PENALIDADE_POUCO_EFETIVO;
        }

        if (terreno.equals("Asfalto Quente") && tipo.equals("Fogo")) {
            dano = dano + BONUS_ASFALTO;
        }
        if (terreno.equals("Poça de Chuva") && tipo.equals("Água")) {
            dano = dano + BONUS_POCA;
        }

        if (estaCritico()) {
            dano = dano * BONUS_CRITICO;
        }
        if (estaQueimado()) {
            dano = dano * PENALIDADE_PARALISIA;
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
        String texto = nome + " (" + tipo + ") - HP: " + hp + "/"
                + hpMaximo + " - Status: " + status;
        if (estaCritico() && !estaDerrotado()) {
            texto = texto + " - ESTADO CRÍTICO";
        }
        return texto;
    }
}
