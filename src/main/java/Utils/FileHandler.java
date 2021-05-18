package Utils;

import Data.Storage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
        return storage;
    }
}
