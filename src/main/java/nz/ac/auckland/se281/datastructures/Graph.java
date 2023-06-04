package nz.ac.auckland.se281.datastructures;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * A graph that is composed of a set of vertices and edges.
 *
 * <p>The Graph class represents a directed graph with vertices and edges. It provides various
 * methods to perform operations and checks on the graph, such as finding roots, checking properties
 * like reflexivity, symmetry, transitivity, antisymmetry, and equivalence, and performing
 * breadth-first and depth-first searches.
 *
 * @param <T> The type of each vertex, that have a total ordering.
 */
public class Graph<T extends Comparable<T>> {
  private Set<T> vertices;
  private Set<Edge<T>> edges;

  /**
   * Constructs a graph with the given set of vertices and edges.
   *
   * @param vertices The set of vertices in the graph.
   * @param edges The set of edges in the graph.
   */
  public Graph(Set<T> vertices, Set<Edge<T>> edges) {
    this.vertices = vertices;
    this.edges = edges;
  }

  /**
   * Retrieves the roots of the graph.
   *
   * <p>The roots are the vertices that have an in-degree of 0 (i.e., no incoming edges). Or the
   * node is the minimum value in an equivalence class. The roots are returned as a set sorted in
   * numerical order.
   *
   * @return The set of roots in the graph.
   */
  public Set<T> getRoots() {
    TreeSet<T> sortedRoots = createSortedSet(vertices);

    // Remove vertices that don't have an in-degree of 0
    for (Edge<T> edge : edges) {
      sortedRoots.remove(edge.getDestination());
    }

    // If it the graph is an equivalence class, add the minimum value in each class
    if (this.isEquivalence()) {
      for (T vertex : vertices) {
        Set<T> currentEquivalenceClass = getEquivalenceClass(vertex);
        T currentMinNumber = this.getMinimum(currentEquivalenceClass);
        sortedRoots.add(currentMinNumber);
      }
    }

    return sortedRoots;
  }

  /**
   * Checks if the graph is reflexive.
   *
   * <p>A graph is reflexive if every vertex has a self-loop, i.e., an edge from the vertex to
   * itself.
   *
   * @return {@code true} if the graph is reflexive, {@code false} otherwise.
   */
  public boolean isReflexive() {
    for (T vertex : vertices) {
      boolean hasSelfLoop = false;

      for (Edge<T> edge : edges) {
        // If the edge is a self loop, and the self loop is starts from the vertex
        // then set the the vertex has having a self loop
        if (edge.getSource().equals(edge.getDestination()) && edge.getSource().equals(vertex)) {
          hasSelfLoop = true;
        }
      }
      // If we find a vertex without a self loop we return false
      if (!hasSelfLoop) {
        return false;
      }
    }

    return true;
  }

  /**
   * Checks if the graph is symmetric.
   *
   * <p>A graph is symmetric if for every edge from vertex A to vertex B, there exists an edge from
   * vertex B to vertex A.
   *
   * @return {@code true} if the graph is symmetric, {@code false} otherwise.
   */
  public boolean isSymmetric() {
    Edge<T> opposite;

    for (Edge<T> edge : edges) {
      // if A to B exists but not B to A, return false
      opposite = new Edge<T>(edge.getDestination(), edge.getSource());
      if (!edges.contains(opposite)) {
        return false;
      }
    }

    return true;
  }

