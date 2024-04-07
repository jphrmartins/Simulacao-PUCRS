import java.util.Random;

public class QueueBuilder {

    private final Random random;
    private TimeRange tempoEntrada;
    private TimeRange tempoSaida;
    private int servidores;
    private int capacidadeMaxima;

    public QueueBuilder(Random random) {
        this.random = random;
    }

    public QueueBuilder withIntervaloEntrada(int min, int max) {
        tempoEntrada = new TimeRange(min, max, random);
        return this;
    }

    public QueueBuilder withIntervaloSaida(int min, int max) {
        tempoSaida = new TimeRange(min, max, random);
        return this;
    }

    public QueueBuilder withServidoresDisponiveis(int servidores) {
        this.servidores = servidores;
        return this;
    }

    public QueueBuilder withCapcidadeMaxima(int capacidadeMaxima) {
        this.capacidadeMaxima = capacidadeMaxima;
        return this;
    }

    public Queue build() {
        return new Queue(new TimeHandler(this.tempoEntrada, this.tempoSaida), this.servidores,
                this.capacidadeMaxima);
    }
}
