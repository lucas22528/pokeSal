import java.util.Random;

/*
 * Classe que controla uma batalha entre dois treinadores.
 * Ela organiza os rounds, a ordem dos ataques, o uso de itens,
 * a desistência e o fim da batalha.
 */
public class Batalha {

    private Treinador treinador1;
    private Treinador treinador2;
    private String terreno;
    private int rodada;
    private Random random;

    public Batalha(Treinador treinador1, Treinador treinador2, String terreno) {
        this.treinador1 = treinador1;
        this.treinador2 = treinador2;
        this.terreno = terreno;
        this.rodada = 0;
        this.random = new Random();
    }

    // ---------- Fluxo principal da batalha ----------
    public void iniciar() {
        // O contador de itens é reiniciado no início de cada batalha
        treinador1.reiniciarItens();
        treinador2.reiniciarItens();

        System.out.println("\n===== A BATALHA COMEÇOU! =====");
        System.out.println("Terreno: " + terreno);

        boolean batalhaAcabou = false;

        while (!batalhaAcabou) {
            rodada++;
            System.out.println("\n========== ROUND " + rodada + " ==========");
            mostrarPlacar();

            // Descobre quem age primeiro neste round
            Treinador primeiro = definirQuemComeca();
            Treinador segundo;
            if (primeiro == treinador1) {
                segundo = treinador2;
            } else {
                segundo = treinador1;
            }

            // Turno do primeiro
            batalhaAcabou = executarTurno(primeiro, segundo);

            // Turno do segundo (só acontece se a batalha não acabou)
            if (!batalhaAcabou) {
                batalhaAcabou = executarTurno(segundo, primeiro);
            }

            // Efeitos de fim de round (status e terreno)
            if (!batalhaAcabou) {
                batalhaAcabou = finalizarRound();
            }
        }

        System.out.println("\n===== FIM DA BATALHA =====");
    }

    // ---------- Iniciativa ----------
    // Quem tem maior velocidade (com bônus de estado crítico e paralisia) age primeiro.
    // Se empatar, sorteia.
    private Treinador definirQuemComeca() {
        double spd1 = treinador1.getPokesal().getSpdEfetiva();
        double spd2 = treinador2.getPokesal().getSpdEfetiva();

        System.out.println("Velocidade: " + treinador1.getPokesal().getNome() + " = " + spd1
                + " | " + treinador2.getPokesal().getNome() + " = " + spd2);

        if (spd1 > spd2) {
            return treinador1;
        } else if (spd2 > spd1) {
            return treinador2;
        } else {
            System.out.println("Empate de velocidade! Sorteando quem começa...");
            if (random.nextInt(2) == 0) {
                return treinador1;
            } else {
                return treinador2;
            }
        }
    }

    // ---------- Turno de um treinador ----------
    // Devolve true se a batalha terminou durante este turno.
    private boolean executarTurno(Treinador atual, Treinador oponente) {
        System.out.println("\n--- Vez de " + atual.getNome() + " (" + atual.getPokesal().getNome() + ") ---");

        boolean turnoUsado = false;

        while (!turnoUsado) {
            System.out.println("1 - Atacar");
            System.out.println("2 - Usar item (usados: " + atual.getItensUsados() + "/" + Treinador.MAX_ITENS + ")");

            // A desistência só aparece depois que o terceiro round terminou
            int ultimaOpcao = 2;
            if (rodada > 3) {
                System.out.println("3 - Desistir");
                ultimaOpcao = 3;
            }

            int opcao = Entrada.lerInteiro("Escolha sua ação: ", 1, ultimaOpcao);

            if (opcao == 1) {
                atacar(atual, oponente);
                turnoUsado = true;
            } else if (opcao == 2) {
                if (!atual.podeUsarItem()) {
                    System.out.println("Você já usou o máximo de itens nesta batalha! Escolha outra ação.");
                } else {
                    turnoUsado = escolherItem(atual);
                }
            } else if (opcao == 3) {
                System.out.println(atual.getNome() + " desistiu da batalha!");
                anunciarVencedor(oponente);
                return true;
            }
        }

        // Depois da ação, verifica se o oponente foi derrotado
        if (oponente.getPokesal().estaDerrotado()) {
            System.out.println(oponente.getPokesal().getNome() + " foi derrotado!");
            anunciarVencedor(atual);
            return true;
        }

        return false;
    }

