/**
 * SimuladorCorrida.java
 *
 * Simula uma corrida entre dois veículos. A cada passo é sorteada uma velocidade
 * (1..3) para cada veículo e essa velocidade é somada à distância do veículo.
 * A posição na tela é representada por espaços em branco antes do caractere que
 * representa o veículo.
 *
 * Como usar:
 *   - Compilar: javac SimuladorCorrida.java
 *   - Executar: java SimuladorCorrida [meta] [delayMs]
 *     exemplo: java SimuladorCorrida 30 300
 *
 * Arquivo contém 3 componentes principais (todas em um único arquivo para facilitar):
 *  - classe Veiculo: representa cada veículo (nome, símbolo, velocidade, distância)
 *  - classe Corrida: lógica da corrida (passos, renderização, verificação de fim)
 *  - classe pública SimuladorCorrida: método main() para executar
 */

import java.util.Random;

public class SimuladorCorrida {

    public static void main(String[] args) throws InterruptedException {
        int maxDistance = 30;      // meta padrão
        int delayMs = 1000;         // atraso entre passos (ms) para visualização

        if (args.length >= 1) {
            try { maxDistance = Integer.parseInt(args[0]); } catch (NumberFormatException e) { }
        }
        if (args.length >= 2) {
            try { delayMs = Integer.parseInt(args[1]); } catch (NumberFormatException e) { }
        }

        Veiculo v1 = new Veiculo("Carro A", 'A');
        Veiculo v2 = new Veiculo("Carro B", 'B');

        Corrida corrida = new Corrida(maxDistance, v1, v2, delayMs);
        corrida.start();
    }

    // --- Classe Veiculo ---
    static class Veiculo {
        private final String nome;
        private final char simbolo;
        private int velocidade; // velocidade do passo atual
        private int distancia;  // distância acumulada

        public Veiculo(String nome, char simbolo) {
            this.nome = nome;
            this.simbolo = simbolo;
            this.velocidade = 0;
            this.distancia = 0;
        }

        // sorteia velocidade entre 1 e 3
        public void sortearVelocidade(Random rnd) {
            this.velocidade = rnd.nextInt(3) + 1; // 1..3
        }

        public void mover() { this.distancia += this.velocidade; }

        public int getVelocidade() { return velocidade; }
        public int getDistancia() { return distancia; }
        public String getNome() { return nome; }
        public char getSimbolo() { return simbolo; }
    }

    // --- Classe Corrida (responsável pela lógica e renderização) ---
    static class Corrida {
        private final int maxDistance;
        private final Veiculo v1, v2;
        private final Random rnd = new Random();
        private final int delayMs;
        private int passo = 0;

        public Corrida(int maxDistance, Veiculo v1, Veiculo v2, int delayMs) {
            this.maxDistance = maxDistance;
            this.v1 = v1;
            this.v2 = v2;
            this.delayMs = Math.max(0, delayMs);
        }

        public void start() throws InterruptedException {
            System.out.println("=== SIMULADOR DE CORRIDA ===");
            System.out.println("Meta: " + maxDistance + " unidades");

            while (!acabou()) {
                passo++;
                executarPasso();
                mostrarPista();
                Thread.sleep(delayMs);
            }

            mostrarResultado();
        }

        private void executarPasso() {
            v1.sortearVelocidade(rnd);
            v1.mover();
            v2.sortearVelocidade(rnd);
            v2.mover();
        }

        private boolean acabou() {
            return v1.getDistancia() >= maxDistance || v2.getDistancia() >= maxDistance;
        }

        private void mostrarPista() {
            System.out.println();
            System.out.printf("Passo %d: %s vel=%d dist=%d | %s vel=%d dist=%d%n",
                    passo, v1.getNome(), v1.getVelocidade(), v1.getDistancia(),
                    v2.getNome(), v2.getVelocidade(), v2.getDistancia());

            System.out.println(renderLinha(v1));
            System.out.println(renderLinha(v2));
            System.out.println(renderFinishLine());
        }

        // renderiza uma linha mostrando espaços antes do símbolo do veículo
        private String renderLinha(Veiculo v) {
            int pos = Math.min(v.getDistancia(), maxDistance);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < pos; i++) sb.append(' ');
            sb.append(v.getSimbolo());
            if (v.getDistancia() > maxDistance) {
                sb.append(" (+" + (v.getDistancia() - maxDistance) + ")");
            }
            return sb.toString();
        }

        // linha que representa a meta ao final da pista
        private String renderFinishLine() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < maxDistance; i++) sb.append('-');
            sb.append("| <- Meta");
            return sb.toString();
        }

        private void mostrarResultado() {
            System.out.println("\n=== RESULTADO ===");
            System.out.printf("%s: %d%n", v1.getNome(), v1.getDistancia());
            System.out.printf("%s: %d%n", v2.getNome(), v2.getDistancia());

            boolean a = v1.getDistancia() >= maxDistance;
            boolean b = v2.getDistancia() >= maxDistance;

            if (a && !b) {
                System.out.println("Vencedor: " + v1.getNome());
            } else if (!a && b) {
                System.out.println("Vencedor: " + v2.getNome());
            } else if (a && b) {
                if (v1.getDistancia() > v2.getDistancia())
                    System.out.println("Vencedor: " + v1.getNome() + " (ambos passaram da meta)");
                else if (v2.getDistancia() > v1.getDistancia())
                    System.out.println("Vencedor: " + v2.getNome() + " (ambos passaram da meta)");
                else
                    System.out.println("Empate!");
            } else {
                // fallback (não deve acontecer porque o loop para quando alguém alcança a meta)
                if (v1.getDistancia() > v2.getDistancia()) System.out.println("Vencedor: " + v1.getNome());
                else if (v2.getDistancia() > v1.getDistancia()) System.out.println("Vencedor: " + v2.getNome());
                else System.out.println("Empate!");
            }
        }
    }
}
