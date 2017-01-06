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
import java.util.ArrayList;
import java.util.List;

/**
 * 执行编译输出.
 * 
 * @author Reion
 * 
 */
public class CompilationEngine {

	/**
	 * 目标文件
	 */
	private File tFile;
	/**
	 * 字元分析器
	 */
	private JackTokenizer tokenizer;
	/**
	 * Symbol Table
	 */
	private SymbolTabel table;
	/**
	 * Class name
	 */
	private String className;
	/**
	 * VM Writer
	 */
	private VMWriter vmWriter;

	/**
	 * 构造方法.
	 * 
	 * @param fw
	 *            输出流
	 * @param oFile
	 *            输出文件
	 */
	CompilationEngine(final JackTokenizer jToken) {
		tokenizer = jToken;
		File sFile = tokenizer.getJFile();
		className = sFile.getName().substring(0, sFile.getName().indexOf("."));
		File output = new File(sFile.getParent() + File.separator + "output");
		if (!output.exists()) {
			output.mkdir();
		}
		tFile = new File(output.getPath() + File.separator + className
				+ JackCompiler.TARGET_FIX);
		if (tFile.exists()) {
			tFile.delete();
		}
		table = new SymbolTabel(); // create symbol table
		vmWriter = new VMWriter(tFile); // create VM filer writer
	}
	/**
	 * 编译整个类.
	 */
	public void compileClass() {
		Object value = null;
		/*
		 * set tag to validate whether fields/statics have been defined after
		 * method or not
		 */
		boolean methodTag = false;

		// class
		tokenizer.advance();
		validate("class", (String) tokenizer.getTokenValue(TokenType.KEYWORD));
		// class name
		tokenizer.advance();
		if (!className.equals(tokenizer.getTokenValue(TokenType.IDENTIFIER))) {
			throw new RuntimeException(
					"The class name doesn't match the file name!\n"
							+ tokenizer.getCurrentInfo());
		}
		// {
		tokenizer.advance();
		validate("{", (String) tokenizer.getTokenValue(TokenType.SYMBOL));
		// field/static & subroutine
		tokenizer.advance();
		value = tokenizer.getTokenValue(TokenType.KEYWORD);
		do {
			// field or static
			if (value != null && ((String) value).matches("field|static")) {
				if (methodTag) {
					throw new RuntimeException(
							"field or static must be defined before subroutine!");
				}
				compileClassVarDec();
			}
			// subroutine
			if (value != null
					&& ((String) value).matches("constructor|function|method")) {
				methodTag = true;
				compileSubroutine();
			}
			if (tokenizer.hasMoreTokens()) {
				tokenizer.advance();
				value = tokenizer.getTokenValue(null);
			}
		} while (!"}".equals(value));
		// }
		vmWriter.close();
	}

	/**
	 * 编译静态声明或字段声明.
	 */
	public void compileClassVarDec() {
		TokenType tokenType = null;
		Object value = tokenizer.getTokenValue(TokenType.KEYWORD);

		String type = null;
		Kind kind = null;

		// static define
		if (value != null && (value.toString()).matches("static|field")) {
			// static field
			kind = Kind.valueOf(value.toString().toUpperCase());
			// type
			tokenizer.advance();
			tokenType = tokenizer.getTokenType();
			if (TokenType.KEYWORD.equals(tokenType)) {
				value = tokenizer.getTokenValue(null);
				if (!(value.toString()).matches("int|char|boolean")) {
					throw new RuntimeException(
							"Primary type can only be int char boolean!");
				}
			} else if (TokenType.IDENTIFIER.equals(tokenType)) {
				value = tokenizer.getTokenValue(null);
			}
			type = value.toString();
			// name1, name2, ...;
			do {
				// name1
				tokenizer.advance();
				table.define(tokenizer.getTokenValue(null).toString(), type,
						kind);
				// , or ;
				tokenizer.advance();
				value = tokenizer.getTokenValue(TokenType.SYMBOL);
			} while (",".equals(value));
			// ;
			validate(";", (String) tokenizer.getTokenValue(TokenType.SYMBOL));
		}
	}

