import java.util.*;
import java.util.stream.Stream;

public class Graph {
    private static int maxDistance, edgeNumber, verticesNumber;
    private final static Random rng = new Random();
    private final static Scanner scanner = new Scanner(System.in);
    private static Vertex[] vertices;
    private static int[][] matrix;

    public static int[][] createGraphData() {
        getGraphData();
        createGraph();
        generateMatrixFromGraph();
        System.out.print("Wyświetlić dane grafu oraz powstałej macierzy? (true / false) ");
        boolean displayGraph = scanner.nextBoolean();
        if (displayGraph) {
            printGraph();
            printMatrix();
        }
        return matrix;
    }

    static class Vertex {
        int source, destination, distance;
    }

    private static void createVertices(int verticesNumber) {
        vertices = new Vertex[verticesNumber * 2];
        for (int i = 0; i < vertices.length; i++) {
            vertices[i] = new Vertex();
        }
    }

    private static boolean isVerticalDuplicated(int source, int destination) {
        return Arrays.stream(vertices).anyMatch(v -> v.source == source &&
                v.destination == destination ||
                v.source == destination && v.destination == source);
    }

    private static void getGraphData() {
        System.out.print("\nWprowadź liczbę wierzchołków: ");
        edgeNumber = scanner.nextInt();

        int maxVertices = edgeNumber + ((edgeNumber * (edgeNumber - 3)) / 2);
        System.out.print("Wprowadź liczbę połączeń między wierzchołkami: ");
        while (scanner.hasNextInt()) {
            verticesNumber = scanner.nextInt();
            if (verticesNumber > maxVertices)
                System.out.print("Maksymalna wartość to " + maxVertices + ". Wprowadź liczbę połączeń między wierzchołkami: ");
            else if (verticesNumber < edgeNumber)
                System.out.print("Minimalna wartość to " + edgeNumber + ". Wprowadź liczbę połączeń między wierzchołkami: ");
            else {
                createVertices(verticesNumber);
                break;
            }
        }

        System.out.print("Wprowadź maksymalną możliwą wartość dla dystansu między wierzchołkami: ");
        maxDistance = scanner.nextInt();
    }

    private static void createGraph () {
        int source, destination, distance, freeIndexFromEnd, increasedIndex;
        List<Integer> requiredDestinations = new ArrayList<>(Stream.iterate(1, e -> e + 1)
                .limit(edgeNumber).toList());
        Collections.shuffle(requiredDestinations);

        for (int i = 0; i < verticesNumber; i++) {
            increasedIndex = i + 1;
            freeIndexFromEnd = vertices.length - (increasedIndex);
            do {
                if (i < edgeNumber) {
                    source = increasedIndex;
                    int duplicatedDestination = source;
                    List<Integer> tempDestinations = new ArrayList<>(requiredDestinations);
                    tempDestinations.removeIf(d -> d.equals(duplicatedDestination));
                    if (tempDestinations.size() == 0)
                        destination = rng.nextInt(edgeNumber) + 1;
                    else
                        destination = tempDestinations.get(0);
                    requiredDestinations.removeIf(d -> d.equals(tempDestinations.get(0)));
                }
                else {
                    source = rng.nextInt(edgeNumber) + 1;
                    destination = rng.nextInt(edgeNumber) + 1;
                }
            } while(source == destination || isVerticalDuplicated(source, destination));

            distance = rng.nextInt(maxDistance) + 1;

            Graph.vertices[i].source = source;
            Graph.vertices[i].destination = destination;
            Graph.vertices[i].distance = distance;

            Graph.vertices[freeIndexFromEnd].source = destination;
            Graph.vertices[freeIndexFromEnd].destination = source;
            Graph.vertices[freeIndexFromEnd].distance = distance;
        }
    }

    private static void printGraph(){
        System.out.println();
        for (int i = 0; i < vertices.length; i++) {
            System.out.println(Graph.vertices[i].source + " - " +
                    Graph.vertices[i].destination + " : " + Graph.vertices[i].distance);
        }
    }

    private static void generateMatrixFromGraph(){
        matrix = new int[edgeNumber][edgeNumber];
        for (int i = 0; i < vertices.length; i++) {
            int row = Graph.vertices[i].source - 1;
            int positionInRow = Graph.vertices[i].destination - 1;
            matrix[row][positionInRow] = Graph.vertices[i].distance;
        }
    }

    private static void printMatrix() {
        System.out.println("\nmatrix = [");
        for (int[] row : matrix) {
            System.out.print("  [ ");
            for (int vertex : row) {
                System.out.print(vertex + " ");
            }
            System.out.println("]");
        }
        System.out.println("]");
    }
}