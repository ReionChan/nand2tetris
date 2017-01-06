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

import java.util.HashMap;

/**
 * 将Hack汇编语言助记符翻译成二进制码
 * @author Reion Chan
 */
public class Code {
	
	/**
	 * C指令前缀字符串
	 */
	public static final String C_PRIFIX = "111";
	
	/**
	 * dest助记符转二进制码翻译表
	 */
	public static HashMap<String, String> destTable;
	
	/**
	 * comp助记符转二进制码翻译表
	 */
	public static HashMap<String, String> compTable;
	
	/**
	 * jump助记符转二进制码翻译表
	 */
	public static HashMap<String, String> jumpTable;
	
	/**
	 * 初始化码表
	 */
	static {
		destTable = new HashMap<String, String>();
		compTable = new HashMap<String, String>();
		jumpTable = new HashMap<String, String>();
		
		destTable.put("null", "000");
		destTable.put("M", "001");
		destTable.put("D", "010");
		destTable.put("MD", "011");
		destTable.put("A", "100");
		destTable.put("AM", "101");
		destTable.put("AD", "110");
		destTable.put("AMD", "111");
		
		compTable.put("0", "0101010");
		compTable.put("1", "0111111");
		compTable.put("-1", "0111010");
		compTable.put("D", "0001100");
		compTable.put("A", "0110000");
		compTable.put("!D", "0001101");
		compTable.put("!A", "0110001");
		compTable.put("-D", "0001111");
		compTable.put("-A", "0110011");
		compTable.put("D+1", "0011111");
		compTable.put("A+1", "0110111");
		compTable.put("D-1", "0001110");
		compTable.put("A-1", "0110010");
		compTable.put("D+A", "0000010");
		compTable.put("D-A", "0010011");
		compTable.put("A-D", "0000111");
		compTable.put("D&A", "0000000");
		compTable.put("D|A", "0010101");
		compTable.put("M", "1110000");
		compTable.put("!M", "1110001");
		compTable.put("-M", "1110011");
		compTable.put("M+1", "1110111");
		compTable.put("M-1", "1110010");
		compTable.put("D+M", "1000010");
		compTable.put("D-M", "1010011");
		compTable.put("M-D", "1000111");
		compTable.put("D&M", "1000000");
		compTable.put("D|M", "1010101");
			
		jumpTable.put("null", "000");
		jumpTable.put("JGT", "001");
		jumpTable.put("JEQ", "010");
		jumpTable.put("JGE", "011");
		jumpTable.put("JLT", "100");
		jumpTable.put("JNE", "101");
		jumpTable.put("JLE", "110");
		jumpTable.put("JMP", "111");
	}
	
	/**
	 * 获取dest的二进制码字符串
	 * @param exp 助记符
	 * @return
	 */
	public String dest(final String exp) {
		String ret = destTable.get(exp);
		if (ret == null) {
			throw new RuntimeException("Can not find dest expresstion '" + exp + "'");
		}
		return ret;
	}
	
	/**
	 * 获取comp的二进制码字符串
	 * @param exp 助记符
	 * @return
	 */
	public String comp(final String exp) {
		String ret = compTable.get(exp);
		if (ret == null) {
			throw new RuntimeException("Can not find comp expresstion '" + exp + "'");
		}
		return ret;
	}
	
	/**
	 * 获取jump的二进制码字符串
	 * @param exp 助记符
	 * @return
	 */
	public String jump(final String exp) {
		String ret = jumpTable.get(exp);
		if (ret == null) {
			throw new RuntimeException("Can not find jump expresstion '" + exp + "'");
		}
		return ret;
	}
	
	public String getATypeBinary(final int address) {
		String biStr = Integer.toBinaryString(address);
		StringBuffer strB = new StringBuffer();
		for(int i=0; i<16-biStr.length(); i++) {
			strB.append('0');
		}
		return strB.append(biStr).toString();
	}
}
