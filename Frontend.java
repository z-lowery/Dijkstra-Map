package DijkstraMap;

import java.util.*;

public class Frontend implements FrontendInterface {
    BackendInterface backend;

    /**
     * Implementing classes should support the constructor below.
     * @param backend is used for shortest path computations
     */
    public Frontend(BackendInterface backend){
        this.backend = backend;
    };
    
    /**
     * Returns an HTML fragment that can be embedded within the body of a
     * larger html page.  This HTML output should include:
     * - a text input field with the id="start", for the start location
     * - a text input field with the id="end", for the destination
     * - a button labelled "Find Shortest Path" to request this computation
     * Ensure that these text fields are clearly labelled, so that the user
     * can understand how to use them.
     * @return an HTML string that contains input controls that the user can
     *         make use of to request a shortest path computation
     */
    public String generateShortestPathPromptHTML(){
        StringBuilder htmlFragment = new StringBuilder(); // string that will store HTML prompt
        
        htmlFragment.append("<label for=\"start\">Starting Location:</label><br>\r\n") // starting location label
                    .append("<input type=\"text\" id=\"start\"></br>\r\n") // starting location textbox
                    .append("<br>\r\n") //
                    .append("<label for=\"end\">Ending Location:</label><br>\r\n") // ending location label
                    .append("<input type=\"text\" id=\"end\"><br>\r\n") // ending location textbox
                    .append("<input type=\"button\" value=\"Find Shortest Path\">"); // button to confirm path find

        return htmlFragment.toString();
    }
    
    /**
     * Returns an HTML fragment that can be embedded within the body of a
     * larger html page.  This HTML output should include:
     * - a paragraph (p) that describes the path's start and end locations
     * - an ordered list (ol) of locations along that shortest path
     * - a paragraph (p) that includes the total travel time along this path
     * Or if there is no such path, the HTML returned should instead indicate 
     * the kind of problem encountered.
     * @param start is the starting location to find a shortest path from
     * @param end is the destination that this shortest path should end at
     * @return an HTML string that describes the shortest path between these
     *         two locations
     */
    public String generateShortestPathResponseHTML(String start, String end){
        StringBuilder htmlFragment = new StringBuilder(); // string that will store HTML response

        // print out path start and end locations ----------
        if(start == null || start.isEmpty() || end == null || end.isEmpty()){
            htmlFragment.append("<p style=\"color: red\"> Start and end locations can not be empty</p>\r\n");
        } else {
            htmlFragment.append("<p> You chose the starting location <u class=\"location\">").append(start).append("</u>\r\n") // starting location
                        .append(" and the ending location <u class=\"location\">").append(end).append("</u>.</p>\r\n"); // ending location

            // append an unordered list of locations from the start location to end location
            // the method appendLocationAlongPath() will instead return an error message if there
            //      is no such path or another problem was encountered
            htmlFragment.append("<p> The shortest path between these two locations is: </p>\r\n");
            htmlFragment = (appendLocationsAlongPath(htmlFragment, start, end));
        }

        return htmlFragment.toString();
    }

    /**
     * Returns an HTML fragment that can be embedded within the body of a
     * larger html page.  This HTML output should include:
     * - a text input field with the id="from", for the start location
     * - a button labelled "Furthest Destination From" to submit this request
     * Ensure that this text field is clearly labelled, so that the user
     * can understand how to use it.
     * @return an HTML string that contains input controls that the user can
     *         make use of to request a furthest destination calculation
     */
    public String generateFurthestDestinationFromPromptHTML(){
        StringBuilder htmlFragment = new StringBuilder(); // string that will store HTML prompt

        htmlFragment.append("<label for=\"from\">From Location:</label><br>\r\n") // from location label
                    .append("<input type=\"text\" id=\"from\"></br>\r\n") // from location textbox
                    .append("<input type=\"button\" value=\"Furthest Destination From\">\r\n"); // button to confirm path find

        return htmlFragment.toString();
    };
    
    /**
     * Returns an HTML fragment that can be embedded within the body of a
     * larger html page.  This HTML output should include:
     * - a paragraph (p) that describes the starting point being searched from
     * - a paragraph (p) that describes the furthest destination found
     * - an ordered list (ol) of locations on the path between these locations
     * Or if there is no such destination, the HTML returned should instead 
     * indicate the kind of problem encountered.
     * @param start is the starting location to find the furthest dest from
     * @return an HTML string that describes the furthest destination from the
     *        specified start location
     */
    public String generateFurthestDestinationFromResponseHTML(String start){
        StringBuilder htmlFragment = new StringBuilder(); // string that will store HTML response

        // print out starting point location
        htmlFragment.append("<p> You chose the starting point <u class=\"location\">").append(start).append("</u></p>\r\n"); // starting location

        // print out furthest destination found
        try {
            String furthestLocation = backend.getFurthestDestinationFrom(start); // get furthest destination
            htmlFragment.append("<p> The furthest destination from <u class=\"location\">").append(start).append("</u>")
                        .append( " is <u class=\"location\">").append(furthestLocation).append("</u></p>\r\n");
        
            // append an unordered list of locations from the starting point to furthestLocation
            htmlFragment = (appendLocationsAlongPath(htmlFragment, start, furthestLocation));
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("No location found while attemping to grab the furthest destination from start location");
        } catch (Exception e){
            System.out.println(e);
        }

        return htmlFragment.toString();
    };

    
    /**
     * Appends an unordered list of locations to a given StringBuilder object
     * starting from a given start location and ending with the given end
     * location. If an error occures, the method will instead add a line of text
     * saying as such 
     * @param htmlFragment is the StringBuilder object to add the unordered list to
     * @param start is start location. Will the first item in the list
     * @param end is end location. Will be the last item in the list
     * @return the htmlFragment with the new unordered list
     */
    public StringBuilder appendLocationsAlongPath(StringBuilder htmlFragment, String start, String end){
        // ordered list of locations along path from starting point to ending point
        List<String> locations = backend.findLocationsOnShortestPath(start, end);

        // if statment checks that
        //     1) the last location on the list is the ending location:
        //          T = Expected. Print out locations from start to end in a list
        //              - last location in the list should be the end location given in parameter
        //              - size of location list should not equal zero   
        //          F = Error: last element in locations list contains an unexpected result
        //     2) the locations list is not empty
        
        if (locations != null && !locations.isEmpty() && locations.get(locations.size()-1).equals(end)){            
            htmlFragment.append("<ol>\r\n"); // start of unordered list

            // add list items to unordered list 
            for (int i = 0; i < locations.size(); i++) {
                htmlFragment.append("   <li class=\"locations\">").append(locations.get(i)).append("</li>\r\n");
            }

            htmlFragment.append("</ol>\r\n"); // end of unordered list 
        } else { // if the locations list contains an unexpected result
            String error ="";
            if(locations.isEmpty()){
                    error = "locations provided may not have been valid, or another error occured.";
            } else if(locations.get(locations.size()-1).equals(end)){
                    error = "last item in locations list was not the ending location";
            } else {
                    error = "This one was unexpected.";
            }
                    htmlFragment.append("<p style=\"color: red\">")                        
                    .append("ERROR: " + error)
                    .append("</p>\r\n");
    }

        return htmlFragment;
    }
}