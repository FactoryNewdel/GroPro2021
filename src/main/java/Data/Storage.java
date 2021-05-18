package Data;

import java.util.List;

public class Storage {
    private String path;
    private String solutionPath;
    private List<String> comments;
    private int dimX;
    private int dimY;
    private int dimZ;
    private List<Cube> cubes;
    private int[][] solution;

    public Storage(String path) {
        this.path = path;
        //TODO solutionPath
    }


}
