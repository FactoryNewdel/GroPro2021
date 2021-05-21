package Utils;

import Data.Cube;
import Data.CubeType;
import Data.Storage;
import Data.Vektor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class FileHandler {
    /**
     * Liest Dateien aus
     * @param files Wandelt Liste an Files um
     * @return Storage-Liste
     */
    public static List<Storage> transformFiles(List<File> files) {
        List<Storage> list = new ArrayList<>();
        for (File file : files) {
            System.out.println("Transforming..." + file.getAbsolutePath());
            Storage storage = transformFile(file);
            System.out.println("Adding..." + (storage != null));
            if (storage != null) list.add(storage);
        }
        return list;
    }

    /**
     * Liest Datei aus
     * @param file Wandelt Datei um
     * @return Storage-Liste
     */
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
                if (!storage.setDimensionString(line)) break;
                continue;
            }
            String[] split = line.split("(:)\\s+");
            if (split.length != 2) break;
            String[] numberStr = split[1].split("\\s+");
            if (numberStr.length != 6) break;
            int[] triangles = new int[6];
            for (int i = 0; i < 6; i++) {
                int tri = Integer.parseInt(numberStr[i]);
                if (tri < 0 || tri > 4) break;
                triangles[i] = tri;
            }
            storage.addCube(new Cube(split[0], triangles));
        }
        if (!storage.isFinished()) return null;
        Vektor v = storage.getDimVector();
        if (v.x == 1 && v.y == 1 && v.z == 1) return null;
        if (!storage.orderGroups()) return null;
        int edges = storage.getEdgeCount();
        if (storage.getOrderedCubes().get(CubeType.ECKE).size() != edges) return null;
        return storage;
    }

    /**
     * Erstell Ausgabedatein
     * @param storages Liste von Storage-Objekten
     */
    public static void transformIntoFiles(List<Storage> storages) {
        for (Storage storage : storages) {
            try {
                System.out.println("Transforming back..." + storage.getSolutionPath());
                if (storage.getSolution() == null) {
                    transformIntoFileNoSolution(storage);
                    continue;
                }
                transformIntoFile(storage);
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
        }
    }

    /**
     * Erstellt Ausgabedatei
     * @param storage Storage-Objekt zum umwandeln
     * @throws IOException Falls die Ausgabedatei nicht erstellt werden konnte
     */
    private static void transformIntoFile(Storage storage) throws IOException {
        FileWriter fileWriter = new FileWriter(storage.getSolutionPath());
        System.out.println(storage.getSolutionPath());
        for (String comment : storage.getComments()) {
            fileWriter.write(comment + "\n");
        }
        fileWriter.write(storage.getDimensionString() + "\n");
        String[][][] solution = storage.getSolution();
        for (int x = 0; x < solution.length; x++) {
            for (int y = 0; y < solution[0].length; y++) {
                for (int z = 0; z < solution[0][0].length; z++) {
                    Cube cube = storage.getCube(solution[x][y][z]);
                    StringBuilder sb = new StringBuilder(cube.getName()).append(": ");
                    for (int i : cube.getTriangles()) {
                        sb.append(i).append(' ');
                    }
                    sb.deleteCharAt(sb.length() - 1);
                    fileWriter.write("[" + (x + 1) + "," + (y + 1) + "," + (z + 1) + "] " + sb + "\n");
                }
            }
        }
        fileWriter.close();
    }
    /**
     * Erstellt Ausgabedatei mit der Anmerkung, dass keine Lösung existiert
     * @param storage Storage-Objekt zum umwandeln
     * @throws IOException Falls die Ausgabedatei nicht erstellt werden konnte
     */
    public static void transformIntoFileNoSolution(Storage storage) throws IOException {
        FileWriter fileWriter = new FileWriter(storage.getSolutionPath());
        System.out.println(storage.getSolutionPath());
        for (String comment : storage.getComments()) {
            fileWriter.write(comment + "\n");
        }
        fileWriter.write(storage.getDimensionString() + "\n");
        fileWriter.write("//***** Keine Lösung *****");
        for (Cube cube : storage.getCubes().values()) {
            fileWriter.write(cube + "\n");
        }
        fileWriter.close();
    }
}
