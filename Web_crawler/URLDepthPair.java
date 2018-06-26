/**
 * This class stores information about current URL and current depth
 * the web crawler is visiting, together with some minor methods that
 * extracts information from the URL
 * @author BOTAO
 *
 */
public class URLDepthPair {
	public static final String URL_PREFIX = "http://";
	
	public String URL;
	public int depth;

	public URLDepthPair(String URL, int depth) {
		this.URL = URL;
		this.depth = depth;
	}
	
	// puts URL and depth into String to print
	public String toString() {
		return "URL: " + URL + ", Depth: " + depth;
	}
	
	// checks the URL is valid or not by checking the prefix is "http://" or not;
	// returns true if the URL is valid
	public boolean isValid() {
		if (URL.indexOf(URL_PREFIX) == 0) return true;
		else return false;
	}
	
	// returns the DocPath part of the URL as a String, if there is no DocPath, returns '/'
	public String getDocPath() {
		int start = URL_PREFIX.length();
		int end = URL.indexOf('/', start);
		if (end == -1) return "/";
		else return URL.substring(end);
	}
	
	// returns the WebHost part of the URL as a String
	public String getWebHost() {
		int start = URL_PREFIX.length();
		int end = URL.indexOf('/', start);
		if (end == -1) return URL.substring(start);
		else return URL.substring(start, end);
	}
}
