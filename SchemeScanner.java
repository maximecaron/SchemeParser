package org.scheme4edu.parser;

import java.util.*;


import org.codehaus.jparsec.*;
import org.codehaus.jparsec.Parser;
import org.codehaus.jparsec.functors.*;
import org.codehaus.jparsec.functors.Map;
import org.codehaus.jparsec.misc.*;
import org.codehaus.jparsec.pattern.*;
import org.scheme4edu.parser.object.*;
import org.codehaus.jparsec.Terminals;

public class SchemeScanner {
	
	private static final Parser<Void> SPACES = interlexeme_space().skipMany();
	
	private static final Parser<String> dot = Scanners.isChar('.').source(); 
	private static final Parser<String> plus = Scanners.isChar('+').source(); 
	private static final Parser<String> minus = Scanners.isChar('-').source(); 
	private static final Parser<String> imaginary = Scanners.isChar('i').source(); 
	private static final Parser<Void> digit = Scanners.among("0123456789");
	private static final Parser<String> hexdigit = digit.or(Scanners
			.among("aAbBcCdDeEfF")).source();
	private static final Parser<String> hex_scalar = hexdigit.many1().source();
	private static final Parser<String> inline_hex = Scanners.string("\\x").next(hex_scalar).source();
	private static final Parser<String> exponentMarker = Scanners.among("eEsSfFdDlL").source();
	private static final Parser<String> sign = Scanners.among("+-").optional().source();
	private static final Parser<String> exactness =
			Parsers.or(Scanners.stringCaseInsensitive("#i"),
			Scanners.stringCaseInsensitive("#e")).optional().source();
	
	private static final Parser<String> characterName(){
		
		return Scanners.string("nul").or(
			   Scanners.string("alarm")).or(
			   Scanners.string("backspace")).or(
			   Scanners.string("tab")).or(
			   Scanners.string("linefeed")).or(
			   Scanners.string("newline")).or(
			   Scanners.string("vtab")).or(
			   Scanners.string("page")).or(
			   Scanners.string("return")).or(
			   Scanners.string("esc")).or(
			   Scanners.string("space")).or(
			   Scanners.WHITESPACES).or(
			   Scanners.string(System.getProperty("line.separator"))).or(
			   Scanners.string("delete")
			   ).source();
	}

	private static final Parser<Lexeme> variable(){
		//TODO: remove syntactic keyword
		/* 
		"quote"
		"lambda"
		"if"
	    "set!"
	    "begin"
	    "cond"
	    "and"
	    "or"
	    "case"
	    "let"
	    "let*"
	    "letrec"
	    "do"
	    "delay"
	    "quasiquote"
	    "else"
	    "=>"
	    "define" 
        "unquote"
        "unquote-splicing"
	     */
		return identifier();
	}
	
	private static final Parser<Lexeme> identifier(){
		Parser<?> p = Terminals.IntegerLiteral.TOKENIZER;
		
		
		return initial().next(subsequent().many().source()).or(
				peculiarIdentifier()).source().token().map(new Map<Token, Lexeme>() {
					public Lexeme map(Token s) {
						return new SchemeSymbol(s);
					}
				});	
	}
	
	private static final Parser<String> delimiter(){
		return Scanners.among("()[]\";#").source().or(whitespace());	
	}
	
	private static final Parser<String> atmosphere(){
		return  whitespace().or(comment());	
	}
	
	private static final Parser<String> interlexeme_space(){
		return atmosphere().many().source();
	}
	
	private static final Parser<String> whitespace(){
		return Scanners.WHITESPACES.source();
	}
	
	private static final Parser<String> subsequent(){
		return initial().or(
			   digit()).or(
			   specialSubsequent());
	}
	
	private static final Parser<String> digit(){
		return Scanners.pattern(Patterns.isChar(CharPredicates.IS_DIGIT), "digit").source();
	}
	
	private static final Parser<String> specialSubsequent(){
		return plus.or(minus).or(dot).or(Scanners.isChar('@').source()).source();
	}
	private static final Parser<String> comment(){
		return Scanners.lineComment(";").source();
	}

	private static final Parser<String> commentText(){
		return Scanners.pattern(Patterns.string("|#").not().next(Patterns.string("#|").not()).many(),"wow").source();
	}
	
	private static final Parser<String> peculiarIdentifier(){
		return plus.or(minus).or(Scanners.string("...").source()).or(
			   Scanners.string("->").next(subsequent().many().source())).source();
	}
	
	private static final Parser<String> initial(){
		return constituent().or(specialInitial()).or(inline_hex).source();
	}

