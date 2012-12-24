package jashi;

import static jashi.ExecuteHelper.exec;
import static jashi.FileHelper.toFile;

import java.io.File;


public class Executor {
	
	private String javaFilename;
	private File javaSrcFile;
	private File jashiDir;

	public static void main(String[] args) {
		
		Executor e = new Executor();
		
		e.parse(args);
		e.compile();
		
	}
	
	public File getHomeDir() {
		String homeDirName = System.getProperty("user.home");
		if (FileHelper.ftest("-d", homeDirName)) {
			System.err.println("Directory doesn't exist");
			
		}
		return new File(homeDirName);
	}
	
	public File getJashiDir() {
		
		if (jashiDir != null) {
			return jashiDir;
		}
		
		jashiDir = new File(getHomeDir(), ".jashi"); 
		if (!jashiDir.exists()) {
			if (jashiDir.mkdir()) {
				return jashiDir;
			}
			throw new JashiException("Cannot create directory '"+jashiDir+"'");
		}
		
		if (!jashiDir.isDirectory()) {
			throw new JashiException("'"+jashiDir+"' is not a directory");
		}
		
		return jashiDir;
	}

	public void compile() {
		
		String command[] = {
				"javac",
				javaSrcFile.toString()
		};
		
		int exitVal =  exec(command);
		System.out.println("exitVal="+exitVal);
		System.out.flush();
		if (exitVal != 0) {
			throw new JashiException("Failed compilation");
		}
	}

	public void parse(String[] args) {
		if (args.length == 0) {
			usage();
		}
		
		javaFilename = args[0];
		
		if (!FileHelper.ftest("-f", javaFilename)) {
			throw new JashiException("Can't find file '"+javaFilename+"'");
		}
		
		javaSrcFile = toFile(javaFilename);
	}

	private void usage() {
		throw new JashiException("usage: jashi javaclass arg1 arg2 arg3 ...");
	}
}
