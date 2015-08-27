package org.reion;

/**
 * Hack语言编译器实现 
 * @author Reion
 *
 */
public class Assembler {
	public static void main(String[] args) {
		if (args.length < 1 || args.length > 1) {
			System.out.println("Usage: Assembler Foo.asm");
			return;
		}
		SymbolTable symTable = new SymbolTable();
		Code code = new Code();
		Parser parser = new Parser(args[0], symTable, code);
		parser.compile();
	}
}
