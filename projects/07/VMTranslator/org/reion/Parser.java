package org.reion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 解析.vm文件中的VM命令，提供访问入口，移除空格和注释。
 * 
 * @author Reion
 */
public class Parser {

	// C指令dest分隔符
	public static final String DEST_SEPARATOR = "=";
	// C指令jump分割付
	public static final String JUMP_SEPARATOR = ";";
	// VM指令分割付
	public static final String VM_SEPARATOR = " ";

	// 数字正则
	// public static final String NUMBER_REGULAR = "[0-9]*";

	// 算术逻辑命令集
	public static final String[] SET_ARI_LOG = { "add", "sub", "neg", "eq",
			"gt", "lt", "and", "or", "not" };
	// push命令
	public static final String SET_PUSH = "push";
	// pop命令
	public static final String SET_POP = "pop";

	// 指令类型
	public static enum CommandType {
		C_ARITHMETIC, C_PUSH, C_POP, C_LABEL, C_GOTO, C_IF, C_FUNCTION, C_RETURN, C_CALL
	}

	// 当前VM命令
	private String[] curInstruct;
	// 当前VM命令字符串
	private String curInsStr;
	// 当前汇编命令行号
	private int curInsNum = -1;
	// 当前指令类型
	private CommandType curType;
	// 汇编源文件总命令条数
	private int totalNum = -1;
	// 去除注释后的纯命令序列
	private Map<Integer, String> insSeqs = new LinkedHashMap<Integer, String>();
	// 汇编代码写入器
	private CodeWriter code;

	/**
	 * 设置汇编代码写入器.
	 * 
	 * @param code
	 *            写入器
	 */
	public void setCode(CodeWriter code) {
		this.code = code;
	}

	/**
	 * 构造方法
	 */
	public Parser(final File file) {
		FileReader fr = null;
		BufferedReader br = null;

		try {
			fr = new FileReader(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		br = new BufferedReader(fr);

		// 将源文件提取出纯汇编命令行序列
		String curStr = null;
		int lineNum = -1;
		try {
			while ((curStr = br.readLine()) != null) {
				if (curStr.startsWith(CodeWriter.COMMENT_TAG)
						|| curStr.trim().length() < 1) {
					continue;
				}
				if (curStr.indexOf(CodeWriter.COMMENT_TAG) > 0) {
					curStr = curStr.substring(0,
							curStr.indexOf(CodeWriter.COMMENT_TAG)).trim();
				}
				curStr = curStr.replaceAll("\\s+", VM_SEPARATOR).trim();
				insSeqs.put(++lineNum, curStr);
			}

			totalNum = insSeqs.size();

			if (fr != null) {
				fr.close();
			}
			if (br != null) {
				br.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 汇编源文件是否还有命令
	 * 
	 * @return 有-true 无-false
	 */
	public boolean hasMoreCommands() {
		return totalNum - curInsNum - 1 > 0;
	}

	/**
	 * 载入下一条汇编命令
	 */
	public void advance() {
		if (!hasMoreCommands()) {
			return;
		}
		curInsNum++;
		curInsStr = insSeqs.get(curInsNum);
		curInstruct = insSeqs.get(curInsNum).split(VM_SEPARATOR);
		curType = commandType();
	}

	/**
	 * 判断当前指令类型
	 * 
	 * @return 指令类型
	 */
	public CommandType commandType() {
		if (Arrays.asList(SET_ARI_LOG).contains(curInstruct[0])) {
			return CommandType.C_ARITHMETIC;
		}
		if (SET_PUSH.contains(curInstruct[0])) {
			return CommandType.C_PUSH;
		}
		if (SET_POP.contains(curInstruct[0])) {
			return CommandType.C_POP;
		}
		return null;
	}

	/**
	 * 获取当前命令的第一个参数，算数命令返回命令本身，return不允许调用此方法
	 * 
	 * @return 参数字符串
	 */
	public String arg1() {
		if (curType.compareTo(CommandType.C_RETURN) == 0) {
			throw new RuntimeException("No arg1 if type is C_RETURN!");
		}

		if (curType.compareTo(CommandType.C_ARITHMETIC) == 0) {
			return curInstruct[0];
		}

		if (curType.compareTo(CommandType.C_PUSH) == 0
				|| curType.compareTo(CommandType.C_POP) == 0) {
			return curInstruct[1];
		}

		return null;
	}

	/**
	 * 获取当前命令的第二个参数，只允许堆栈命令 函数声明及调用调用此方法
	 * 
	 * @return 参数值
	 */
	public int arg2() {
		if (curType.compareTo(CommandType.C_PUSH) == 0
				|| curType.compareTo(CommandType.C_POP) == 0) {
			return Integer.parseInt(curInstruct[2]);
		} else {
			throw new RuntimeException("No arg2 if type is "
					+ curType.toString());
		}
	}

	/**
	 * 执行当前VM命令的翻译操作.
	 * 
	 */
	public void trans() {
		while (hasMoreCommands()) {
			advance();
			code.writeComment("'" + curInsStr + "'" + " (Line " + curInsNum
					+ ")");
			if (curType.compareTo(CommandType.C_PUSH) == 0
					|| curType.compareTo(CommandType.C_POP) == 0) {
				code.writePushPop(curInstruct[0], arg1(), arg2());
			}
			if (curType.compareTo(CommandType.C_ARITHMETIC) == 0) {
				code.writeArithmetic(curInstruct[0]);
			}
		}
	}
}
