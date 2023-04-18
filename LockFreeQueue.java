
import java.util.concurrent.atomic.AtomicReference;

public class LockFreeQueue<T> implements Queue<T> {

  private AtomicReference<Node> head;
  private AtomicReference<Node> tail;

  /**
   * Create a new object of this class.
   */
  public LockFreeQueue() {
    Node sentinel = new Node(null);
    head = new AtomicReference<Node>(sentinel);
    tail = new AtomicReference<Node>(sentinel);
  }

  /**
   * Enqueue an item.
   * @param value Item to enqueue.
   */
  public void enq(T value) {
    //TODO
    Node node = new Node(value);
    while(true){
      Node last = tail.get();
      Node next = last.next.get();
      if(last == tail.get()){
        if(next == null){
          if(last.next.compareAndSet(next,node)){
            tail.compareAndSet(last,node);
            return;
          }
        }
        else{
          tail.compareAndSet(last,next);
        }
      }
    }
  }

  /**
   * Dequeue an item.
   * @throws queue.EmptyException The queue is empty.
   * @return Item at the head of the queue.
   */
  public T deq() throws EmptyException {
    //TODO
    while(true){
      Node first = head.get();
      Node last = tail.get();
      Node next = first.next.get();
      if(first == head.get()){
        if(first == last){
          if(next == null){
            throw new EmptyException();
          }
          tail.compareAndSet(last,next);
        }
        else{
          T value = next.value;
          if(head.compareAndSet(first,next)){
            return value;
          }
        }
      }
    }
  }

  /**
   * Items are kept in a list of nodes.
   */
  public class Node {

    /**
     * Item kept by this node.
     */
    public T value;
    /**
     * Next node in the queue.
     */
    public AtomicReference<Node> next;

    /**
     * Create a new node.
     * @param value the value stored in this node
     */
    public Node(T value) {
      this.next = new AtomicReference<Node>(null);
      this.value = value;
    }
  }
}
