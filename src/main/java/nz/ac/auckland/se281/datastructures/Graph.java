package nz.ac.auckland.se281.datastructures;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    // TODO: why might you use a hashset here:
    Set<T> roots = verticies;

    for (Edge<T> edge : edges) {
      roots.remove(edge.getDestination());
    }

    return roots;

    // TODO: Implement equivalence class thign
  }

  public boolean isReflexive() {
    boolean hasSelfLoop = false;

    for (T vertex : verticies) {
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
      opposite = new Edge(edge.getDestination(), edge.getSource());
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
        Edge<T> edgeB = new Edge(edge.getDestination(), vertex);
        Edge<T> edgeC = new Edge(edge.getSource(), vertex);

        if (edges.contains(edgeB) && !edges.contains(edgeC)) {
          return false;
        }
      }
    }
    return true;
  }

  public boolean isAntiSymmetric() {

    for (Edge<T> edge : edges) {
      Edge<T> edgeB = new Edge(edge.getDestination(), edge.getSource());

      if (edges.contains(edgeB) && edge != edgeB) {
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

  public List<T> iterativeBreadthFirstSearch() {
    // TODO: Task 2.
    throw new UnsupportedOperationException();
  }

  public List<T> iterativeDepthFirstSearch() {
    // TODO: Task 2.
    throw new UnsupportedOperationException();
  }

  public List<T> recursiveBreadthFirstSearch() {
    // TODO: Task 3.
    throw new UnsupportedOperationException();
  }

  public List<T> recursiveDepthFirstSearch() {
    // TODO: Task 3.
    throw new UnsupportedOperationException();
  }

  private boolean isSameEquivalenceClass(T currentVertex, T vertex) {
    // TODO: Is this correct? Does it have to be an equivalence relation???\
    // TODO: I can probably make this simpler

    if (!this.isEquivalence()) {
      return false;
    }

    Edge<T> edgeA = new Edge(currentVertex, vertex);
    Edge<T> edgeB = new Edge(vertex, currentVertex);
    if (!edges.contains(edgeA) || !edges.contains(edgeB)) {
      return false;
    }
    return true;
  }
}
