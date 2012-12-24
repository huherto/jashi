package jashi;

import static jashi.FileHelper.ftest;
import static jashi.FileHelper.getCurrentDir;
import static jashi.FileHelper.glob;
import static jashi.FileHelper.iterable;
import static jashi.FileHelper.openReader;
import static jashi.FileHelper.openWriter;
import static jashi.FileHelper.readAll;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.junit.Test;


@jashi.Config({
	@jashi.Classpath("."),
	@jashi.Classpath(".."),
	@jashi.Classpath("org.apache.commons:commons-io:2.4")
})
public class TestFileHelper {

	@Test
	public void test() {
		
		assertTrue(true);
		System.out.println("It worked!");
	}

	@Test
	public void testWriter() {
		PrintWriter writer = openWriter("myfile.txt", false);
		for(int i = 0; i < 10; i++) {
			writer.println("i="+i);
		}
		writer.close();
	}
	
	@Test
	public void testReader() throws IOException 
	{
		BufferedReader reader = openReader("myfile.txt");
		String line = reader.readLine();
		while (line != null) {
			System.out.println(line);
			line = reader.readLine();
		}
	}
	
	@Test
	public void testReadAll() {
		List<String> lines = readAll("myfile.txt");
		assertTrue(lines.size() == 10);
	}
	
	@Test
	public void testIterate() {
		
		int count = 0;
		for(String line : iterable("myfile.txt")) {
			count++;
		}	
		assertEquals(10, count);
	}
	
	@Test
	public void testftest() {
		assertTrue(ftest("-r", "myfile.txt"));
		assertTrue(ftest("-w", "myfile.txt"));
		assertFalse(ftest("-x", "myfile.txt"));
		assertTrue(ftest("-f", "myfile.txt"));
		assertFalse(ftest("-d", "myfile.txt"));
	}
	
	@Test
	public void testCurrentDir() {
		// We need to make it independent.
		assertEquals(new File("/home/humberto/workspace/jashi"), getCurrentDir());
	}
	
	public void testGlob() {
		assertEquals("", glob(""));
	}
}
