# What is this project?
This project includes a Java implementation of Diakstra's algorithm to find distances between locations on the UW-Madison campus. Data comes from campus.dot

# Instructions 
This project is intended to run through the command line by:
1) First compiling all applicable files via javac -cp lib\junit.jar src/*.java then
2) sudo java src/WebApp [port_number]. If you are running this thorugh Windows, there is no need to use sudo.
3) Finally, you can visit the web application through a browser via http://localhost:[port_number]/

Here, there are two options:
1) A starting and ending location can be provided by the user. Clicking the button "Find Shortest Path" will list the shortest path between those two locations (inclusive) from the data provided in campus.dot.
2) A single location can be provided in the box labeled "From Location." Clicking the button "Furthest Destination From" will list the path between the user provided location to the furthest location from those provided in campus.dot

# Known Issues
1) Inputting a location that is not valid will result in an error. 
2) Locations are case-dependent. Not including the correct capitalization for locations will result in an error.

# Improvements That Can Be Made
1) Improving the look of the webpage 
2) Providing the total distance between the two relevant locations