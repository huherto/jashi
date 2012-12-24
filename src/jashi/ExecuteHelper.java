package jashi;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Arrays;

public class ExecuteHelper {
	
	private static void redirectInputOutput(InputStream in, PrintStream out) throws Exception {
		BufferedReader input = new BufferedReader(new InputStreamReader(in));

		String line = null;

		while ((line = input.readLine()) != null) {
			out.println(line);
		}
		out.flush();
		
	}

	public static int exec(String[] command) {
		try {
			Runtime rt = Runtime.getRuntime();
			System.out.println(Arrays.toString(command));
			System.out.flush();
			Process proc = rt.exec(command);
			redirectInputOutput(proc.getInputStream(), System.out);
			redirectInputOutput(proc.getErrorStream(), System.err);
			return proc.waitFor();
		}
		catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}
