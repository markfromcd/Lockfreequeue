import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.Random;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;

public class QueueTester implements Runnable {
	final private int numThr;
	final private Queue<Integer> queue;
	final private CyclicBarrier interimBarr;
	final private CyclicBarrier printResBarr;
	final private Random rand = new Random();
	Lock lock;
	AtomicIntegerArray testVals = new AtomicIntegerArray(10);
	int lastVal = 0;

	QueueTester(int _numThr, Queue<Integer> _queue) {
		numThr = _numThr;
		queue = _queue;
		lock = new ReentrantLock();

		Runnable interimRun = new Runnable() {
			public void run() {
				System.out.println("Next test...");
			}
		};
		Runnable printResRun = new Runnable() {
			public void run() {
				System.out.println("Tests complete!");
			}
		};

		interimBarr = new CyclicBarrier(numThr, interimRun);
		printResBarr = new CyclicBarrier(numThr, printResRun);
	}

	/**
	 * Test to see if sentinels are in the list, and that removing items
	 * not in the list works. Be sure to write additional tests as well!
	 * NOTE: This means your implementation must have sentinel nodes,
	 * with minimum and maximum values at the beginning and end respectively.
	 */
	public void run() {
		// Child threads start here to begin testing-- tests are below
	
		// We suggest writing a series of zero-sum tests,
		// i.e. lists should be empty between tests. 
		// The interimBarr enforces this.
		try {
			enq100();
			deq100();
			interimBarr.await();
			allZeros();
			interimBarr.await();
			getLast();
			interimBarr.await();
			testLast();
			interimBarr.await();
			printResBarr.await();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	private void enq100(){
		for (int i = 0; i<100; i++){
			int myRandInt = rand.nextInt(5);
			testVals.getAndIncrement(myRandInt);
			queue.enq(myRandInt);
		}
	}
	
	private void deq100(){
		for (int i = 0; i<100; i++){
			try{
				testVals.getAndDecrement(queue.deq());
			}
			catch(Exception e){
				assert (false) : "tried deq while empty";
			}
		}
	}

	private void allZeros(){
		for (int i = 0; i < 10; i++){
			assert (testVals.get(i) == 0) : "not all enq were met with a deq";
		}
	}

	private void getLast(){
		for (int i = 0; i < 30; i++){
			queue.enq(10);
		}
		queue.enq(20);
		for (int i = 0; i < 31; i++){
			lock.lock();
			try{
				lastVal = queue.deq();
			}
			catch (Exception e){
				assert (false) : "tried deq while empty";
			}
			finally{
				lock.unlock();
			}
		}
	}
	private void testLast(){
		assert (lastVal == 20) : "your program is not linearizeable";
	}


}
	