	/**
	 * 编译整个方法、函数或构造函数.
	 */
	public void compileSubroutine() {
		TokenType tokenType = null;
		Object value = tokenizer.getTokenValue(TokenType.KEYWORD);

		if (value != null
				&& (value.toString()).matches("constructor|function|method")) {

			String fucName = null;
			boolean isCons = false;
			boolean isMeth = false;
			// constructor function method
			table.startSubroutine(); // clear method-scope
			VMWriter.ifCounter = 0; // reset if Counter
			VMWriter.whileCounter = 0; // reset whileConter
			if ("method".equals(value)) { // need add this
				table.define("this", className, Kind.ARG);
				isMeth = true;
			} else if ("constructor".equals(value)) { // need get memory
				isCons = true;
			}
			// return type
			tokenizer.advance();
			tokenType = tokenizer.getTokenType();
			if (TokenType.KEYWORD.equals(tokenType)) {
				value = tokenizer.getTokenValue(null);
				if (!(value.toString()).matches("int|char|boolean|void")) {
					throw new RuntimeException(
							"Primary return type can only be int char boolean void!");
				}
			} else if (TokenType.IDENTIFIER.equals(tokenType)) {
				value = tokenizer.getTokenValue(null);
			} else {
				throw new RuntimeException(
						"constructor,function or method type error!\n"
								+ tokenizer.getCurrentInfo());
			}
			// name of c f m
			tokenizer.advance();
			fucName = className + "."
					+ tokenizer.getTokenValue(TokenType.IDENTIFIER).toString();
			// (
			tokenizer.advance();
			validate("(", (String) tokenizer.getTokenValue(TokenType.SYMBOL));
			// 参数列表
			compileParameterList();
			// )
			tokenizer.advance();
			validate(")", (String) tokenizer.getTokenValue(TokenType.SYMBOL));
			// {
			tokenizer.advance();
			validate("{", (String) tokenizer.getTokenValue(TokenType.SYMBOL));
			// var statements
			tokenizer.advance();
			List<String> locals = new ArrayList<String>();// locals number
			boolean isWriteFun = false;
			while (!"}".equals(tokenizer.getTokenValue(null))) {
				value = tokenizer.getTokenValue(null);
				if ("var".equals(value)) {
					tokenizer.recede();
					compileVarDec(locals); // var
				} else if ((value.toString()).matches("let|if|while|do|return")) {
					if (!isWriteFun) {
						vmWriter.writeFunction(fucName, locals.size());
						isWriteFun = true;
						if (isCons) {
							vmWriter.writePush(Segment.CONST,
									table.varCount(Kind.FIELD));
							vmWriter.writeCall("Memory.alloc", 1);
							vmWriter.writePop(Segment.POINTER, 0);
							isCons = false;
						} else if (isMeth) {
							vmWriter.writePush(Segment.ARG, 0);
							vmWriter.writePop(Segment.POINTER, 0);
						}
					}
					tokenizer.recede();
					compileStatements(); // statements
				}
				tokenizer.advance();
			}
			// }
			validate("}", (String) tokenizer.getTokenValue(TokenType.SYMBOL));
		}
	}

	/**
	 * 编译参数列表，不包含括号.
	 */
	public void compileParameterList() {
		TokenType tokenType = null;
		Object value = null;
		String type = null;
		boolean isComma = false;

		// read token to compile parameter
		tokenizer.advance();
		value = tokenizer.getTokenValue(null);
		if (")".equals(value)) {
			tokenizer.recede();
			return;
		}

		do {
			// type
			tokenType = tokenizer.getTokenType();
			if (TokenType.KEYWORD.equals(tokenType)) {
				value = tokenizer.getTokenValue(null);
				if (!(value.toString()).matches("int|char|boolean")) {
					throw new RuntimeException(
							"Primary type can only be int char boolean!");
				}
			} else if (TokenType.IDENTIFIER.equals(tokenType)) {
				value = tokenizer.getTokenValue(null);
			} else {
				throw new RuntimeException("parameter type error!"
						+ tokenizer.getCurrentInfo());
			}
			type = value.toString();
			// param name
			tokenizer.advance();
			table.define(tokenizer.getTokenValue(null).toString(), type,
					Kind.ARG);
			// ,
			tokenizer.advance();
			value = tokenizer.getTokenValue(TokenType.SYMBOL);
			isComma = ",".equals(value);
			if (isComma) {
				tokenizer.advance();
			} else {
				tokenizer.recede();
			}
		} while (isComma);
	}

