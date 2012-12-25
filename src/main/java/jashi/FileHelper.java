package jashi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.tools.ant.DirectoryScanner;

/**
 * Help ease some file operations.
 * 
 * Assumptions:
 *   - Filenames use unix separator '/'
 *   - Avoid checked exceptions.
 * 
 * @author humberto
 *
 */
public class FileHelper {
	
	private static File homeDir = null;
	
	public static File toFile(String fname) {
		String[] segments = fname.split("/");
		File[] files = new File[segments.length];
		for(int i = 0; i < segments.length; i++) {
			if (i == 0) {
				if ("~".equals(segments[i])) {
					files[i] = getHomeDir();
				}
				else if ("".equals(segments[i])) {
					files[i] = getRootDir();
				}
				else {
					files[i] = new File(segments[i]);					
				}
			}
			else {
				files[i] = new File(files[i-1], segments[i]);
			}
		}
		
		return files[files.length - 1];	
	}
	
	public static BufferedReader openReader(String fname) {
		return openReader(toFile(fname));
	}
	
	private static BufferedReader openReader(File file) {
		BufferedReader input = null;
		try {
			input = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException ex) {
			throw new RuntimeException(ex);
		}
		return input;
	}

	public static PrintWriter openWriter(String fname, boolean append) {
		return openWriter(toFile(fname), append);
	}
	
	public static PrintWriter openWriter(File file, boolean append) {
	    FileWriter writer = null;
	    PrintWriter printer = null;
	    try {
	        writer = new FileWriter(file, append);
	        printer = new PrintWriter(writer);
	    }
	    catch (IOException e) {	    
	    	throw new RuntimeException(e);
	    }
    	return printer;
	}
	
	public static List<String> readAll(String fname) {
		return readAll(toFile(fname));
	}

	public static List<String> readAll(File file) {
		List<String> result = new ArrayList<String>();
		
		BufferedReader reader = openReader(file);
		String line;
		try {
			line = reader.readLine();
			while(line != null) {
				result.add(line);
				line = reader.readLine();
			}
		}
		catch (IOException e) {
	    	throw new RuntimeException(e);
		}
		return result;
	}
	
	public static boolean ftest(String flag, String fname) {
		return ftest(flag, toFile(fname));
	}
	
	public static boolean ftest(String flag, File file) {
		if (flag == null)
			throw new IllegalArgumentException("flag is null");
		
		if (flag.equals("-r"))
			return file.canRead();
		if (flag.equals("-w"))
			return file.canWrite();
		if (flag.equals("-x"))
			return file.canExecute();
		if (flag.equals("-d"))
			return file.isDirectory();
		if (flag.equals("-f"))
			return file.isFile();
		
		throw new IllegalArgumentException("Unknown flag "+flag);

	}
	
	public static class IterableFileAdapter implements Iterable<String> {

		File file;
		
		IterableFileAdapter(File file) {
			this.file = file;
		}

		@Override
		public Iterator<String> iterator() {
			return new LineIterator(openReader(file));
		}
		
	}
	
	public static IterableFileAdapter iterable(File file) {
		return new IterableFileAdapter(file);
	}
	
	public static IterableFileAdapter iterable(String fname) {
		return iterable(toFile(fname));
	}
	
	private static <T> T[] concat(T[] first, T[] second) {
		T[] result = Arrays.copyOf(first, first.length + second.length);
		System.arraycopy(second, 0, result, first.length, second.length);
		return result;
	}
	
	public static File getCurrentDir() {
		try {
			File dir = new File(".");
			return dir.getCanonicalFile();
		}
		catch(IOException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	public static File getHomeDir() {
		if (homeDir == null) {
			homeDir = new File(System.getProperty("user.home"));
		}
		return homeDir;
	}
	
	private static File getRootDir() {
		// Assume unix
		return File.listRoots()[0];
	}

	public static String[] glob(String glob) {
		
		try {
			DirectoryScanner scanner = new DirectoryScanner();
			scanner.setIncludes(glob.split("\\s"));
			scanner.setCaseSensitive(true);
			scanner.setBasedir((new File(".")).getCanonicalFile());
			scanner.scan();
			return concat(scanner.getIncludedDirectories(), scanner.getIncludedFiles());
		} 
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
