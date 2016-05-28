package org.reion;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;

/**
 * 程序入口.
 * 
 * @author Reion
 * 
 */
public class JackCompiler {

	/**
	 * JACK源文件后缀名
	 */
	public static final String SOURCE_FIX = ".jack";

	/**
	 * 目标文件后缀名
	 */
	public static final String TARGET_FIX = ".vm";

	/**
	 *  字元转换器
	 */
	public static JackTokenizer tokenizer = null;

	public static void main(String[] args) {
		if (args.length < 1 || args.length > 1) {
			System.out.println("Usage: JackCompiler Foo.jack | file_director");
			return;
		}
		File file = new File(args[0].trim());
		if (!file.exists()) {
			System.out.println("File or Directory doesn't exist!");
			return;
		}
		if (file.isFile()) {
			if (file.getName().lastIndexOf(SOURCE_FIX) < 0) {
				System.out.println("JackCompiler only accepts .jack file!");
				return;
			}
			doTrans(file);
		} else if (file.isDirectory()) {
			// 文件过滤器，只过滤出以.jack结尾的文件
			FileFilter filter = new FileFilter() {
				public boolean accept(File pathname) {
					return pathname.getName().lastIndexOf(SOURCE_FIX) > 0;
				}
			};
			File[] files = file.listFiles(filter);
			for (File f : files) {
				doTrans(f);
			}
		}
	}

	/**
	 * JACK翻译成VM
	 * 
	 * @param file
	 *            JACK文件
	 */
	private static void doTrans(final File file) {
		try {
			tokenizer = new JackTokenizer(file, new FileReader(file));
			CompilationEngine engin = new CompilationEngine(tokenizer);
			engin.compileClass();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
