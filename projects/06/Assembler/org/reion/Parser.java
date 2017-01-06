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
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 读取汇编源文件，提取汇编命令序列并解析，提供方便访问汇编命令成分（域和符号）的方法
 * @author Reion Chan
 */
public class Parser {
	
	// 汇编源文件后缀名
	public static final String SOURCE_FIX = ".asm";
	// 汇编编译后机器码文件后缀名
	public static final String DEST_FIX = ".hack";
	// 注释标签
	public static final String COMMENT_TAG = "//";
	// A指令标签
	public static final String A_TAG= "@";
	// L指令开始标签
	public static final String L_BEGIN_TAG = "(";
	// L指令结束标签
	public static final String L_END_TAG = ")";
	// C指令dest分隔符
	public static final String DEST_SEPARATOR = "=";
	// C指令jump分割付
	public static final String JUMP_SEPARATOR = ";";
	// 换行符
	public static final String LINE_SEPARATOR = System.getProperty("line.separator");
	
	// 数字正则
	public static final String NUMBER_REGULAR = "[0-9]*";
	
	// 指令类型
	public static enum CommandType {
		A_COMMAND, C_COMMAND, L_COMMAND
	}
	
	// 当前汇编命令
	private String curInstruct;
	// 当前汇编命令行号
	private int curInsNum = -1;
	// 当前指令类型
	private CommandType curType;
	// 汇编源文件总命令条数
	private int totalNum = -1;
	// 汇编源文件所在路径
	private String filePath;
	// 汇编源文件名称
	private String fileName;
	// 去除注释后的纯命令序列
	private Map<Integer, String> insSeqs = new LinkedHashMap<Integer, String>();
	// 符号表
	private SymbolTable symTable;
	// 译码表
	private Code code;

	/**
	 * 构造方法
	 * @param path 汇编源文件全路径
	 * @param sybTable 符号地址映射表
	 */
	public Parser(final String path, final SymbolTable sybTable, final Code code) {
		this.symTable = sybTable;
		this.code = code;
		init(path);
	}
	
