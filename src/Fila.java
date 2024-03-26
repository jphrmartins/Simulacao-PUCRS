import java.util.*;

public class Fila {
    private static int ID = 0;
    private final int id;
    private final IntervaloTempo tempoEntrada;
    private final IntervaloTempo tempoSaida;
    private final double tempoPrimeiraChegada;
    private final int servidores;
    private final int capacidadeMaxima;
    private List<EventoProcessado> eventos;
    private List<Evento> eventosAgendados;
    private double[] temposOcupados;
    private int servidoresOcupados;
    private int capacidadeAtual;
    private double tempoAtual;
    private boolean countPerda;
    private long perda;

    public Fila(IntervaloTempo tempoEntrada, IntervaloTempo tempoSaida, double tempoPrimeiraChegada,
                int servidores, int capacidadeMaxima, boolean countPerda) {
        this(tempoEntrada, tempoSaida, tempoPrimeiraChegada, servidores, capacidadeMaxima);
        this.countPerda = countPerda;
    }

    public Fila(IntervaloTempo tempoEntrada, IntervaloTempo tempoSaida, double tempoPrimeiraChegada,
                int servidores, int capacidadeMaxima) {
        this.id = ID++;
        this.tempoEntrada = tempoEntrada;
        this.tempoSaida = tempoSaida;
        this.tempoPrimeiraChegada = tempoPrimeiraChegada;
        this.eventos = new ArrayList<>();
        this.eventosAgendados = new ArrayList<>();
        this.capacidadeMaxima = capacidadeMaxima;
        this.servidores = servidores;
        countPerda = false;
        temposOcupados = new double[capacidadeMaxima + 1];
    }

    public int getId() {
        return id;
    }

    public void executa(int lacos) {
        eventosAgendados.add(Evento.newChegada(tempoPrimeiraChegada));
        for (int i = 0; i < lacos; i++) {
            if (i % 1000 == 0) System.out.print(id + " : " + i + ", ");
            if (i % 20000 == 0) System.out.println();
            Evento evento = proxEvento();
            temposOcupados[capacidadeAtual] += (evento.getTempo() - tempoAtual);
            tempoAtual = evento.getTempo();
            evento.executado();
            eventos.add(new EventoProcessado(evento, Arrays.copyOf(temposOcupados, temposOcupados.length)));
            if (evento.isChegada()) {
                if (capacidadeAtual < capacidadeMaxima) {
                    capacidadeAtual++;
                    if (servidoresOcupados < servidores) {
                        servidoresOcupados++;
                        agendaSaida();
                    }
                } else if (countPerda){
                    perda++;
                }
                agendaChegada();
            } else if (evento.isSaida()) {
                capacidadeAtual--;
                servidoresOcupados--;
                if (capacidadeAtual > 0) {
                    int servidoresDisponiveis = servidores - servidoresOcupados;
                    int aguardandoNaFila = capacidadeAtual - servidoresOcupados;
                    for (int j = 0; j < aguardandoNaFila && j < servidoresDisponiveis; j++) {
                        servidoresOcupados++;
                        agendaSaida();
                    }
                }
            } else {
                throw new RuntimeException("Evento: " + evento.getTipo() + " Não conhecido");
            }
        }
        System.out.println();
    }

    private void agendaChegada() {
        double tempo = tempoEntrada.proximoTempo();
        tempo += tempoAtual;
        Evento chegada = Evento.newChegada(tempo);
        eventosAgendados.add(chegada);
    }

    private void agendaSaida() {
        double tempo = tempoSaida.proximoTempo();
        tempo += tempoAtual;
        Evento saida = Evento.newSaida(tempo);
        eventosAgendados.add(saida);
    }

    private Evento proxEvento() {
        Evento proxEvento = eventosAgendados.stream()
                .sorted(Comparator.comparingDouble(Evento::getTempo))
                .filter(it -> !it.isExecuted())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Nenhum proximo evento a ser processado."));
        eventosAgendados.remove(proxEvento);
        return proxEvento;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("Fila: " + id + "\n");
        builder.append("Tempo total: ").append(tempoAtual).append("\n");

        builder.append("Tempos por posicao: \n\n");
        for (int i = 0; i < temposOcupados.length; i++)
            builder.append("\t Ocupacao: ").append(i).append(" -> Tempo: ").append(temposOcupados[i]).append("\n");

        builder.append("\n===================\n");
        builder.append("Distribuição probabilidades por posição: \n" );

        for (int i = 0; i < temposOcupados.length; i++) {
            double probabilidadeAtual = (temposOcupados[i] / tempoAtual) * 100;
            builder.append("\t Ocupacao: ").append(i).append(" -> ")
                    .append(probabilidadeAtual).append(" %")
                    .append("\n");
        }

        builder.append("\n===================\n");
        builder.append("Quantidade de Perda:\n");
        builder.append(perda);

        builder.append("\n===================\n");
        builder.append("Eventos Processados:\n");
        for (int i = 0; i < eventos.size(); i++)
            writeEvent(i, eventos.get(i), builder);

        builder.append("\n===================\n");
        builder.append("Eventos Agendados:\n");
        for (int i = 0; i < eventosAgendados.size(); i++)
            writeEvent(i, eventosAgendados.get(i), builder);

        return builder.toString();
    }

    private void writeEvent(int i, Evento evento, StringBuilder builder) {
        builder.append("\t").append(i + 1).append(": ").append(evento.toString()).append("\n");
    }
}
