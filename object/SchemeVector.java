/*
 * Copyright (c) 2004-2006 SchemeWay Project. All rights reserved.
 */
package org.scheme4edu.parser.object;

import org.codehaus.jparsec.*;
import org.scheme4edu.parser.*;

/**
 * @author SchemeWay Project.
 * 
 */
public class SchemeVector implements Lexeme, Datum {
	protected String content;
    protected int index;
    protected int length;

	public SchemeVector(Token t) {
		this.content = (String) t.value();
		this.index = t.index();
		this.length = t.length();
	}

	public String toString() {
		return "<vector>\n"
		        + "<value>" + content+ "</value>\n"
		        + "<index>" + String.valueOf(index)+"</index>\n"
		        + "<length>" + String.valueOf(length)+"</length>\n"
		        + "</vector>\n";
	}
	
	public String type() {
		return "Vector";
	}

	@Override
	public String toSchemeString() {
		return content;
	}
}
