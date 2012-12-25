package jashi;

import static jashi.FileHelper.ftest;
import static jashi.FileHelper.getCurrentDir;
import static jashi.FileHelper.glob;
import static jashi.FileHelper.iterable;
import static jashi.FileHelper.openReader;
import static jashi.FileHelper.openWriter;
import static jashi.FileHelper.readAll;
import static jashi.FileHelper.toFile;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.junit.Test;

public class TestFileHelper {

	@Test
	public void test() {
		
		assertTrue(true);
		System.out.println("It worked!");
	}

	@Test
	public void testWriter() {
		PrintWriter writer = openWriter("target/myfile.txt", false);
		for(int i = 0; i < 10; i++) {
			writer.println("i="+i);
		}
		writer.close();
	}
	
	@Test
	public void testReader() throws IOException 
	{
		BufferedReader reader = openReader("target/myfile.txt");
		String line = reader.readLine();
		while (line != null) {
			System.out.println(line);
			line = reader.readLine();
		}
	}
	
	@Test
	public void testReadAll() {
		List<String> lines = readAll("target/myfile.txt");
		assertTrue(lines.size() == 10);
	}
	
	@Test
	public void testIterate() {
		
		int count = 0;
		for(String line : iterable("target/myfile.txt")) {
			count++;
		}	
		assertEquals(10, count);
	}
	
	@Test
	public void testftest() {
		assertTrue(ftest("-r", "target/myfile.txt"));
		assertTrue(ftest("-w", "target/myfile.txt"));
		assertFalse(ftest("-x", "target/myfile.txt"));
		assertTrue(ftest("-f", "target/myfile.txt"));
		assertFalse(ftest("-d", "target/myfile.txt"));
	}
	
	@Test
	public void testCurrentDir() {
		// We need to make it independent.
		assertEquals(new File("/home/humberto/workspace/jashi"), getCurrentDir());
	}
	
	public void testGlob() {
		assertEquals("", glob(""));
	}
	
	@Test
	public void testToFile() {
		
		assertEquals("/home/humberto",toFile("~").toString());
		assertEquals("/tmp",toFile("/tmp").toString());
		assertEquals("/home/humberto", toFile("/home/humberto").toString());
		assertEquals("/home/humberto/.jashi", toFile("~/.jashi").toString());
		assertEquals("src", toFile("src").toString());
		assertEquals("src/java", toFile("src/java").toString());
		
		File file = toFile("src/test/java/jashi/hellojashi.java");
		assertTrue(file.exists());
		assertEquals("src/test/java/jashi/hellojashi.java", file.toString());
	}
}
