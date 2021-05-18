package Utils;

import Data.Cube;
import Data.Storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class FileHandler {
    public static List<Storage> transformFiles(List<File> files) {
        List<Storage> list = new ArrayList<>();
        for (File file : files) {
            list.add(transformFile(file));
        }
        return list;
    }

    public static Storage transformFile(File file) {
        Storage storage = new Storage(file.getAbsolutePath());
        Scanner scanner;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            if (line.startsWith("//")) {
                storage.addComment(line);
                continue;
            }
            if (line.startsWith("Dimension")) {
                storage.setDimensionString(line);
                continue;
            }
            String[] split = line.split("(:)\\s+");
            String[] numberStr = split[1].split("\\s+");
            int[] triangles = new int[6];
            for (int i = 0; i < 6; i++) {
                triangles[i] = Integer.parseInt(numberStr[i]);
            }
            storage.addCube(new Cube(split[0], triangles));
        }
        /*try {
            storage.orderGroups();
        } catch (RuntimeException ignored) {}*/
        return storage;
    }

    public static void transformIntoFiles(List<Storage> storages) {
        for (Storage storage : storages) {
            try {
                transformIntoFile(storage);
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
        }
    }

    public static void transformIntoFile(Storage storage) throws IOException {
        FileWriter fileWriter = new FileWriter(storage.getSolutionPath());
        System.out.println(storage.getSolutionPath());
        for (String comment : storage.getComments()) {
            fileWriter.write(comment + "\n");
        }
        fileWriter.write(storage.getDimensionString() + "\n");
        String[][][] solution = storage.getSolution();
        for (int i = 0; i < solution.length; i++) {
            for (int j = 0; j < solution[0].length; j++) {
                for (int k = 0; k < solution[0][0].length; k++) {
                    fileWriter.write("[" + i + "," + j + "," + k + "] " + storage.getCube(solution[i][j][k]) + "\n");
                }
            }
        }
        fileWriter.close();
    }

}