	private static final Parser<String> unicode_constituent(){
		return Scanners.isChar(new CharPredicate() {
	           public boolean isChar(char c) {
	        	   int car = Character.getType(c);
	        	   boolean cathegorie = car == Character.UPPERCASE_LETTER ||
	        	          car == Character.LOWERCASE_LETTER ||
	        	          car == Character.TITLECASE_LETTER ||
	        	          car == Character.MODIFIER_LETTER  ||
	        	          car == Character.OTHER_LETTER ||
	        	          car == Character.NON_SPACING_MARK ||
	        	          car == Character.TITLECASE_LETTER ||
	        	          car == Character.LETTER_NUMBER ||
	        	          car == Character.TITLECASE_LETTER ||
	        	          car == Character.OTHER_NUMBER ||
	        	          car == Character.CONNECTOR_PUNCTUATION ||
	        	          car == Character.DASH_PUNCTUATION ||
	        	          car == Character.OTHER_PUNCTUATION ||
	        	          car == Character.CURRENCY_SYMBOL ||
	        	          car == Character.MODIFIER_SYMBOL ||
	        	          car == Character.MATH_SYMBOL ||
	        	          car == Character.OTHER_SYMBOL ||
	        	          car == Character.PRIVATE_USE;
	        	   return ((int)c > 127) && cathegorie;
		        }
		      }).source();
	}
	
	private static final Parser<String> constituent(){
		return letter().or(unicode_constituent());
	}
	
	private static final Parser<String> specialInitial(){
		return Scanners.among("!$%&*/:<=>?^_~").source();
	}
	
	private static final Parser<String> letter(){
	   return Scanners.pattern(Patterns.isChar(CharPredicates.IS_ALPHA_), "alpha").source();
	}
	
	/*
	 * A <boolean> is one of
	 * 
	 * #t | #f | #T | #F
	 */
	private static final Parser<Lexeme> schemeBoolean(){
		return Scanners.stringCaseInsensitive("#t").or(
		Scanners.stringCaseInsensitive("#f")).source().token().map(new Map<Token, Lexeme>() {
			public Lexeme map(Token s) {
				return new SchemeBoolean(s);
			}
		});
	}
	
	/*
	 * An instance of Mapper to SchemeCharacter
	 */
	private static final Map<Token, Lexeme> character_mapper = new Map<Token, Lexeme>() {
		public Lexeme map(Token s) {
			return new SchemeCharacter(s);
		}
	};
	
	/*
	 * A <character> is one of
	 * 
	 * #\<characterName> | #\<character> 
	 */
	private static final Parser<Lexeme> character(){
		return Scanners.string("#\\").next(characterName()).or(
				Scanners.string("#\\").next(Scanners.ANY_CHAR).source()).source().token().map(character_mapper);
	}
	
	/*
	 * A <stringElement> is one of
	 * 
	 * <any character other than " or \> 
	 * | \a | \b | \t | \n | \v | \f | \r 
	 * | \" | \\
	 * | <inline hex escape>
	 */
	private static final Parser<String> stringElement(){
		return Scanners.notAmong("\"\\").or(
			   Scanners.string("\\\"")).or(
			   Scanners.string("\\\\")).or(
			   Scanners.string("\\a")).or(
               Scanners.string("\\b")).or(	   
               Scanners.string("\\t")).or(
               Scanners.string("\\n")).or(
               Scanners.string("\\v")).or(
               Scanners.string("\\f")).or(
               Scanners.string("\\r")
               ).source().or(inline_hex).source();
	}
    
	/*
	 * A String is 
	 * 
	 * " <string element>* "
	 */
	private static final Parser<Lexeme> string(){
		return Scanners.isChar('\"').next(
				stringElement().many()
				).next(
				Scanners.isChar('\"')).source().token().map(new Map<Token, Lexeme>() {
			public Lexeme map(Token s) {
				return new SchemeString(s);
			}
		});
	}
	
	/*
	 * A Number is 
	 * 
	 * <num 16> | <num 10> | <num 8> | <num 2> 
	 */
	private static final Parser<Lexeme> number(){
		return num(16).or(num(10)).or(num(8)).or(num(2)).source().token().map(new Map<Token, Lexeme>() {
			public Lexeme map(Token s) {
				return new SchemeNumber(s);
			}
		}
		);
	}
	
	/*
	 * A <num R> is 
	 * 
	 * <prefix R> <complex R>
	 */
	private static final Parser<String> num(int R){
		return prefix(R).next(Complex(R));
	}
	
