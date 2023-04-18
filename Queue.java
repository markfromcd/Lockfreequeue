/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author mph
 */
public interface Queue<T> {

  T deq() throws EmptyException;

  void enq(T x);

}
