import java.util.Random;

class Veiculo {

    protected String nome;
    protected char simbolo;
    protected int distancia = 0;
    protected Random rnd = new Random();

    /**
     * Metodo utilizada para receber nome e simbolo para diferenciar os veiculo
     * @param nome 
     * @param simbolo
     */
    public Veiculo(String nome, char simbolo) {
        this.nome = nome;
        this.simbolo = simbolo;
    }
    public void avancar() {
        int velocidade = rnd.nextInt(3) + 1; // 1,2,3
        distancia += velocidade;
    }

    public int getDistancia() { return distancia; }
    public String getNome() { return nome; }
    public char getSimbolo() { return simbolo; }
    public void resetar() { distancia = 0; }
}

class Carro extends Veiculo {
    public Carro(String nome) { super(nome, 'C'); }
}

class Moto extends Veiculo {
    public Moto(String nome) { super(nome, 'M'); }
}

public class Corrida {
    /**
     * Metodo responsavel por realizar a corrida verificando as exceções 
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        int maxDistance = 30;
        Carro carro = new Carro("Carro");
        Moto moto = new Moto("Moto");

        while (carro.getDistancia() < maxDistance && moto.getDistancia() < maxDistance) {
            carro.avancar();
            moto.avancar();
            mostrarPista(carro, moto, maxDistance);
            Thread.sleep(3000);
        }

        verificarVencedor(carro, moto, maxDistance);
    }

    /**
     * Metodo utilizado para mostrar a pista e avanço dos veiculos durante a corrida 
     * @param v1 Recebe a posição do Primeiro Veiculo
     * @param v2 Recebe a posição do Segundo Veiculo
     * @param maxDistance Limite da pista de prova
     */
    private static void mostrarPista(Veiculo v1, Veiculo v2, int maxDistance) {
        System.out.println(" ".repeat(Math.min(v1.getDistancia(), maxDistance)) + v1.getSimbolo());
        System.out.println(" ".repeat(Math.min(v2.getDistancia(), maxDistance)) + v2.getSimbolo());
        System.out.println("-".repeat(maxDistance) + "| <- Meta");
        System.out.println();
    }

    /**
     * Metodo utlizado para identificar e mostrar o vencedor
     * @param v1 Recebe a distancia percorrida pelo primeiro veiculo
     * @param v2 Recebe a distancia percorrida pelo segundo veiculo
     * @param maxDistance Recebe a distancia da pista 
     */
    private static void verificarVencedor(Veiculo v1, Veiculo v2, int maxDistance) {
        System.out.println("Distâncias finais: " + v1.getNome() + "=" + v1.getDistancia() +
                ", " + v2.getNome() + "=" + v2.getDistancia());

        if (v1.getDistancia() >= maxDistance && v2.getDistancia() < maxDistance)
            System.out.println(v1.getNome() + " venceu!");
        else if (v2.getDistancia() >= maxDistance && v1.getDistancia() < maxDistance)
            System.out.println(v2.getNome() + " venceu!");
        else if (v1.getDistancia() > v2.getDistancia())
            System.out.println(v1.getNome() + " venceu!");
        else if (v2.getDistancia() > v1.getDistancia())
            System.out.println(v2.getNome() + " venceu!");
        else
            System.out.println("Empate!");
    }
}
