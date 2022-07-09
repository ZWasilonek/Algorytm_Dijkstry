import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

class DijkstrasAlgorithm {
    private static final int NO_PARENT = -1;

    public static void main(String[] args)
    {
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out), true, StandardCharsets.UTF_8));

        int[][] adjacencyMatrix = Graph.createGraphData();
        dijkstra(adjacencyMatrix);
    }
    private static void dijkstra(int[][] adjacencyMatrix)
    {
        int nVertices = adjacencyMatrix[0].length;
        int[] shortestDistances = new int[nVertices];
        boolean[] added = new boolean[nVertices];

        long startTime = System.nanoTime();
        for (int vertexIndex = 0; vertexIndex < nVertices;
             vertexIndex++)
        {
            shortestDistances[vertexIndex] = Integer.MAX_VALUE;
            added[vertexIndex] = false;
        }

        shortestDistances[0] = 0;
        int[] parents = new int[nVertices];
        parents[0] = NO_PARENT;

        for (int i = 1; i < nVertices; i++)
        {
            int nearestVertex = -1;
            int shortestDistance = Integer.MAX_VALUE;
            for (int vertexIndex = 0;
                 vertexIndex < nVertices;
                 vertexIndex++)
            {
                if (!added[vertexIndex] &&
                        shortestDistances[vertexIndex] <
                                shortestDistance)
                {
                    nearestVertex = vertexIndex;
                    shortestDistance = shortestDistances[vertexIndex];
                }
            }
            added[nearestVertex] = true;

            for (int vertexIndex = 0;
                 vertexIndex < nVertices;
                 vertexIndex++)
            {
                int edgeDistance = adjacencyMatrix[nearestVertex][vertexIndex];

                if (edgeDistance > 0
                        && ((shortestDistance + edgeDistance) <
                        shortestDistances[vertexIndex]))
                {
                    parents[vertexIndex] = nearestVertex;
                    shortestDistances[vertexIndex] = shortestDistance +
                            edgeDistance;
                }
            }
        }
        long endTime = System.nanoTime();
        long timeElapsed = endTime - startTime;
        double milisec = (double)timeElapsed / 1000000;
        System.out.println("\nCzas wykonania w nanosekundach: " + timeElapsed);
        System.out.println("Czas wykonania w milisekundach: " + String.format("%.2f", milisec) + "\n");

        printSolution(shortestDistances, parents);
    }

    private static void printSolution(int[] distances, int[] parents)
    {
        int nVertices = distances.length;
        System.out.print("Wierzchołki\tDystans \tŚcieżka");

        for (int vertexIndex = 0;
             vertexIndex < nVertices;
             vertexIndex++)
        {
            if (vertexIndex != 0)
            {
                System.out.print("\n" + 0 + " -> " + vertexIndex);
                if (vertexIndex < 10)
                    System.out.print(" \t\t ");
                else
                    System.out.print(" \t ");
                System.out.print(distances[vertexIndex]);
                System.out.print(" \t\t ");
                printPath(vertexIndex, parents);
            }
        }
    }

    private static void printPath(int currentVertex, int[] parents)
    {
        if (currentVertex == NO_PARENT) return;
        printPath(parents[currentVertex], parents);
        System.out.print(currentVertex + " ");
    }
}