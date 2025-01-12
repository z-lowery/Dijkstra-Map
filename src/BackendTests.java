package src;

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
       String relativePath = "src\\campus.dot";
      
       // Get the absolute path
       Path absolutePath = Paths.get(relativePath).toAbsolutePath();
      Assertions.assertTrue(Files.exists(absolutePath), "File not found: " + absolutePath);
       /* Mock the loading of graph data from the file
       List<String> graphData = Arrays.asList(
           "A -> B [weight=5.0]",
           "B -> C [weight=3.0]",
           "A -> C [weight=8.0]"
       ); */
       backend.loadGraphData(relativePath.toString());
       // Check if the locations are loaded
       List<String> allLocations = backend.getListOfAllLocations();
       Assertions.assertTrue(allLocations.containsAll(Arrays.asList("Union South", "Computer Sciences and Statistics", 
       "Atmospheric, Oceanic and Space Sciences", "Memorial Union")), "Locations not loaded correctly.");
       // Ensure the edges are inserted correctly
       Assertions.assertEquals(1.0, graph.getEdge("Union South", "Computer Sciences and Statistics"), "Edge A -> B not loaded correctly.");
       Assertions.assertEquals(2.0, graph.getEdge("Computer Sciences and Statistics", "Atmospheric, Oceanic and Space Sciences"), "Edge B -> C not loaded correctly.");
       Assertions.assertEquals(3.0, graph.getEdge("Atmospheric, Oceanic and Space Sciences", "Memorial Union"), "Edge A -> C not loaded correctly.");
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

       // Test: Find the shortest path between Union South and Atmospheric, Oceanic and Space Sciences
       List<String> path = backend.findLocationsOnShortestPath("Union South", "Atmospheric, Oceanic and Space Sciences");
       Assertions.assertEquals(Arrays.asList("Union South", "Computer Sciences and Statistics", 
       "Atmospheric, Oceanic and Space Sciences"), path, 
            "Shortest path between Union South and Atmospheric, Oceanic and Space Sciences is incorrect.");
       
       // Test: Shortest path between Computer Sciences and Statistics and Atmospheric, Oceanic and Space Sciences
       path = backend.findLocationsOnShortestPath("Computer Sciences and Statistics", "Atmospheric, Oceanic and Space Sciences");
       Assertions.assertEquals(Arrays.asList("Computer Sciences and Statistics", "Atmospheric, Oceanic and Space Sciences"), path,
               "Shortest path between Computer Sciences and Statistics and Atmospheric, Oceanic and Space Sciences is incorrect.");
   }
   /**
    * Test Case: roleTest3
    * Purpose: Check if the backend correctly identifies the furthest reachable location from a given start node.
    * Also, verify that walking times are as expected.
    */
   @Test
   public void roleTest3()
   {
       Graph_Placeholder graph = new Graph_Placeholder();
       Backend backend = new Backend(graph);

       String furthestLocation = backend.getFurthestDestinationFrom("Union South");
       // Expected furthest location based on the placeholder setup
       Assertions.assertEquals("Atmospheric, Oceanic and Space Sciences", furthestLocation, "Furthest location from Union South is incorrect.");
       // Test: Walking times
       List<Double> times = backend.findTimesOnShortestPath("Union South", "Atmospheric, Oceanic and Space Sciences");
       System.out.println(times);
       Assertions.assertEquals(Arrays.asList(1.0,2.0), times, "Walking times from Union South to Atmospheric, Oceanic and Space Sciences are incorrect.");
   }
}
