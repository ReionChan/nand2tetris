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
import java.io.FileWriter;
import java.io.IOException;

/**
 * Write VM to file
 * 
 * @author Reion
 * 
 */
public class VMWriter {
	
	/**
	 * while Label Counter
	 */
	public static int whileCounter = 0;
	/**
	 * if Label Counter
	 */
	public static int ifCounter = 0;

	/**
	 * target file
	 */
	private File tFile;
	/**
	 * target file writer
	 */
	private FileWriter fw;

	/**
	 * Constructor
	 * 
	 * @param file
	 */
	VMWriter(final File file) {
		tFile = file;
		try {
			fw = new FileWriter(tFile);
		} catch (IOException e) {
			throw new RuntimeException("VMWriter init error!", e);
		}
	}

	/**
	 * write VM push
	 * 
	 * @param segment
	 *            segment of RAM
	 * @param index
	 *            the offset of segment
	 */
	public void writePush(final Segment segment, final int index) {
		try {
			fw.write("push " + segment.getValue() + " " + index
					+ JackTokenizer.LINE_SEPARATOR);
		} catch (IOException e) {
			throw new RuntimeException("writePush error!", e);
		}
	}

	/**
	 * write VM pop
	 * 
	 * @param segment
	 *            segment of RAM
	 * @param index
	 *            the offset of segment
	 */
	public void writePop(final Segment segment, final int index) {
		try {
			fw.write("pop " + segment.getValue() + " " + index
					+ JackTokenizer.LINE_SEPARATOR);
		} catch (IOException e) {
			throw new RuntimeException("writePop error!", e);
		}
	}

	/**
	 * write VM arithmetic
	 * 
	 * @param command
	 *            arithmetic command
	 */
	public void writeArithmetic(final Command command) {
		if (Command.MUT.equals(command)) {
			writeCall("Math.multiply", 2);
			return;
		} else if (Command.DIV.equals(command)) {
			writeCall("Math.divide", 2);
			return;
		}
		try {
			fw.write(command.toString() + JackTokenizer.LINE_SEPARATOR);
		} catch (IOException e) {
			throw new RuntimeException("writeArithmetic error!", e);
		}
	}

	/**
	 * write VM label
	 * 
	 * @param label
	 *            label name
	 */
	public void writeLabel(final String label) {
		try {
			fw.write("label " + label + JackTokenizer.LINE_SEPARATOR);
		} catch (IOException e) {
			throw new RuntimeException("writeLabel error!", e);
		}
	}

	/**
	 * write VM goto
	 * 
	 * @param label
	 *            label name
	 */
	public void writeGoto(final String label) {
		try {
			fw.write("goto " + label + JackTokenizer.LINE_SEPARATOR);
		} catch (IOException e) {
			throw new RuntimeException("writeGoto error!", e);
		}
	}

	/**
	 * write VM if-goto
	 * 
	 * @param label
	 *            label name
	 */
	public void writeIf(final String label) {
		try {
			fw.write("if-goto " + label + JackTokenizer.LINE_SEPARATOR);
		} catch (IOException e) {
			throw new RuntimeException("writeIf error!", e);
		}
	}

	/**
	 * write VM call
	 * 
	 * @param name
	 *            subroutine name
	 * @param nArgs
	 *            subroutine arguments number
	 */
	public void writeCall(final String name, final int nArgs) {
		try {
			fw.write("call " + name + " " + nArgs
					+ JackTokenizer.LINE_SEPARATOR);
		} catch (IOException e) {
			throw new RuntimeException("writeCall error!", e);
		}
	}

	/**
	 * write VM function
	 * 
	 * @param name
	 *            subroutine name
	 * @param nLocals
	 *            subroutine locals number
	 */
	public void writeFunction(final String name, final int nLocals) {
		try {
			fw.write("function " + name + " " + nLocals
					+ JackTokenizer.LINE_SEPARATOR);
		} catch (IOException e) {
			throw new RuntimeException("writeFunction error!", e);
		}
	}

	/**
	 * write VM return
	 */
	public void writeReturn() {
		try {
			fw.write("return" + JackTokenizer.LINE_SEPARATOR);
		} catch (IOException e) {
			throw new RuntimeException("writeReturn error!", e);
		}
	}

	/**
	 * close resources
	 */
	public void close() {
		if (fw != null) {
			try {
				fw.close();
			} catch (IOException e) {
				throw new RuntimeException("close VMWriter error!", e);
			}
		}
	}
}

/**
 * RAM segment
 * 
 * @author Reion
 * 
 */
enum Segment {
	CONST("constant"), ARG("argument"), LOCAL("local"), STATIC("static"), THIS(
			"this"), THAT("that"), POINTER("pointer"), TEMP("temp");
	/**
	 * segment name
	 */
	private String value;

	private Segment(final String str) {
		value = str;
	}

	/**
	 * get segment name
	 * 
	 * @return segment name
	 */
	public String getValue() {
		return value;
	}
}

/**
 * Arithmetic command
 * 
 * @author Reion
 * 
 */
enum Command {
	ADD, SUB, NEG, EQ, GT, LT, AND, OR, NOT, MUT, DIV;
	
	@Override
	public String toString() {
		return this.name().toLowerCase();
	}
}
