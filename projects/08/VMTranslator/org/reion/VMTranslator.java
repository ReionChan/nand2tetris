package org.reion;

import java.io.File;
import java.io.FileFilter;

/**
 * Hack VM翻译器 
 * @author Reion
 *
 */
public class VMTranslator {
	
	// 汇编代码输出器
	public static CodeWriter cWriter = null;
	
	public static void main(String[] args) {
		
		if (args.length < 1 || args.length > 1) {
			System.out.println("Usage: VMTranslator Foo.vm | file_director");
			return;
		}
		File file = new File(args[0].trim());
		if (!file.exists()) {
			System.out.println("File or Directory doesn't exist!");
			return;
		}
		if (file.isFile()) {
			if (file.getName().lastIndexOf(CodeWriter.SOURCE_FIX) < 0) {
				System.out.println("VMTranslator only accepts .vm file!");
				return;
			}
			cWriter = new CodeWriter();
			cWriter.setFilePath(file.getPath().substring(0, file.getPath().lastIndexOf(file.getName())));
			doTrans(file);
		} else if (file.isDirectory()) {
			// 文件过滤器，只过滤出以.vm结尾的文件
			FileFilter filter = new FileFilter() {
				public boolean accept(File pathname) {
					return pathname.getName().lastIndexOf(CodeWriter.SOURCE_FIX) > 0;
				}
			};
			cWriter = new CodeWriter();
			cWriter.setFilePath(file.getPath());
			// 如果是编译目录下所有VM文件则ASM文件名称就取目录名
			cWriter.setAsmName(file.getName());
			for (File f : file.listFiles(filter)) {
				doTrans(f);
			}
		}
		cWriter.close();
	}

	/**
	 * VM翻译成ASM
	 * @param file VM文件
	 */
	private static void doTrans(final File file) {
		cWriter.setFileName(file.getName().substring(0, file.getName().indexOf(CodeWriter.SOURCE_FIX)));
		Parser parser = new Parser(file);
		parser.setCode(cWriter);
		parser.trans();
	}
}
