import java.util.*;

public class ShortPath {
    private static final double INF = Double.MAX_VALUE;
    private static Map<String, Double> dpCache = new HashMap<>();
    
public static double calculateDistance(int[] point1, int[] point2) {  // Calculate the distance 
        return Math.sqrt(Math.pow(point1[0] - point2[0], 2) + Math.pow(point1[1] - point2[1], 2));
    }
    // prioritize nearest unvisited location first
public static double dp(int currentIndex, int visitedMask, int[][] locations, double[][] distanceMatrix) {
        if (visitedMask == (1 << locations.length) - 1) {
            return distanceMatrix[currentIndex][0]; 
        }

        String cacheKey = currentIndex + "," + visitedMask;
        if (dpCache.containsKey(cacheKey)) {
            return dpCache.get(cacheKey);
        }
        double minCost = INF;
        // list of unvisited indices and sort by distance 
        List<Integer> unvisited = new ArrayList<>();
        for (int i = 0; i < locations.length; i++) {
            if ((visitedMask & (1 << i)) == 0) {
                unvisited.add(i);
            }
        }

        // Sort unvisited locations by distance
        unvisited.sort(Comparator.comparingDouble(i -> distanceMatrix[currentIndex][i]));
        for (int nextIndex : unvisited) {
            double cost = distanceMatrix[currentIndex][nextIndex]
                    + dp(nextIndex, visitedMask | (1 << nextIndex), locations, distanceMatrix);
            minCost = Math.min(minCost, cost);
        }

        dpCache.put(cacheKey, minCost);
        return minCost;
    }

    // Generate the distance 
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
        int[][] locations = {
                {0, 0},  // Restaurant
                {1, 1},  // Delivery 1
                {3, 4},  // Delivery 2
                {2, 2},  // Delivery 3
                {5, 6}   // Delivery 4
        };

        double[][] distanceMatrix = createDistanceMatrix(locations);
        double minCost = dp(0, 1, locations, distanceMatrix);

        System.out.println("Minimum Delivery Route Cost: " + minCost);
    }
}
// https://gamma.app/docs/Food-Delivery-Route-Optimization-2mpvgp3yd3kz0m7?mode=doc