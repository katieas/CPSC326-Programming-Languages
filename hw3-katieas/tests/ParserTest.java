/*
 * File: ParserTest.java
 * Date: Spring 2022
 * Auth: 
 * Desc: Basic unit tests for the MyPL parser class.
 */


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Ignore;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;


public class ParserTest {

  //------------------------------------------------------------
  // HELPER FUNCTIONS
  //------------------------------------------------------------
  
  private static Parser buildParser(String s) throws Exception {
    InputStream in = new ByteArrayInputStream(s.getBytes("UTF-8"));
    Parser parser = new Parser(new Lexer(in));
    return parser;
  }

  private static String buildString(String... args) {
    String str = "";
    for (String s : args)
      str += s + "\n";
    return str;
  }


  //------------------------------------------------------------
  // POSITIVE TEST CASES
  //------------------------------------------------------------

  @Test
  public void emptyParse() throws Exception {
    Parser parser = buildParser("");
    parser.parse();
  }

  @Test
  public void implicitVariableDecls() throws Exception {
    String s = buildString
      ("fun void main() {",
       "  var v1 = 0",
       "  var v2 = 0.0",
       "  var v3 = false",
       "  var v4 = 'a'",
       "  var v5 = \"abc\"",
       "  var v6 = new Node",
       "}");
    Parser parser = buildParser(s);
    parser.parse();
  }

  @Test
  public void explicitVariableDecls() throws Exception {
    String s = buildString
      ("fun void main() {",
       "  var int v1 = 0",
       "  var double v2 = 0.0",
       "  var bool v3 = false",
       "  var char v4 = 'a'",
       "  var string v5 = \"abc\"",
       "  var Node v5 = new Node",
       "}");
    Parser parser = buildParser(s);
    parser.parse();
  }

  /* TODO: 

       (1). Add your own test cases below as you create your recursive
            descent functions. By the end you should have a full suite
            of positive test cases that "pass" the tests. 

       (2). Ensure your program (bazel-bin/mypl --parse) works for the
            example file (examples/parser.txt). 
 
       (3). For the parser, the "negative" tests below are just as
            important as the "positive" test cases. Like in (1), be
            sure to add negative test cases as you build out your
            parser. By the end you should also have a full set of
            negative cases as well.
  */

  @Test
  public void simpleConditionStatement() throws Exception {
    String s = buildString
      ("fun void main() {",
       "  var int v1 = 0",
       "  var int v2 = 0",
       "  if (v1 == v2) {",
       "    var v3 = 1",
       "  }",
       "}");
    Parser parser = buildParser(s);
    parser.parse();
  }
  
  @Test
  public void complicatedConditionStatement() throws Exception {
    String s = buildString
      ("fun void main() {",
       "  var v1 = 1",
       "  var v2 = 2",
       "  var thisIsTrue = true",
       "  if (v1 == v2) {",
       "    var v3 = 3",
       "  }",
       "  elif (thisIsTrue) {",
       "    var nothing = false",
       "  }",
       "  else {",
       "    var v4 = 4",
       "  }",
       "}");
    Parser parser = buildParser(s);
    parser.parse();
  }

  @Test
  public void whileStatementTest() throws Exception {
    String s = buildString
      ("fun void main() {",
       "  var counter = 0",
       "  while counter < 3 {",
       "    counter = counter + 1",
       "  }",
       "}");
    Parser parser = buildParser(s);
    parser.parse();
  }

  @Test
  public void forStatementIncrement() throws Exception {
    String s = buildString
      ("fun void main() {",
       "  var x = 0",
       "  for i from 1 upto 3 {",
       "    x = x + i",
       "  }",
       "}");
    Parser parser = buildParser(s);
    parser.parse();
  }

  @Test
  public void forStatementDecrement() throws Exception {
    String s = buildString
      ("fun void main() {",
       "  var y = 10",
       "  for i from 3 downto 0 {",
       "    y = y - i",
       "  }",
       "}");
    Parser parser = buildParser(s);
    parser.parse();
  }

  @Test
  public void returnStatementTest() throws Exception {
    String s = buildString
      ("fun int f() {",
       "  return 1",
       "}");
    Parser parser = buildParser(s);
    parser.parse();
  }

  @Test
  public void deleteStatementTest() throws Exception {
    String s = buildString
      ("fun void main() {",
       "  var Node n1 = new Node",
       "  delete n1",
       "}");
    Parser parser = buildParser(s);
    parser.parse();
  }

  @Test
  public void simpleMath() throws Exception {
    String s = buildString
      ("fun void main() {",
       "  var double x = 1.1",
       "  var double y = 2.2",
       "  var double z = 3.3",
       "  x = (x * x) + y - z + (x / z)",
       "}");
    Parser parser = buildParser(s);
    parser.parse();
  }
  
  //------------------------------------------------------------
  // NEGATIVE TEST CASES
  //------------------------------------------------------------
  
  @Test
  public void statementOutsideOfFunction() throws Exception {
    String s = "var v1 = 0";
    Parser parser = buildParser(s);
    try {
      parser.parse();
      fail("syntax error not detected");
    } catch(MyPLException e) {
      // can check message here if desired
      // e.g., assertEquals("...", e.getMessage());
    }
  }

  @Test
  public void functionWithoutReturnType() throws Exception {
    String s = "fun main() {}";
    Parser parser = buildParser(s);
    try {
      parser.parse();
      fail("syntax error not detected");
    } catch(MyPLException e) {
      // can check message here if desired
      // e.g., assertEquals("...", e.getMessage());
    }
  }

  @Test
  public void functionWithoutClosingBrace() throws Exception {
    String s = "fun void main() {";
    Parser parser = buildParser(s);
    try {
      parser.parse();
      fail("syntax error not detected");
    } catch(MyPLException e) {
      // can check message here if desired
      // e.g., assertEquals("...", e.getMessage());
    }
  }
  
  /* add additional negative test cases here */ 

  @Test
  public void expressionWithoutClosingParens() throws Exception {
    String s = buildString
      ("fun void main() {",
       "  if (",
       "    var v1 = 0",
       "}");
    Parser parser = buildParser(s);
    try {
      parser.parse();
      fail("syntax error not detected");
    } catch(MyPLException e) {

    }
  }
}
