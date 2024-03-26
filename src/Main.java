import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {
        boolean paralelo = false;
        int laco = 100000;
        long[] seeds = {8106721679461579810L};

        IntStream executions = IntStream.range(0, seeds.length);

        if (paralelo) {
            executions = executions.parallel();
        }
        executions.forEach(i -> execute(i, seeds, laco));
    }

    private static void execute(int i, long[] seeds, int laco) {
        System.out.println("Starting with index: " + i + " and seed: " + seeds[i]);
        Random random = new Random(seeds[i]);
        Fila[] filas = {
                new FilaBuilder(random) //g/g/1/5 2..5 3..5
                        .withIntervaloEntrada(2, 5)
                        .withIntervaloSaida(3,5)
                        .withPrimeiraChegadaEm(2)
                        .withServidoresDisponiveis(1)
                        .withCapcidadeMaxima(5)
                        .contaPerda(true)
                        .build(),
                new FilaBuilder(random) //g/g/2/5 2..5 3..5
                        .withIntervaloEntrada(2, 5)
                        .withIntervaloSaida(3,5)
                        .withPrimeiraChegadaEm(2)
                        .withServidoresDisponiveis(2)
                        .withCapcidadeMaxima(5)
                        .contaPerda(true)
                        .build()
        };
        for (Fila fila : filas) {
            System.out.println("Executing fila: " + fila.getId() + " execution: " + (i + 1));
            fila.executa(laco);
            save(fila, i + 1);
        }
    }

    private static void save(Fila fila, int i) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("fila_" + fila.getId() + "_exec_" + i + ".txt"));
            writer.write(fila.toString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}