package jashi;

import static jashi.ExecuteHelper.exec;
import static jashi.FileHelper.toFile;

import java.io.File;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Executor {
	
	private String javaFilename;
	private File javaSrcFile;
	private File jashiDir;
	private File workDir;
	private List<String> sourceLines;
	public String packname;

	public static void main(String[] args) {
		
		Executor e = new Executor();
		
		e.parseArgs(args);
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
	
	public void parseSource() {
	
		Pattern pat = Pattern.compile("package\\s+(\\w+)");
		for(String line : sourceLines) {
			
			Matcher mat = pat.matcher(line);
			if (mat.find()) {
				packname = mat.group(1);
			}
			
		}
	}	

	public void compile() {
		
		
		sourceLines = FileHelper.readAll(javaSrcFile);
		
		parseSource();

		String str = joinString(sourceLines);
		
		String sha1 = Encoder.toSha1(str.getBytes());
		
		workDir = new File(getJashiDir(), sha1);
		if (workDir.exists() && workDir.isDirectory()) {
			// No need to compile.
			return;			
		}
		
		if (!workDir.mkdirs()) {
			throw new JashiException("Cannot mkdir '"+workDir+"'");
		}
		
		String command[] = {
				"javac",
				"-d", workDir.toString(),
				javaSrcFile.toString()
		};
		
		int exitVal =  exec(command, null, null);
		if (exitVal != 0) {
			throw new JashiException("Failed compilation");
		}
	}

	private String joinString(List<String> lines) {
		StringBuilder sb = new StringBuilder();
		for(String line : lines ) {
			sb.append(line);
		}
		return sb.toString();
	}
	
	public int execute() {
		
		String classname = javaSrcFile.getName().replace(".java", "");
		
		if (packname != null) {
			classname = packname + "." + classname;
		}
		
		String command[] = {
				"java",
				"-cp", workDir.toString(),
				classname
				
		};
		
		int exitVal =  exec(command, null, null);
		
		return exitVal;
	}
	
	public void parseArgs(String[] args) {
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
