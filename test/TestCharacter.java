package org.scheme4edu.parser.test;
import org.junit.*;

public class TestCharacter extends SchemeParserTest{
	
	@Test
	public void character() {
		p.parse("#\\a");
		p.parse("#\\A");
		p.parse("#\\(");
		p.parse("#\\)");
		p.parse("#\\ ");
	}
	
	@Test
	public void words() {
		p.parse("#\\nul");
		p.parse("#\\alarm");
		p.parse("#\\backspace");
		p.parse("#\\tab");
		p.parse("#\\linefeed");
		p.parse("#\\newline");
		p.parse("#\\vtab");
		p.parse("#\\page");
		p.parse("#\\return");
		p.parse("#\\esc");
		p.parse("#\\space");
		p.parse("#\\delete");
	}
}
