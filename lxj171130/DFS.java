/**
 * @author Leejia James
 * @author Nirbhay Sibal
 *
 * DFS - Strongly Connected Components
 * Implementing the algorithm to find strongly connected components 
 * of a directed graph
 *
 * Ver 1.0: 2018/11/10
 */

package lxj171130;

import rbk.Graph.Vertex;
import rbk.Graph;
import rbk.Graph.Edge;
import rbk.Graph.GraphAlgorithm;
import rbk.Graph.Factory;
import rbk.Graph.Timer;

import java.io.File;
import java.util.List;
import java.util.LinkedList;
import java.util.Scanner;

public class DFS extends GraphAlgorithm<DFS.DFSVertex> {
	
    int time;
    int topNum;
    LinkedList<Vertex> finishList;
    boolean notDAG;
    int cno;

    enum Color {
        WHITE, BLACK, GREY;
    }

    public static class DFSVertex implements Factory {
        int cno;
        Color color;
        Vertex parent;
        int top;
        int dis;
        int fin;
		public DFSVertex(Vertex u) {
            color = Color.WHITE;
            parent = null;
            top = 0;
            cno = 0;
		}
		public DFSVertex make(Vertex u) { 
			return new DFSVertex(u); 
		}
    }

    public DFS(Graph g) {
    	super(g, new DFSVertex(null));
    }

    /**
     * DFS to visit the nodes of a graph
     * @param Graph g
     * @return DFS
     */
    public static DFS depthFirstSearch(Graph g) {
    	DFS d = new DFS(g);
        d.dfs(g);
        return d;
    }

	/**
	 * Member function to find topological order
	 * @return List of vertices in topological order if graph is DAG, 
	 * 			null otherwise
	 */
    public List<Vertex> topologicalOrder1() {
    	DFS d1 = depthFirstSearch(g);
    	if(d1.notDAG) {
    		return null;
    	}
    	else {
    		return d1.finishList;
    	}
    }

    // Find the number of connected components of the graph g by running dfs.
    // Enter the component number of each vertex u in u.cno.
    // Note that the graph g is available as a class field via GraphAlgorithm.
    public int connectedComponents() {
    	return 0;
    }

    // After running the connected components algorithm, the component no of each 
    // vertex can be queried.
    public int cno(Vertex u) {
    	return get(u).cno;
    }
    
    /**
     * Finding topological order of a DAG using DFS
     * @param Graph g
     * @return List of vertices in topological order, null if g is not a DAG
     */
    public static List<Vertex> topologicalOrder1(Graph g) {
		DFS d = new DFS(g);
		return d.topologicalOrder1();
    }

    // Find topological oder of a DAG using the second algorithm. Returns null if g is not a DAG.
    public static List<Vertex> topologicalOrder2(Graph g) {
	return null;
    }

    /**
     * Initializing Vertices of graph: setting color to white
     */
    private void initialize() {
        cno= 0;
        topNum = g.size();
        finishList = new LinkedList<>();
        for(Vertex u: g) {
            get(u).color = Color.WHITE;
            get(u).parent = null;
            get(u).top = 0;
        }
        time = 0;
    }

    
    /**
     * Utility method for depthFirstSearch()
     * @param Vertex u
     */
    private void dfsVisit(Vertex u) { 
    	// u is visited by DFS
    	// Precondition: u is white
		get(u).color = Color.GREY;
		get(u).dis = ++time;
		
	    for(Edge e: g.incident(u)) {
			Vertex v = e.otherEnd(u);
			if(get(v).color == Color.WHITE) {
			    get(v).parent = u;
			    get(v).cno = get(u).cno;
			    dfsVisit(v);
			}
			else if(get(v).color == Color.GREY) {
				// cycle detected
				notDAG = true;
			}
	    }
		get(u).top = topNum--;
		finishList.addFirst(u);
		get(u).color = Color.BLACK;
		get(u).fin = ++time;
	}
    

    /**
     * Recursive algorithm to visit the nodes of a graph/list
     * @param iterable
     */
    private void dfs(Iterable<Vertex> iterable) {
        initialize();
        for (Vertex u: iterable) {
            if (get(u).color == Color.WHITE) {
                get(u).cno = ++cno;
                dfsVisit(u);
            }
        }
    }
    
    /**
     * Finding strongly connected components of the graph
     * @param g
     * @return DFS
     */
    public static DFS stronglyConnectedComponents(Graph g) {
        DFS d = new DFS(g);
        d.dfs(g);

        List<Vertex> list = d.finishList;
        g.reverseGraph();

        d.dfs(list);
        g.reverseGraph();

        return d;
    }
    
    public static void main(String[] args) throws Exception {
	//String string = "7 8   1 2 2   1 3 3   2 4 5   3 4 4   4 5 1   5 1 7   6 7 1   7 6 1 0";
	String string = "11 17   1 11 0   2 7 0   2 3 0   3 10 0   4 9 0   4 1 0   5 4 0   5 8 0   5 7 0   6 3 0   7 8 0   8 2 0   9 11 0   10 6 0   11 3 0   11 6 0   11 4 0 0";
	//String string = "7 6   1 2 2   1 3 3   2 4 5   3 4 4   5 1 7   7 6 1 0";
	Scanner in;
	// If there is a command line argument, use it as file from which
	// input is read, otherwise use input from string.
	in = args.length > 0 ? new Scanner(new File(args[0])) : new Scanner(string);
	
	// Read graph from input
    //Graph g = Graph.readGraph(in);
    Graph g = Graph.readDirectedGraph(in);
	g.printGraph(false);
		
	DFS d = DFS.stronglyConnectedComponents(g);
	int numscc = d.cno;
	System.out.println("Number of strongly connected components: " + numscc + "\nu\tcno");
	for(Vertex u: d.finishList) {
	    System.out.println(u + "\t" + d.cno(u));
	}
	System.out.println();
	
    }
}
