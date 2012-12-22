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


public class FileHelper {
	
	public static BufferedReader openReader(String fname) {
		return openReader(new File(fname));
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
		return openWriter(new File(fname), append);
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
		return readAll(new File(fname));
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
		return ftest(flag, new File(fname));
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
		return iterable(new File(fname));
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
	
	public static String[] glob(String glob) {
		
		try {
			DirectoryScanner scanner = new DirectoryScanner();
			scanner.setIncludes(glob.split("\\s"));
			scanner.setCaseSensitive(true);
			scanner.setBasedir((new File(".")).getCanonicalFile());
			scanner.scan();
			return concat(scanner.getIncludedDirectories(), scanner.getIncludedFiles());

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
