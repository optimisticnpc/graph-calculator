package nz.ac.auckland.se281.datastructures;

/**
 * A node in a linked data structure.
 *
 * @param <T> The type of data stored in the node.
 */
public class Node<T> {
  private T data;
  private Node<T> next;
  private Node<T> prev;

  /**
   * Constructs a node with the specified data.
   *
   * @param data The data to store in the node.
   */
  public Node(T data) {
    this.data = data;
  }

  /**
   * Retrieves the data stored in the node.
   *
   * @return The data stored in the node.
   */
  public T getData() {
    return data;
  }

  /**
   * Sets the data to be stored in the node.
   *
   * @param data The data to store in the node.
   */
  public void setData(T data) {
    this.data = data;
  }

  /**
   * Retrieves the next node in the linked structure.
   *
   * @return The next node.
   */
  public Node<T> getNext() {
    return next;
  }

  /**
   * Sets the next node in the linked structure.
   *
   * @param next The next node.
   */
  public void setNext(Node<T> next) {
    this.next = next;
  }

  /**
   * Retrieves the previous node in the linked structure.
   *
   * @return The previous node.
   */
  public Node<T> getPrev() {
    return prev;
  }

  /**
   * Sets the previous node in the linked structure.
   *
   * @param prev The previous node.
   */
  public void setPrev(Node<T> prev) {
    this.prev = prev;
  }
}
