public class TestQueues {

	public static void main(String args[]) throws InterruptedException {
		int numThr = 10;
		if (args.length > 0) {
			numThr = Integer.parseInt(args[0]);
		}

		Thread threads[] = new Thread[numThr];
		// Create Lists
		Queue<Integer> queue = new LockFreeQueue<Integer>();
		// Create Tester for each list 
		QueueTester tester = new QueueTester(numThr, queue);
		System.out.println("starting test");
		for (int i = 0; i < numThr; i++) { threads[i] = new Thread(tester); }
		for (int i = 0; i < numThr; i++) { threads[i].start(); }
		try {
			for (int i = 0; i < numThr; i++) { threads[i].join(); }
		} catch (InterruptedException ex) {
			System.out.println(ex.getMessage());
		}
	}
}
