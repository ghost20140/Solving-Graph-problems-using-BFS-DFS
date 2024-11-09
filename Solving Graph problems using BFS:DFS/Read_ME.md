# Solving problems with Graphs

# Overall motivation

AI tools such as Github Copilot can write a lot of correct code, and a lot of incredibly wrong code. What role do humans need to play in the software development process?

If you ask AI to write some code, and it does, how do you know the code is correct? You have to “prove” the code is correct; not prove it in a mathematical sense, but rather provide test cases that give you confidence that the code is correct.

The idea here is to take simple 

# Preliminary notes

Our graphlib code is on github here:  
[https://github.com/jspacco/graphlib](https://github.com/jspacco/graphlib)

To read from a file with a scanner in main you can always write this:

| public static void main(String\[\] args) throws Exception{    // create part1-1.txt in the datafiles folder of your graphlib project     Scanner sc \= new Scanner(new FileInputStream("datafiles/part1-1.txt"));    // now use sc to read the file} |
| :---- |

# Part 1: How many components?

Given an undirected, unweighted graph as a list of edges, return the number of components. A component is a collection of nodes that can reach each other. Given N edges, there are always between 1 and N components. (Although given our problem formulation, since we are giving edges, we cannot list a single node by itself).

Practice Test Driven Development\!

1. Generate graph test inputs. Start by drawing these, then turn them into input files that graphlib can read.  
   1. Try to figure out the edge cases. What things will break your algorithm? What cases are tricky to implement?  
2. Create a new JUnit file to contain your test cases.  
3. Then finally, implement the code, possibly with copilot, and test it with your test cases.  
4. How confident are you that the code is correct?

# Part 2: Largest island

Find the size of the largest island. The world will be a text file with two ints on one line representing the number of ***rows*** and the number of ***columns***, and then ***row*** number of strings of length ***column*** containing only 0 and 1\. Read the input and then print the size of the largest island. Islands are 1s connected either vertically or horizontally or diagonally.

For example:  
3 4  
1100  
0010  
1000

This should print 3 because the largest island (the green one) is of size 3\.

Again, practice Test Driven Development.

1. Generate graph test inputs. Start by outlining these, then turn them into input files in the format given above.  
   1. Try to figure out the edge cases. What things will break your algorithm? What cases are tricky to implement?  
2. Create a new JUnit file to contain your test cases.  
   1. Start with JUnit tests that ensure that you can read the graph correctly.  
3. Write code to read the input files and create a graph (probably make it a static factory method)  
4. Then finally, implement the code (possibly with copilot) for your algorithm and test it.  
5. How confident are you that your code is correct?

# Part 3: Inverse of a graph

Add a method to our graph library that inverts a graph. Given a graph G, its inverse G’ has the same nodes but the opposite edges. So if there is an edge between node A and node B in G, there will no such edge in G’. Similarly, if there is no edge between node A and node B in G, then there will be an edge between A and B in G’.

1. Create sample graphs.  
2. Write JUnit tests.  
3. Write the code.  
4. Test the code.

# Part 4: Reachability

For each node of a directed graph G, what other nodes can be reached?

Assume G is given as a collection of edges.  
