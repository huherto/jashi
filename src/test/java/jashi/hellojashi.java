package jashi;


@jashi.Config({
	@jashi.Classpath("."),
	@jashi.Classpath(".."),
	@jashi.Classpath("org.apache.commons:commons-io:2.4")
})
 

public class hellojashi {
	
	public static void main(String[] args) {
		System.out.println("Hello world!"); 
	}

}
