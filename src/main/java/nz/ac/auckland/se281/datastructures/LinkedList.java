package nz.ac.auckland.se281.datastructures;

// TODO: IF I DONT USE THE LINKED LIST, remove it

/**
 * A linked list data structure that stores elements by linking each element to the elements in
 * front and behind it.
 *
 * @param <T> The type of elements stored in the linked list.
 */
public class LinkedList<T> {
  private int size;
  private Node<T> head;
  private Node<T> tail;

  /** Constructs an empty linked list. */
  public LinkedList() {
    size = 0;
  }

  /**
   * Adds an element to the end of the linked list.
   *
   * @param data The element to add.
   */
  public void add(T data) {
    Node<T> newNode = new Node<>(data);

    if (size == 0) {
      // If the linked list is empty, set the new node as both head and tail
      head = newNode;
      tail = newNode;
    } else {
      // If there are already nodes in the linked list
      // Attach the new node to the rear of the list
      tail.setNext(newNode);
      newNode.setPrev(tail);
      tail = newNode;
    }
    size++;
  }

  /**
   * Retrieves the element at the specified index in the linked list.
   *
   * @param index The index of the element to retrieve.
   * @return The element at the specified index.
   */
  public T get(int index) {
    Node<T> currentNode = head;
    for (int i = 1; i <= index; i++) {
      currentNode = currentNode.getNext();
    }

    return currentNode.getData();
  }

  /**
   * Inserts an element at the specified index in the linked list.
   *
   * @param index The index at which to insert the element.
   * @param data The element to insert.
   */
  public void insert(int index, T data) {
    Node<T> nodeToInsert = new Node<T>(data);

    Node<T> currentNode = head;
    for (int i = 1; i <= index; i++) {
      currentNode = currentNode.getNext();
    }

    Node<T> nodeBefore = currentNode.getPrev();
    Node<T> nodeAfter = currentNode;

    // Link the new node to the nodes before and after it
    nodeBefore.setNext(nodeToInsert);
    nodeToInsert.setPrev(nodeBefore);
    nodeToInsert.setNext(nodeAfter);
    nodeAfter.setPrev(nodeToInsert);
    size++;
  }

  /**
   * Removes the element at the specified index from the linked list.
   *
   * @param index The index of the element to remove.
   */
  public void remove(int index) {
    if (index == 0) {
      // If removing the first node, update the head
      head = head.getNext();
      size--;
      return;
    }
    if (index == size - 1) {
      // If removing the last node, update the tail
      tail = tail.getPrev();
      size--;
      return;
    }

    Node<T> nodeToRemove = head;
    for (int i = 1; i <= index; i++) {
      nodeToRemove = nodeToRemove.getNext();
    }
    // Find the node before and after the node we want to remove
    Node<T> nodeBefore = nodeToRemove.getPrev();
    Node<T> nodeAfter = nodeToRemove.getNext();

    // Link the nodes before and after the removed node together
    nodeBefore.setNext(nodeAfter);
    nodeAfter.setPrev(nodeBefore);
    size--;
  }

  /**
   * Retrieves the number of elements in the linked list.
   *
   * @return The size of the linked list.
   */
  public int size() {
    return size;
  }

  /**
   * Checks if the linked list is empty.
   *
   * @return {@code true} if the linked list is empty, {@code false} otherwise.
   */
  public boolean isEmpty() {
    return size == 0;
  }

  /**
   * Returns the index of the first occurrence of the specified element in the linked list.
   *
   * @param data The element to search for.
   * @return The index of the first occurrence of the element, or -1 if the element is not found.
   */
  public int indexOf(T data) {
    if (isEmpty()) {
      System.out.println("The list is empty");
      return -1;
    }
    Node<T> currentNode = head;
    for (int i = 0; i < size; i++) {
      if (currentNode.getData().equals(data)) {
        return i;
      }
      currentNode = currentNode.getNext();
    }

    System.out.println("The element " + data + " does not appear in the list.");
    return -1;
  }

  /**
   * Returns a string representation of the linked list.
   *
   * @return A string representation of the linked list.
   */
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
      sb.append(", " + temp.getData());
    }

    sb.append("]");

    return sb.toString();
  }
}
