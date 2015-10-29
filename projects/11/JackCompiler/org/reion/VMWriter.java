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
