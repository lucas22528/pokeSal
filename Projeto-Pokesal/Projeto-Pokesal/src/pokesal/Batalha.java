package pokesal;

import java.util.Random;

/**
 * Representa uma batalha entre dois treinadores e seus respectivos Pokésais.
 * Controla turnos, ataques, itens, efeitos de status e condições de vitória.
 */
public class Batalha {

    private static final int OPCAO_ATACAR = 1;
    private static final int OPCAO_ITEM = 2;
    private static final int OPCAO_DESISTIR = 3;
    private static final int OPCAO_VOLTAR = 0;
    private static final int LIMITE_DESISTENCIA = 3;
    private static final int PERCENTUAL_CRITICO = 10;
    private static final int CHANCE_STATUS = 10;
    private static final int LIMITE_SORTEIO = 100;
    private static final int LIMITE_SORTEIO_BINARIO = 2;
    private static final int CURA_CANTEIRO_PERCENTUAL = 5;
    private static final double MULTIPLICADOR_SUPER_EFETIVO = 2.0;
    private static final double MULTIPLICADOR_POUCO_EFETIVO = 0.5;

    private Treinador treinador1;
    private Treinador treinador2;
    private String terreno;
    private int rodada;
    private Random random;

    /**
     * Cria uma batalha entre dois treinadores no terreno informado.
     *
     * @param treinador1 primeiro treinador
     * @param treinador2 segundo treinador
     * @param terreno terreno onde a batalha ocorrerá
     */
    public Batalha(Treinador treinador1, Treinador treinador2, String terreno) {
        this.treinador1 = treinador1;
        this.treinador2 = treinador2;
        this.terreno = terreno;
        this.rodada = 0;
        this.random = new Random();
    }

    /**
     * Inicia a batalha e executa seus turnos até que exista um vencedor ou empate.
     */
    public void iniciar() {

        treinador1.reiniciarItens();
        treinador2.reiniciarItens();

        System.out.println("\n A BATALHA COMEÇOU");
        System.out.println("Terreno: " + terreno);

        boolean batalhaAcabou = false;

        while (!batalhaAcabou) {
            rodada++;
            System.out.println("\n RODADA " + rodada);
            mostrarPlacar();

            Treinador primeiro = definirQuemComeca();
            Treinador segundo;
            if (primeiro == treinador1) {
                segundo = treinador2;
            } else {
                segundo = treinador1;
            }

            batalhaAcabou = executarTurno(primeiro, segundo);

            if (!batalhaAcabou) {
                batalhaAcabou = executarTurno(segundo, primeiro);
            }

            if (!batalhaAcabou) {
                batalhaAcabou = finalizarRound();
            }
        }

        System.out.println("\n FIM DA BATALHA. Obrigado!");
    }

    private Treinador definirQuemComeca() {
        double spd1 = treinador1.getPokesal().getSpdEfetiva();
        double spd2 = treinador2.getPokesal().getSpdEfetiva();

        System.out.println("Velocidade: " + treinador1.getPokesal().getNome() + " = " + spd1 + " | "
                + treinador2.getPokesal().getNome() + " = " + spd2);

        if (spd1 > spd2) {
            return treinador1;
        } else if (spd2 > spd1) {
            return treinador2;
        } else {
            System.out.println("Empate de velocidade, sorteando quem comeca.");
            if (random.nextInt(LIMITE_SORTEIO_BINARIO) == OPCAO_VOLTAR) {
                return treinador1;
            } else {
                return treinador2;
            }
        }
    }

    private boolean executarTurno(Treinador atual, Treinador oponente) {
        System.out.println("\n Vez de " + atual.getNome()
                + " (" + atual.getPokesal().getNome() + ")");

        boolean turnoUsado = false;

        while (!turnoUsado) {
            System.out.println("1: Atacar");
            System.out.println("2: Usar item (usados: " + atual.getItensUsados()
                    + "/" + Treinador.MAX_ITENS + ")");

            int ultimaOpcao = OPCAO_ITEM;
            if (rodada > LIMITE_DESISTENCIA) {
                System.out.println("3: Desistir");
                ultimaOpcao = OPCAO_DESISTIR;
            }

            int opcao = Entrada.lerInteiro("Escolha sua ação: ", 1, ultimaOpcao);

            if (opcao == OPCAO_ATACAR) {
                atacar(atual, oponente);
                turnoUsado = true;
            } else if (opcao == OPCAO_ITEM) {
                if (!atual.podeUsarItem()) {
                    System.out.println("Você ja usou o máximo de itens nesta batalha. "
                        + "Escolha outra ação.");
                } else {
                    turnoUsado = escolherItem(atual);
                }
            } else if (opcao == OPCAO_DESISTIR) {
                System.out.println(atual.getNome() + " desistiu da batalha!");
                anunciarVencedor(oponente);
                return true;
            }
        }

        if (oponente.getPokesal().estaDerrotado()) {
            System.out.println(oponente.getPokesal().getNome() + " foi derrotado!");
            anunciarVencedor(atual);
            return true;
        }

        return false;
    }

