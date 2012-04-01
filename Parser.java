/*
 * Copyright (c) 2004-2006 SchemeWay Project. All rights reserved.
 */
package org.scheme4edu.parser;


import org.junit.*;
import org.codehaus.jparsec.*;

/**
 * @author SchemeWay Project.
 *
 */
public class Parser {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		org.codehaus.jparsec.Parser<Datum> p = null;
		p = SchemeScanner.schemeParser();
		//Datum d = p.parse("'(lambda (a) (+ 1 a))");
		Datum d = p.parse("'(lambda (a p1 p2) (+ 1 p1 p2))");
		System.out.println(d);
		System.out.println(d.toSchemeString());
	}

}
