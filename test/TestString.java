package org.scheme4edu.parser.test;
import org.junit.*;

public class TestString extends SchemeParserTest{
	@Test
	public void test() {
		p.parse("\"\"");
		p.parse("\" \"");
		p.parse("\"ABC\"");
		p.parse("\"   AB    C   \"");
		p.parse("\" \\x41;bc \"");
		p.parse("\" \\x41; bc \"");
		p.parse("\" \\a \"");
		p.parse("\" \\b \"");
		p.parse("\" \\t \"");
		p.parse("\" \\n \"");
		p.parse("\" \\v \"");
		p.parse("\" \\f \"");
		p.parse("\" \\r \"");
		p.parse("\" \\\" \"");
		p.parse("\" \\\\ \"");
	}
}
