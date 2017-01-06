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
 * 在符号标签和数字地址之间建立关联
 *
 * @author Reion Chan
 *
 */
public class SymbolTable {
	// 符号表
	private HashMap<String, Integer> symbolTable;
	
	/**
	 * 获得符号表Map
	 * @return
	 */
	public HashMap<String, Integer> getSymbolTable() {
		return symbolTable;
	}

	/**
	 * 构造函数
	 */
	public SymbolTable() {
		symbolTable = new HashMap<String, Integer>();
		initTable();
	}
	
	/**
	 * 初始化符号表
	 */
	private void initTable() {
		symbolTable.put("R0", 0);
		symbolTable.put("R1", 1);
		symbolTable.put("R2", 2);
		symbolTable.put("R3", 3);
		symbolTable.put("R4", 4);
		symbolTable.put("R5", 5);
		symbolTable.put("R6", 6);
		symbolTable.put("R7", 7);
		symbolTable.put("R8", 8);
		symbolTable.put("R9", 9);
		symbolTable.put("R10", 10);
		symbolTable.put("R11", 11);
		symbolTable.put("R12", 12);
		symbolTable.put("R13", 13);
		symbolTable.put("R14", 14);
		symbolTable.put("R15", 15);
		
		symbolTable.put("SP", 0);
		symbolTable.put("LCL", 1);
		symbolTable.put("ARG", 2);
		symbolTable.put("THIS", 3);
		symbolTable.put("THAT", 4);
		symbolTable.put("SCREEN", 16384);
		symbolTable.put("KBD", 24576);
		
	}
	
	/**
	 * 将符号添加到符号表
	 * @param symbol 符号
	 * @param address 地址
	 */
	public void addEntry(final String symbol, final int address) {
		if (contains(symbol)) {
			throw new RuntimeException("Symbol '" + symbol + "' already used!");
		}
		
		/*System.out.println("[address] " + address + "[symbol] " + symbol);
		if (address > 0x6000) {
			throw new RuntimeException("address is out of range!");
		}*/
		symbolTable.put(symbol, address);
	}
	
	/**
	 * 符号表是否包含了指定的symbol
	 * @param symbol 符号
	 * @return 包含-true 不包含-false
	 */
	public boolean contains(final String symbol) {
		return symbolTable.containsKey(symbol);
	}
	
	/**
	 * 获取与symbol关联的地址
	 * @param symbol
	 * @return
	 */
	public int getAddress(final String symbol) {
		return symbolTable.get(symbol);
	}
	
	
}
