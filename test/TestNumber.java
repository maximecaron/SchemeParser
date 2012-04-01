package org.scheme4edu.parser.test;

import org.codehaus.jparsec.Parser;
import org.junit.*;
import org.scheme4edu.parser.*;

public class TestNumber {
	
	Parser<Datum> p = null;
	
	@Before
	public void setUp() {
		p = SchemeScanner.schemeParser();
	}
	
	@Test
	public void testBinaryFail(){
		try{
			p.parse("#b10012");
			org.junit.Assert.fail();
		} catch(Exception e) {
			// Good
		}
	}
	
	@Test
	public void testOctalFail(){
		try{
			p.parse("#o7509");
			org.junit.Assert.fail();
		} catch(Exception e) {
			// Good
		}
	}
	
	@Test
	public void testDecimalFail(){
		try{
			p.parse("#d961A");
			org.junit.Assert.fail();
		} catch(Exception e) {
			// Good
		}
	}
	
	@Test
	public void testHexFail(){
		try{
			p.parse("#xCAFEX");
			org.junit.Assert.fail();
		} catch(Exception e) {
			// Good
		}
	}
	
	@Test
	public void testBase2() {
		p.parse("#b1001");
		p.parse("#b#e1001");
		p.parse("#b#i1001");
		p.parse("#b+1001");
		p.parse("#b-1001");
	}
	
	@Test
	public void testBase8() {
		p.parse("#o741");
		p.parse("#o#e741");
		p.parse("#o#i741");
		p.parse("#o+741");
		p.parse("#o-741");
	}
	
	@Test
	public void testBase10() {
		p.parse("12");
		p.parse("12.0");
		p.parse("12.99584569437");
		p.parse("-12");
		p.parse("-12.0");
		p.parse("-12.48372573");
		
		p.parse("#d-12");
		p.parse("#d#e3.2");
		p.parse("#d#i3.2");
	}
	
	@Test
	public void testBase10Scientific() {
		p.parse("12e2");
		p.parse("12e+2");
		p.parse("12e-2");
		p.parse("12.2e2");
		p.parse("12.2e+2");
		p.parse("12.2e-2");
		p.parse("-12e2");
		p.parse("-12e+2");
		p.parse("-12e-2");
		p.parse("-12.2e2");
		p.parse("-12.2e+2");
		p.parse("-12.2e-2");
	}
	
	@Test
	public void testBase10Long() {
		p.parse("12l2");
		p.parse("12l+2");
		p.parse("12l-2");
		p.parse("12.2l2");
		p.parse("12.2l+2");
		p.parse("12.2l-2");
		p.parse("-12l2");
		p.parse("-12l+2");
		p.parse("-12l-2");
		p.parse("-12.2l2");
		p.parse("-12.2l+2");
		p.parse("-12.2l-2");
	}
	
	@Test
	public void testBase16() {
		p.parse("#xF41");
		p.parse("#x#eF41");
		p.parse("#x#iF41");
		p.parse("#x+F41");
		p.parse("#x-F41");
	}
}
