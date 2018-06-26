/**
 * This class is the web crawler itself. A crawler instance contains max depth,
 * number of threads under control, and a URL pool to store URLDepthPairs.
 * For comments of main method, see below.
 */
public class WebCrawler {
	public int maxDepth;
	public int numThreads;
	public URLPool pool;
	public WebCrawler(URLDepthPair pair, int maxDepth, int numThreads) {
		pool = new URLPool(pair, maxDepth);
		this.maxDepth = maxDepth;
		this.numThreads = numThreads;
	}
	
	/**
	 * In the main method, we take in three command line arguments: URL, max depth, 
	 * and number of threads. Max depth is the deepest a crawler can reach, and 
	 * the URL is the starting point for crawler. The method then creates a WebCrawler
	 * instance, and initialize the URLPool of this instance by storing the starting
	 * URLDepthPair. After that, it creates several thread instance of CrawlerTask,
	 * and ask them to do the job specified in CrawlerTask. Meanwhile, the main method
	 * keeps track of waiting threads. If all the threads are waiting, meaning the job is
	 * done, it closes the system.
	 * @param args
	 */
	public static void main(String[] args) {
		// reads in command line arguments, prints error if arguments are illegal
		int maxDepth = 0;
		int numThreads = 0;
		if (args.length != 3) {
			System.out.println("usage: java Crawler <URL> <depth> <number of threads>");
            System.exit(1);
		}
		String URL = args[0];
		try {
			maxDepth = Integer.parseInt(args[1]);
			if (maxDepth < 0) {
				System.out.println("input error: depth should not be negative");
	            System.exit(1);
			}
			numThreads = Integer.parseInt(args[2]);
			if (maxDepth <= 0) {
				System.out.println("input error: number of threads should be positive");
	            System.exit(1);
			}
		}
		catch (NumberFormatException nfe) {
			System.out.println("usage: java Crawler <URL> <depth> <number of threads>");
            System.exit(1);
		}
		
		// initiates WebCrawler instance, set up pool
		URLDepthPair firstPair = new URLDepthPair(URL, 0);
		WebCrawler crawler = new WebCrawler(firstPair, maxDepth, numThreads);
		
		// start threads
		for (int i = 0; i < numThreads; i++) {
			CrawlerTask c = new CrawlerTask(crawler.pool);
			Thread t = new Thread(c); 
			t.start();
		}
		
		// monitor pool: if everyone is waiting, then job is done
		while (crawler.pool.getWaitCount() != crawler.numThreads) {
			 try {
			 Thread.sleep(100); // 0.1 second
			 } catch (InterruptedException ie) {
			 System.out.println("Caught unexpected " +
					 "InterruptedException, ignoring...");
			 }
		}
		
		// job is done: prints out all processed URL and their depths
		crawler.pool.print();
		System.exit(0);
	}
}
