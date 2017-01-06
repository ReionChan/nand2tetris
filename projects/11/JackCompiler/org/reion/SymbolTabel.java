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