	/**
	 * 编译var声明.
	 */
	public void compileVarDec(List<String> locals) {
		TokenType tokenType = null;
		Object value = null;
		String type = null;

		// var
		tokenizer.advance();
		validate("var", (String) tokenizer.getTokenValue(TokenType.KEYWORD));
		// type
		tokenizer.advance();
		tokenType = tokenizer.getTokenType();
		if (TokenType.KEYWORD.equals(tokenType)) {
			value = tokenizer.getTokenValue(null);
			if (!(value.toString()).matches("int|char|boolean")) {
				throw new RuntimeException(
						"Primary type can only be int char boolean!"
								+ tokenizer.getCurrentInfo());
			}
		} else if (TokenType.IDENTIFIER.equals(tokenType)) {
			value = tokenizer.getTokenValue(null);
		} else {
			throw new RuntimeException("var type error!\n"
					+ tokenizer.getCurrentInfo());
		}
		type = value.toString();
		// names
		do {
			tokenizer.advance();
			table.define(tokenizer.getTokenValue(TokenType.IDENTIFIER)
					.toString(), type, Kind.VAR);
			locals.add(tokenizer.getTokenValue(null).toString());
			// ,name...
			tokenizer.advance();
			value = tokenizer.getTokenValue(TokenType.SYMBOL);
		} while (",".equals(value));

		// ;
		validate(";", (String) tokenizer.getTokenValue(TokenType.SYMBOL));
	}

	/**
	 * 编译一系列语句，不包括大括号.
	 */
	public void compileStatements() {
		Object value = null;
		boolean statEnd = false;

		// let do if while return
		tokenizer.advance();
		value = tokenizer.getTokenValue(null);
		if ("}".equals(value)) {
			tokenizer.recede();
		}
		do {
			if ("let".equals(value)) {
				compileLet();
			} else if ("do".equals(value)) {
				compileDo();
			} else if ("if".equals(value)) {
				compileIf();
			} else if ("while".equals(value)) {
				compileWhile();
			} else if ("return".equals(value)) {
				compileReturn();
			}
			// read next token to find out the end of statements
			tokenizer.advance();
			value = tokenizer.getTokenValue(null);
			if (!value.toString().matches("let|do|if|while|return")) {
				tokenizer.recede();
				statEnd = true;
			}
		} while (!statEnd);
	}

	/**
	 * 编译do语句.
	 */
	public void compileDo() {
		Object value = tokenizer.getTokenValue(TokenType.KEYWORD);

		// do
		// ClassName.subroutineName methodName varName.methodName
		String callName = "";
		int index = 0;
		tokenizer.advance();
		value = tokenizer.getTokenValue(TokenType.IDENTIFIER);
		do {
			callName += value;
			tokenizer.advance();
			value = tokenizer.getTokenValue(null);
		} while (!"(".equals(tokenizer.getTokenValue(null)));

		int dot = callName.indexOf(".");
		if (dot < 0) {
			// 调用自己的方法 需要将方法替换成类名.方法名 并将this指针压入栈
			vmWriter.writePush(Segment.POINTER, 0);
			index++;
			callName = className + "." + callName;
		} else {
			String pre = callName.substring(0, dot);
			// 将被调用的类对象压人调用参数的第1个参数
			if (!Kind.NONE.equals(table.kindOf(pre))) {
				vmWriter.writePush(getSegment(table.kindOf(pre)),
						table.indexOf(pre));
				index++;
				callName = callName.replace(pre, table.typeOf(pre));
			}
		}
		// (
		validate("(", (String) tokenizer.getTokenValue(TokenType.SYMBOL));
		// expList
		index += compileExpressionList();
		// )
		tokenizer.advance();
		validate(")", (String) tokenizer.getTokenValue(TokenType.SYMBOL));
		// ;
		tokenizer.advance();
		validate(";", (String) tokenizer.getTokenValue(TokenType.SYMBOL));

		vmWriter.writeCall(callName, index);
		vmWriter.writePop(Segment.TEMP, 0);
	}

