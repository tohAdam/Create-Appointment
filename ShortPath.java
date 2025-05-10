// https://gamma.app/docs/Food-Delivery-Route-Optimization-2mpvgp3yd3kz0m7?mode=doc
import java.util.*;

public class ShortPath {
    private static final double INF = Double.MAX_VALUE;
    private static Map<String, Double> dpCache = new HashMap<>();
    private static Map<String, List<Integer>> pathCache = new HashMap<>();

    public static double calculateDistance(int[] point1, int[] point2) {
        return Math.sqrt(Math.pow(point1[0] - point2[0], 2) + Math.pow(point1[1] - point2[1], 2));
    }

    public static double dp(int currentIndex, int visitedMask, int[][] locations, double[][] distanceMatrix) {
        if (visitedMask == (1 << locations.length) - 1) {
            return distanceMatrix[currentIndex][0]; 
        }

        String cacheKey = currentIndex + "," + visitedMask;
        if (dpCache.containsKey(cacheKey)) {
            return dpCache.get(cacheKey);
        }

        double minCost = INF;
        List<Integer> bestPath = new ArrayList<>();

        List<Integer> unvisited = new ArrayList<>();
        for (int i = 0; i < locations.length; i++) {
            if ((visitedMask & (1 << i)) == 0) {
                unvisited.add(i);
            }
        }

        unvisited.sort(Comparator.comparingDouble(i -> distanceMatrix[currentIndex][i]));
        for (int nextIndex : unvisited) {
            double cost = distanceMatrix[currentIndex][nextIndex]
                    + dp(nextIndex, visitedMask | (1 << nextIndex), locations, distanceMatrix);
            if (cost < minCost) {
                minCost = cost;
                bestPath.clear();
                bestPath.add(nextIndex);
                bestPath.addAll(pathCache.getOrDefault(nextIndex + "," + (visitedMask | (1 << nextIndex)), new ArrayList<>()));
            }
        }

        dpCache.put(cacheKey, minCost);
        pathCache.put(cacheKey, bestPath);
        return minCost;
    }

    public static double[][] createDistanceMatrix(int[][] locations) {
        int n = locations.length;
        double[][] distanceMatrix = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                distanceMatrix[i][j] = calculateDistance(locations[i], locations[j]);
            }
        }
        return distanceMatrix;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // Set default restaurant location to (0, 0)
        int[] restaurant = {0, 0};

        // Input delivery location
        System.out.println("Enter the delivery location (x y):");
        int[] delivery = {scanner.nextInt(), scanner.nextInt()};

        // Create locations array with restaurant and delivery
        int[][] locations = {restaurant, delivery};

        double[][] distanceMatrix = createDistanceMatrix(locations);
        double minCost = dp(0, 1, locations, distanceMatrix);

        // Retrieve the path
        List<Integer> path = pathCache.get("0,1");
        System.out.println("Minimum Delivery Route Cost: " + minCost);
        System.out.print("Delivery Route: Restaurant -> ");
        for (int index : path) {
            System.out.print("Delivery -> ");
        }
        System.out.println("Restaurant");
    }
}