    // ---------- Ataque ----------
    private void atacar(Treinador atacante, Treinador defensor) {
        Pokesal pokesalAtacante = atacante.getPokesal();
        Pokesal pokesalDefensor = defensor.getPokesal();

        // 10% de chance de acerto crítico (sorteia de 0 a 99; menor que 10 = crítico)
        boolean acertoCritico = random.nextInt(100) < 10;

        double danoCalculado = pokesalAtacante.calcularDano(pokesalDefensor, terreno, acertoCritico);

        // O dano é arredondado para o inteiro mais próximo antes de tirar o HP
        int danoFinal = (int) Math.round(danoCalculado);
        pokesalDefensor.receberDano(danoFinal);

        System.out.println(pokesalAtacante.getNome() + " atacou " + pokesalDefensor.getNome() + "!");

        if (acertoCritico) {
            System.out.println("Acerto crítico!");
        }

        double multiplicador = pokesalAtacante.multiplicadorDeTipo(pokesalDefensor);
        if (multiplicador == 2.0) {
            System.out.println("É super efetivo!");
        } else if (multiplicador == 0.5) {
            System.out.println("Não é muito efetivo...");
        }

        System.out.println("Dano calculado: " + danoCalculado + " -> dano causado: " + danoFinal);
        System.out.println(pokesalDefensor.getNome() + " ficou com " + pokesalDefensor.getHp() + " HP.");

        if (pokesalDefensor.estaCritico() && !pokesalDefensor.estaDerrotado()) {
            System.out.println(pokesalDefensor.getNome() + " entrou em estado crítico!");
        }

        tentarAplicarStatus(pokesalAtacante, pokesalDefensor);
    }

    // 10% de chance de o golpe causar um status no defensor
    private void tentarAplicarStatus(Pokesal atacante, Pokesal defensor) {
        if (defensor.estaDerrotado()) {
            return;
        }

        int sorteio = random.nextInt(100);
        if (sorteio < 10) {
            String novoStatus = atacante.statusQueProvoca();
            boolean aplicou = defensor.aplicarStatus(novoStatus);
            if (aplicou) {
                System.out.println(defensor.getNome() + " ficou " + novoStatus + "! (o efeito começa no fim do round)");
            }
        }
    }

    // ---------- Itens ----------
    // Devolve true se um item foi usado (o turno é consumido) e false se o treinador voltou.
    private boolean escolherItem(Treinador treinador) {
        System.out.println("\nItens disponíveis:");
        System.out.println("1 - Potion (+20 HP)");
        System.out.println("2 - Super Potion (+40 HP)");
        System.out.println("3 - Antidote (remove o veneno)");
        System.out.println("0 - Voltar");

        int opcao = Entrada.lerInteiro("Escolha o item: ", 0, 3);

        if (opcao == 0) {
            return false;
        }

        String mensagem = "";
        if (opcao == 1) {
            mensagem = treinador.usarPotion();
        } else if (opcao == 2) {
            mensagem = treinador.usarSuperPotion();
        } else if (opcao == 3) {
            mensagem = treinador.usarAntidote();
        }

        System.out.println(mensagem);
        return true;
    }

    // ---------- Fim do round ----------
    // Devolve true se a batalha terminou no fim do round.
    private boolean finalizarRound() {
        System.out.println("\n--- Fim do round " + rodada + " ---");

        Pokesal p1 = treinador1.getPokesal();
        Pokesal p2 = treinador2.getPokesal();

        // Efeitos de status
        mostrarMensagem(p1.processarStatusNoFimDoTurno());
        mostrarMensagem(p2.processarStatusNoFimDoTurno());

        // Recuperação do Canteiro Central
        curarNoCanteiro(p1);
        curarNoCanteiro(p2);

        // Verifica se alguém foi derrotado pelos efeitos
        if (p1.estaDerrotado() && p2.estaDerrotado()) {
            System.out.println("Os dois Pokésais foram derrotados ao mesmo tempo! A batalha terminou em empate.");
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

    // No Canteiro Central, Pokésais de Planta recuperam 5% do HP máximo (5 HP)
    private void curarNoCanteiro(Pokesal pokesal) {
        if (terreno.equals("Canteiro Central") && pokesal.getTipo().equals("Planta") && !pokesal.estaDerrotado()) {
            int cura = pokesal.getHpMaximo() * 5 / 100;
            int hpAntes = pokesal.getHp();
            pokesal.curar(cura);
            int recuperado = pokesal.getHp() - hpAntes;

            if (recuperado > 0) {
                System.out.println(pokesal.getNome() + " recuperou " + recuperado + " HP no Canteiro Central.");
            }
        }
    }

    // ---------- Métodos de exibição ----------
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
        System.out.println("\n*** VENCEDOR: " + vencedor.getNome() + " com " + vencedor.getPokesal().getNome() + "! ***");
    }
}