	/**
	 * 编译let语句.
	 */
	public void compileLet() {
		Object value = null;
		String varName = null;
		boolean isArray = false;
		// let
		// identifier
		tokenizer.advance();
		varName = (String) tokenizer.getTokenValue(TokenType.IDENTIFIER);
		// check identifier were array: name[exp]
		tokenizer.advance();
		value = tokenizer.getTokenValue(TokenType.SYMBOL);
		// [exp]
		if ("[".equals(value)) {
			// [
			validate("[", (String) tokenizer.getTokenValue(TokenType.SYMBOL));
			// exp
			compileExpression();
			// ]
			tokenizer.advance();
			validate("]", (String) tokenizer.getTokenValue(TokenType.SYMBOL));
			vmWriter.writePush(getSegment(table.kindOf(varName)),
					table.indexOf(varName));
			vmWriter.writeArithmetic(Command.ADD);
			isArray = true;

		} else {
			tokenizer.recede();
		}
		// =
		tokenizer.advance();
		validate("=", (String) tokenizer.getTokenValue(TokenType.SYMBOL));
		// exp
		compileExpression();
		if (!isArray) {
			vmWriter.writePop(getSegment(table.kindOf(varName)),
					table.indexOf(varName));
		} else {
			vmWriter.writePop(Segment.TEMP, 0);
			vmWriter.writePop(Segment.POINTER, 1);
			vmWriter.writePush(Segment.TEMP, 0);
			vmWriter.writePop(Segment.THAT, 0);
			isArray = false;
		}

		// ;
		tokenizer.advance();
		validate(";", (String) tokenizer.getTokenValue(TokenType.SYMBOL));
	}
	/**
	 * 编译while语句.
	 */
	public void compileWhile() {
		String whileLabel = "WHILE_EXP" + VMWriter.whileCounter;
		String endLabel = "WHILE_END" + VMWriter.whileCounter++;

		// while
		vmWriter.writeLabel(whileLabel);
		// (
		tokenizer.advance();
		validate("(", (String) tokenizer.getTokenValue(TokenType.SYMBOL));
		// exp
		compileExpression();
		vmWriter.writeArithmetic(Command.NOT);
		vmWriter.writeIf(endLabel);
		// )
		tokenizer.advance();
		validate(")", (String) tokenizer.getTokenValue(TokenType.SYMBOL));
		// {
		tokenizer.advance();
		validate("{", (String) tokenizer.getTokenValue(TokenType.SYMBOL));
		// statements
		compileStatements();
		// }
		tokenizer.advance();
		validate("}", (String) tokenizer.getTokenValue(TokenType.SYMBOL));

		vmWriter.writeGoto(whileLabel);
		vmWriter.writeLabel(endLabel);
	}

	/**
	 * 编译return语句.
	 */
	public void compileReturn() {
		Object value = null;
		// return
		// exp
		tokenizer.advance();
		value = tokenizer.getTokenValue(null);
		if (!";".equals(value)) {
			tokenizer.recede();
			// 表达式
			compileExpression();
		} else {
			tokenizer.recede();
			vmWriter.writePush(Segment.CONST, 0);
		}
		// ;
		tokenizer.advance();
		validate(";", (String) tokenizer.getTokenValue(TokenType.SYMBOL));

		vmWriter.writeReturn();
	}

	/**
	 * 编译if语句，包含可选的else从句.
	 */
	public void compileIf() {
		Object value = null;
		String labelTrue = "IF_TRUE" + VMWriter.ifCounter;
		String labelFalse = "IF_FALSE" + VMWriter.ifCounter;
		String labelEnd = "IF_END" + VMWriter.ifCounter++;

		// if
		// (
		tokenizer.advance();
		validate("(", (String) tokenizer.getTokenValue(TokenType.SYMBOL));
		// exp
		compileExpression();
		vmWriter.writeIf(labelTrue);
		vmWriter.writeGoto(labelFalse);
		// )
		tokenizer.advance();
		validate(")", (String) tokenizer.getTokenValue(TokenType.SYMBOL));
		// {
		tokenizer.advance();
		validate("{", (String) tokenizer.getTokenValue(TokenType.SYMBOL));
		vmWriter.writeLabel(labelTrue);
		// statements
		compileStatements();
		// }
		tokenizer.advance();
		validate("}", (String) tokenizer.getTokenValue(TokenType.SYMBOL));

		// else
		tokenizer.advance();
		value = tokenizer.getTokenValue(null);
		if ("else".equals(value)) {
			vmWriter.writeGoto(labelEnd);
			vmWriter.writeLabel(labelFalse);
			// else
			// {
			tokenizer.advance();
			validate("{", (String) tokenizer.getTokenValue(TokenType.SYMBOL));
			// statements
			compileStatements();
			// }
			tokenizer.advance();
			validate("}", (String) tokenizer.getTokenValue(TokenType.SYMBOL));

			vmWriter.writeLabel(labelEnd);
		} else {
			vmWriter.writeLabel(labelFalse);
			tokenizer.recede();
		}
	}

