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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 解析.vm文件中的VM命令，提供访问入口，移除空格和注释。
 * 
 * @author Reion Chan
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
	public static final String[] TYPE_ARI_LOG = { "add", "sub", "neg", "eq",
			"gt", "lt", "and", "or", "not" };
	// push命令
	public static final String TYPE_PUSH = "push";
	// pop命令
	public static final String TYPE_POP = "pop";
	// label命令
	public static final String TYPE_LABEL = "label";
	// goto命令
	public static final String TYPE_GOTO = "goto";
	// if命令
	public static final String TYPE_IF = "if-goto";
	// function命令
	public static final String TYPE_FUNCTION = "function";
	// return命令
	public static final String TYPE_RETURN = "return";
	// call命令
	public static final String TYPE_CALL = "call";

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
	// 当前方法名称
	private String funcName;

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
		if (curType.compareTo(CommandType.C_FUNCTION) == 0) {
			funcName = arg1();
		}
	}

	/**
	 * 判断当前指令类型
	 * 
	 * @return 指令类型
	 */
	public CommandType commandType() {
		if (Arrays.asList(TYPE_ARI_LOG).contains(curInstruct[0])) {
			return CommandType.C_ARITHMETIC;
		} else if (TYPE_PUSH.equalsIgnoreCase(curInstruct[0])) {
			return CommandType.C_PUSH;
		} else if (TYPE_POP.equalsIgnoreCase(curInstruct[0])) {
			return CommandType.C_POP;
		} else if (TYPE_LABEL.equalsIgnoreCase(curInstruct[0])) {
			return CommandType.C_LABEL;
		} else if (TYPE_GOTO.equalsIgnoreCase(curInstruct[0])) {
			return CommandType.C_GOTO;
		} else if (TYPE_IF.equalsIgnoreCase(curInstruct[0])) {
			return CommandType.C_IF;
		} else if (TYPE_FUNCTION.equalsIgnoreCase(curInstruct[0])) {
			return CommandType.C_FUNCTION;
		} else if (TYPE_RETURN.equalsIgnoreCase(curInstruct[0])) {
			return CommandType.C_RETURN;
		} else if (TYPE_CALL.equalsIgnoreCase(curInstruct[0])) {
			return CommandType.C_CALL;
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

		return curInstruct[1];
	}

	/**
	 * 获取当前命令的第二个参数，只允许堆栈命令 函数声明及调用调用此方法
	 * 
	 * @return 参数值
	 */
	public int arg2() {
		if (curType.compareTo(CommandType.C_PUSH) == 0
				|| curType.compareTo(CommandType.C_POP) == 0
				|| curType.compareTo(CommandType.C_FUNCTION) == 0
				|| curType.compareTo(CommandType.C_CALL) == 0) {
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
			} else if (curType.compareTo(CommandType.C_ARITHMETIC) == 0) {
				code.writeArithmetic(curInstruct[0]);
			} else if (curType.compareTo(CommandType.C_LABEL) == 0) {
				code.writeLabel(MessageFormat.format(CodeWriter.LABEL_PATTEN1,
						new Object[] { funcName, arg1() }));
			} else if (curType.compareTo(CommandType.C_IF) == 0) {
				code.writeIf(MessageFormat.format(CodeWriter.LABEL_PATTEN1,
						new Object[] { funcName, arg1() }));
			} else if (curType.compareTo(CommandType.C_GOTO) == 0) {
				code.writeGoto(MessageFormat.format(CodeWriter.LABEL_PATTEN1,
						new Object[] { funcName, arg1() }));
			} else if (curType.compareTo(CommandType.C_FUNCTION) == 0) {
				code.writeFunction(arg1(), arg2());
			} else if (curType.compareTo(CommandType.C_RETURN) == 0) {
				code.writeReturn();
			} else if (curType.compareTo(CommandType.C_CALL) == 0) {
				code.writeCall(arg1(), arg2());
			}
		}
	}
}
