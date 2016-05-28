package org.reion;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Symbol Table
 * 
 * @author Reion
 * 
 */
public class SymbolTabel {

	/**
	 * index of type in array
	 */
	public static final int NUM_TYPE = 0;
	/**
	 * index of kind in array
	 */
	public static final int NUM_KIND = 1;
	/**
	 * index of number in array
	 */
	public static final int NUM_INDEX = 2;

	/**
	 * class scope symbol table
	 */
	private HashMap<String, Object[]> classTabel;
	/**
	 * subroutine scope symbol table
	 */
	private HashMap<String, Object[]> subroutineTabel;


	/**
	 * constructor
	 */
	public SymbolTabel() {
		classTabel = new LinkedHashMap<String, Object[]>();
		subroutineTabel = new LinkedHashMap<String, Object[]>();
	}

	/**
	 * start a new subroutine scope
	 */
	public void startSubroutine() {
		subroutineTabel.clear();
	}

	/**
	 * define a new identifier
	 * 
	 * @param name
	 *            identifier name
	 * @param type
	 *            identifier type
	 * @param kind
	 *            identifier kind
	 */
	public void define(final String name, final String type, final Kind kind) {
		if (Kind.STATIC.equals(kind) || Kind.FIELD.equals(kind)) {
			
			if (classTabel.get(name) == null) {
				classTabel.put(name, new Object[]{type, kind, varCount(kind)});
			}
		} else if (!Kind.NONE.equals(kind)) {
			if (subroutineTabel.get(name) == null) {
				subroutineTabel.put(name, new Object[]{type, kind, varCount(kind)});
			}
		}
	}

	/**
	 * get sum of defined in the scope by kind
	 * 
	 * @param kind
	 *            identifier kind
	 * @return sum
	 */
	public int varCount(final Kind kind) {
		int count = 0;
		HashMap<String, Object[]> hMap = null;
		if (Kind.STATIC.equals(kind) || Kind.FIELD.equals(kind)) {
			hMap = classTabel;
		} else if (!Kind.NONE.equals(kind)) {
			hMap = subroutineTabel;
		} else {
			return count;
		}
		for (String name : hMap.keySet()) {
			if (hMap.get(name)[NUM_KIND].equals(kind)) {
				count++;
			}
		}
		return count;
	}

	/**
	 * get kind of defined in the scope by identifier name
	 * 
	 * @param name
	 *            identifier name
	 * @return identifier kind
	 */
	public Kind kindOf(final String name) {
		for (String str : subroutineTabel.keySet()) {
			if (name.equals(str)) {
				return (Kind) subroutineTabel.get(name)[NUM_KIND];
			}
		}
		for (String str : classTabel.keySet()) {
			if (name.equals(str)) {
				return (Kind) classTabel.get(name)[NUM_KIND];
			}
		}

		return Kind.NONE;
	}

	/**
	 * get type of defined in the scope by identifier name
	 * 
	 * @param name
	 *            identifier name
	 * @return identifier type
	 */
	public String typeOf(final String name) {
		for (String str : subroutineTabel.keySet()) {
			if (name.equals(str)) {
				return (String) subroutineTabel.get(name)[NUM_TYPE];
			}
		}
		for (String str : classTabel.keySet()) {
			if (name.equals(str)) {
				return (String) classTabel.get(name)[NUM_TYPE];
			}
		}
		return null;
	}

	/**
	 * get index of defined in the scope by identifier name
	 * 
	 * @param name
	 *            identifier name
	 * @return index
	 */
	public int indexOf(final String name) {
		for (String str : subroutineTabel.keySet()) {
			if (name.equals(str)) {
				return (Integer) subroutineTabel.get(name)[NUM_INDEX];
			}
		}
		for (String str : classTabel.keySet()) {
			if (name.equals(str)) {
				return (Integer) classTabel.get(name)[NUM_INDEX];
			}
		}
		return -1;
	}

	@Override
	public String toString() {
		StringBuffer strB = new StringBuffer();
		strB.append("STATIC & FIELD =========\n");
		Object[] objs = null;
		for (String str : classTabel.keySet()) {
			objs = classTabel.get(str);
			strB.append(str + "\t").append(objs[NUM_TYPE] + "\t")
					.append(objs[NUM_KIND] + "\t")
					.append(objs[NUM_INDEX] + "\n");
		}
		strB.append("ARG & VAR =========\n");
		for (String str : subroutineTabel.keySet()) {
			objs = subroutineTabel.get(str);
			strB.append(str + "\t").append(objs[NUM_TYPE] + "\t")
			.append(objs[NUM_KIND] + "\t")
			.append(objs[NUM_INDEX] + "\n");
		}

		return strB.toString();
	}
}

/**
 * Symbol Kind
 * 
 * @author Reion
 * 
 */
enum Kind {
	STATIC, FIELD, ARG, VAR, NONE;
	@Override
	public String toString() {
		return super.toString().toLowerCase();
	}
}
