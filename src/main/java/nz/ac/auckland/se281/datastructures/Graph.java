package nz.ac.auckland.se281.datastructures;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * A graph that is composed of a set of verticies and edges.
 *
 * <p>You must NOT change the signature of the existing methods or constructor of this class.
 *
 * @param <T> The type of each vertex, that have a total ordering.
 */
public class Graph<T extends Comparable<T>> {
  private Set<T> verticies;
  private Set<Edge<T>> edges;

  public Graph(Set<T> verticies, Set<Edge<T>> edges) {
    this.verticies = verticies;
    this.edges = edges;
  }

  public Set<T> getRoots() {
    TreeSet<T> sortedRoots = createSortedSet(verticies);

    // Remove vertices that don't have an in-degree of 0
    for (Edge<T> edge : edges) {
      sortedRoots.remove(edge.getDestination());
      // TODO: Implement checking for minimum of 1 out-degree
      // Do we even need to??
    }

    if (this.isEquivalence()) {
      for (T vertex : verticies) {
        Set<T> currentEquivalenceClass = getEquivalenceClass(vertex);
        T currentMinNumber = this.getMinimum(currentEquivalenceClass);
        sortedRoots.add(currentMinNumber);
      }
    }

    return sortedRoots;
  }

  public boolean isReflexive() {

    for (T vertex : verticies) {
      // Initialise
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

  public boolean isSymmetric() {

    Edge<T> opposite;

    for (Edge<T> edge : edges) {
      opposite = new Edge<T>(edge.getDestination(), edge.getSource());
      // TODO: Note, overided equals method to make contains work
      if (!edges.contains(opposite)) {
        return false;
      }
    }

    return true;
  }

  public boolean isTransitive() {
    for (Edge<T> edge : edges) {
      for (T vertex : verticies) {
        Edge<T> edgeB = new Edge<T>(edge.getDestination(), vertex);
        Edge<T> edgeC = new Edge<T>(edge.getSource(), vertex);

        if (edges.contains(edgeB) && !edges.contains(edgeC)) {
          return false;
        }
      }
    }
    return true;
  }

  public boolean isAntiSymmetric() {

    for (Edge<T> edge : edges) {
      Edge<T> edgeB = new Edge<T>(edge.getDestination(), edge.getSource());

      if (edges.contains(edgeB) && (!edge.equals(edgeB))) {
        return false;
      }
    }

    return true;
  }

  public boolean isEquivalence() {
    if (this.isReflexive() && this.isSymmetric() && this.isTransitive()) {
      return true;
    }
    return false;
  }

  public Set<T> getEquivalenceClass(T vertex) {
    Set<T> equivalenceClass = createSortedSet(verticies);

    for (T currentVertex : verticies) {
      if (!this.isSameEquivalenceClass(currentVertex, vertex)) {
        equivalenceClass.remove(currentVertex);
      }
    }
    return equivalenceClass;
  }

  /**
   * @return
   */
  public List<T> iterativeBreadthFirstSearch() {
    TreeSet<T> roots = createSortedSet(getRoots());

    Queue<T> queue = new Queue<T>();

    List<T> visited = new ArrayList<T>();

    for (T currentRoot : roots) {
      queue.enqueue(currentRoot);
      visited.add(currentRoot);

      while (!queue.isEmpty()) {
        Node<T> currentNode = queue.dequeue();
        Set<T> currentDestinations = this.findAllDestinations(currentNode.getData());

        for (T destination : currentDestinations) {
          if (!visited.contains(destination)) {
            visited.add(destination);
            queue.enqueue(destination);
          }
        }
      }
    }

    return visited;
  }

  public List<T> iterativeDepthFirstSearch() {
    TreeSet<T> roots = createSortedSet(getRoots());
    Stack<T> stack = new Stack<T>();

    // This stack is for reversing the order of things
    Stack<T> holdingStack = new Stack<T>();

    List<T> visited = new ArrayList<T>();

    for (T currentRoot : roots) {
      stack.push(currentRoot);

      while (!stack.isEmpty()) {
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
    }

    return visited;
  }

  public List<T> recursiveBreadthFirstSearch() {
    TreeSet<T> roots = createSortedSet(getRoots());

    Queue<T> queue = new Queue<T>();

    // Initialise visited with the root values
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
      Node<T> currentNode = queue.dequeue();
      Set<T> currentDestinations = this.findAllDestinations(currentNode.getData());
      for (T destination : currentDestinations) {
        if (!visited.contains(destination)) {
          visited.add(destination);
          queue.enqueue(destination);
        }
      }
      visited = recursiveBfsCall(queue, visited);
    }

    return visited;
  }

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
      // Initialise a "holding stack" to help reverse the order of things
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
      holdingStack.unloadStackOnto(stack);
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

  public TreeSet<T> createSortedSet() {
    return new TreeSet<T>(
        new Comparator<T>() {
          @Override
          public int compare(T o1, T o2) {
            return Integer.compare(Integer.parseInt((String) o1), Integer.parseInt((String) o2));
          }
        });
  }

  public TreeSet<T> createSortedSet(Set<T> set) {
    TreeSet<T> sortedSet = createSortedSet();
    sortedSet.addAll(set);
    return sortedSet;
  }
}
