package jashi;

import static jashi.ExecuteHelper.exec;
import static jashi.FileHelper.toFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Executor {
	
	private String javaFilename;
	private File javaSrcFile;
	private File jashiDir;
	private File workDir;
	private List<String> sourceLines;
	private List<String> dependencies = new ArrayList<String>();
	public String packname;

	public static void main(String[] args) {
		
		Executor e = new Executor();
		
		e.parseArgs(args);
		e.compile();
		int ret = e.execute();
		System.exit(ret);		
	}
	
	public File getHomeDir() {
		String homeDirName = System.getProperty("user.home");
		if (!FileHelper.ftest("-d", homeDirName)) {
			System.err.println("Directory doesn't exist"+homeDirName);
			
		}
		return new File(homeDirName);
	}
	
	public File getJashiDir() {
		
		if (jashiDir != null) {
			return jashiDir;
		}
		
		jashiDir = toFile("~/.jashi");
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
	
		Pattern cpPat = Pattern.compile("^\\s*@jashi.Classpath\\(\"(.*)\"\\)");
		
		Pattern pat = Pattern.compile("package\\s+(\\w+)");
		System.out.println("parsing source code");
		for(String line : sourceLines) {
			
			Matcher mat = pat.matcher(line);
			if (mat.find()) {
				packname = mat.group(1);
				continue;
			}
			
			mat = cpPat.matcher(line);
			if (mat.find()) {
				dependencies.add(mat.group(1));
			}
		}
		
		for(String dependency : dependencies) {
			System.out.println(dependency);
		}
		
	}	
	
	private String makeClasspath() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("target/classes");
	    for(String d : dependencies) {
	        if (d.equals(".")) {
	            sb.append(":.");
	        }
	        else if (d.equals("..")) {
                sb.append(":..");
	        }
	        else {
    	        
    	            
    	        String f[] = d.split(":");
    	        if (f.length != 3) {
    	            throw new JashiException("bad dependency format..."+d);
    	        }
    	        sb
    	            .append(":")    	            
    	            .append(FileHelper.getHomeDir()+"/.m2/repository/")
    	            .append(f[0].replace(".", "/") + "/")
                    .append(f[1] + "/")
    	            .append(f[2] + "/")
    	            .append(f[1]+"-"+f[2]+".jar");	        
	        }
	    }
	    return sb.toString();
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
				"-cp", makeClasspath(),
				javaSrcFile.toString()
		};
		
		int exitVal =  exec(command, null, null);
		if (exitVal != 0) {
			workDir.delete();
			throw new JashiException("Failed compilation");
		}
	}

	private String joinString(List<String> lines) {
		StringBuilder sb = new StringBuilder();
		for(String line : lines ) {
			sb.append(line).append("\n");
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
