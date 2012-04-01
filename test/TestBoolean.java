package org.scheme4edu.parser.test;

import org.junit.*;
import org.scheme4edu.parser.*;

public class TestBoolean extends SchemeParserTest {

	@Test
	public void test() {
		Datum d = p.parse("#t");
		System.out.println(d);
		d = p.parse("#f");
		System.out.println(d);
	}
}
