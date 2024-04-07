public class Queue {
    private static int ID = 0;
    private final int id;
    private final TimeHandler timeHandler;
    private final int servers;
    private final int maxCapacity;
    private final double[] timeBusy;
    private int busyServers;
    private int currentCapacity;
    private long loss;

    public Queue(TimeHandler timeHandler, int servers, int maxCapacity) {
        this.id = ID++;
        this.timeHandler = timeHandler;
        this.maxCapacity = maxCapacity;
        this.servers = servers;
        timeBusy = new double[maxCapacity + 1];
    }

    public int getId() {
        return id;
    }

    public long getLoss() {
        return loss;
    }

    public double[] getTimeBusy() {
        return timeBusy;
    }

    public void executePass(Scheduler scheduler) {
        this.handleOut(scheduler);
    }

    public void execute(Scheduler scheduler, Event event, double currentTimeBeforeEvent) {
        timeBusy[currentCapacity] += (event.getTime() - currentTimeBeforeEvent);
        if (event.isIn()) {
            handleIn(scheduler, false);
        } else if (event.isOut()) {
            handleOut(scheduler);
        } else if (event.isPass() && event.getFrom() != id) {
            handleIn(scheduler, true);
        } else {
            throw new RuntimeException("Evento: " + event.getType() + " Não conhecido");
        }
    }

    private void handleIn(Scheduler scheduler, boolean isPass) {
        if (currentCapacity < maxCapacity) {
            currentCapacity++;
            if (busyServers < servers) {
                busyServers++;
                scheduler.schedule(timeHandler.nextOut(scheduler.getCurrentTime(), id));
            }
        } else {
            loss++;
        }
        if (!isPass) {
            scheduler.schedule(timeHandler.nextIn(scheduler.getCurrentTime(), this.id));
        }
    }

    private void handleOut(Scheduler scheduler) {
        currentCapacity--;
        busyServers--;
        if (currentCapacity > 0) {
            int availableServers = servers - busyServers;
            int waiting = currentCapacity - busyServers;
            for (int j = 0; j < waiting && j < availableServers; j++) {
                busyServers++;
                scheduler.schedule(timeHandler.nextOut(scheduler.getCurrentTime(), id));
            }
        }
    }

    public String print(Scheduler scheduler) {
        StringBuilder builder = new StringBuilder("Fila: " + id + "\n");
        builder.append("Notacao: ")
                .append("g/g/")
                .append(servers)
                .append("/")
                .append(maxCapacity)
                .append(" ")
                .append(timeHandler)
                .append("\n");
        builder.append("Tempos por posicao: \n");
        for (int i = 0; i < timeBusy.length; i++)
            builder.append("\t Ocupacao: ").append(i).append(" -> Tempo: ").append(timeBusy[i]).append("\n");

        builder.append("\n===================\n");
        builder.append("Distribuição probabilidades por posição: \n");

        for (int i = 0; i < timeBusy.length; i++) {
            double probabilidadeAtual = (timeBusy[i] / scheduler.getCurrentTime()) * 100;
            builder.append("\t Ocupacao: ").append(i).append(" -> ")
                    .append(probabilidadeAtual).append(" %")
                    .append("\n");
        }

        builder.append("\n===================\n");
        builder.append("Quantidade de Perda: ");
        builder.append(loss);
        builder.append("\n===================\n\n");

        return builder.toString();
    }
}
