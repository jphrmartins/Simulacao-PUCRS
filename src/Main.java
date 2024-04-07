import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {
        boolean paralelo = false;
        int limit = 100000;
        long[] seeds = {8106721679461579810L};

        IntStream executions = IntStream.range(0, seeds.length);

        if (paralelo) {
            executions = executions.parallel();
        }
        executions.forEach(i -> execute(i, seeds, limit));
    }

    private static void execute(int i, long[] seeds, int limit) {
        System.out.println("Starting with index: " + i + " and seed: " + seeds[i]);
        Random random = new RandomWIthLimit(seeds[i], limit);
        Scheduler scheduler = new Scheduler()
                .withQueue(new QueueBuilder(random) //g/g/1/5 2..5 3..5
                        .withIntervaloEntrada(2, 5)
                        .withIntervaloSaida(3,5)
                        .withServidoresDisponiveis(1)
                        .withCapcidadeMaxima(5)
                        .build());
//                .withQueue(new QueueBuilder(random) //g/g/2/5 2..5 3..5
//                        .withIntervaloEntrada(2, 5)
//                        .withIntervaloSaida(3,5)
//                        .withServidoresDisponiveis(2)
//                        .withCapcidadeMaxima(5)
//                        .build());
        scheduler.execute(2.0);
        save(scheduler, i);
    }

    private static void save(Scheduler scheduler, int i) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("execution_" + i + "_print.txt"));
            writer.write(scheduler.toString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}