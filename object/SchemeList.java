/*
 * Copyright (c) 2004-2006 SchemeWay Project. All rights reserved.
 */
package org.scheme4edu.parser.object;

import java.util.*;
import org.scheme4edu.parser.*;

/**
 * @author SchemeWay Project.
 * 
 */
public class SchemeList implements Lexeme, Datum {

	private List<Datum> mylist = null;

	public String type() {
		return "schemeList";
	}

	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("<list>\n");
		for (Datum d : mylist) {
			b.append(d.toString());
		}
		b.append("</list>\n");
		return b.toString();
	}
	
	public String toSchemeString() {
		StringBuilder b = new StringBuilder();
		b.append("(");
		for (Datum d : mylist) {
			b.append(d.toSchemeString());
			b.append(" ");
		}
		b.deleteCharAt(b.length()-1);
		b.append(")");
		return b.toString();
	}

	public SchemeList(List<Datum> data) {
		this.mylist = data;
	}
}
