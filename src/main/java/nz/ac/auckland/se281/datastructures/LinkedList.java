package nz.ac.auckland.se281.datastructures;

public class LinkedList<T> {
  private int size;
  private Node<T> head;
  private Node<T> tail;

  public LinkedList() {
    size = 0;
  }

  public void add(T data) {
    Node<T> newNode = new Node<>(data);

    if (size == 0) {
      head = newNode;
      tail = newNode;
    } else {
      tail.setNext(newNode);
      newNode.setPrev(tail);
      tail = newNode;
    }
    size++;
  }

  public T get(int index) {
    Node<T> currentNode = head;
    for (int i = 1; i <= index; i++) {
      currentNode = currentNode.getNext();
    }

    return currentNode.getData();
  }

  public void insert(int index, T data) {
    Node<T> nodeToInsert = new Node<T>(data);

    Node<T> currentNode = head;
    for (int i = 1; i <= index; i++) {
      currentNode = currentNode.getNext();
    }

    Node<T> nodeBefore = currentNode.getPrev();
    Node<T> nodeAfter = currentNode;

    nodeBefore.setNext(nodeToInsert);
    nodeToInsert.setPrev(nodeBefore);
    nodeToInsert.setNext(nodeAfter);
    nodeAfter.setPrev(nodeToInsert);
    size++;
  }

  public void remove(int index) {
    if (index == 0) {
      head = head.getNext();
      size--;
      return;
    }
    if (index == size - 1) {
      tail = tail.getPrev();
      size--;
      return;
    }

    Node<T> nodeToRemove = head;
    for (int i = 1; i <= index; i++) {
      nodeToRemove = nodeToRemove.getNext();
    }
    // Find the node before and after the node we want to remove
    // Link them together
    Node<T> nodeBefore = nodeToRemove.getPrev();
    Node<T> nodeAfter = nodeToRemove.getNext();

    nodeBefore.setNext(nodeAfter);
    nodeAfter.setPrev(nodeBefore);
    size--;
  }

  public int size() {
    return size;
  }

  public boolean isEmpty() {
    return size == 0;
  }

  public int indexOf(T data) {
    if (isEmpty()) {
      System.out.println("The list is empty");
      return -1;
    }
    Node<T> currentNode = head;
    for (int i = 0; i < size; i++) {
      if ((int) currentNode.getData() == (int) data) {
        return i;
      }
      currentNode = currentNode.getNext();
    }

    System.out.println("The number " + data + " does not appear in the list.");
    return -1;
  }

  @Override
  public String toString() {
    if (size == 0) {
      return "[]";
    }

    StringBuilder sb = new StringBuilder();
    sb.append("[" + head.getData());

    Node<T> temp = head;

    for (int i = 1; i < size; i++) {
      temp = temp.getNext();
      int currentNumber = (int) temp.getData();
      sb.append(", " + currentNumber);
    }

    sb.append("]");

    return sb.toString();
  }
}
