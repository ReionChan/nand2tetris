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

import org.reion.JackTokenizer.TokenType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * 执行编译输出.
 * 
 * @author Reion
 * 
 */
public class CompilationEngine {

	// 目标文件
	private File tFile;
	// 字元分析器
	private JackTokenizer tokenizer;
	// DOM对象
	private Document document;
	// root元素
	private Element root;

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
		File output = new File(sFile.getParent() + File.separator + "output");
		if (!output.exists()) {
			output.mkdir();
		}
		tFile = new File(output.getPath()
				+ File.separator
				+ sFile.getName().replace(JackAnalyzer.SOURCE_FIX,
						JackAnalyzer.TARGET_FIX));
		if (tFile.exists()) {
			tFile.delete();
		}
	}

	/**
	 * 编译整个类.
	 */
	public void compileClass() {
		Element ele = null;
		Object value = null;
		boolean methodTag = false;

		document = XmlUtils.getDocument();
		root = document.createElement("class");
		// class
		tokenizer.advance();
		ele = createElemnet(TokenType.KEYWORD);
		root.appendChild(ele);
		// class name
		tokenizer.advance();
		ele = createElemnet(TokenType.IDENTIFIER);
		root.appendChild(ele);
		// {
		tokenizer.advance();
		ele = createElemnet(TokenType.SYMBOL);
		root.appendChild(ele);

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
		ele = createElemnet(TokenType.SYMBOL);
		root.appendChild(ele);
		XmlUtils.writeXml(tFile, root);
	}

	/**
	 * 编译静态声明或字段声明.
	 */
	public void compileClassVarDec() {
		Element ele = null, classVarEle = null;
		TokenType tokenType = null;
		Object value = tokenizer.getTokenValue(TokenType.KEYWORD);

		// static define
		if (value != null && (value.toString()).matches("static|field")) {
			classVarEle = document.createElement("classVarDec");
			// static field
			ele = createElemnet(TokenType.KEYWORD);
			classVarEle.appendChild(ele);
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
			ele = createElemnet(null);
			classVarEle.appendChild(ele);
			// name1, name2, ...;
			do {
				// name1
				tokenizer.advance();
				ele = createElemnet(TokenType.IDENTIFIER);
				classVarEle.appendChild(ele);
				// , or ;
				tokenizer.advance();
				ele = createElemnet(TokenType.SYMBOL);
				classVarEle.appendChild(ele);
				value = tokenizer.getTokenValue(TokenType.SYMBOL);
			} while (",".equals(value));
		}
		// add to class node
		if (classVarEle != null) {
			root.appendChild(classVarEle);
		}
	}

	/**
	 * 编译整个方法、函数或构造函数.
	 */
	public void compileSubroutine() {
		Element ele = null, subroutineEle = null, bodyEle;
		TokenType tokenType = null;
		Object value = tokenizer.getTokenValue(TokenType.KEYWORD);

		if (value != null
				&& (value.toString()).matches("constructor|function|method")) {
			subroutineEle = document.createElement("subroutineDec");
			// constructor function method
			ele = createElemnet(TokenType.KEYWORD);
			subroutineEle.appendChild(ele);
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
			}
			ele = createElemnet(null);
			subroutineEle.appendChild(ele);
			// name of c f m
			tokenizer.advance();
			ele = createElemnet(TokenType.IDENTIFIER);
			subroutineEle.appendChild(ele);
			// (
			tokenizer.advance();
			ele = createElemnet(TokenType.SYMBOL);
			subroutineEle.appendChild(ele);
			// 参数列表
			subroutineEle.appendChild(compileParameterList());
			// )
			tokenizer.advance();
			ele = createElemnet(TokenType.SYMBOL);
			subroutineEle.appendChild(ele);
			// subroutineBody
			bodyEle = document.createElement("subroutineBody");
			subroutineEle.appendChild(bodyEle);
			// {
			tokenizer.advance();
			ele = createElemnet(TokenType.SYMBOL);
			bodyEle.appendChild(ele);

			// var statements
			tokenizer.advance();
			value = tokenizer.getTokenValue(null);
			while (!"}".equals(tokenizer.getTokenValue(null))) {
				value = tokenizer.getTokenValue(null);
				if ("var".equals(value)) {
					tokenizer.recede();
					bodyEle.appendChild(compileVarDec()); // var
				} else if ((value.toString()).matches("let|if|while|do|return")) {
					tokenizer.recede();
					bodyEle.appendChild(compileStatements()); // statements
				}
				tokenizer.advance();
			}

			// }
			ele = createElemnet(TokenType.SYMBOL);
			bodyEle.appendChild(ele);
		}
		// add to class node
		if (subroutineEle != null) {
			root.appendChild(subroutineEle);
		}
	}

	/**
	 * 编译参数列表，不包含括号.
	 */
	public Element compileParameterList() {
		Element ele = null, paramListEle = null;
		TokenType tokenType = null;
		Object value = null;

		paramListEle = document.createElement("parameterList");

		tokenizer.advance();
		value = tokenizer.getTokenValue(null);
		if (")".equals(value)) {
			tokenizer.recede();
			return paramListEle;
		}

		boolean isComma = false;
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
			}
			ele = createElemnet(null);
			paramListEle.appendChild(ele);
			// param name
			tokenizer.advance();
			ele = createElemnet(TokenType.IDENTIFIER);
			paramListEle.appendChild(ele);

			// ,
			tokenizer.advance();
			value = tokenizer.getTokenValue(TokenType.SYMBOL);
			isComma = ",".equals(value);
			if (isComma) {
				ele = createElemnet(null);
				paramListEle.appendChild(ele);
				tokenizer.advance();
			} else {
				tokenizer.recede();
			}
		} while (isComma);
		return paramListEle;
	}

	/**
	 * 编译var声明.
	 */
	public Element compileVarDec() {
		Element ele = null, varDecEle = null;
		TokenType tokenType = null;
		Object value = null;

		varDecEle = document.createElement("varDec");

		// var
		tokenizer.advance();
		ele = createElemnet(TokenType.KEYWORD);
		varDecEle.appendChild(ele);
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
		ele = createElemnet(null);
		varDecEle.appendChild(ele);
		// var names
		do {
			tokenizer.advance();
			ele = createElemnet(TokenType.IDENTIFIER);
			varDecEle.appendChild(ele);

			tokenizer.advance();
			value = tokenizer.getTokenValue(TokenType.SYMBOL);
			if (",".equals(value)) {
				ele = createElemnet(null);
				varDecEle.appendChild(ele);
			}
		} while (",".equals(value));

		// ;
		ele = createElemnet(TokenType.SYMBOL);
		varDecEle.appendChild(ele);

		return varDecEle;
	}

	/**
	 * 编译一系列语句，不包括大括号.
	 */
	public Element compileStatements() {
		Element ele = null, statementsEle = null;
		Object value = null;
		boolean statEnd = false;
		statementsEle = document.createElement("statements");

		tokenizer.advance();
		value = tokenizer.getTokenValue(null);
		if ("}".equals(value)) {
			tokenizer.recede();
		}
		do {
			if ("let".equals(value)) {
				ele = compileLet();
			} else if ("do".equals(value)) {
				ele = compileDo();
			} else if ("if".equals(value)) {
				ele = compileIf();
			} else if ("while".equals(value)) {
				ele = compileWhile();
			} else if ("return".equals(value)) {
				ele = compileReturn();
			}
			statementsEle.appendChild(ele);
			tokenizer.advance();
			value = tokenizer.getTokenValue(null);
			if (!value.toString().matches("let|do|if|while|return")) {
				tokenizer.recede();
				statEnd = true;
			}
		} while (!statEnd);

		return statementsEle;
	}

	/**
	 * 编译do语句.
	 */
	public Element compileDo() {
		Element ele = null, doStaEle = null;
		Object value = tokenizer.getTokenValue(TokenType.KEYWORD);

		doStaEle = document.createElement("doStatement");

		// do
		value = tokenizer.getTokenValue(TokenType.KEYWORD);
		ele = createElemnet(TokenType.KEYWORD);
		doStaEle.appendChild(ele);

		// ClassName.subroutineName methodName varName.methodName
		tokenizer.advance();
		value = tokenizer.getTokenValue(TokenType.IDENTIFIER);
		do {
			ele = createElemnet(null);
			doStaEle.appendChild(ele);

			tokenizer.advance();
			value = tokenizer.getTokenValue(null);
		} while (!"(".equals(value));

		// (
		ele = createElemnet(TokenType.SYMBOL);
		doStaEle.appendChild(ele);
		// expList
		doStaEle.appendChild(compileExpressionList());
		// )
		tokenizer.advance();
		ele = createElemnet(TokenType.SYMBOL);
		doStaEle.appendChild(ele);
		// ;
		tokenizer.advance();
		ele = createElemnet(TokenType.SYMBOL);
		doStaEle.appendChild(ele);
		return doStaEle;
	}

	/**
	 * 编译let语句.
	 */
	public Element compileLet() {
		Element ele = null, letStaEle = null;
		Object value = null;

		letStaEle = document.createElement("letStatement");

		// let
		ele = createElemnet(TokenType.KEYWORD);
		letStaEle.appendChild(ele);
		// identifier
		tokenizer.advance();
		ele = createElemnet(TokenType.IDENTIFIER);
		letStaEle.appendChild(ele);

		tokenizer.advance();
		value = tokenizer.getTokenValue(TokenType.SYMBOL);
		// [exp]
		if ("[".equals(value)) {
			// [
			ele = createElemnet(null);
			letStaEle.appendChild(ele);

			// exp
			letStaEle.appendChild(compileExpression());

			// ]
			tokenizer.advance();
			ele = createElemnet(TokenType.SYMBOL);
			letStaEle.appendChild(ele);
		} else {
			tokenizer.recede();
		}
		// =
		tokenizer.advance();
		ele = createElemnet(TokenType.SYMBOL);
		letStaEle.appendChild(ele);
		// exp
		letStaEle.appendChild(compileExpression());
		// ;
		tokenizer.advance();
		ele = createElemnet(TokenType.SYMBOL);
		letStaEle.appendChild(ele);
		return letStaEle;
	}

	/**
	 * 编译while语句.
	 */
	public Element compileWhile() {
		Element ele = null, whileStatEle = null;
		Object value = null;

		whileStatEle = document.createElement("whileStatement");

		// while
		ele = createElemnet(TokenType.KEYWORD);
		whileStatEle.appendChild(ele);
		// (
		tokenizer.advance();
		ele = createElemnet(TokenType.SYMBOL);
		whileStatEle.appendChild(ele);
		// exp
		whileStatEle.appendChild(compileExpression());
		// )
		tokenizer.advance();
		ele = createElemnet(TokenType.SYMBOL);
		whileStatEle.appendChild(ele);

		// {
		tokenizer.advance();
		ele = createElemnet(TokenType.SYMBOL);
		whileStatEle.appendChild(ele);

		// statements
		whileStatEle.appendChild(compileStatements());

		// }
		tokenizer.advance();
		value = tokenizer.getTokenValue(TokenType.SYMBOL);
		if (!"}".equals(value)) {
			throw new RuntimeException(
					"while statement syntax error, missing '}'!");
		}
		ele = createElemnet(null);
		whileStatEle.appendChild(ele);

		return whileStatEle;
	}

	/**
	 * 编译return语句.
	 */
	public Element compileReturn() {
		Element ele = null, retStaEle = null;
		Object value = null;

		retStaEle = document.createElement("returnStatement");

		// return
		ele = createElemnet(TokenType.KEYWORD);
		retStaEle.appendChild(ele);

		tokenizer.advance();
		value = tokenizer.getTokenValue(null);
		// exp
		if (!";".equals(value)) {
			tokenizer.recede();
			// 表达式
			retStaEle.appendChild(compileExpression());
		} else {
			tokenizer.recede();
		}
		// ;
		tokenizer.advance();
		ele = createElemnet(TokenType.SYMBOL);
		retStaEle.appendChild(ele);

		return retStaEle;
	}

	/**
	 * 编译if语句，包含可选的else从句.
	 */
	public Element compileIf() {
		Element ele = null, ifStatEle = null;
		Object value = null;

		ifStatEle = document.createElement("ifStatement");

		// if
		ele = createElemnet(TokenType.KEYWORD);
		ifStatEle.appendChild(ele);
		// (
		tokenizer.advance();
		ele = createElemnet(TokenType.SYMBOL);
		ifStatEle.appendChild(ele);

		// exp
		ifStatEle.appendChild(compileExpression());
		// )
		tokenizer.advance();
		ele = createElemnet(TokenType.SYMBOL);
		ifStatEle.appendChild(ele);

		// {
		tokenizer.advance();
		ele = createElemnet(TokenType.SYMBOL);
		ifStatEle.appendChild(ele);

		// statements
		ifStatEle.appendChild(compileStatements());

		// }
		tokenizer.advance();
		value = tokenizer.getTokenValue(TokenType.SYMBOL);
		if (!"}".equals(value)) {
			throw new RuntimeException(
					"if statement syntax error, missing '}'!");
		}
		ele = createElemnet(null);
		ifStatEle.appendChild(ele);
		
		// else
		tokenizer.advance();
		value = tokenizer.getTokenValue(null);
		if ("else".equals(value)) {
			// else
			ele = createElemnet(null);
			ifStatEle.appendChild(ele);
			// {
			tokenizer.advance();
			ele = createElemnet(TokenType.SYMBOL);
			ifStatEle.appendChild(ele);

			// statements
			ifStatEle.appendChild(compileStatements());

			// }
			tokenizer.advance();
			value = tokenizer.getTokenValue(TokenType.SYMBOL);
			if (!"}".equals(value)) {
				throw new RuntimeException(
						"else statement syntax error, missing '}'!");
			}
			ele = createElemnet(null);
			ifStatEle.appendChild(ele);
		} else {
			tokenizer.recede();
		}
		
		return ifStatEle;
	}

	/**
	 * 编译一个表达式.
	 */
	public Element compileExpression() {
		Element ele = null, expEle = null;
		Object value = null;
		boolean opTag = false;
		expEle = document.createElement("expression");
		do {
			// term
			ele = compileTerm();
			expEle.appendChild(ele);
			tokenizer.advance();
			value = tokenizer.getTokenValue(null);
			// op
			if (value.toString().matches("\\+|-|\\*|/|\\&|\\||<|=|>|~")) {
				ele = createElemnet(null);
				expEle.appendChild(ele);
				opTag = true;
			} else {
				tokenizer.recede();
				opTag = false;
			}
		} while (opTag);

		return expEle;
	}
	/**
	 * 编译一个term.
	 */
	public Element compileTerm() {
		Element ele = null, termEle = null;
		TokenType tokenType = null;
		Object value = null;

		termEle = document.createElement("term");

		tokenizer.advance();
		value = tokenizer.getTokenValue(null);
		tokenType = tokenizer.getTokenType();
		// ( exp )
		if ("(".equals(value.toString())) {
			// (
			ele = createElemnet(TokenType.SYMBOL);
			termEle.appendChild(ele);
			// exp
			termEle.appendChild(compileExpression());
			// )
			tokenizer.advance();
			ele = createElemnet(TokenType.SYMBOL);
			termEle.appendChild(ele);
		} else if (value.toString().matches("\\-|~")) { // ~|- term
			// - ~
			ele = createElemnet(null);
			termEle.appendChild(ele);
			// term
			termEle.appendChild(compileTerm());
		} else if (TokenType.INT_CONST.equals(tokenType)
				|| TokenType.STRING_CONST.equals(tokenType)
				|| TokenType.KEYWORD.equals(tokenType)) { // 123 "abc" KEYWORD
			ele = createElemnet(tokenType);
			termEle.appendChild(ele);
		} else if (TokenType.IDENTIFIER.equals(tokenType)) {
			// name
			ele = createElemnet(TokenType.IDENTIFIER);
			termEle.appendChild(ele);

			tokenizer.advance();
			value = tokenizer.getTokenValue(null);

			// name[]
			if ("[".equals(value.toString())) {
				ele = createElemnet(TokenType.SYMBOL);
				termEle.appendChild(ele);
				// exp
				//
				termEle.appendChild(compileExpression());
				tokenizer.advance();
				ele = createElemnet(TokenType.SYMBOL);
				termEle.appendChild(ele);
			} else if ("(".equals(value)) { // name(expList)
				ele = createElemnet(TokenType.SYMBOL);
				termEle.appendChild(ele);
				// expList
				termEle.appendChild(compileExpressionList());
				// )
				tokenizer.advance();
				ele = createElemnet(TokenType.SYMBOL);
				termEle.appendChild(ele);
			} else if (".".equals(value)) { // name.sub(expList)
				ele = createElemnet(TokenType.SYMBOL);
				termEle.appendChild(ele);
				// sub
				tokenizer.advance();
				ele = createElemnet(TokenType.IDENTIFIER);
				termEle.appendChild(ele);
				// (
				tokenizer.advance();
				ele = createElemnet(TokenType.SYMBOL);
				termEle.appendChild(ele);
				// expList
				termEle.appendChild(compileExpressionList());
				// )
				tokenizer.advance();
				ele = createElemnet(TokenType.SYMBOL);
				termEle.appendChild(ele);
			} else {
				tokenizer.recede();
			}
		}
		return termEle;
	}

	/**
	 * 创建一个DOM元素.
	 * @param checkType
	 *            校验类型
	 * 
	 * @return Element
	 */
	private Element createElemnet(TokenType checkType) {
		Element ele;
		ele = document.createElement(tokenizer.getTokenType().toString());
		ele.setTextContent(" " + tokenizer.getTokenValue(checkType) + " ");
		return ele;
	}

	/**
	 * 编译由逗号分隔的表达式列表.
	 */
	public Element compileExpressionList() {
		Element ele = null, expListEle = null;
		Object value = null;
		boolean tag = false;

		expListEle = document.createElement("expressionList");
		tokenizer.advance();
		value = tokenizer.getTokenValue(null);
		if (")".equals(value)) {
			tokenizer.recede();
			return expListEle;
		}
		tokenizer.recede();
		do {
			// hasRecursionTerm = true;
			expListEle.appendChild(compileExpression());
			tokenizer.advance();
			value = tokenizer.getTokenValue(null);
			if (value.toString().equals(",")) {
				ele = createElemnet(null);
				expListEle.appendChild(ele);
				tag = true;
			} else {
				tokenizer.recede();
				tag = false;
			}
		} while (tag);

		return expListEle;
	}
}