	/*
	 * A <complex R> is
	 * 
	 * <real R> 
	 * | <real R> @ <real R>
	 * | <real R> + <ureal R> i
	 * | <real R> - <ureal R> i
	 * | <real R> + i
	 * | <real R> - i
	 * | + <ureal R> i
	 * | - <ureal R> i
	 * | + i
	 * | - i
	 */
	private static final Parser<String> Complex(int R){
		return real(R).or(
				real(R).next(Scanners.isChar('@')).next(real(R))
		        ).or(
		        real(R).next(plus).next(real(R)).next(imaginary)
		        ).or(
		        real(R).next(minus).next(real(R)).next(imaginary) 
		        ).or(
		        real(R).next(plus).next(imaginary)
		        ).or(
		        real(R).next(minus).next(imaginary)
		        ).or(
		        plus.next(ureal(R)).next(imaginary)
		        ).or(
		        minus.next(ureal(R)).next(imaginary)
		        ).or(
		        plus.next(imaginary)
		        ).or(
		        minus.next(imaginary)
		        );
	}
	
	/*
	 * A <real> is
	 * 
	 * <sign> <ureal R>
	 * | + <naninf> 
	 * | - <naninf>
	 */
	private static final Parser<String> real(int R){
		return sign.next(ureal(R)).or(
			Scanners.isChar('+').next(nanInf())
		    ).or(
		    Scanners.isChar('-').next(nanInf())
		    );
	}
	
	/*
	 * <naninf> is
	 * 
	 * nan.0 | inf.0
	 */
	private static final Parser<String> nanInf(){
		return Scanners.string("nan.0").or(Scanners.string("inf.0")).source();
	}
	
	/*
	 * <ureal R> is
	 * 
	 * <uinteger R>
	 * | <uinteger R> / <uinteger R>
	 * | <decimal R>
	 */
	private static final Parser<String> ureal(int R){
		return decimal(R).next(suffix).or(
				uinteger(R).next(Scanners.isChar('/')).next(uinteger(R))
				).or(
				uinteger(R)
				).source();
	}
	
	/*
	 * <Decimal R> is <Decimal 10> for R == 10
	 * else it fail.
	 */
	private static final Parser<String> decimal(int R){
		if (R  == 10){	
			return decimal10();
		}else{
			return Parsers.expect("expect decimal10");
		}
	}
	
	/*
	 * <Decimal 10> is
	 * 
	 * <uinteger 10> <suffix>
     * . <digit 10>+ #* <suffix>
     * <digit 10>+ . <digit 10>* #* <suffix>
     * <digit 10>+ #+ . #* <suffix>
	 */
	private static final Parser<String> decimal10(){
		return Parsers.or(
				digit(10).many1().next(dot).next(digit(10).many()).next(suffix),
				dot.next(digit(10).many1()).next(suffix),
				uinteger(10).next(suffix)
		        ).source();
	}

	/*
	 * A <uinteger R> is
	 * 
	 * <digit R>+
	 */
	private static final Parser<String> uinteger(int R){
		return digit(R).many1().source();
	}
	
	/*
	 * A <prefix R> is
	 * 
	 * <radix R> <exactness>  | <exactness> <radix R>
	 */
	private static final Parser<String> prefix(int R){
		return radix(R).next(SchemeScanner.exactness).or(SchemeScanner.exactness.next(radix(R))).source();
	}
	
	/*
	 * <radix 2> is 
	 * 
	 * #b | #B
	 */
	private static final Parser<String> radix2 = Scanners
			.stringCaseInsensitive("#b").source();

	/*
	 * <radix 8> is 
	 * 
	 * #o | #O
	 */
	private static final Parser<String> radix8 = Scanners
			.stringCaseInsensitive("#o").source();
	
	/*
	 * <radix 10> is 
	 * 
	 * #d | #D
	 */
	private static final Parser<String> radix10 = Scanners.stringCaseInsensitive(
			"#d").optional().source();
	
	/*
	 * <radix 16> is 
	 * 
	 * #x | #X
	 */
	private static final Parser<String> radix16 = Scanners.stringCaseInsensitive("#x").source();

	/*
	 * <digit 2> is 
	 * 
	 * 0 | 1
	 */
	private static final Parser<String> digit2 = Scanners.among("01").source();

	/*
	 * <digit 8> is 
	 * 
	 * 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 
	 */
	private static final Parser<String> digit8 = Scanners.among("01234567").source();
	
	/*
	 * <digit 10> is 
	 *
	 * <digit>
	 */
	private static final Parser<String> digit10 = digit.source();

	/*
	 * <digit 16> is 
	 *
	 * <hex digit>
	 */
	private static final Parser<String> digit16 = hexdigit.source();
	
	/*
	 * <suffix> is 
	 *
	 * <empty> | <exponent marker> <sign> <digit 10>+
	 */
	private static final Parser<String> suffix = Parsers.sequence(exponentMarker,sign,digit(10).many1().source()).optional().source();
	