	/**
	 * 初始化资源
	 * @param path 汇编源文件全路径
	 */
	private void init(String path) {
		File file = new File(path);
		if (!file.exists()) {
			System.out.println("File '" + file.getAbsolutePath() + "' doesn't exist!");
			return;
		}
		
		String fName = file.getName();
		String fileExt = fName.substring(fName.lastIndexOf('.'));
		fileName = fName.substring(0, fName.lastIndexOf('.'));
		if (!SOURCE_FIX.equalsIgnoreCase(fileExt)) {
			System.out.println("Assembler only accepte asm file!");
		}
		
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
			while ((curStr=br.readLine()) != null) {
				if (curStr.startsWith(COMMENT_TAG) || curStr.trim().length() < 1) {
					continue;
				}
				if (curStr.indexOf(COMMENT_TAG) > 0) {
					curStr = curStr.substring(0, curStr.indexOf(COMMENT_TAG)).trim();
				}
				curStr = curStr.trim();
				if (curStr.startsWith(L_BEGIN_TAG) && curStr.endsWith(L_END_TAG)) {
					symTable.addEntry(curStr.substring(curStr.indexOf(L_BEGIN_TAG)+1,
							curStr.indexOf(L_END_TAG)).trim(), lineNum+1);
					continue;
				}
				insSeqs.put(++lineNum, curStr);
			}
			
			totalNum = insSeqs.size();
			String abPath = file.getAbsolutePath();
			filePath = abPath.substring(0, abPath.indexOf(fName));
			
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
	 * @return 有-true 无-false
	 */
	public boolean hasMoreCommands() {
		return totalNum - curInsNum - 1 > 0;
	}
	
	/**
	 * 载入下一条汇编命令
	 */
	public void advance() {
		if (!hasMoreCommands()){
//			curInstruct = null;
//			curInsNum = -1;
//			curType = null;
			return;
		}
		curInsNum++;
		curInstruct = insSeqs.get(curInsNum);
		curType = commandType();
	}
	
	/**
	 * 判断当前指令类型
	 * @return 指令类型
	 */
	public CommandType commandType() {
		if (curInstruct.startsWith(A_TAG)) {
			return CommandType.A_COMMAND;
		}
		
		if (curInstruct.startsWith(L_BEGIN_TAG) && curInstruct.endsWith(L_END_TAG)) {
			return CommandType.L_COMMAND;
		}
		
		//TODO 此处用了非A非L即C的理想假设
		return CommandType.C_COMMAND;
	}
	
	/**
	 * 获取A或L命令的符号或十进制值
	 * @return A或L命令符号或十进制字符串
	 */
	public String symbol() {
		if (curType.compareTo(CommandType.C_COMMAND) == 0) {
			throw new RuntimeException("Line " + curInsNum + " instruction isn't A_COMMAND or C_COMMAND type!");
		}
		
		if (curType.compareTo(CommandType.A_COMMAND) == 0) {
			return curInstruct.substring(1);
		}
		
		if (curType.compareTo(CommandType.L_COMMAND) == 0) {
			return curInstruct.substring(1, curInstruct.indexOf(L_END_TAG));
		}
		return null;
	}
	
	/**
	 * 获取C命令dest部分
	 * @return dest部分字符串
	 */
	public String dest() {
		if (curType.compareTo(CommandType.C_COMMAND) == 0) {
			int dIndex = curInstruct.indexOf(DEST_SEPARATOR);
			if (dIndex > 0) {
				return curInstruct.substring(0, dIndex).trim().toUpperCase();
			} else {
				return "null";
			}
		} else {
			throw new RuntimeException("Line " + curInsNum + " instruction isn't C_COMMAND type!");
		}
	}
	
	/**
	 * 获取C命令comp部分
	 * @return comp部分字符串
	 */
	public String comp() {
		String compStr = null;
		if (curType.compareTo(CommandType.C_COMMAND) == 0) {
			int dIndex = curInstruct.indexOf(DEST_SEPARATOR);
			int jIndex = curInstruct.indexOf(JUMP_SEPARATOR);
			int start = dIndex>0 ? dIndex+1 : 0;
			int end = jIndex>0 ? jIndex : curInstruct.length();
			compStr = curInstruct.substring(start, end).trim().toUpperCase();
			if (compStr.length() < 1) {
				throw new RuntimeException("Line " + curInsNum + " instruction syntax error!");
			}
			return compStr;
		} else {
			throw new RuntimeException("Line " + curInsNum + " instruction isn't C_COMMAND type!");
		}
	}
	
	/**
	 * 获取C命令jump部分
	 * @return jump部分字符串
	 */
	public String jump() {
		if (curType.compareTo(CommandType.C_COMMAND) == 0) {
			int jIndex = curInstruct.indexOf(JUMP_SEPARATOR);
			if (jIndex > 0) {
				return curInstruct.substring(jIndex+1).trim().toUpperCase();
			} else {
				return "null";
			}
		} else {
			throw new RuntimeException("Line " + curInsNum + " instruction isn't C_COMMAND type!");
		}
	}
	
	/**
	 * 解析生成二进制命令列表
	 * @return
	 */
	public ArrayList<String> parse() {
		ArrayList<String> ret = new ArrayList<String>();
		String symbol = null;
		int address = -1;
		int varAddress = 0x0F;
		StringBuffer strB = new StringBuffer();
		
		while (hasMoreCommands()) {
			advance();
			if (curType.compareTo(CommandType.A_COMMAND) == 0) {
				symbol = symbol();
				if (isNumeric(symbol)) {
					address = Integer.parseInt(symbol);				
				} else {
					if (symTable.contains(symbol)) {
						address = symTable.getAddress(symbol);
					} else {
						address = ++varAddress;
						symTable.addEntry(symbol, address);
					}
				}
				
				ret.add(code.getATypeBinary(address) + LINE_SEPARATOR);
			}
			
			if(curType.compareTo(CommandType.C_COMMAND) == 0) {
				strB.delete(0, strB.length());
				strB.append(Code.C_PRIFIX).append(code.comp(comp())).append(code.dest(dest())).append(code.jump(jump()));
				ret.add(strB.toString() + LINE_SEPARATOR);
			}
		}
		return ret;
	}
	
	/**
	 * 判断是否是数字字符串
	 * @param str 字符串
	 * @return 是-true 否-false
	 */
	private static boolean isNumeric(final String str) {
		Pattern pattern = Pattern.compile(NUMBER_REGULAR);
		Matcher isNum = pattern.matcher(str);
		return isNum.matches();
	}
	
	public void compile() {
		ArrayList<String> ret = parse();
		File dest = new File(filePath + File.separator + fileName + DEST_FIX);
		FileWriter fw = null;
		try {
			fw = new FileWriter(dest);
			for (String str : ret) {
				fw.write(str);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
