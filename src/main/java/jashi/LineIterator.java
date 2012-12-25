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

	@Override
	public boolean hasNext() {
		return next != null;
	}

	@Override
	public String next() {
		String sav = next;
		next = readLine();
		return sav;
	}

	@Override
	public void remove() {
		throw new RuntimeException("Operation not allowed");
	}		
}