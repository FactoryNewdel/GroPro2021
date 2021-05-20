import Data.Cube;
import Data.Storage;
import Utils.FileHandler;
import Utils.Puzzle;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main {
    /**
     * Start- und Hauptmethode für das Programm
     * @param args Dateipfade, durch ',' getrennt
     */
    public static void main(String[] args) {
        List<File> files = new ArrayList<>();
        for (String path : args) {
            if (path.contains("_Loesung_")) continue;
            if (path.contains("Musterloesung")) continue;
            if (path.contains("Lösung")) continue;
            File file = new File(path);
            if (!file.exists()) continue;
            if (file.isDirectory()) {
                for (File dirFile : file.listFiles()) {
                    if (dirFile.getAbsolutePath().contains("_Loesung_")) continue;
                    if (dirFile.getAbsolutePath().contains("Musterloesung")) continue;
                    if (dirFile.getAbsolutePath().contains("Lösung")) continue;
                    if (!dirFile.exists() || dirFile.isDirectory()) continue;
                    files.add(dirFile);
                }
                continue;
            }
            files.add(file);
        }
        if (files.isEmpty()) {
            System.out.println("No files specified! Shutting down...");
            System.exit(-1);
        }
        List<Storage> storages = FileHandler.transformFiles(files);
        System.out.println("STORAGES = " + storages);
        for (Storage storage : storages) { ;
            System.out.println("Solving " + storage.getPath());
            long start = System.currentTimeMillis();
            new Puzzle(storage).solve();
            System.out.println("Time elapsed: " + (System.currentTimeMillis() - start));
        }
        FileHandler.transformIntoFiles(storages);
    }
}