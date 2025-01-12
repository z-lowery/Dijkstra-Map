package Tests;

import java.util.NoSuchElementException;
import org.junit.*;
import junit.framework.AssertionFailedError;
import src.Backend;
import src.DijkstraGraph;
import src.Frontend;
import src.GraphADT;
import src.Graph_Placeholder;

public class FrontendTests {

    /* 
     * ----------- FULL INTEGRATION TESTS ------------ 
     * These tests are created to test the integration 
     * of the Frontend and Backend classes together. 
     * These tests are not exhaustive, but they do
     * check the functionality of the methods in the
     * Frontend and Backend classes.
     * -----------------------------------------------
    */

    /**
     * Test Case: Integration_test1()
     * Purpose: Tests the implementation of the new getKeys() method 
     * and the loading of data from campus.dot
     */
    @Test
    public void integration_test1(){
        GraphADT<String, Double> graph = new DijkstraGraph<String, Double>();
        Backend backend = new Backend(graph);
        
        Assert.assertTrue(graph.getAllNodes().toString().equals("[]")); // confirms an empty list is returned

        graph.insertNode("Banana");
        graph.insertNode("Apple");
        graph.insertNode("Cheese");
        graph.insertNode("Cheese");

        // confirms size of list that is returned when a node with duplicate data was attempted to be inserted 
        Assert.assertTrue(graph.getAllNodes().size() == 3); 

        // confirms that loadGraphData() works as expected
        try {
            backend.loadGraphData("src\\campus.dot");
        } catch (Exception e) {
            throw new AssertionFailedError("Problem loading graph data");
        }

        Assert.assertTrue(graph.getNodeCount() > 150);
    }

    /**
     * Test Case: Integration_test2()
     * Purpose: Tests the functionality of getting the locations along the 
     * shortest path between two locations using the findLocationsOnShortestPath() method
     */
    @Test
    public void integration_test2(){
        GraphADT<String, Double> graph = new DijkstraGraph<>();
        Backend backend = new Backend(graph);

        try {
            backend.loadGraphData("src\\campus.dot");
        } catch (Exception e) {
            throw new AssertionFailedError("Problem loading graph data");
        }

        // for a small amount of locations
        Assert.assertEquals("[Music Hall, Law Building, South Hall]", 
        backend.findLocationsOnShortestPath("Music Hall", "South Hall").toString());   
        
        // for a larger set
        Assert.assertEquals("[Law Building, X01, Luther Memorial Church, Noland Hall, Meiklejohn House, "+
        "Computer Sciences and Statistics, Rust-Schreiner Hall, Humbucker Apartments, Harlow Primate Laboratory, "+
        "Budget Bicycle Center - New Bicycles, Jenson Auto]",
        backend.findLocationsOnShortestPath("Law Building", "Jenson Auto").toString());
    }

    /**
     * Test Case: Integration_test3()
     * Purpose: Tests the functionality of getting the furthest location
     * from a given starting location and obtaining the locations
     * on the path between them using the getFurthestDestinationFrom() method.
     * 
     * Location used is valid.
     */
    @Test
    public void integration_test3(){
        GraphADT<String, Double> graph = new DijkstraGraph<>();
        Backend backend = new Backend(graph);
        Frontend frontend = new Frontend(backend);

        try {
            backend.loadGraphData("src\\campus.dot");
        } catch (Exception e) {
            throw new AssertionFailedError("Problem loading graph data");
        }

        Assert.assertEquals("Smith Residence Hall", backend.getFurthestDestinationFrom("Music Hall"));
        
        // checks for various elements that should be present in the output of using "Music Hall" as a starting location
        String response = frontend.generateFurthestDestinationFromResponseHTML("Music Hall");
        Assert.assertTrue(response.contains("<p") && response.contains("</p>")); // check for paragraph tag
        Assert.assertTrue(response.contains("<ol") && response.contains("</ol>")); // check for unordered list tag
        Assert.assertTrue(response.contains("<li") && response.contains("</li>")); // check for list item tag
        Assert.assertTrue(response.contains("Music Hall"));
        Assert.assertTrue(response.contains("Smith Residence Hall"));

    }

