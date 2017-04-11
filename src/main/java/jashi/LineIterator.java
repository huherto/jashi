package jashi;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Iterator;

class LineIterator implements Iterator<String> {
	
	BufferedReader reader;
	
	String next = null;
	
	public LineIterator(BufferedReader reader) {
		this.reader = reader;
		this.next = readLine();
	}
	
	private String readLine() {
		try {
			return reader.readLine();
		}
		catch(IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	public boolean hasNext() {
		return next != null;
	}

	public String next() {
		String sav = next;
		next = readLine();
		return sav;
	}

	public void remove() {
		throw new RuntimeException("Operation not allowed");
	}		
}