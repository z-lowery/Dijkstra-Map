package DijkstraMap;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Arrays;
public class BackendTests
{
	/**
    * Test Case: roleTest1
    * Purpose: Verify that the `loadGraphData` method in the Backend class correctly loads data
    * from a DOT file and inserts nodes and edges accurately into the graph.
    */
   @Test
   public void roleTest1() throws IOException
   {
       Graph_Placeholder graph = new Graph_Placeholder();
       Backend backend = new Backend(graph);
       // Relative path to the DOT file
       String relativePath = "campus.dot";
      
       // Get the absolute path
       Path absolutePath = Paths.get(relativePath).toAbsolutePath();
      Assertions.assertTrue(Files.exists(absolutePath), "File not found: " + absolutePath);
       // Mock the loading of graph data from the file
       List<String> graphData = Arrays.asList(
           "A -> B [weight=5.0]",
           "B -> C [weight=3.0]",
           "A -> C [weight=8.0]"
       );
       backend.loadGraphData(absolutePath.toString());
       // Check if the locations are loaded
       List<String> allLocations = backend.getListOfAllLocations();
       Assertions.assertTrue(allLocations.containsAll(Arrays.asList("A", "B", "C")), "Locations not loaded correctly.");
       // Ensure the edges are inserted correctly
       Assertions.assertEquals(5.0, graph.getEdge("A", "B"), "Edge A -> B not loaded correctly.");
       Assertions.assertEquals(3.0, graph.getEdge("B", "C"), "Edge B -> C not loaded correctly.");
       Assertions.assertEquals(8.0, graph.getEdge("A", "C"), "Edge A -> C not loaded correctly.");
   }
   /**
    * Test Case: roleTest2
    * Purpose: Check if the shortest path method correctly finds the shortest paths
    * between specified nodes in the graph.
    */
   @Test
   public void roleTest2()
   {
       Graph_Placeholder graph = new Graph_Placeholder();
       Backend backend = new Backend(graph);
       // Insert nodes and edges for testing
       graph.insertNode("Union South");
       graph.insertNode("Computer Sciences and Statistics");
       graph.insertNode("Atmospheric, Oceanic and Space Sciences");
       graph.insertEdge("Union South", "Computer Sciences and Statistics", 5.0);
       graph.insertEdge("Computer Sciences and Statistics", "Atmospheric, Oceanic and Space Sciences", 3.0);
       // Test: Find the shortest path between Union South and Atmospheric, Oceanic and Space Sciences
       List<String> path = backend.findLocationsOnShortestPath("Union South", "Atmospheric, Oceanic and Space Sciences");
       Assertions.assertTrue(path.isEmpty(), "Shortest path between Union South and Atmospheric, Oceanic and Space Sciences is incorrect.");
       // Test: Shortest path between Computer Sciences and Statistics and Atmospheric, Oceanic and Space Sciences
       path = backend.findLocationsOnShortestPath("Computer Sciences and Statistics", "Atmospheric, Oceanic and Space Sciences");
       Assertions.assertEquals(Arrays.asList("Computer Sciences and Statistics", "Atmospheric, Oceanic and Space Sciences"), path,
               "Shortest path between Computer Sciences and Statistics and Atmospheric, Oceanic and Space Sciences is incorrect.");
   }
   /**
    * Test Case: roleTest3
    * Purpose: Check if the backend correctly identifies the furthest reachable location from a given start node.
    * Also, verify that walking times
    */
   @Test
   public void roleTest3()
   {
       Graph_Placeholder graph = new Graph_Placeholder();
       Backend backend = new Backend(graph);
       // Insert nodes and edges for testing
       graph.insertNode("Union South");
       graph.insertNode("Computer Sciences and Statistics");
       graph.insertNode("Atmospheric, Oceanic and Space Sciences");
       graph.insertEdge("Union South", "Computer Sciences and Statistics", 5.0);
       graph.insertEdge("Computer Sciences and Statistics", "Atmospheric, Oceanic and Space Sciences", 3.0);
       String furthestLocation = backend.getFurthestDestinationFrom("Union South");
       // Expected furthest location based on the placeholder setup
       Assertions.assertEquals("Atmospheric, Oceanic and Space Sciences", furthestLocation, "Furthest location from Union South is incorrect.");
       // Test: Walking times
       List<Double> times = backend.findTimesOnShortestPath("Union South", "Atmospheric, Oceanic and Space Sciences");
       Assertions.assertTrue(times.isEmpty(), "Walking times from Union South to Atmospheric, Oceanic and Space Sciences are incorrect.");
   }
}
