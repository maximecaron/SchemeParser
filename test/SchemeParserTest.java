/*
 * Copyright (c) 2004-2006 SchemeWay Project. All rights reserved.
 */
package org.scheme4edu.parser.test;

import org.codehaus.jparsec.Parser;
import org.junit.*;
import org.scheme4edu.parser.*;

/**
 * @author SchemeWay Project.
 *
 */
public class SchemeParserTest {
	Parser<Datum> p = null;
	
	@Before
	public void setUp() {
		p = SchemeScanner.schemeParser();
	}
}