	/**
	 * 编译一个表达式.
	 */
	public void compileExpression() {
		Object value = null;
		boolean opTag = false, isSecondTerm = false;
		String op = null;
		do {
			// term
			compileTerm();
			if (isSecondTerm) {
				isSecondTerm = false;
				vmWriter.writeArithmetic(getCommand(op, false));
				op = null;
			}
			// op
			tokenizer.advance();
			value = tokenizer.getTokenValue(null);
			if (value.toString().matches("\\+|-|\\*|/|\\&|\\||<|=|>")) {
				opTag = true;
				op = value.toString();
			} else {
				tokenizer.recede();
				opTag = false;
			}
			isSecondTerm = true;
		} while (opTag);
	}
	/**
	 * 编译一个term.
	 */
	public void compileTerm() {
		TokenType tokenType = null;
		Object value = null;

		// read token to confirm the structure of term
		tokenizer.advance();
		value = tokenizer.getTokenValue(null);
		tokenType = tokenizer.getTokenType();
		// TYPE_1 ( exp )
		if ("(".equals(value.toString())) {
			// (
			// exp
			compileExpression();
			// )
			tokenizer.advance();
			validate(")", (String) tokenizer.getTokenValue(TokenType.SYMBOL));
		} else if (value.toString().matches("\\-|~")) { // TYPE_2 ~|- term
			// - ~
			String op = value.toString();
			// term
			compileTerm();
			vmWriter.writeArithmetic(getCommand(op, true));
		} else if (TokenType.INT_CONST.equals(tokenType)) { // TYPE_3 intConst
			value = tokenizer.getTokenValue(null);
			vmWriter.writePush(Segment.CONST, (Integer) value);
		} else if (TokenType.STRING_CONST.equals(tokenType)) { // TYPE_4 String
			value = tokenizer.getTokenValue(null);
			vmWriter.writePush(Segment.CONST, ((String) value).length());
			vmWriter.writeCall("String.new", 1);
			for (char c : ((String) value).toCharArray()) {
				vmWriter.writePush(Segment.CONST, c);
				vmWriter.writeCall("String.appendChar", 2);
			}
		} else if (TokenType.KEYWORD.equals(tokenType)) { // TYPE_5 keyword
			value = tokenizer.getTokenValue(null);
			if ("true".equals(value)) {
				vmWriter.writePush(Segment.CONST, 0);
				vmWriter.writeArithmetic(Command.NOT);
			} else if ("false".equals(value) || "null".equals(value)) {
				vmWriter.writePush(Segment.CONST, 0);
			} else if ("this".equals(value)) {
				vmWriter.writePush(Segment.POINTER, 0);
			} else if ("that".equals(value)) {
				vmWriter.writePush(Segment.POINTER, 1);
			}
		} else if (TokenType.IDENTIFIER.equals(tokenType)) {
			// TYPE_6 varName
			String varName = tokenizer.getTokenValue(null).toString();

			tokenizer.advance();
			value = tokenizer.getTokenValue(null);
			// TYPE_8 varName[exp]
			if ("[".equals(value.toString())) {
				// [
				// exp
				compileExpression();
				vmWriter.writePush(getSegment(table.kindOf(varName)),
						table.indexOf(varName));
				vmWriter.writeArithmetic(Command.ADD);
				vmWriter.writePop(Segment.POINTER, 1);
				vmWriter.writePush(Segment.THAT, 0);
				// ]
				tokenizer.advance();
				validate("]",
						(String) tokenizer.getTokenValue(TokenType.SYMBOL));
			} else if ("(".equals(value)) { // TYPE_9 subroutineName(expList)
				// (
				// expList
				compileExpressionList();
				// )
				tokenizer.advance();
				validate(")",
						(String) tokenizer.getTokenValue(TokenType.SYMBOL));
			} else if (".".equals(value)) {
				// TYPE_10 className|varName.subroutineNmae(expList)
				int argsNum = 0;
				// 调用自己的方法 需要将方法替换成类名.方法名 并将变量对应的Segment段指针压入栈
				if (!Kind.NONE.equals(table.kindOf(varName))) {
					// var 本地变量调用自己的方法，要获取Segment,不能写死This
					//vmWriter.writePush(Segment.THIS, table.indexOf(varName));
					vmWriter.writePush(getSegment(table.kindOf(varName)), table.indexOf(varName));
					argsNum++;
					varName = table.typeOf(varName);
				}

				// .
				varName += tokenizer.getTokenValue(TokenType.SYMBOL);
				// 调用自己的方法 需要将方法替换成类名.方法名 并将this指针压入栈
				if (!Kind.NONE.equals(table.kindOf(varName))) {
					vmWriter.writePush(Segment.THIS, 0);
					varName = table.typeOf(varName);
				}
				// sub
				tokenizer.advance();
				varName += tokenizer.getTokenValue(TokenType.IDENTIFIER);
				// (
				tokenizer.advance();
				validate("(",
						(String) tokenizer.getTokenValue(TokenType.SYMBOL));
				// expList
				argsNum += compileExpressionList();
				// )
				tokenizer.advance();
				validate(")",
						(String) tokenizer.getTokenValue(TokenType.SYMBOL));

				vmWriter.writeCall(varName, argsNum);
			} else {
				vmWriter.writePush(getSegment(table.kindOf(varName)),
						table.indexOf(varName));
				tokenizer.recede();
			}
		}
	}

