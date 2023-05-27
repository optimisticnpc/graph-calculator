package nz.ac.auckland.se281.datastructures;

import java.util.ArrayList;
import java.util.List;

public class Queue<T> {
    List<T> queueOfObjects;
    public Queue() {
        queueOfObjects = new ArrayList<T>();
    }

    public void enqueue(T data) {
        queueOfObjects.add(data);
    }
    
    public T dequeue() {
        T temp = queueOfObjects.get(0);
        queueOfObjects.remove(0);
        return temp;
    }

    public T peek() {
        return queueOfObjects.get(0);
    }

    public boolean isEmpty() {
        if (queueOfObjects.size() == 0) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return queueOfObjects.toString();
    }

    
}