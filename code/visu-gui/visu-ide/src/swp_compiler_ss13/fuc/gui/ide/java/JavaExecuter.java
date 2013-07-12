package swp_compiler_ss13.fuc.gui.ide.java;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class JavaExecuter {
	private final static boolean NO_VERIFY_NEEDED = false;
	private final static String NO_VERIFY = "-noverify";

	Process p;

	public JavaExecuter(File classFile) throws IOException {
		if (classFile == null) throw new NullPointerException();

		if (!classFile.exists())
			throw new JavaClassProcessRuntimeException(
					"JavaClassProcess can not be started. File does not exists.");

		if (classFile.isDirectory())
			throw new JavaClassProcessRuntimeException(
					"JavaClassProcess can not be started. Need a file not a directory.");

		if (!classFile.getName().endsWith(".class"))
			throw new JavaClassProcessRuntimeException(
					"JavaClassProcess can not be started. File has not expected extension.");

		String classPath = classFile.getParent();
		String className = classFile.getName();
		className = className.substring(0, className.lastIndexOf('.'));

		String javaExecutablePath = System.getProperty("java.home") + File.separator + "bin"
				+ File.separator + "java";
		ProcessBuilder processBuilder;
		if (NO_VERIFY_NEEDED) {
			processBuilder = new ProcessBuilder(javaExecutablePath, "-Dfile.encoding=UTF-8", "-cp",
					classPath, NO_VERIFY, className);
		} else {
			processBuilder = new ProcessBuilder(javaExecutablePath, "-Dfile.encoding=UTF-8", "-cp",
					classPath, className);
		}
		try {
			p = processBuilder.
					redirectErrorStream(true).
					start();
			p.waitFor();
		} catch (IOException | InterruptedException e) {
			throw new JavaClassProcessRuntimeException(e.getMessage(), e);
		}
	}

	public Integer getReturnValue() {
		return p.exitValue();
	}

	public String getProcessOutput() {
		StringBuilder writer = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				writer.append(line);
				writer.append('\n');
			}
			if (writer.length() > 0) {
				writer.setLength(writer.length() - 1);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return writer.toString();
	}

	public InputStream getInputstream() {
		return p.getInputStream();
	}

	public static File cloneInputStream(String name, InputStream input) throws IOException {
		File file = new File("build",name);
		if(!file.exists()){
			(new File("build")).mkdirs();
			file.createNewFile();
		}
		FileOutputStream bout = new FileOutputStream(file);
		byte buffer[] = new byte[1024];
		int len;
		while ((len = input.read(buffer)) > 0) {
			bout.write(buffer, 0, len);
		}
		bout.flush();
		bout.close();
		
		return file;
	}
}
