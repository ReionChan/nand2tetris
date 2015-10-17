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
		tFile = new File(tokenizer.getJFile().getAbsolutePath()
				.replace(JackAnalyzer.SOURCE_FIX, JackAnalyzer.TARGET_FIX));

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
		ele = document.createElement(tokenizer.getTokenType().toString());
		ele.setTextContent(" " + tokenizer.getTokenValue(TokenType.KEYWORD)
				+ " ");
		root.appendChild(ele);
		// class name
		tokenizer.advance();
		ele = document.createElement(tokenizer.getTokenType().toString());
		ele.setTextContent(" " + tokenizer.getTokenValue(TokenType.IDENTIFIER)
				+ " ");
		root.appendChild(ele);
		// {
		tokenizer.advance();
		ele = document.createElement(tokenizer.getTokenType().toString());
		ele.setTextContent(" " + tokenizer.getTokenValue(TokenType.SYMBOL)
				+ " ");
		root.appendChild(ele);

		tokenizer.advance();
		value = tokenizer.getTokenValue(TokenType.KEYWORD);
		do {
			// field or static
			if (tokenizer.getTokenValue(null) != null
					&& ((String) tokenizer.getTokenValue(null))
							.matches("field|static")) {
				if (methodTag) {
					throw new RuntimeException(
							"field or static must be defined before subroutine!");
				}
				compileClassVarDec();
			}
			// subroutine
			if (tokenizer.getTokenValue(null) != null
					&& ((String) tokenizer.getTokenValue(null))
							.matches("constructor|function|method")) {
				methodTag = true;
				compileSubroutine();
			}
			if (tokenizer.hasMoreTokens()) {
				tokenizer.advance();
			}
		} while (!"}".equals(tokenizer.getTokenValue(null)));

		// }
		value = tokenizer.getTokenValue(TokenType.SYMBOL);
		ele = document.createElement(tokenizer.getTokenType().toString());
		ele.setTextContent(" " + value + " ");
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
			ele = document.createElement(tokenizer.getTokenType().toString());
			ele.setTextContent(" " + value + " ");
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
			ele = document.createElement(tokenType.name().toLowerCase());
			ele.setTextContent(" " + value + " ");
			classVarEle.appendChild(ele);
			// name1, name2, ...;
			do {
				// name1
				tokenizer.advance();
				value = tokenizer.getTokenValue(TokenType.IDENTIFIER);
				ele = document.createElement(tokenizer.getTokenType()
						.toString());
				ele.setTextContent(" " + value + " ");
				classVarEle.appendChild(ele);
				// , or ;
				tokenizer.advance();
				value = tokenizer.getTokenValue(TokenType.SYMBOL);
				ele = document.createElement(tokenizer.getTokenType()
						.toString());
				ele.setTextContent(" " + value + " ");
				classVarEle.appendChild(ele);
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
			ele = document.createElement(tokenizer.getTokenType().toString());
			ele.setTextContent(" " + value + " ");
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
			ele = document.createElement(tokenizer.getTokenType().toString());
			ele.setTextContent(" " + value + " ");
			subroutineEle.appendChild(ele);
			// name of c f m
			tokenizer.advance();
			value = tokenizer.getTokenValue(TokenType.IDENTIFIER);
			ele = document.createElement(tokenizer.getTokenType().toString());
			ele.setTextContent(" " + value + " ");
			subroutineEle.appendChild(ele);
			// (
			tokenizer.advance();
			value = tokenizer.getTokenValue(TokenType.SYMBOL);
			ele = document.createElement(tokenizer.getTokenType().toString());
			ele.setTextContent(" " + value + " ");
			subroutineEle.appendChild(ele);
			// 参数列表
			subroutineEle.appendChild(compileParameterList());
			// )
			tokenizer.advance();
			value = tokenizer.getTokenValue(TokenType.SYMBOL);
			ele = document.createElement(tokenizer.getTokenType().toString());
			ele.setTextContent(" " + value + " ");
			subroutineEle.appendChild(ele);
			// subroutineBody
			bodyEle = document.createElement("subroutineBody");
			subroutineEle.appendChild(bodyEle);
			// {
			tokenizer.advance();
			value = tokenizer.getTokenValue(TokenType.SYMBOL);
			ele = document.createElement(tokenizer.getTokenType().toString());
			ele.setTextContent(" " + value + " ");
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
			value = tokenizer.getTokenValue(TokenType.SYMBOL);
			ele = document.createElement(tokenizer.getTokenType().toString());
			ele.setTextContent(" " + value + " ");
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
			ele = document.createElement(tokenType.name().toLowerCase());
			ele.setTextContent(" " + value + " ");
			paramListEle.appendChild(ele);
			// param name
			tokenizer.advance();
			value = tokenizer.getTokenValue(TokenType.IDENTIFIER);
			ele = document.createElement(tokenizer.getTokenType().toString());
			ele.setTextContent(" " + value + " ");
			paramListEle.appendChild(ele);

			// ,
			tokenizer.advance();
			value = tokenizer.getTokenValue(TokenType.SYMBOL);
			isComma = ",".equals(value);
			if (isComma) {
				ele = document.createElement(tokenizer.getTokenType()
						.toString());
				ele.setTextContent(" " + value + " ");
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
		value = tokenizer.getTokenValue(TokenType.KEYWORD);
		ele = document.createElement(tokenizer.getTokenType().toString());
		ele.setTextContent(" " + value + " ");
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
		ele = document.createElement(tokenType.name().toLowerCase());
		ele.setTextContent(" " + value + " ");
		varDecEle.appendChild(ele);
		// var names
		do {
			tokenizer.advance();
			value = tokenizer.getTokenValue(TokenType.IDENTIFIER);
			ele = document.createElement(tokenizer.getTokenType().toString());
			ele.setTextContent(" " + value + " ");
			varDecEle.appendChild(ele);

			tokenizer.advance();
			value = tokenizer.getTokenValue(TokenType.SYMBOL);
		} while (",".equals(value));

		// ;
		value = tokenizer.getTokenValue(TokenType.SYMBOL);
		ele = document.createElement(tokenizer.getTokenType().toString());
		ele.setTextContent(" " + value + " ");
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
		value = tokenizer.getTokenValue(TokenType.KEYWORD);
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
		// TokenType tokenType = null;
		Object value = tokenizer.getTokenValue(TokenType.KEYWORD);

		doStaEle = document.createElement("doStatement");

		// do
		value = tokenizer.getTokenValue(TokenType.KEYWORD);
		ele = document.createElement(tokenizer.getTokenType().toString());
		ele.setTextContent(" " + value + " ");
		doStaEle.appendChild(ele);

		// ClassName.subroutineName methodName varName.methodName
		tokenizer.advance();
		value = tokenizer.getTokenValue(TokenType.IDENTIFIER);
		do {
			ele = document.createElement(tokenizer.getTokenType().toString());
			ele.setTextContent(" " + value + " ");
			doStaEle.appendChild(ele);

			tokenizer.advance();
			value = tokenizer.getTokenValue(null);
		} while (!"(".equals(value));

		// (
		value = tokenizer.getTokenValue(TokenType.SYMBOL);
		ele = document.createElement(tokenizer.getTokenType().toString());
		ele.setTextContent(" " + value + " ");
		doStaEle.appendChild(ele);
		// expList
		doStaEle.appendChild(compileExpressionList());
		// )
		tokenizer.advance();
		value = tokenizer.getTokenValue(TokenType.SYMBOL);
		ele = document.createElement(tokenizer.getTokenType().toString());
		ele.setTextContent(" " + value + " ");
		doStaEle.appendChild(ele);
		// ;
		tokenizer.advance();
		value = tokenizer.getTokenValue(TokenType.SYMBOL);
		ele = document.createElement(tokenizer.getTokenType().toString());
		ele.setTextContent(" " + value + " ");
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
		value = tokenizer.getTokenValue(TokenType.KEYWORD);
		ele = document.createElement(tokenizer.getTokenType().toString());
		ele.setTextContent(" " + value + " ");
		letStaEle.appendChild(ele);
		// identifier
		tokenizer.advance();
		value = tokenizer.getTokenValue(TokenType.IDENTIFIER);
		ele = document.createElement(tokenizer.getTokenType().toString());
		ele.setTextContent(" " + value + " ");
		letStaEle.appendChild(ele);

		tokenizer.advance();
		value = tokenizer.getTokenValue(TokenType.SYMBOL);
		// [exp]
		if ("[".equals(value)) {
			// [
			ele = document.createElement(tokenizer.getTokenType().toString());
			ele.setTextContent(" " + value + " ");
			letStaEle.appendChild(ele);

			// exp
			letStaEle.appendChild(compileExpression());

			// ]
			tokenizer.advance();
			value = tokenizer.getTokenValue(TokenType.SYMBOL);
			ele = document.createElement(tokenizer.getTokenType().toString());
			ele.setTextContent(" " + value + " ");
			letStaEle.appendChild(ele);
		} else {
			tokenizer.recede();
		}
		// =
		tokenizer.advance();
		value = tokenizer.getTokenValue(TokenType.SYMBOL);
		ele = document.createElement(tokenizer.getTokenType().toString());
		ele.setTextContent(" " + value + " ");
		letStaEle.appendChild(ele);
		// exp
		letStaEle.appendChild(compileExpression());
		// ;
		tokenizer.advance();
		value = tokenizer.getTokenValue(TokenType.SYMBOL);
		ele = document.createElement(tokenizer.getTokenType().toString());
		ele.setTextContent(" " + value + " ");
		letStaEle.appendChild(ele);
		return letStaEle;
	}

	/**
	 * 编译while语句.
	 */
	public Element compileWhile() {
		Element ele = null, whileStatEle = null;
		// TokenType tokenType = null;
		Object value = null;

		whileStatEle = document.createElement("whileStatement");

		// while
		value = tokenizer.getTokenValue(TokenType.KEYWORD);
		ele = document.createElement(tokenizer.getTokenType().toString());
		ele.setTextContent(" " + value + " ");
		whileStatEle.appendChild(ele);
		// (
		tokenizer.advance();
		value = tokenizer.getTokenValue(TokenType.SYMBOL);
		ele = document.createElement(tokenizer.getTokenType().toString());
		ele.setTextContent(" " + value + " ");
		whileStatEle.appendChild(ele);
		// exp
		whileStatEle.appendChild(compileExpression());
		// )
		tokenizer.advance();
		value = tokenizer.getTokenValue(TokenType.SYMBOL);
		ele = document.createElement(tokenizer.getTokenType().toString());
		ele.setTextContent(" " + value + " ");
		whileStatEle.appendChild(ele);

		// {
		tokenizer.advance();
		value = tokenizer.getTokenValue(TokenType.SYMBOL);
		ele = document.createElement(tokenizer.getTokenType().toString());
		ele.setTextContent(" " + value + " ");
		whileStatEle.appendChild(ele);

		// statements
		whileStatEle.appendChild(compileStatements());

		// }
		tokenizer.advance();
		value = tokenizer.getTokenValue(TokenType.SYMBOL);
		if (!"}".equals(value)) {
			throw new RuntimeException(
					"Return statement must be fowllowed by symbol '}'!");
		}
		ele = document.createElement(tokenizer.getTokenType().toString());
		ele.setTextContent(" " + value + " ");
		whileStatEle.appendChild(ele);

		return whileStatEle;
	}

	/**
	 * 编译return语句.
	 */
	public Element compileReturn() {
		Element ele = null, retStaEle = null;
		// TokenType tokenType = null;
		Object value = null;

		retStaEle = document.createElement("returnStatement");

		// return
		value = tokenizer.getTokenValue(TokenType.KEYWORD);
		ele = document.createElement(tokenizer.getTokenType().toString());
		ele.setTextContent(" " + value + " ");
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
		value = tokenizer.getTokenValue(TokenType.SYMBOL);
		ele = document.createElement(tokenizer.getTokenType().toString());
		ele.setTextContent(" " + value + " ");
		retStaEle.appendChild(ele);

		return retStaEle;
	}

	/**
	 * 编译if语句，包含可选的else从句.
	 */
	public Element compileIf() {
		Element ele = null, ifStatEle = null;
		// TokenType tokenType = null;
		Object value = null;

		ifStatEle = document.createElement("ifStatement");

		// if
		value = tokenizer.getTokenValue(TokenType.KEYWORD);
		ele = document.createElement(tokenizer.getTokenType().toString());
		ele.setTextContent(" " + value + " ");
		ifStatEle.appendChild(ele);
		// (
		tokenizer.advance();
		value = tokenizer.getTokenValue(TokenType.SYMBOL);
		ele = document.createElement(tokenizer.getTokenType().toString());
		ele.setTextContent(" " + value + " ");
		ifStatEle.appendChild(ele);

		// exp
		ifStatEle.appendChild(compileExpression());
		// )
		tokenizer.advance();
		value = tokenizer.getTokenValue(TokenType.SYMBOL);
		ele = document.createElement(tokenizer.getTokenType().toString());
		ele.setTextContent(" " + value + " ");
		ifStatEle.appendChild(ele);

		// {
		tokenizer.advance();
		value = tokenizer.getTokenValue(TokenType.SYMBOL);
		ele = document.createElement(tokenizer.getTokenType().toString());
		ele.setTextContent(" " + value + " ");
		ifStatEle.appendChild(ele);

		// statements
		ifStatEle.appendChild(compileStatements());

		// }
		tokenizer.advance();
		value = tokenizer.getTokenValue(TokenType.SYMBOL);
		if (!"}".equals(value)) {
			throw new RuntimeException(
					"Return statement must be fowllowed by symbol '}'!");
		}
		ele = document.createElement(tokenizer.getTokenType().toString());
		ele.setTextContent(" " + value + " ");
		ifStatEle.appendChild(ele);

		// else
		tokenizer.advance();
		value = tokenizer.getTokenValue(null);
		if ("else".equals(value)) {
			// else
			ele = document.createElement(tokenizer.getTokenType().toString());
			ele.setTextContent(" " + value + " ");
			ifStatEle.appendChild(ele);
			// {
			tokenizer.advance();
			value = tokenizer.getTokenValue(TokenType.SYMBOL);
			ele = document.createElement(tokenizer.getTokenType().toString());
			ele.setTextContent(" " + value + " ");
			ifStatEle.appendChild(ele);

			// statements
			ifStatEle.appendChild(compileStatements());

			// }
			tokenizer.advance();
			value = tokenizer.getTokenValue(TokenType.SYMBOL);
			if (!"}".equals(value)) {
				throw new RuntimeException(
						"Return statement must be fowllowed by symbol '}'!");
			}
			ele = document.createElement(tokenizer.getTokenType().toString());
			ele.setTextContent(" " + value + " ");
			ifStatEle.appendChild(ele);
		}
		tokenizer.recede();
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
				ele = document.createElement(tokenizer.getTokenType()
						.toString());
				ele.setTextContent(" " + tokenizer.getTokenValue(null) + " ");
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
			ele = document.createElement(tokenType.toString());
			ele.setTextContent(" " + value + " ");
			termEle.appendChild(ele);
			// exp
			termEle.appendChild(compileExpression());
			// )
			tokenizer.advance();
			value = tokenizer.getTokenValue(TokenType.SYMBOL);
			ele = document.createElement(tokenType.toString());
			ele.setTextContent(" " + value + " ");
			termEle.appendChild(ele);
		} else if (value.toString().matches("\\-|~")) { // ~|- term
			// - ~
			ele = document.createElement(tokenType.toString());
			ele.setTextContent(" " + value + " ");
			termEle.appendChild(ele);
			// term
			termEle.appendChild(compileTerm());
		} else if (TokenType.INT_CONST.equals(tokenType)
				|| TokenType.STRING_CONST.equals(tokenType)
				|| TokenType.KEYWORD.equals(tokenType)) { // 123 "abc" KEYWORD
			ele = document.createElement(tokenType.toString());
			ele.setTextContent(" " + value + " ");
			termEle.appendChild(ele);
		} else if (TokenType.IDENTIFIER.equals(tokenType)) {
			// name
			value = tokenizer.getTokenValue(null);
			ele = document.createElement(tokenType.toString());
			ele.setTextContent(" " + value + " ");
			termEle.appendChild(ele);

			tokenizer.advance();
			value = tokenizer.getTokenValue(null);

			// name[]
			if ("[".equals(value.toString())) {
				// [
				ele = document.createElement(tokenType.toString());
				ele.setTextContent(" "
						+ tokenizer.getTokenValue(TokenType.SYMBOL) + " ");
				termEle.appendChild(ele);
				// exp
				tokenizer.advance();
				termEle.appendChild(compileExpression());
				// ]
				ele = document.createElement(tokenType.toString());
				ele.setTextContent(" "
						+ tokenizer.getTokenValue(TokenType.SYMBOL) + " ");
				termEle.appendChild(ele);
			} else if ("(".equals(value)) { // name(expList)
				// (
				ele = document.createElement(tokenType.toString());
				ele.setTextContent(" "
						+ tokenizer.getTokenValue(TokenType.SYMBOL) + " ");
				termEle.appendChild(ele);
				// expList
				termEle.appendChild(compileExpressionList());
				// )
				tokenizer.advance();
				ele = document.createElement(tokenType.toString());
				ele.setTextContent(" "
						+ tokenizer.getTokenValue(TokenType.SYMBOL) + " ");
				termEle.appendChild(ele);
			} else if (".".equals(value)) { // name.sub(expList)
				// .
				ele = document.createElement(tokenType.toString());
				ele.setTextContent(" "
						+ tokenizer.getTokenValue(TokenType.SYMBOL) + " ");
				termEle.appendChild(ele);
				// sub
				tokenizer.advance();
				value = tokenizer.getTokenValue(TokenType.IDENTIFIER);
				ele = document.createElement(tokenType.toString());
				ele.setTextContent(" " + value + " ");
				termEle.appendChild(ele);
				// (
				tokenizer.advance();
				value = tokenizer.getTokenValue(TokenType.SYMBOL);
				ele = document.createElement(tokenType.toString());
				ele.setTextContent(" "
						+ tokenizer.getTokenValue(TokenType.SYMBOL) + " ");
				termEle.appendChild(ele);
				// expList
				termEle.appendChild(compileExpressionList());
				// )
				tokenizer.advance();
				ele = document.createElement(tokenType.toString());
				ele.setTextContent(" "
						+ tokenizer.getTokenValue(TokenType.SYMBOL) + " ");
				termEle.appendChild(ele);
			} else {
				tokenizer.recede();
			}
		}
		return termEle;
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
				ele = document.createElement(tokenizer.getTokenType()
						.toString());
				ele.setTextContent(" " + value + " ");
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
