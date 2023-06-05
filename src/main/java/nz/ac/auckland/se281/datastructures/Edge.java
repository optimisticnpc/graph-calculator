package nz.ac.auckland.se281.datastructures;

import java.util.Objects;

/**
 * An edge in a graph that connects two verticies.
 *
 * <p>You must NOT change the signature of the constructor of this class.
 *
 * @param <T> The type of each vertex.
 */
public class Edge<T> {
  private T source;
  private T destination;

  /**
   * Constructs an edge between the specified source and destination vertices.
   *
   * @param source The source vertex.
   * @param destination The destination vertex.
   */
  public Edge(T source, T destination) {
    this.source = source;
    this.destination = destination;
  }

  /**
   * Retrieves the source vertex of the edge.
   *
   * @return The source vertex.
   */
  public T getSource() {
    return source;
  }

  /**
   * Retrieves the destination vertex of the edge.
   *
   * @return The destination vertex.
   */
  public T getDestination() {
    return destination;
  }

  /**
   * Computes the hash code of the edge.
   *
   * @return The hash code of the edge.
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((source == null) ? 0 : source.hashCode());
    result = prime * result + ((destination == null) ? 0 : destination.hashCode());
    return result;
  }

  /**
   * Checks if the edge is equal to another object.
   *
   * @param obj The object to compare.
   * @return {@code true} if the edge is equal to the object, {@code false} otherwise.
   */
  @Override
  public boolean equals(Object obj) {
    // If the object is compared with itself then return true
    if (this == obj) {
      return true;
    }

    if (!(obj instanceof Edge)) {
      return false;
    }

    // Typecast obj to Edge so that we can compare data members
    @SuppressWarnings("unchecked")
    Edge<T> other = (Edge<T>) obj;

    // Objects.equals() checks for nulls
    return Objects.equals(source, other.source) && Objects.equals(destination, other.destination);
  }
}
