package nz.ac.auckland.se281.datastructures;

public class Stack<T> {
  private Node<T> top;
  private int size;

  public Stack() {
    this.size = 0;
  }

  public void push(T data) {
    Node<T> newNode = new Node<>(data);

    // Set the old top as the 'next' for the newNode
    newNode.setNext(top);
    // Set the new node as top
    top = newNode;

    size++;
  }

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

  public T peek() {
    if (isEmpty()) {
      System.out.println("Stack is empty");
      return null;
    }
    return top.getData();
  }

  public int size() {
    return size;
  }

  public boolean isEmpty() {
    return size == 0;
  }

  public void unloadStack(Stack<T> receiverStack) {
    // Unload all the contents of the stack onto a receiver stack
    // This reverses the order
    int sizeOfStackToUnload = this.size;
    for (int i = 0; i < sizeOfStackToUnload; i++) {
      receiverStack.push(this.pop());
    }
  }
}
