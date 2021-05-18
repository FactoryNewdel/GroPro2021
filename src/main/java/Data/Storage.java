package Data;

import java.util.*;

import static Data.CubeType.*;

public class Storage {
    private String path;
    private String solutionPath;
    private List<String> comments;
    private String dimensionString;
    private int dimX;
    private int dimY;
    private int dimZ;
    private Map<String, Cube> cubes;
    private Map<CubeType, List<String>> cubeTypeMap;
    private String[][][] solution;

    public Storage(String path) {
        this.path = path;
        comments = new ArrayList<>();
        cubes = new HashMap<>();
        cubeTypeMap = new HashMap<>();
        cubeTypeMap.put(ECKE, new ArrayList<>());
        cubeTypeMap.put(KANTE, new ArrayList<>());
        cubeTypeMap.put(SEITE, new ArrayList<>());
        cubeTypeMap.put(MITTE, new ArrayList<>());
        //TODO solutionPath
    }

    public String getSolutionPath() {
        return solutionPath;
    }

    public void addComment(String comment) {
        comments.add(comment);
    }

    public List<String> getComments() {
        return comments;
    }

    public int getDimX() {
        return dimX;
    }

    public int getDimY() {
        return dimY;
    }

    public int getDimZ() {
        return dimZ;
    }

    public void addCube(Cube cube) {
        cubes.put(cube.getName(), cube);
    }

    public Cube getCube(String name) {
        if (!cubes.containsKey(name)) return null;
        return cubes.get(name);
    }

    public Map<String, Cube> getCubes() {
        return cubes;
    }

    public void orderGroups() {
        for (Cube cube : cubes.values()) {
            cubeTypeMap.get(cube.getType()).add(cube.getName());
        }
    }

    public void setDimensionString(String s) {
        dimensionString = s;
        String[] split = s.split("\\s+")[1].split(",");
        dimX = Integer.parseInt(split[0]);
        dimY = Integer.parseInt(split[1]);
        dimZ = Integer.parseInt(split[2]);
    }

    public String getDimensionString() {
        return dimensionString;
    }

    public String[][][] getSolution() {
        return solution;
    }

    @Override
    public String toString() {
        return "Storage{" +
                "path='" + path + '\'' +
                ", solutionPath='" + solutionPath + '\'' +
                ", comments=" + comments +
                ", dimensionString='" + dimensionString + '\'' +
                ", cubes=" + cubes +
                ", orderedCubes=" + cubeTypeMap +
                ", solution=" + Arrays.toString(solution) +
                '}';
    }
}
