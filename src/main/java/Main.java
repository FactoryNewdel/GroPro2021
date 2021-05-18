import Data.*;
import Utils.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
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
        
        //FileHandler.transformIntoFiles(storages);
    }
}
