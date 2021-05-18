import Data.*;
import Utils.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Cube cube = new Cube("Test", new int[]{3,1,3,3,1,0});
        System.out.println(cube);


        //cube.rotateZ(true);
        //System.out.println(cube);

        while (cube.hasNextRotation(true)) {
            cube.nextRotation(1, 1, 0, new Vektor(3, 3, 3));
            System.out.println(cube);
        }

        //if (true) return;
        List<File> files = new ArrayList<>();
        for (String path : args) {
            File file = new File(path);
            if (!file.exists()) continue;
            if (file.isDirectory()) {
                files.addAll(Arrays.asList(file.listFiles()));
                continue;
            }
            files.add(file);
        }
        List<Storage> storages = FileHandler.transformFiles(files);
        System.out.println(storages.get(8));
        new Puzzle(storages.get(8)).solve();
        System.out.println(storages.get(8));
        System.out.println(Arrays.deepToString(storages.get(8).getSolution()));
        try {
            FileHandler.transformIntoFile(storages.get(8));
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*for (Storage storage : storages) {
            new Puzzle(storage).solve();
        }*/
    }
}
