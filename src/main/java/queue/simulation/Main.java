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
            String exit = save(scheduler, interaction);
            System.out.println("Exit on file: " + exit);
        });

    }

    private static void help() {
        System.out.println("Run With: ");
        System.out.println("java -jar queueSim.jar model");
        System.out.println("or");
        System.out.println("java -jar queueSim.jar --file=<file.json>");
    }

    private static String save(Scheduler scheduler, int i) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("execution_" + i + "_print.txt"));
            writer.write(scheduler.toString());
            writer.flush();
            writer.close();
            return "execution_" + i + "_print.txt";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}