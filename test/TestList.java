package org.scheme4edu.parser.test;
import org.junit.*;
import org.scheme4edu.parser.*;

// http://www.r6rs.org/final/html/r6rs/r6rs-Z-H-7.html#node_sec_4.3.2
public class TestList extends SchemeParserTest{

	@Test
	public void badList() {
		try {
			p.parse("(");
			org.junit.Assert.fail();
		} catch (Exception e) {
			// Good
		}
	}
	
	@Test
	public void test() {
		p.parse("()");
		p.parse("(1 2 3)");
		p.parse("((#f) 2 3)");
		p.parse("(1 (2) 3)");
		p.parse("(1 2 (3))");
		p.parse("(1 2 () 4 ((7) (8)) 99)");
	}
	
	@Test
	public void quote() {
		p.parse("'()");
		p.parse("'(1 2 3)");
		p.parse("'((#f) 2 3)");
		p.parse("'(1 (2) 3)");
		p.parse("'(1 2 (3))");
		p.parse("'(1 2 () 4 ((7) (8)) 99)");
	}
	
	@Test
	public void quasiquote() {
		p.parse("`()");
		p.parse("`(1 2 3)");
		p.parse("`((#f) 2 3)");
		p.parse("`(1 (2) 3)");
		p.parse("`(1 2 (3))");
		p.parse("`(1 2 () 4 ((7) (8)) 99)");
	}	
	
	@Test
	public void unquote() {
		p.parse(",()");
		p.parse(",(1 2 3)");
		p.parse(",((#f) 2 3)");
		p.parse(",(1 (2) 3)");
		p.parse(",(1 2 (3))");
		p.parse(",(1 2 () 4 ((7) (8)) 99)");
	}	
	
	@Test
	public void unquoteSplicing() {
		p.parse(",@()");
		p.parse(",@(1 2 3)");
		p.parse(",@((#f) 2 3)");
		p.parse(",@(1 (2) 3)");
		p.parse(",@(1 2 (3))");
		p.parse(",@(1 2 () 4 ((7) (8)) 99)");
	}	
	
}
