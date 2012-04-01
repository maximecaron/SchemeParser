package org.scheme4edu.parser.test;
import org.junit.*;

// NOT IMPLEMENTED
// http://www.r6rs.org/final/html/r6rs/r6rs-Z-H-7.html#node_sec_4.2.3
public class TestComment extends SchemeParserTest{
	
	@Test
	public void oneLine() {	
		p.parse("(1);dfsd");
		p.parse("(1);;");
		p.parse("(1);;;");
		p.parse("(1);;;;");
		p.parse("(1);    ");
		p.parse("(1);Comment ");
		p.parse("(1);; Comment  ");
	}
	
	// TODO Implements multi line comments in the parser
//	@Test
//	public void multiLine() {
//		p.parse("(1)#||#");
//		p.parse("(1)#|\n|#");
//		p.parse("(1)#|\n\n\n|#");
//		p.parse("(1)#|\ncomments\n|#");
//		p.parse("(1)#|comments|#");
//		p.parse("(1)#|comments\n|#");
//		p.parse("(1)#|\ncomments|#");
//		p.parse("(1)#| Comment |#");
//	}
}
