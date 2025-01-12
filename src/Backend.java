package src;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*; // For collections like List, ArrayList, Map, etc.
import java.util.NoSuchElementException;

/**
* This class implements the BackendInterface, implement functionality
* to interact with the GraphADT and  operations frontend.
*/
public class Backend implements BackendInterface
{
  private GraphADT<String, Double> graph;
  // Constructor
  public Backend(GraphADT<String, Double> graph)
  {
      this.graph = graph;
  }
  /**
   * Loads graph data from a dot file. If previously loaded, this
   * method first deletes the nodes and edges of  existing graph
   * before loading a new one.
   *
   * @param filename the path to a dot file to read graph data from
   * @throws IOException    */
  @Override
  public void loadGraphData(String filename) throws IOException
   {                         
     clearGraph();
     List<String> lines = Files.readAllLines(Paths.get(filename));
     for (String line : lines) {
         line = line.trim();
         if (line.isEmpty() || line.startsWith("//") || line.startsWith("#")) 
         {
             continue;
         }
         if (line.contains("->")) 
         {
             try {
                 String[] parts = line.split("->");
                 String startNode = parts[0].trim().replace("\"", ""); // Remove quotes from node name
                 String endNode = parts[1].split("\\[")[0].trim().replace("\"", ""); // Remove quotes from node name
                 String weightString = parts[1].split("=")[1].split("\\]")[0].trim();
                 double weight = Double.parseDouble(weightString);

                 // Insert nodes first
                 graph.insertNode(startNode);
                 graph.insertNode(endNode);

                 // Then insert the edge
                 graph.insertEdge(startNode, endNode, weight);
             } 
             catch (Exception e) 
             {
                 System.out.println("Skipping malformed edge line: " + line);
             }
         } 
         else if (line.contains("node")) 
         {
             try 
             {
                 String nodeName = line.replace("node", "").replace(";", "").trim().replace("\"", ""); // Remove quotes from node name
                 graph.insertNode(nodeName);
             } catch (Exception e) {
                 System.out.println("Skipping malformed node line: " + line);
             }
         }
     }
 

  }
  /**
   * Clears the entire graph nodes and edges
      */
  private void clearGraph()
  {
      List<String> allNodes = graph.getAllNodes();
      for (String node : allNodes)
      {
          graph.removeNode(node);
      }
  }
  /**
   * Returns a list of all locations (node data) available in the graph.
   *
   * @return list of all location names
   */
  @Override
  public List<String> getListOfAllLocations()
  {
      try
      {
          return graph.getAllNodes();
      }
      catch (Exception e)
      {
          System.out.println("Error retrieving nodes from the graph: " + e.getMessage());
          return new ArrayList<>();
      }
  }
  /**
   * Return the sequence of locations along the shortest path from startLocation
   * to endLocation, or an empty list if no such path exists.
   *
   * @param startLocation the start location of the path
   * @param endLocation   the end location of the path
   * @return a list with the nodes on shortest path from start to end
   *         
   */
  @Override
  public List<String> findLocationsOnShortestPath(String startLocation, String endLocation)
  {
      // Throw NoSuchElementException if the locations are not in the graph
      if (!graph.containsNode(startLocation)) 
      {
          throw new NoSuchElementException("Start location not found in the graph: " + startLocation);
      }
      if (!graph.containsNode(endLocation)) 
      {
          throw new NoSuchElementException("End location not found in the graph: " + endLocation);
      }

      List<String> rawPath = new ArrayList<>();
      rawPath = graph.shortestPathData(startLocation, endLocation);

      if (!rawPath.isEmpty() && rawPath.get(rawPath.size() - 1).equals(startLocation))
      {
          rawPath.remove(rawPath.size() - 1);  // Remove the extra start node
      }
      return rawPath;  // Return the cleaned path
  }
  
  /**
   * Return the walking times in seconds between each two nodes on the
   * shortest path from start to end  or an empty list if no
   * 
   *
   * @param startLocation the start location of the path
   * @param endLocation   the end location of the path
   * @return a list with the walking times in seconds between two nodes along
   *         the shortest path from startLocation to endLocation, or an empty
   *         list if no such path exists
   */
  @Override
  public List<Double> findTimesOnShortestPath(String startLocation, String endLocation)
  {
      List<String> path = findLocationsOnShortestPath(startLocation, endLocation);
      List<Double> times = new ArrayList<>();
      // If path size is greater than 1, calculate edge weights (times)
      if (path.size() > 1)
      {
          for (int i = 0; i < path.size() - 1; i++)
          {
              try
              {
                  Double time = graph.getEdge(path.get(i), path.get(i + 1));
                  times.add(time);
              }
              catch (NoSuchElementException e)
              {
                  System.out.println("No edge exists between " + path.get(i) + " and " + path.get(i + 1));
                  return new ArrayList<>();
              }
              catch (Exception e)
              {
                  System.out.println("Error retrieving edge weight: " + e.getMessage());
                  return new ArrayList<>();
              }
          }
      }
      return times;
  }
  /**
   * Returns the most distant location (the one that takes the longest time to
   * reach) when comparing all shortest paths that begin from the provided
   * startLocation.
   *
   * @param startLocation the location to find the most distant location from
   * @return the most distant location 
   *   * @throws NoSuchElementException         
   */
  @Override
  public String getFurthestDestinationFrom(String startLocation) throws NoSuchElementException
  {
      if (startLocation == null || !graph.containsNode(startLocation))
      {
          throw new NoSuchElementException("Start location does not exist or is invalid: " + startLocation);
      }
      Map<String, Double> distances = new HashMap<>();
      List<String> allLocations = graph.getAllNodes();
      for (String location : allLocations)
      {
          if (!location.equals(startLocation))
           {
            
          	try
          	{
                  double cost = graph.shortestPathCost(startLocation, location);
                  distances.put(location, cost);
              }
          	catch (NoSuchElementException e)
          	{
                  System.out.println("No path to location " + location);
                  continue; // Skip this location if no path exists
              }
          	catch (Exception e)
          	{
                  System.out.println("Error calculating shortest path to " + location + ": " + e.getMessage());
                  continue;
              }
          }
      }
      // Find the location with the maximum distance (longest time)
      if (distances.isEmpty())
      {
          throw new NoSuchElementException("No reachable locations from " + startLocation);
      }
      return distances.entrySet().stream()
              .max(Map.Entry.comparingByValue())
              .map(Map.Entry::getKey)
              .orElseThrow(() -> new NoSuchElementException("No furthest location found"));
  }
}