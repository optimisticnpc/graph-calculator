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
        Edge<T> edgeB = new Edge<T>(edge.getDestination(), vertex);
        Edge<T> edgeC = new Edge<T>(edge.getSource(), vertex);

        if (edges.contains(edgeB) && !edges.contains(edgeC)) {
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
      Edge<T> edgeB = new Edge<T>(edge.getDestination(), edge.getSource());

      if (edges.contains(edgeB) && (!edge.equals(edgeB))) {
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
    Set<T> equivalenceClass = createSortedSet(vertices);

    for (T currentVertex : vertices) {
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
    TreeSet<T> roots = createSortedSet(getRoots());
    Queue<T> queue = new Queue<T>();
    List<T> visited = new ArrayList<T>();

    for (T currentRoot : roots) {
      queue.enqueue(currentRoot);
      visited.add(currentRoot);

      while (!queue.isEmpty()) {
        moveOneLayerBFS(queue, visited);
      }
    }

    return visited;
  }

  private void moveOneLayerBFS(Queue<T> queue, List<T> visited) {
    Node<T> currentNode = queue.dequeue();
    Set<T> currentDestinations = this.findAllDestinations(currentNode.getData());

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
    TreeSet<T> roots = createSortedSet(getRoots());
    Queue<T> queue = new Queue<T>();
    List<T> visited = new ArrayList<T>();

    for (T currentRoot : roots) {
      queue.enqueue(currentRoot);
      visited.add(currentRoot);
      visited = recursiveBfsCall(queue, visited);
    }

    return visited;
  }

  private List<T> recursiveBfsCall(Queue<T> queue, List<T> visited) {
    if (!queue.isEmpty()) {
      moveOneLayerBFS(queue, visited);
      visited = recursiveBfsCall(queue, visited);
    }

    return visited;
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
    TreeSet<T> roots = createSortedSet(getRoots());
    Stack<T> stack = new Stack<T>();
    List<T> visited = new ArrayList<T>();

    for (T currentRoot : roots) {
      stack.push(currentRoot);

      while (!stack.isEmpty()) {
        moveOneLayerDFS(stack, visited);
      }
    }

    return visited;
  }

  private void moveOneLayerDFS(Stack<T> stack, List<T> visited) {
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
    // Unload the holding stack onto the stack, which subsequently reverses the order
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
    TreeSet<T> roots = createSortedSet(getRoots());
    Stack<T> stack = new Stack<T>();
    List<T> visited = new ArrayList<T>();

    for (T currentRoot : roots) {
      stack.push(currentRoot);
      visited = recursiveDfsCall(stack, visited);
    }

    return visited;
  }

  private List<T> recursiveDfsCall(Stack<T> stack, List<T> visited) {
    if (!stack.isEmpty()) {
      moveOneLayerDFS(stack, visited);
      visited = recursiveDfsCall(stack, visited);
    }

    return visited;
  }

  private boolean isSameEquivalenceClass(T currentVertex, T vertex) {
    // TODO: I can probably make this simpler

    if (!this.isEquivalence()) {
      return false;
    }

    Edge<T> edgeA = new Edge<T>(currentVertex, vertex);
    Edge<T> edgeB = new Edge<T>(vertex, currentVertex);

    if (!edges.contains(edgeA) || !edges.contains(edgeB)) {
      return false;
    }

    return true;
  }

  private T getMinimum(Set<T> numbers) {
    T currentMinT = null;
    for (T number : numbers) {
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
      if (vertex.equals(edge.getSource())) {
        destinations.add(edge.getDestination());
      }
    }

    return destinations;
  }

  private TreeSet<T> createSortedSet() {
    return new TreeSet<T>(
        new Comparator<T>() {
          @Override
          public int compare(T o1, T o2) {
            return Integer.compare(Integer.parseInt((String) o1), Integer.parseInt((String) o2));
          }
        });
  }

  private TreeSet<T> createSortedSet(Set<T> set) {
    TreeSet<T> sortedSet = createSortedSet();
    sortedSet.addAll(set);
    return sortedSet;
  }
}
