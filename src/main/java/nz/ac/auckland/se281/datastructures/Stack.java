package nz.ac.auckland.se281.datastructures;

/**
 * A stack data structure that stores elements in a last-in, first-out (LIFO) order.
 *
 * @param <T> The type of elements stored in the stack.
 */
public class Stack<T> {
  private Node<T> top;
  private int size;

  /** Constructs an empty stack. */
  public Stack() {
    this.size = 0;
  }

  /**
   * Pushes an element onto the top of the stack.
   *
   * @param data The element to push.
   */
  public void push(T data) {
    Node<T> newNode = new Node<>(data);

    // Set the old top as the 'next' for the newNode
    newNode.setNext(top);
    // Set the new node as top
    top = newNode;

    size++;
  }

  /**
   * Pops the element from the top of the stack.
   *
   * @return The popped element, or {@code null} if the stack is empty.
   */
  public T pop() {
    if (isEmpty()) {
      System.out.println("Stack is empty");
      return null;
    }

    Node<T> temp = top;
    top = top.getNext();
    size--;
    return temp.getData();
  }

  /**
   * Retrieves the element at the top of the stack without removing it.
   *
   * @return The element at the top of the stack, or {@code null} if the stack is empty.
   */
  public T peek() {
    if (isEmpty()) {
      System.out.println("Stack is empty");
      return null;
    }
    return top.getData();
  }

  /**
   * Retrieves the number of elements in the stack.
   *
   * @return The size of the stack.
   */
  public int size() {
    return size;
  }

  /**
   * Checks if the stack is empty.
   *
   * @return {@code true} if the stack is empty, {@code false} otherwise.
   */
  public boolean isEmpty() {
    return size == 0;
  }

  /**
   * Unloads all the elements from this stack onto a receiver stack, reversing their order.
   *
   * @param receiverStack The stack to unload the elements onto.
   */
  public void unloadStackOnto(Stack<T> receiverStack) {
    int sizeOfStackToUnload = this.size;
    for (int i = 0; i < sizeOfStackToUnload; i++) {
      receiverStack.push(this.pop());
    }
  }
}
