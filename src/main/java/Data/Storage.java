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
    private Vektor dimVector = null;
    private HashMap<String, Cube> cubes;
    private HashMap<CubeType, ArrayList<String>> cubeTypeMap;
    private String[][][] solution;

    /**
     * Berechnet Output-Pfad und initialisiert Objekt
     * @param path Input-Pfad
     */
    public Storage(String path) {
        this.path = path;
        comments = new ArrayList<>();
        cubes = new HashMap<>();
        cubeTypeMap = new HashMap<>();
        cubeTypeMap.put(ECKE, new ArrayList<>());
        cubeTypeMap.put(KANTE, new ArrayList<>());
        cubeTypeMap.put(SEITE, new ArrayList<>());
        cubeTypeMap.put(MITTE, new ArrayList<>());
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

    public String getPath() {
        return path;
    }

    public String getSolutionPath() {
        return solutionPath;
    }

    /**
     * Fügt Kommentar hinzu, der aus der Eingabedatei gelesen wurde
     * @param comment Kommentar zum Hinzufügen
     */
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

    /**
     * @return Vektordarstellung der Dimensionen
     */
    public Vektor getDimVector() {
        if (dimVector != null) return dimVector;
        dimVector = new Vektor(dimX, dimY, dimZ);
        return dimVector;
    }

    /**
     * Fügt Würfelobjekt zu Map hinzu. Name dient als Schlüsselattribut
     * @param cube Würfelobjekt
     */
    public void addCube(Cube cube) {
        cubes.put(cube.getName(), cube);
    }

    /**
     * @param name Bezeichnung des Würfels
     * @return Würfel aus Map
     */
    public Cube getCube(String name) {
        if (!cubes.containsKey(name)) return null;
        return cubes.get(name);
    }

    public HashMap<String, Cube> getCubes() {
        return cubes;
    }
    /**
     * Sortiert Würfel nach Typen in neue Map
     */
    public boolean orderGroups() {
        for (Cube cube : cubes.values()) {
            try {
                CubeType type = cube.getType(getDimVector());
                if (type == null) return false;
                cubeTypeMap.get(type).add(cube.getName());
            } catch (NullPointerException e) {
                continue;
            }
        }
        return true;
    }

    public HashMap<CubeType, ArrayList<String>> getOrderedCubes() {
        return cubeTypeMap;
    }

    /**
     * Legt Dimensionen fest
     * @param s Zeile aus Eingabedatei, in der die Dimension steht
     * @return ob dir Dimension gültig ist
     */
    public boolean setDimensionString(String s) {
        String[] split = s.split("\\s+")[1].split(",");
        if (split.length != 3) return false;
        dimX = Integer.parseInt(split[0]);
        dimY = Integer.parseInt(split[1]);
        dimZ = Integer.parseInt(split[2]);
        if (dimX == 1 && dimY == 1 && dimZ == 1) return false;
        dimensionString = s;
        return true;
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

    /**
     * Testes Objekt auf Gültigkeit
     * Gültig bedeutet, dass die Dimension angegeben wurde und
     * es der Dimension entsprechend genug Würfel gibt
     * @return ob Storage-Objekt gültig ist
     */
    public boolean isFinished() {
        return dimensionString != null && cubes.size() == dimX * dimY * dimZ;
    }

    /**
     * @return Wie viele Ecken ein Quader hat
     */
    public int getEdgeCount() {
        int count = 1;
        if (dimX > 1) count *= 2;
        if (dimY > 1) count *= 2;
        if (dimZ > 1) count *= 2;
        return count;
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
