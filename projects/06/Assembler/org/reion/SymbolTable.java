package org.reion;

import java.util.HashMap;

/**
 * 在符号标签和数字地址之间建立关联
 * @author Reion
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