    /**
     * Test Case: Integration_test14()
     * Purpose: Tests the implementation of HTML prompts for
     * generateFurthestDestinationFromPromptHTML() and generateShortestPathPromptHTML().
     * 
     * Additionally, tests that an NoSuchElementException is thrown when invalid
     * locations are inputted as the parameter
     */
    @Test
    public void integration_test4(){
        Backend backend = new Backend(new DijkstraGraph<>());
        Frontend frontend = new Frontend(backend);

        // checks for behavior when given an invalid location in generateFurthestDestinationFromResponseHTML()
        Assert.assertThrows( NoSuchElementException.class, () -> {
            frontend.generateFurthestDestinationFromResponseHTML("The Best Location EVER!!!");
        });

        // checks for behavior when given an invalid location in findLocationsOnShortestPath()
        Assert.assertThrows( NoSuchElementException.class, () -> {
            frontend.generateShortestPathResponseHTML("Cheese City", "Popcorn Plateau");
        });

        String response = frontend.generateFurthestDestinationFromPromptHTML();
        Assert.assertTrue(response.contains("<label for=\"from\">")); // check for from label
        Assert.assertTrue(response.contains("<input type=\"text\" id=\"from\">")); // check for from textbox
        Assert.assertTrue(response.contains("<input type=\"button\" value=\"Furthest Destination From\">")); // check for button

        response = frontend.generateShortestPathPromptHTML();        
        Assert.assertTrue(response.contains("<label for=\"start\">")); // check for start label
        Assert.assertTrue(response.contains("<label for=\"end\">")); // check for end label
        Assert.assertTrue(response.contains("<input type=\"text\" id=\"start\">")); // check for start textbox
        Assert.assertTrue(response.contains("<input type=\"text\" id=\"end\">")); // check for end textbox
        Assert.assertTrue(response.contains("<input type=\"button\" value=\"Find Shortest Path\">")); // check for button
    }

    /*
     * These tests after were created earlier to test the
     * functionality of the frontend class before integrating
     * with DijkstraGraph, MapADT, and GraphADT. Instead, they
     * use incomplete placeholders for testing making what they
     * could test limited and strickly for the Frontend class.
     */

    /*
     * Test Case: frontendTest1()
     * Purpose: Checks the implementation of the 
     * generateShortestPathPromptHTML() method
     */
    @Test
    public void frontendTest1(){
        Backend backend = new Backend(new Graph_Placeholder());
        Frontend frontend = new Frontend(backend);

        // "Union South", "Atmospheric, Oceanic and Space Sciences"

        String result = frontend.generateShortestPathPromptHTML();        
        Assert.assertTrue(result.contains("<label for=\"start\">")); // check for start label
        Assert.assertTrue(result.contains("<label for=\"end\">")); // check for end label
        Assert.assertTrue(result.contains("<input type=\"text\" id=\"start\">")); // check for start textbox
        Assert.assertTrue(result.contains("<input type=\"text\" id=\"end\">")); // check for end textbox
        Assert.assertTrue(result.contains("<input type=\"button\" value=\"Find Shortest Path\">")); // check for button
    }

    /*
     * Test Case: frontendTest2()
     * Purpose: Checks the implementation of the 
     * generateShortestPathResponseHTML() method
     */
    @Test
    public void frontendTest2(){
        Backend backend = new Backend(new Graph_Placeholder());
        Frontend frontend = new Frontend(backend);

        // "Union South", "Atmospheric, Oceanic and Space Sciences"
        String result = frontend.generateShortestPathResponseHTML("Union South", "Atmospheric, Oceanic and Space Sciences");
        System.out.println(result);
        Assert.assertTrue(result.contains("Union South")); // check for start location
        Assert.assertTrue(result.contains("Atmospheric, Oceanic and Space Sciences")); // check for end location 
        Assert.assertTrue(result.contains("<p") && result.contains("</p>")); // check for paragraph tag 
        Assert.assertTrue(result.contains("<ol") && result.contains("</ol>")); // check for unordered list tag
        Assert.assertTrue(result.contains("<li") && result.contains("</li>")); // check for list item tag
    }

    /*
     * Test Case: frontendTest3()
     * Purpose: checks the implementation of the 
     * generateFurthestDestinationFromPromptHTML() method
     */
    @Test
    public void frontendTest3(){
        Backend backend = new Backend(new Graph_Placeholder());
        Frontend frontend = new Frontend(backend);

        String result = frontend.generateFurthestDestinationFromPromptHTML();
        Assert.assertTrue(result.contains("<label for=\"from\">")); // check for from label
        Assert.assertTrue(result.contains("<input type=\"text\" id=\"from\">")); // check for from textbox
        Assert.assertTrue(result.contains("<input type=\"button\" value=\"Furthest Destination From\">")); // check for button
    }

    /*
     * Test Case: frontendTest1()
     * Purpose: Checks the implementation of the 
     * generateFurthestDestinationFromResponseHTML() method
     */
    @Test
    public void frontendTest4(){
        Backend backend = new Backend(new Graph_Placeholder());
        Frontend frontend = new Frontend(backend);

        // "Computer Sciences and Statistics"
        String result = frontend.generateFurthestDestinationFromResponseHTML("Computer Sciences and Statistics");
        Assert.assertTrue(result.contains("Computer Sciences and Statistics")); // check for start location
        Assert.assertTrue(result.contains("<p") && result.contains("</p>")); // check for paragraph tag
        Assert.assertTrue(result.contains("<ol") && result.contains("</ol>")); // check for unordered list tag
        Assert.assertTrue(result.contains("<li") && result.contains("</li>")); // check for list item tag
    }
}