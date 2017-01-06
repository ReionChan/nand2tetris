/**
 *
 * ############################################################################
 * 
 *                          版权声明（中文版）
 *
 * ############################################################################
 * 
 * 版权所有 2015-2017 Reion Chan
 * 
 * 本程序遵照GPLv3，在你以任何方式使用本程序时，请务必署名此程序作者（Reion Chan）。
 * 本程序为自由软件：你可以在遵照GNU GPLv3（及后续版本）条款的前提下，转发、修改本程序。
 * 尝试在你的计算机要素的课程中盗用本程序的源代码将被诅咒会一生只写漏洞百出的程序。
 * 本程序旨在学习、交流，对本程序不承担任何担保责任，详细参见GPLv3许可。
 * 本程序包含一个GUN 通用公共授权。如果没有，请访问<http://www.gnu.org/licenses/>
 *
 *
 *
 * ############################################################################
 * 
 *                          Copyright （English Edition）
 *
 * ############################################################################
 *
 * Copyright 2015-2017 Reion Chan.
 *
 * You are required to give attribution to the author (Reion Chan) for any
 * use of this program (GPLv3 Section 7b).
 * 
 * Trying to pass off my code as your own in your Elements of Computing classes
 * will result in a cursed life of forever buggy software.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
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
public class JackAnalyzer {

	/**
	 * JACK源文件后缀名
	 */
	public static final String SOURCE_FIX = ".jack";

	/**
	 * 目标文件后缀名
	 */
	public static final String TARGET_FIX = ".xml";

	// 字元转换器
	public static JackTokenizer tokenizer = null;

	public static void main(String[] args) {
		if (args.length < 1 || args.length > 1) {
			System.out.println("Usage: JackAnalyzer Foo.jack | file_director");
			return;
		}
		File file = new File(args[0].trim());
		if (!file.exists()) {
			System.out.println("File or Directory doesn't exist!");
			return;
		}
		if (file.isFile()) {
			if (file.getName().lastIndexOf(SOURCE_FIX) < 0) {
				System.out.println("JackAnalyzer only accepts .jack file!");
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
