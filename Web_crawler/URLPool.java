import java.util.LinkedList;
/**
 * This class stores two linked lists for URLDepthPair, one stores
 * pending URLs and the other one stores processed URLs. This class
 * also provides synchronized get and put method for threads to
 * get pending URLs to process and store processed URLs. It also 
 * keeps track of waiting threads for main method to determine 
 * when to stop the whole program.
 * 
 * @author BOTAO
 *
 */
public class URLPool {
	public LinkedList<URLDepthPair> pendingURLs;
	public LinkedList<URLDepthPair> processedURLs;
	public int maxDepth;
	public int waitingThreads;
	
	public URLPool(URLDepthPair pair, int maxDepth) {
		pendingURLs = new LinkedList<URLDepthPair>();
		processedURLs = new LinkedList<URLDepthPair>();
		pendingURLs.add(pair);
		processedURLs.add(pair);
		this.maxDepth = maxDepth;
		this.waitingThreads = 0;
	}
	
	/**
	 * This synchronized method allows threads to get the first
	 * URLDepthPair from the pending queue. If the queue is 
	 * empty, it forces thread to wait. Meanwhile, it keeps
	 * track of the number of waiting threads.
	 */
	public synchronized URLDepthPair get() throws InterruptedException {
		 while (pendingURLs.size() == 0) {
			 waitingThreads++;
			 wait();
			 waitingThreads--;
		 }
		 return pendingURLs.removeFirst();
	}
	
	/**
	 * This synchronized method allows threads to put newly-met
	 * URLDepthPair into the processed list. If a pair whose
	 * depth is lower than maxDepth is added, it will notify
	 * all threads to come and process this pair.
	 */
	public synchronized void put(URLDepthPair pair) {
		processedURLs.addLast(pair);
		if (pair.depth < maxDepth) {
			pendingURLs.addLast(pair);
			notify();
		}
	}
	
	// returns the number of waiting threads
	public synchronized int getWaitCount() {
		return waitingThreads;
	}
	
	// print all items in processedURLs list
	public void print() {
		while (!processedURLs.isEmpty()) {
			System.out.println(processedURLs.removeFirst().toString());
		}
	}
}
