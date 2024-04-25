package queue.simulation;

import com.fasterxml.jackson.databind.ObjectMapper;
import queue.simulation.builder.Model;
import queue.simulation.builder.QueueBuilder;
import queue.simulation.model.ModelInfo;
import queue.simulation.random.RandomGenerator;
import queue.simulation.random.RandomLinearCongruentialGenerator;
import queue.simulation.scheduler.Scheduler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {
        args = new String[]{"model"};
        if (args.length == 0) {
            help();
        } else {
            ModelInfo model = null;
            if (args[0].equals("model")) {
                model = getModel("model.json", true);
            } else if (args[0].startsWith("--file=")) {
                String file = args[0].split("--file=")[1];
                model = getModel(file, false);
            }
            run(model);
        }
    }


    private static ModelInfo getModel(String fileName, boolean model) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(fileName)));
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(content, ModelInfo.class);
        } catch (IOException ex) {
            if (model) throw new RuntimeException("Could not found defualt model");
            throw new RuntimeException("Could not found file: " + fileName, ex);
        }
    }

    private static void run(ModelInfo modelInfo) {
        Model model = new Model(modelInfo);
        IntStream.range(0, model.getSchedulers().size()).forEach(interaction -> {
            Scheduler scheduler = model.getSchedulers().get(interaction);
            scheduler.execute(2.0);
            save(scheduler, interaction);
        });

    }

    private static void help() {
        System.out.println("Run With: ");
        System.out.println("java -jar queueSim.jar model");
        System.out.println("or");
        System.out.println("java -jar queueSim.jar --file=<file.json>");
    }


    private static void execute(int i, long[] seeds, int limit) {
        System.out.println("Starting with index: " + i + " and seed: " + seeds[i]);
        RandomGenerator random = new RandomLinearCongruentialGenerator(seeds[i], limit);
        Scheduler scheduler = new Scheduler()
                .withQueue(new QueueBuilder(random) //g/g/2/3 1..4 3..4
                        .withInRange(1, 4)
                        .withExitRange(3, 4)
                        .withServidors(2)
                        .withMaxCapacity(3)
                        .build())
                .withQueue(new QueueBuilder(random) //g/g/1/5 2..3
                        .withExitRange(2, 3)
                        .withServidors(1)
                        .withMaxCapacity(5)
                        .build())
                .printEvent(false);
        scheduler.execute(1.5);
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