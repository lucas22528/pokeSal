/*
 * Classe que representa um Pokésal.
 * Todos os atributos são privados (encapsulamento) e só podem ser
 * acessados pelos métodos get e set.
 */
public class Pokesal {

    // ---------- Atributos ----------
    private String nome;
    private String tipo;          // "Fogo", "Água" ou "Planta"
    private int hp;               // vida atual
    private int hpMaximo;         // vida máxima (100)
    private int atk;              // dano base (10)
    private int spd;              // velocidade original
    private String status;        // "Nenhum", "Queimado", "Envenenado" ou "Paralisado"
    private boolean statusAtivo;  // false = acabou de receber o status; true = já está fazendo efeito

    // ---------- Construtor ----------
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

    // ---------- Getters e Setters ----------
    public String getNome() {
        return nome;
    }

    public String getTipo() {
        return tipo;
    }

    public int getHp() {
        return hp;
    }

    // O HP nunca pode ficar abaixo de 0 nem acima do máximo
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

    // ---------- Métodos de vida ----------
    public void receberDano(int dano) {
        setHp(hp - dano);
    }

    public void curar(int quantidade) {
        setHp(hp + quantidade);
    }

    public boolean estaDerrotado() {
        return hp == 0;
    }

    // Estado crítico: HP menor que 30 (HP = 30 ainda é normal)
    public boolean estaCritico() {
        return hp < 30;
    }

    // ---------- Métodos de status ----------
    // Um status novo só entra se o Pokésal não tiver nenhum status.
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

    // Só conta como queimado/paralisado quando o efeito já está ativo
    public boolean estaQueimado() {
        return status.equals("Queimado") && statusAtivo;
    }

    public boolean estaParalisado() {
        return status.equals("Paralisado") && statusAtivo;
    }

    // Qual status este Pokésal pode causar ao acertar um golpe
    public String statusQueProvoca() {
        if (tipo.equals("Fogo")) {
            return "Queimado";
        } else if (tipo.equals("Planta")) {
            return "Envenenado";
        } else {
            return "Paralisado";
        }
    }

    /*
     * Chamado no fim de cada round.
     * - Status novo: passa a fazer efeito (Queimado e Envenenado tiram 3 de HP agora).
     * - Status que já estava ativo: dura só 1 turno, então é removido.
     * Devolve uma mensagem para a batalha mostrar na tela (ou texto vazio).
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

    // ---------- Velocidade ----------
    // Velocidade usada na iniciativa: considera estado crítico (x1,25) e paralisia (x0,5)
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

    // ---------- Dano ----------
    // Devolve o multiplicador de tipo: 2.0 (super efetivo), 0.5 (pouco efetivo) ou 1.0 (normal)
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
        return 1.0; // mesmo tipo
    }

    /*
     * Calcula o dano SEM arredondar.
     * Começa no dano base (ATK = 10) e cada modificador soma ou subtrai uma parcela:
     *   Acerto crítico ......... +5
     *   Super efetivo .......... +10
     *   Pouco efetivo .......... -5
     *   Asfalto Quente (Fogo) .. +1,5
     *   Poça de Chuva (Água) ... +1
     *   Estado crítico (ATK x1,25) ... +2,5
     *   Queimado (-5 de ATK) ........ -5
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
            dano = dano* 1.25;
        }
        if (estaQueimado()) {
            dano = dano*0.5;
        }

        // O dano nunca pode ser negativo
        if (dano < 0) {
            dano = 0;
        }
        return dano;
    }

    // ---------- Texto para mostrar na tela ----------
    public String getResumo() {
        String texto = nome + " (" + tipo + ") - HP: " + hp + "/" + hpMaximo + " - Status: " + status;
        if (estaCritico() && !estaDerrotado()) {
            texto = texto + " - ESTADO CRÍTICO";
        }
        return texto;
    }
}