	/**
	 * 编译由逗号分隔的表达式列表.
	 */
	public int compileExpressionList() {
		Object value = null;
		boolean tag = false;

		int expCount = 0;

		// read token to starting compilation
		tokenizer.advance();
		value = tokenizer.getTokenValue(null);
		if (")".equals(value)) {
			tokenizer.recede();
			return expCount = 0;
		}
		tokenizer.recede();
		do {
			// one expression
			compileExpression();

			// read next to find out the end of expression list
			tokenizer.advance();
			value = tokenizer.getTokenValue(null);
			if (value.toString().equals(",")) {
				tag = true;
			} else {
				tokenizer.recede();
				tag = false;
			}
			expCount++;
		} while (tag);

		return expCount;
	}

	/**
	 * get op token Command type
	 * 
	 * @param str
	 *            op string
	 * @param isSingle
	 *            is single op
	 * @return Command
	 */
	public Command getCommand(final String str, final boolean isSingle) {
		Command c = null;
		if ("+".equals(str) && !isSingle) {
			c = Command.ADD;
		} else if ("-".equals(str) && !isSingle) {
			c = Command.SUB;
		} else if ("-".equals(str) && isSingle) {
			c = Command.NEG;
		} else if ("*".equals(str)) {
			c = Command.MUT;
		} else if ("/".equals(str)) {
			c = Command.DIV;
		} else if (">".equals(str)) {
			c = Command.GT;
		} else if ("<".equals(str)) {
			c = Command.LT;
		} else if ("=".equals(str)) {
			c = Command.EQ;
		} else if ("&".equals(str)) {
			c = Command.AND;
		} else if ("|".equals(str)) {
			c = Command.OR;
		} else if ("~".equals(str)) {
			c = Command.NOT;
		}
		return c;
	}

	/**
	 * get segment by kind
	 * 
	 * @param kind
	 * @return
	 */
	private Segment getSegment(final Kind kind) {
		Segment seg = null;
		switch (kind) {
			case ARG :
				seg = Segment.ARG;
				break;
			case VAR :
				seg = Segment.LOCAL;
				break;
			case FIELD :
				seg = Segment.THIS;
				break;
			case STATIC :
				seg = Segment.STATIC;
				break;
			case NONE :
				throw new RuntimeException("Can not find segment of "
						+ kind.toString() + "\n" + tokenizer.getCurrentInfo());
		}
		return seg;
	}

	/**
	 * validate token
	 * 
	 * @param expected
	 *            expected token
	 * @param actual
	 *            actual token
	 */
	private void validate(final String expected, final String actual) {
		if (!expected.equals(actual)) {
			throw new RuntimeException("Syntax expected token '" + expected
					+ "' but actual was '" + actual + "'\n"
					+ tokenizer.getCurrentInfo());
		}
	}
}
