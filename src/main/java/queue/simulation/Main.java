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
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            help();
        } else {
            ModelInfo model = null;
            if (args[0].equals("model")) {
                model = getModel("model.json");
            } else if (args[0].startsWith("--file=")) {
                String file = args[0].split("--file=")[1];
                model = getModel(file);
            }
            run(model);
        }
    }


    private static ModelInfo getModel(String fileName) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(fileName)));
            ObjectMapper mapper = new ObjectMapper();
            ModelInfo modelInfo = mapper.readValue(content, ModelInfo.class);
            modelInfo.setFileName(fileName);
            return modelInfo;
        } catch (IOException ex) {
            throw new RuntimeException("Could not found file: " + fileName, ex);
        }
    }

    private static void run(ModelInfo modelInfo) {
        Model model = new Model(modelInfo);
        IntStream.range(0, model.getSchedulers().size()).forEach(interaction -> {
            Scheduler scheduler = model.getSchedulers().get(interaction);
            scheduler.execute(2.0);
            String exit = save(scheduler, interaction, modelInfo.getFileName());
            System.out.println("Exit on file: " + exit);
        });

    }

    private static void help() {
        System.out.println("Run With: ");
        System.out.println("java -jar queueSim.jar model");
        System.out.println("or");
        System.out.println("java -jar queueSim.jar --file=<file.json>");
    }

    private static String save(Scheduler scheduler, int i, String fileName) {
        try {
            String resultName = "execution_" + fileName + "_" + i + "_print.txt";
            BufferedWriter writer = new BufferedWriter(new FileWriter(resultName, StandardCharsets.UTF_8));
            writer.write(scheduler.toString());
            writer.flush();
            writer.close();
            return resultName;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}