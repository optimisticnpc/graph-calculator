package nz.ac.auckland.se281.datastructures;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
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
  private HashMap<T, LinkedList<Edge<T>>> adjacencyMap;

  public Graph(Set<T> verticies, Set<Edge<T>> edges) {
    adjacencyMap = new HashMap<T, LinkedList<Edge<T>>>();

    for (T vertex : verticies) {
      LinkedList<Edge<T>> list = new LinkedList<>();
      for (Edge<T> edge : edges) {
        if (edge.getSource().equals(vertex)) {
          list.add(edge);
        }
      }
      adjacencyMap.put(vertex, list);
    }

    this.verticies = verticies;
    this.edges = edges;
  }

  public Set<T> getRoots() {
    Set<T> roots = new HashSet<T>(verticies);

    // Remove vertices that don't have an in-degree of 0
    for (Edge<T> edge : edges) {
      roots.remove(edge.getDestination());
      // TODO: Implement checking for minimum of 1 out-degree
      // Do we even need to??
    }

    if (this.isEquivalence()) {
      for (T vertex : verticies) {
        Set<T> currentEquivalenceClass = getEquivalenceClass(vertex);
        T currentMinNumber = this.getMinimum(currentEquivalenceClass);
        roots.add(currentMinNumber);
      }
    }

    // Transform HashSet to TreeSet to sort elements numerically
    Set<T> sortedRoots =
        new TreeSet<T>(
            new Comparator<T>() {
              // Create an anonymous class to override the compare() method
              @Override
              public int compare(T o1, T o2) {
                return Integer.compare(
                    Integer.parseInt((String) o1), Integer.parseInt((String) o2));
              }
            });
    sortedRoots.addAll(roots);

    return sortedRoots;
  }

  public boolean isReflexive() {

    for (T vertex : verticies) {
      // Initialise
      boolean hasSelfLoop = false;

      for (Edge<T> edge : edges) {
        if (edge.getSource() == edge.getDestination() && edge.getSource() == vertex) {
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

      if (edges.contains(edgeB) && (edge != edgeB)) {
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
    Set<T> equivalenceClass = new HashSet<>(verticies);

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
    Set<T> roots = new TreeSet<T>(this.getRoots());
    // TODO: do I need to make the comparator always apply

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
    Set<T> roots = new HashSet<T>(this.getRoots());
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
    Set<T> roots = new TreeSet<T>(this.getRoots());

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
    Set<T> roots = new TreeSet<T>(this.getRoots());
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
      if (currentMinT == null || number.compareTo(currentMinT) < 0) {
        currentMinT = number;
      }
    }
    return currentMinT;
  }

  // TODO: Fix this!! Numerical order instead of lexigraphical

  // private T getMinimum(Set<T> numbers) {
  //   // TODO: Is this bad code
  //   int currentMinNumber = Integer.MAX_VALUE;
  //   T currentMinT = null;

  //   for (T number : numbers) {
  //     if ((int) number < currentMinNumber) {
  //       currentMinNumber = (int) number;
  //       currentMinT = number;
  //     }
  //   }
  //   return currentMinT;
  // }

  // TODO: Why can't this be static
  private Set<T> findAllDestinations(T vertex) {
    Set<T> destinations = new TreeSet<T>();

    // TODO: How does == work with T things
    for (Edge<T> edge : edges) {
      if (vertex == edge.getSource()) {
        destinations.add(edge.getDestination());
      }
    }

    return destinations;
  }
}
