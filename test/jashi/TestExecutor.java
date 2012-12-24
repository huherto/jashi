package jashi;

import org.junit.Test;

public class TestExecutor {

	@Test
	public void testCompile() {
		Executor e = new Executor();
		
		String[] args = {"test/jashi/hellojashi.java"};
		
		e.parse(args);
		e.compile();
	}
}
