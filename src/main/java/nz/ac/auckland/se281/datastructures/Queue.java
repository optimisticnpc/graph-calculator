package nz.ac.auckland.se281.datastructures;

/**
 * A queue data structure that stores elements in a first-in, first-out (FIFO) order.
 *
 * @param <T> The type of elements stored in the queue.
 */
public class Queue<T> {
  private Node<T> front;
  private Node<T> rear;
  private int size;

  /** Constructs an empty queue. */
  public Queue() {
    this.size = 0;
  }

  /**
   * Adds an element to the rear of the queue.
   *
   * @param data The element to enqueue.
   */
  public void enqueue(T data) {
    Node<T> newNode = new Node<>(data);

    // If there are no nodes in the queue, set the node as front
    if (size == 0) {
      front = newNode;
    } else {
      // If there are already nodes in the queue
      // Attach the new node to the rear of the queue
      rear.setNext(newNode);
    }
    // Set the new node as rear
    rear = newNode;
    size++;
  }

  /**
   * Removes and returns the element at the front of the queue.
   *
   * @return The element at the front of the queue, or {@code null} if the queue is empty.
   */
  public Node<T> dequeue() {
    if (isEmpty()) {
      System.out.println("Queue is empty");
      return null;
    }
    Node<T> temp = front;
    // If there is one node, set front and rear to null
    if (size == 1) {
      front = null;
      rear = null;
    }

    front = temp.getNext();
    size--;
    return temp;
  }

  /**
   * Retrieves the element at the front of the queue without removing it.
   *
   * @return The element at the front of the queue, or {@code null} if the queue is empty.
   */
  public T peek() {
    return front.getData();
  }

  /**
   * Checks if the queue is empty.
   *
   * @return {@code true} if the queue is empty, {@code false} otherwise.
   */
  public boolean isEmpty() {
    return size == 0;
  }

  /**
   * Retrieves the number of elements in the queue.
   *
   * @return The size of the queue.
   */
  public int getSize() {
    return size;
  }

  /**
   * Returns a string representation of the queue.
   *
   * @return A string representation of the queue.
   */
  @Override
  public String toString() {
    if (size == 0) {
      return "[]";
    }

    StringBuilder sb = new StringBuilder();
    sb.append("[" + front.getData());

    Node<T> temp = front;

    // Iterate through the elements in the queue
    for (int i = 1; i < size; i++) {
      temp = temp.getNext();
      sb.append(", " + temp.getData());
    }

    sb.append("]");

    return sb.toString();
  }
}