  /**
   * Checks if the graph is transitive.
   *
   * <p>A graph is transitive if for every pair of edges (A, B) and (B, C), there exists an edge (A,
   * C).
   *
   * @return {@code true} if the graph is transitive, {@code false} otherwise.
   */
  public boolean isTransitive() {
    for (Edge<T> edge : edges) {
      for (T vertex : vertices) {
        // Let any edge be A to B
        // If B to C exists where C is any other vertex and A to C doesn't exist, then the graph is
        // not transitive
        Edge<T> edgeBtoC = new Edge<T>(edge.getDestination(), vertex);
        Edge<T> edgeAtoC = new Edge<T>(edge.getSource(), vertex);

        if (edges.contains(edgeBtoC) && !edges.contains(edgeAtoC)) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * Checks if the graph is antisymmetric.
   *
   * <p>A graph is antisymmetric if for every pair of distinct vertices A and B, if there is an edge
   * from A to B, then there is no edge from B to A (unless A and B are the same vertex)
   *
   * @return {@code true} if the graph is antisymmetric, {@code false} otherwise.
   */
  public boolean isAntiSymmetric() {
    for (Edge<T> edge : edges) {
      Edge<T> edgeBtoA = new Edge<T>(edge.getDestination(), edge.getSource());

      // If A to B exist and B to A exist and A does not equal B, then the graph is not
      // antisymmetric
      if (edges.contains(edgeBtoA) && (!edge.equals(edgeBtoA))) {
        return false;
      }
    }

    return true;
  }

  /**
   * Checks if the graph is an equivalence relation.
   *
   * <p>A graph is an equivalence relation if it is reflexive, symmetric, and transitive.
   *
   * @return {@code true} if the graph is an equivalence relation, {@code false} otherwise.
   */
  public boolean isEquivalence() {
    if (this.isReflexive() && this.isSymmetric() && this.isTransitive()) {
      return true;
    }
    return false;
  }

  /**
   * Retrieves the equivalence class of a given vertex.
   *
   * <p>The equivalence class of a vertex is the set of all vertices that are in the same
   * equivalence relation with the given vertex.
   *
   * @param vertex The vertex for which to retrieve the equivalence class.
   * @return The set of vertices in the equivalence class of the given vertex.
   */
  public Set<T> getEquivalenceClass(T vertex) {
    // If it is not an equivalence relation, return an empty set
    if (!isEquivalence()) {
      return new TreeSet<T>();
    }

    // Initialise a sorted set with all the vertices
    Set<T> equivalenceClass = createSortedSet(vertices);

    for (T currentVertex : vertices) {
      // If the vertex is not in the same quivalence class, remove the vertex
      if (!this.isSameEquivalenceClass(currentVertex, vertex)) {
        equivalenceClass.remove(currentVertex);
      }
    }

    return equivalenceClass;
  }

  /**
   * Performs iterative breadth-first search on the graph.
   *
   * <p>Breadth-first search is a graph traversal algorithm that visits all the vertices of a graph
   * in breadth-first order, i.e. it visits all the vertices of the same level before moving to the
   * next level.
   *
   * @return A list of vertices visited during the breadth-first search.
   */
  public List<T> iterativeBreadthFirstSearch() {
    // Get the roots in numerical order
    TreeSet<T> roots = createSortedSet(getRoots());
    Queue<T> queue = new Queue<T>();
    List<T> visited = new ArrayList<T>();

    for (T currentRoot : roots) {
      // Add the current root to the queue
      queue.enqueue(currentRoot);
      visited.add(currentRoot);

      // Fully explore the child nodes of the root
      while (!queue.isEmpty()) {
        moveOneLayerBfs(queue, visited);
      }
    }

    return visited;
  }

  private void moveOneLayerBfs(Queue<T> queue, List<T> visited) {
    // Find all the child nodes of the vertex at the front of the queue
    Node<T> currentNode = queue.dequeue();
    Set<T> currentDestinations = this.findAllDestinations(currentNode.getData());

    // Search the child nodes and add their destinations to the visited list and queue
    for (T destination : currentDestinations) {
      if (!visited.contains(destination)) {
        visited.add(destination);
        queue.enqueue(destination);
      }
    }
  }

  /**
   * Performs recursive breadth-first search on the graph.
   *
   * <p>This method uses recursion to perform breadth-first search. It visits all the vertices of a
   * graph in breadth-first order.
   *
   * @return A list of vertices visited during the breadth-first search.
   */
  public List<T> recursiveBreadthFirstSearch() {
    // Get the roots in numerical order
    TreeSet<T> roots = createSortedSet(getRoots());
    Queue<T> queue = new Queue<T>();
    List<T> visited = new ArrayList<T>();

    for (T currentRoot : roots) {
      // Add the current root to the queue
      queue.enqueue(currentRoot);
      visited.add(currentRoot);
      recursiveBfsCall(queue, visited);
    }

    return visited;
  }

  private void recursiveBfsCall(Queue<T> queue, List<T> visited) {
    if (!queue.isEmpty()) {
      moveOneLayerBfs(queue, visited);
      recursiveBfsCall(queue, visited);
    }
  }

  /**
   * Performs iterative depth-first search on the graph.
   *
   * <p>Depth-first search is a graph traversal algorithm that visits all the vertices of a graph in
   * depth-first order, i.e., it visits a vertex and then recursively visits all its adjacent
   * vertices before backtracking.
   *
   * @return A list of vertices visited during the depth-first search.
   */
  public List<T> iterativeDepthFirstSearch() {
    // Get the roots in numerical order
    TreeSet<T> roots = createSortedSet(getRoots());
    Stack<T> stack = new Stack<T>();
    List<T> visited = new ArrayList<T>();

    for (T currentRoot : roots) {
      // Add current root to the stack
      stack.push(currentRoot);

      while (!stack.isEmpty()) {
        moveOneLayerDfs(stack, visited);
      }
    }

    return visited;
  }

  private void moveOneLayerDfs(Stack<T> stack, List<T> visited) {
    // This stack is for reversing the order of things
    Stack<T> holdingStack = new Stack<T>();

    T currentNode = stack.pop();
    if (!visited.contains(currentNode)) {
      visited.add(currentNode);
    }

    Set<T> currentDestinations = this.findAllDestinations(currentNode);

    for (T destination : currentDestinations) {
      if (!visited.contains(destination)) {
        holdingStack.push(destination);
      }
    }
    // Unload the holding stack onto the stack, this reverses the order
    holdingStack.unloadStackOnto(stack);
  }

  /**
   * Performs recursive depth-first search on the graph.
   *
   * <p>This method uses recursion to perform depth-first search. It visits all the vertices of a
   * graph in depth-first order.
   *
   * @return A list of vertices visited during the depth-first search.
   */
  public List<T> recursiveDepthFirstSearch() {
    // Get the roots in numerical order
    TreeSet<T> roots = createSortedSet(getRoots());
    Stack<T> stack = new Stack<T>();
    List<T> visited = new ArrayList<T>();

    for (T currentRoot : roots) {
      stack.push(currentRoot);
      recursiveDfsCall(stack, visited);
    }

    return visited;
  }

  private void recursiveDfsCall(Stack<T> stack, List<T> visited) {
    if (!stack.isEmpty()) {
      moveOneLayerDfs(stack, visited);
      recursiveDfsCall(stack, visited);
    }
  }

  private boolean isSameEquivalenceClass(T currentVertex, T vertex) {
    // If the grpah isn't an equivalence relation, return false
    if (!this.isEquivalence()) {
      return false;
    }

    // If the two vertices are in the same equivalence class then an edge will exist from any one of
    // the vertices to the other
    Edge<T> edgeA = new Edge<T>(currentVertex, vertex);
    if (!edges.contains(edgeA)) {
      return false;
    }

    return true;
  }

  private T getMinimum(Set<T> numbers) {
    T currentMinT = null;
    for (T number : numbers) {
      // Compare the two numbers, assume they are integers in String type
      if (currentMinT == null
          || Integer.compare(
                  Integer.parseInt((String) number), Integer.parseInt((String) currentMinT))
              < 0) {
        currentMinT = number;
      }
    }
    return currentMinT;
  }

  private Set<T> findAllDestinations(T vertex) {
    TreeSet<T> destinations = createSortedSet();

    for (Edge<T> edge : edges) {
      // If the vertex is the source of the edge
      // Then save the destination of the edge
      if (vertex.equals(edge.getSource())) {
        destinations.add(edge.getDestination());
      }
    }

    return destinations;
  }

  private TreeSet<T> createSortedSet() {
    return new TreeSet<T>(
        new Comparator<T>() {
          // Overide the compare method ensure sorting in numerical order
          @Override
          public int compare(T o1, T o2) {
            return Integer.compare(Integer.parseInt((String) o1), Integer.parseInt((String) o2));
          }
        });
  }

  // Create a sorted set with initial values
  private TreeSet<T> createSortedSet(Set<T> set) {
    TreeSet<T> sortedSet = createSortedSet();
    sortedSet.addAll(set);
    return sortedSet;
  }
}
