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
    private HashMap<String, Cube> cubes;
    private HashMap<CubeType, ArrayList<String>> cubeTypeMap;
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
        String[] pathSplit = path.split("\\.");
        StringBuilder sb = new StringBuilder();
        int length = pathSplit.length;
        for (int i = 0; i < length; i++) {
            if (i != 0) sb.append('.');
            sb.append(pathSplit[i]);
            if (i == length - 2) sb.append("_Loesung_");
        }
        solutionPath = sb.toString();
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

    public Vektor getDimVector() {
        return new Vektor(dimX, dimY, dimZ);
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
            System.out.println(cube);
            cubeTypeMap.get(cube.getType()).add(cube.getName());
        }
        System.out.println(cubeTypeMap);
    }

    public HashMap<CubeType, ArrayList<String>> getOrderedCubes() {
        return cubeTypeMap;
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

    public void setSolution(String[][][] solution) {
        this.solution = solution;
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
