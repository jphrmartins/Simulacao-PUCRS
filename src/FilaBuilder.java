import java.util.Random;

public class FilaBuilder {

    private Random random;
    private IntervaloTempo tempoEntrada;
    private IntervaloTempo tempoSaida;
    private double tempoPrimeiraChegada;
    private int servidores;
    private int capacidadeMaxima;
    private boolean contaPerda;

    public FilaBuilder(Random random) {
        this.random = random;
    }

    public FilaBuilder withIntervaloEntrada(int min, int max) {
        tempoEntrada = new IntervaloTempo(min, max, random);
        return this;
    }

    public FilaBuilder withIntervaloSaida(int min, int max) {
        tempoSaida = new IntervaloTempo(min, max, random);
        return this;
    }

    public FilaBuilder withPrimeiraChegadaEm(double tempoPrimeiraChegada) {
        this.tempoPrimeiraChegada = tempoPrimeiraChegada;
        return this;
    }

    public FilaBuilder withServidoresDisponiveis(int servidores) {
        this.servidores = servidores;
        return this;
    }

    public FilaBuilder withCapcidadeMaxima(int capacidadeMaxima) {
        this.capacidadeMaxima = capacidadeMaxima;
        return this;
    }

    public FilaBuilder contaPerda(boolean contaPerda) {
        this.contaPerda = contaPerda;
        return this;
    }

    public Fila build() {
        return new Fila(this.tempoEntrada, this.tempoSaida, this.tempoPrimeiraChegada, this.servidores,
                this.capacidadeMaxima, this.contaPerda);
    }
}
