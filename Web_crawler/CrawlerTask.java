import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * This class implements runnable interface and specifies the task
 * of threads. 
 * @author BOTAO
 *
 */
public class CrawlerTask implements Runnable {
	public static final String LINK_PREFIX = "a href=\"";
	public URLPool pool;
	
	public CrawlerTask(URLPool pool) {
		this.pool = pool;
	}
	
	/**
	 * This method takes the next URLDepthPair in the queue, and
	 * asks free threads to process it.
	 */
	public void run() {
		while (true) {
			try {
				URLDepthPair nextpair = pool.get();
				unitWork(nextpair);
			}
			catch (InterruptedException e) {
				System.out.println("Caught unexpected " +
						"InterruptedException, ignoring...");
				continue;
			}
			// handles UnknownHostException in socket constructor
			catch (UnknownHostException e) {
				System.out.println("Caught unexpected " +
						"UnknownHostException, ignoring...");
				continue;
			}
			// handles other IOExceptions
			catch (IOException e) {
				System.out.println("Caught unexpected other" +
						"IOException, ignoring...");
				continue;
			}
		}
	}
	
	/**
	 * This function reads in a URLDepthPair, and process that pair by connecting
	 * to the host server, receiving its response and extract deeper URL information
	 * in that response. Finally, this function puts the valid pairs into the URL pool.
	 * @param pair
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public void unitWork(URLDepthPair pair) throws UnknownHostException, IOException {
		// initiates new socket instance and tries to connect remote server
		String webServer = pair.getWebHost();
		int webPort = 80;
		Socket socket = new Socket(webServer, webPort);
		socket.setSoTimeout(3000);
		
		// sends the HTTP request
		OutputStream os = socket.getOutputStream();
		// true tells PrintWriter to flush after every output
		PrintWriter writer = new PrintWriter(os, true);
		writer.println("GET " + pair.getDocPath() + " HTTP/1.1");
		writer.println("Host: " + pair.getWebHost());
		writer.println("Connection: close");
		writer.println();
		
		// reads back HTTP response
		InputStreamReader isr = new InputStreamReader(socket.getInputStream());
		BufferedReader br = new BufferedReader(isr);
		while (true) {
			// reads in each line
			String line = br.readLine();
			if (line == null) break;
			int idx = 0;
			while (true) {
				// for each line, searches for "a href=\"" and read in URLs
				idx = line.indexOf(LINK_PREFIX, idx);
				if (idx == -1) break;
				int start = idx + LINK_PREFIX.length();
				int end = line.indexOf('"', start);
				// URL storage: only stores valid URLs into the pool
				if (end == -1) break;
				String newURL = line.substring(start, end);
				int newDepth = pair.depth + 1;
				URLDepthPair newPair = new URLDepthPair(newURL, newDepth);
				if (newPair.isValid()) {
					pool.put(newPair);
				}
				idx = end + 1;
			}
		}
		
		// put processed URLDepthPair into processed list
		socket.close();
	}
}
