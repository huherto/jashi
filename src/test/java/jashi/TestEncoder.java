package jashi;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestEncoder {
	
	@Test
	public void testSha1() {
		assertEquals("5baa61e4c9b93f3f0682250b6cf8331b7ee68fd8", Encoder.toSha1("password".getBytes()).toLowerCase());
	}

}