	/*
	 * <digit R> is 
	 *
	 * <digit 2>  for R == 2
	 * <digit 8>  for R == 8
	 * <digit 10> for R == 10
	 * <digit 16> for R == 16
	 */
	private static final Parser<String> digit(int R){
		switch (R) {
		case 2:
			return digit2;
		case 8:
			return digit8;
		case 10:
			return digit10;
		case 16:
			return digit16;
		default:
			return null;
		}
	}
	
	
	private static final Parser<String> radix(int R){
		switch (R) {
		case 2:
			return radix2;
		case 8:
			return radix8;
		case 10:
			return radix10;
		case 16:
			return radix16;
		default:
			return null;
		}
	}
	
	private static final Parser<String> SPACE_SEPARATOR = Scanners.isChar(new CharPredicate() {
        public boolean isChar(char c) {
	          return Character.getType(c) == Character.SPACE_SEPARATOR;
	        }
	      }).source();

	private static final Parser<String> intraline_whitespace = Scanners.isChar('\t').source().or(SPACE_SEPARATOR).source();
	
	public static final Parser<Lexeme> lexeme(){
		return identifier().or(
	    schemeBoolean()).or(
	    number()).or(
	    character()).or(
	    string()).or(
	    Scanners.among("()[]'`,.").source().token().map(character_mapper)).or(
	    Scanners.string("#(").source().token().map(character_mapper)).or(
	    Scanners.string("#vu8(").source().token().map(character_mapper)).or(
	    Scanners.string(",@").source().token().map(character_mapper)).or(
	    Scanners.string("#'").source().token().map(character_mapper)).or(
	    Scanners.string("#`").source().token().map(character_mapper)).or(
	    Scanners.string("#,").source().token().map(character_mapper)).or(
	    Scanners.string("#,@").source().token().map(character_mapper));
	}
	
	
	
	
	
	
	// The parser begin her
	private static final Parser<Void> LPAREN = Scanners.isChar('(');
	private static final Parser<Void> RPAREN = Scanners.isChar(')');
	
	private static final Parser<Datum> ignore(Parser<Datum> p){
		return p.between(SPACES.skipMany(), SPACES.skipMany());
	}

	private static final Parser<Token> ignore2(Parser<Token> p){
		return p.between(SPACES.skipMany(), SPACES.skipMany());
	}
	
	public static final Parser<Datum> schemeParser(){
		Parser<Datum> p = Datum();
		datum.lazySet(p);
		return p;
	}
	
	public static final Parser<Datum> Datum(){
		Parser.Reference<Datum> ref = Parser.newReference();
		
		Parser<Datum> list = ref.lazy().sepBy(whitespace().many()).between(ignore2(LPAREN.token()), ignore2(RPAREN.token())).map(new Map<List<Datum>,Datum>(){
			public Datum map(List<Datum> d) {			
				return new SchemeList(d);
			}
		});
		Parser<Datum> compound = list.or(abbreviation());
	    ref.set(simpleDatum().or(compound));
		return ignore(simpleDatum().or(compound));      			
	}
	
	/*
	 * A simple Datum is everything except a List
	 * Boolean | Number | Character | String | Identifier 
	 */
	public static final Parser<Datum> simpleDatum(){
		return schemeBoolean().or(number()).or(character()).or(string()).or(identifier()).map(new Map<Lexeme,Datum>(){
			public Datum map(Lexeme l) {
				return (Datum)l;
			}
		});
	}
	
	private static Mapper<Datum> curry(Class<? extends Datum> clazz, Object... args) {
		    return Mapper.curry(clazz, args);
	}
	
	static Parser.Reference<Datum> datum = Parser.newReference();
	public static final Parser<Datum> abbreviation(){
	

		Parser<Datum> abbrev = Parsers.sequence(abbrevPrefix(),datum.lazy(),		new Map2<Datum, Datum, Datum>(){
				public Datum map(Datum a, Datum b) {
					List<Datum> l = new LinkedList<Datum>();
					l.add(a);
					l.add(b);
				    return new SchemeList(l);
				  }
			});

		return abbrev;
	}
	
	public static final Parser<Datum> abbrevPrefix(){
		return Scanners.string("`").or(Scanners.string("'")).or(Scanners.string(",@")).or(Scanners.string(",")).source().token().map(
				new Map<Token,Datum>(){
					public Datum map(Token s) {
						if (s.value().equals("'")){
							return new SchemeSymbol(new Token(s.index(),s.length(),"quote"));
						} else if (s.value().equals(",")) {
							return new SchemeSymbol(new Token(s.index(),s.length(),"unquote"));
						} else if (s.value().equals("`")) {
							return new SchemeSymbol(new Token(s.index(),s.length(),"quasiquote"));
						} else if (s.value().equals(",@")) {
						   return new SchemeSymbol(new Token(s.index(),s.length(),"unquotesplicing"));
					    }
						return null;
					}
				});
	}
	
}
