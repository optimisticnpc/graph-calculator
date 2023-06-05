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

  public Edge(T source, T destination) {
    this.source = source;
    this.destination = destination;
  }

  public T getSource() {
    return source;
  }

  public T getDestination() {
    return destination;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((source == null) ? 0 : source.hashCode());
    result = prime * result + ((destination == null) ? 0 : destination.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    // If the object is compared with itself then return true
    if (this == obj) {
      return true;
    }

    if (!(obj instanceof Edge)) {
      return false;
    }

    // typecast obj to Edge so that we can compare data members
    @SuppressWarnings("unchecked")
    Edge<T> other = (Edge<T>) obj;

    // Objects.equals() checks for nulls
    return Objects.equals(source, other.source) && Objects.equals(destination, other.destination);
  }
}
