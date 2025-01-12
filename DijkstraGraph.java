package DijkstraMap;

// === CS400 File Header Information ===
// Name: Zander Lowery
// Email: zrlowery@wisc.edu
// Group and Team: P2.2511
// Lecturer: Gary
// Notes to Grader: Good luck.

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

/**
 * This class extends the BaseGraph data structure with additional methods for
 * computing the total cost and list of node data along the shortest path
 * connecting a provided starting to ending nodes. This class makes use of
 * Dijkstra's shortest path algorithm.
 */
public class DijkstraGraph<NodeType, EdgeType extends Number>
        extends BaseGraph<NodeType, EdgeType>
        implements GraphADT<NodeType, EdgeType> {

    /**
     * While searching for the shortest path between two nodes, a SearchNode
     * contains data about one specific path between the start node and another
     * node in the graph. The final node in this path is stored in its node
     * field. The total cost of this path is stored in its cost field. And the
     * predecessor SearchNode within this path is referenced by the predecessor
     * field (this field is null within the SearchNode containing the starting
     * node in its node field).
     *
     * SearchNodes are Comparable and are sorted by cost so that the lowest cost
     * SearchNode has the highest priority within a java.util.PriorityQueue.
     */
    protected class SearchNode implements Comparable<SearchNode> {
        public Node node;
        public double cost;
        public SearchNode predecessor;

        public SearchNode(Node node, double cost, SearchNode predecessor) {
            this.node = node;
            this.cost = cost;
            this.predecessor = predecessor;
        }

        public int compareTo(SearchNode other) {
            if (cost > other.cost)
                return +1;
            if (cost < other.cost)
                return -1;
            return 0;
        }
    }

    /**
     * Constructor that sets the map that the graph uses.
     */
    public DijkstraGraph() {
        super(new HashtableMap<>());
    }

    /**
     * This helper method creates a network of SearchNodes while computing the
     * shortest path between the provided start and end locations. The
     * SearchNode that is returned by this method is represents the end of the
     * shortest path that is found: it's cost is the cost of that shortest path,
     * and the nodes linked together through predecessor references represent
     * all of the nodes along that shortest path (ordered from end to start).
     *
     * @param start the data item in the starting node for the path
     * @param end   the data item in the destination node for the path
     * @return SearchNode for the final end node within the shortest path
     * @throws NoSuchElementException when no path from start to end is found
     *                                or when either start or end data do not
     *                                correspond to a graph node
     */
    protected SearchNode computeShortestPath(NodeType start, NodeType end) {
        if(!containsNode(start) || !containsNode(end)){
            throw new NoSuchElementException("Start or end node does not exist in the graph");
        }
        
        // create priority queue to store searchNodes to easily poll the node with the cheapest cost
        PriorityQueue<SearchNode> priorityQueue = new PriorityQueue<>();
        HashtableMap<NodeType, Double> costs = new HashtableMap<>(); // keep track of costs from all nodes passed through
        Set<NodeType> visited = new HashSet<>(); // tracks visited nodes by storing their data (which is unique!)

        // start algorithm with the given start node by:
        //      1) storing the node information from the graph into a variable
        //      2) pass information into a searchNode variable to track the costs and
        //          predecessors for when the algorithm reaches a dead end.
        //      3) add that searchNode to the priorityQueue
        //      4) add searchNode information to the costs eet to track costs and visited nodes

        /* (1) */Node startNode = nodes.get(start);
        /* (2) */SearchNode startSearchNode = new SearchNode(startNode, 0, null);
        /* (3) */priorityQueue.add(startSearchNode);
        /* (4) */costs.put(start, 0.0);

        while(!priorityQueue.isEmpty()) { // while the priorityQueue is not empty
            SearchNode current = priorityQueue.poll(); // poll node with the cheapest cost
            NodeType currentNodeData = current.node.data; // store data of the polled node

            // if the current node is the end node, we've found the shortest path to it
            if (currentNodeData.equals(end)) {
                return current;
            }

            List<Edge> edgesLeaving = current.node.edgesLeaving; // get list of edges leaving current node

            // Skip if already visited
            if (visited.contains(currentNodeData)) continue;
            visited.add(currentNodeData);
            //System.out.println(currentNodeData);

            for (Edge edge : edgesLeaving) { // for each edge in that list
                Node successor = edge.successor; // store successor
                NodeType successorData = successor.data; // store successor's data (the key)
                double edgeWeight = edge.data.doubleValue(); // store edge weight
                double newCost = current.cost + edgeWeight; // store the cost of the current node plus the cost of the edge

                // if the successor has not been visited OR, if it has been visited but the new cost is cheaper...
                if (!costs.containsKey(successorData) || newCost < (Double) costs.get(successorData)) { 
                    // if the new cost is cheaper, remove node from cost map to update it with a new cost
                    if (costs.containsKey(successorData)) {
                        costs.remove(successorData);
                    }

                    // add node to visited list
                    costs.put(successorData, newCost);
                    SearchNode successorSearchNode = new SearchNode(successor, newCost, current);
                    priorityQueue.add(successorSearchNode);
                }
            }
        }

        throw new NoSuchElementException("No path found between the start and end node");
    }

    /**
     * Returns the list of data values from nodes along the shortest path
     * from the node with the provided start value through the node with the
     * provided end value. This list of data values starts with the start
     * value, ends with the end value, and contains intermediary values in the
     * order they are encountered while traversing this shortest path. This
     * method uses Dijkstra's shortest path algorithm to find this solution.
     *
     * @param start the data item in the starting node for the path
     * @param end   the data item in the destination node for the path
     * @return list of data item from node along this shortest path
     */
    public List<NodeType> shortestPathData(NodeType start, NodeType end) {
        // get ending SearchNode
        DijkstraGraph<NodeType, EdgeType>.SearchNode result = computeShortestPath(start, end);
        // list to store all nodes from ending SearchNode to starting SearchNode
        List<NodeType> shortestPathData = new LinkedList<>();
        // list to store shortestPathData which will be the reverse of pathNodeList
        List<NodeType> reversedShortestPathData = new LinkedList<>();

        shortestPathData.add(result.node.data);
        DijkstraGraph<NodeType, EdgeType>.SearchNode currentNode = result;

        // add all data from the nodes on the shortest path to shortestPathData list (end -> start)
        while(currentNode.predecessor != null){
            shortestPathData.add(currentNode.predecessor.node.data);
            currentNode = currentNode.predecessor;
        }
        // add data from shortestPathData list in reverse order to reversedShortestPathData list (start -> end)
        for (int i = shortestPathData.size()-1; i >= 0; i--) {
            reversedShortestPathData.add(shortestPathData.get(i));
        }

        return reversedShortestPathData;
	}

    /**
     * Returns the cost of the path (sum over edge weights) of the shortest
     * path from the node containing the start data to the node containing the
     * end data. This method uses Dijkstra's shortest path algorithm to find
     * this solution.
     *
     * @param start the data item in the starting node for the path
     * @param end   the data item in the destination node for the path
     * @return the cost of the shortest path between these nodes
     */
    public double shortestPathCost(NodeType start, NodeType end) {
        // implement in step 5.4
        return computeShortestPath(start, end).cost;
    }
    @Test
    /*
     * tests computeShortestPath() between nodes "D" and "I", which was done by hand during lecture
     */
    public void test1(){
        DijkstraGraph<String,Integer> graph = getTestGraph();
        DijkstraGraph<String,Integer>.SearchNode result = graph.computeShortestPath("D","I");
        graph.shortestPathData("D","I");

        /*System.out.println("Node = " + result.node.data +
                "\nCost = " + result.cost +
                "\nPredecessor = " + result.predecessor.node.data);*/

        // checks the information of the resulting SearchNode from the computation
        Assertions.assertEquals(result.node.data, "I"); // check data is "I"
        Assertions.assertEquals(result.cost, 13.0,0.00001); // check expected cost to get to "I" node
        Assertions.assertEquals(result.predecessor.node.data, "H"); // check expected predecessor
    }

    @Test
    /*
     * tests shortestPathDataList() between nodes "M" and "B", which is a separate path done outside
     * of lecture. Test confirms that the path between the nodes is the expected one using the
     * shortestPathDataList() method AND confirms the cost of that path. 
     */
    // tests shortestPathData() between nodes "M" and "B" against personal work done by hand
    public void test2(){
        DijkstraGraph<String,Integer> graph = getTestGraph();
        List<String> shortestPathDataList = graph.shortestPathData("M","B");
        Double shortestPathCost = graph.shortestPathCost("M", "B");

        Assertions.assertEquals(shortestPathDataList.toString(), "[" + "M, I, H, B" + "]");
        Assertions.assertEquals(shortestPathCost, 12.0);
    }

    @Test
    // tests shortestPathData() between nodes of a graph without a path between them
    public void test3(){
        DijkstraGraph<String,Integer> graph = new DijkstraGraph<>();
        graph.insertNode("A");
        graph.insertNode("B");
        graph.insertNode("C");
        graph.insertNode("D");
        graph.insertNode("E");

        graph.insertEdge("A", "B", 1);
        graph.insertEdge("A", "C", 3);
        graph.insertEdge("E", "C", 2);
        graph.insertEdge("E", "D", 2);

        // assertThrows requires the use of a CLASS TYPE, hence .class at the end of the exception.
        //      Additionally, we use a lambda expression on our call to computeShortestPath() because
        //      it causes a delay of its execution, giving asserThrows the ability to catch and verify that
        //      the expected exception was thrown when the code inside the lambda is executed.
        Assertions.assertThrows(NoSuchElementException.class, () -> {
            graph.computeShortestPath("A", "D");
        });
    }

    public DijkstraGraph<String, Integer> getTestGraph(){
        DijkstraGraph<String, Integer> graph = new DijkstraGraph<>();
        graph.insertNode("A");
        graph.insertNode("B");
        graph.insertNode("M");
        graph.insertNode("I");
        graph.insertNode("E");
        graph.insertNode("D");
        graph.insertNode("F");
        graph.insertNode("G");
        graph.insertNode("H");
        graph.insertNode("L");

        graph.insertEdge("A", "B", 1);
        graph.insertEdge("A","H", 7);
        graph.insertEdge("A","M",5);

        graph.insertEdge("B","M",3);

        graph.insertEdge("M","I",4);
        graph.insertEdge("M","E",3);
        graph.insertEdge("M","F",4);

        graph.insertEdge("I","H",2);
        graph.insertEdge("I","D",1);

        graph.insertEdge("D","F",4);
        graph.insertEdge("D","G",2);
        graph.insertEdge("D","A",7);

        graph.insertEdge("F","G",9);

        graph.insertEdge("G","H",9);
        graph.insertEdge("G","L",7);
        graph.insertEdge("G","A",4);

        graph.insertEdge("H","L",2);
        graph.insertEdge("H","B",6);
        graph.insertEdge("H","I",2);

        return graph;
    }

}
