package nz.ac.auckland.se281.datastructures;

public class Queue<T> {
  private Node<T> front;
  private Node<T> rear;
  private int size;

  public Queue() {
    this.size = 0;
  }

  public void enqueue(T data) {
    Node<T> newNode = new Node<>(data);

    // TODO: Test if removing this gives me an excpetion
    // If there are no nodes in the queue, set the node as front
    if (size == 0) {
      front = newNode;
    } else {
      // If there are already nodes in the queue
      // Join the new node to the rear by setting next and prev
      newNode.setNext(rear);
      rear.setPrev(newNode);
    }
    // Set the new node as rear
    rear = newNode;
    size++;
  }

  public Node<T> dequeue() {
    if (isEmpty()) {
      System.out.println("Queue is empty");
      return null;
    }
    Node<T> temp = front;
    // If there is one node, return that node
    if (size == 1) {
      front = null;
      rear = null;
      size--;
      return temp;
    }

    front = temp.getPrev();
    front.setNext(null);
    size--;
    return temp;
  }

  public T peek() {
    return front.getData();
  }

  public boolean isEmpty() {
    return (size == 0) ? true : false;
  }

  public int getSize() {
    return size;
  }

  @Override
  public String toString() {
    if (size == 0) {
      return "[]";
    }

    StringBuilder sb = new StringBuilder();
    sb.append("[" + front.getData());

    Node<T> temp = front;

    for (int i = 1; i < size; i++) {
      temp = temp.getPrev();
      int currentNumber = (int) temp.getData();
      sb.append(", " + currentNumber);
    }

    sb.append("]");

    return sb.toString();
  }
}
