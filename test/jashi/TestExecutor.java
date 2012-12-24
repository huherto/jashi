package jashi;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestExecutor {

	@Test
	public void testCompile() {
		Executor e = new Executor();
		
		String[] args = {"test/jashi/hellojashi.java"};
		
		e.parseArgs(args);
		e.compile();
	}
	
	@Test
	public void testExecute() {
		Executor e = new Executor();
		String[] args = {"test/jashi/hellojashi.java"};
		
		e.parseArgs(args);
		e.compile();
		assertEquals("jashi", e.packname);
		
		assertEquals(0, e.execute());
		
	}
}