    private void atacar(Treinador atacante, Treinador defensor) {
        Pokesal pokesalAtacante = atacante.getPokesal();
        Pokesal pokesalDefensor = defensor.getPokesal();

        boolean acertoCritico = random.nextInt(LIMITE_SORTEIO) < PERCENTUAL_CRITICO;

        double danoCalculado = pokesalAtacante.calcularDano(
                pokesalDefensor, terreno, acertoCritico);

        int danoFinal = (int) Math.round(danoCalculado);
        pokesalDefensor.receberDano(danoFinal);

        System.out.println(pokesalAtacante.getNome() + " atacou "
                + pokesalDefensor.getNome() + "!");

        if (acertoCritico) {
            System.out.println("Acerto crítico!");
        }

        double multiplicador = pokesalAtacante.multiplicadorDeTipo(pokesalDefensor);
        if (multiplicador == MULTIPLICADOR_SUPER_EFETIVO) {
            System.out.println("É super efetivo!");
        } else if (multiplicador == MULTIPLICADOR_POUCO_EFETIVO) {
            System.out.println("Não é muito efetivo...");
        }

        System.out.println("Dano calculado: " + danoCalculado + " -> dano causado: " + danoFinal);
        System.out.println(pokesalDefensor.getNome() + " ficou com "
                + pokesalDefensor.getHp() + " HP.");

        if (pokesalDefensor.estaCritico() && !pokesalDefensor.estaDerrotado()) {
            System.out.println(pokesalDefensor.getNome() + " entrou em estado critico!");
        }

        tentarAplicarStatus(pokesalAtacante, pokesalDefensor);
    }

    private void tentarAplicarStatus(Pokesal atacante, Pokesal defensor) {
        if (defensor.estaDerrotado()) {
            return;
        }

        int sorteio = random.nextInt(LIMITE_SORTEIO);
        if (sorteio < CHANCE_STATUS) {
            String novoStatus = atacante.statusQueProvoca();
            boolean aplicou = defensor.aplicarStatus(novoStatus);
            if (aplicou) {
                System.out.println(defensor.getNome() + " ficou " + novoStatus
                        + "! (o efeito começa no fim do round)");
            }
        }
    }

    private boolean escolherItem(Treinador treinador) {
        System.out.println("\nItens disponíveis:");
        System.out.println("1: Potion (+20 HP)");
        System.out.println("2: Super Potion (+40 HP)");
        System.out.println("3: Antidote (remove o veneno)");
        System.out.println("0: Voltar");

        int opcao = Entrada.lerInteiro("Escolha o item: ", OPCAO_VOLTAR, OPCAO_DESISTIR);

        if (opcao == OPCAO_VOLTAR) {
            return false;
        }

        String mensagem = "";
        if (opcao == OPCAO_ATACAR) {
            mensagem = treinador.usarPotion();
        } else if (opcao == OPCAO_ITEM) {
            mensagem = treinador.usarSuperPotion();
        } else if (opcao == OPCAO_DESISTIR) {
            mensagem = treinador.usarAntidote();
        }

        System.out.println(mensagem);
        return true;
    }

    private boolean finalizarRound() {
        System.out.println("\n Fim da rodada " + rodada + "! ");

        Pokesal p1 = treinador1.getPokesal();
        Pokesal p2 = treinador2.getPokesal();

        mostrarMensagem(p1.processarStatusNoFimDoTurno());
        mostrarMensagem(p2.processarStatusNoFimDoTurno());

        curarNoCanteiro(p1);
        curarNoCanteiro(p2);

        if (p1.estaDerrotado() && p2.estaDerrotado()) {
            System.out.println("Os dois Pokesais foram derrotados ao mesmo tempo! "
                    + "A batalha terminou em empate.");
            return true;
        }
        if (p1.estaDerrotado()) {
            System.out.println(p1.getNome() + " foi derrotado!");
            anunciarVencedor(treinador2);
            return true;
        }
        if (p2.estaDerrotado()) {
            System.out.println(p2.getNome() + " foi derrotado!");
            anunciarVencedor(treinador1);
            return true;
        }

        return false;
    }

    private void curarNoCanteiro(Pokesal pokesal) {
        if (terreno.equals("Canteiro Central")
                && pokesal.getTipo().equals("Planta")
                && !pokesal.estaDerrotado()) {
            int cura = pokesal.getHpMaximo() * CURA_CANTEIRO_PERCENTUAL / LIMITE_SORTEIO;
            int hpAntes = pokesal.getHp();
            pokesal.curar(cura);
            int recuperado = pokesal.getHp() - hpAntes;

            if (recuperado > 0) {
                System.out.println(pokesal.getNome() + " recuperou " + recuperado
                    + " HP no Canteiro Central.");
            }
        }
    }

    private void mostrarPlacar() {
        System.out.println(treinador1.getNome() + ": " + treinador1.getPokesal().getResumo());
        System.out.println(treinador2.getNome() + ": " + treinador2.getPokesal().getResumo());
    }

    private void mostrarMensagem(String mensagem) {
        if (!mensagem.equals("")) {
            System.out.println(mensagem);
        }
    }

    private void anunciarVencedor(Treinador vencedor) {
        System.out.println("\n VENCEDOR: " + vencedor.getNome()
                + " com " + vencedor.getPokesal().getNome() + "!");
    }
}
