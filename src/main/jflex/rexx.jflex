/**
 * Copyright 2020 Markus Holzem
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Based on the jflex-1.8.1/examples/simple
 */
package de.holzem.lsp.lsp4rexx.rexxscanner;

import static de.holzem.lsp.lsp4rexx.rexxscanner.TokenType.*;
import static de.holzem.lsp.lsp4rexx.rexxscanner.RexxError.*;

%%

%public

%{
  private int           _commentCount  = 0;
  private long          _commentChar   = 0L;
  private int           _commentLine   = 0;
  private int           _commentColumn = 0;
  private StringBuilder _commentText   = new StringBuilder();

  /**
   * Resumes scanning until the next regular expression is matched, the end of input is encountered
   * or an I/O-Error occurs. WHITESPACE and REXX_COMMENT Tokens are skipped.
   *
   * @return the next token.
   * @exception java.io.IOException if any I/O-Error occurs.
   */
  public RexxToken nextRealToken() throws java.io.IOException {
    RexxToken nextToken = null;
    do {
      nextToken = nextToken();
    } while (nextToken != null && (nextToken.getType() == REXX_COMMENT || nextToken.getType() == WHITESPACE));
    return nextToken;
  }

  private RexxErrors _rexxErrors = new RexxErrors();

  private void addError(RexxError pRexxError) {
    _rexxErrors.addError(pRexxError);
    System.err.println("Error: "+pRexxError.getMessage());
  }

  public RexxErrors getRexxErrors() {
    return _rexxErrors;
  }
%}

%char
%line
%column

%state COMMENT

%unicode
%ignorecase

%class RexxLexer
%type RexxToken
%function nextToken

%debug

ALPHA=[A-Za-z#$§_]
DIGIT=[0-9]
NONNEWLINE_WHITE_SPACE_CHAR=[\ \b\012]
NEWLINE=\r|\n|\r\n
WHITE_SPACE_CHAR=[\n\r\ \b\012]
DQUOTE_STRING_TEXT=(\"\"|[^\n\r\"]|\\{WHITE_SPACE_CHAR}+\\)*
SQUOTE_STRING_TEXT=(''|[^\n\r']|\\{WHITE_SPACE_CHAR}+\\)*
COMMENT_TEXT=([^*/\n]|[^*\n]"/"[^*\n]|[^/\n]"*"[^/\n]|"*"[^/\n]|"/"[^*\n])+
IDENT = {ALPHA}(\.|{ALPHA}|{DIGIT})*

%%

/*--------------------------------------------------------------------------------------
* State YYINITIAL
*-------------------------------------------------------------------------------------*/
<YYINITIAL> {
  /* keywords */
  "ADDRESS"   { return (new RexxToken(KEYWORD,yytext(),yyline,yycolumn,yychar)); }
  "ARG"       { return (new RexxToken(KEYWORD,yytext(),yyline,yycolumn,yychar)); }
  "CALL"      { return (new RexxToken(KEYWORD,yytext(),yyline,yycolumn,yychar)); }
  "DO"        { return (new RexxToken(KEYWORD,yytext(),yyline,yycolumn,yychar)); }
  "END"       { return (new RexxToken(KEYWORD,yytext(),yyline,yycolumn,yychar)); }
  "TO"        { return (new RexxToken(KEYWORD,yytext(),yyline,yycolumn,yychar)); }
  "BY"        { return (new RexxToken(KEYWORD,yytext(),yyline,yycolumn,yychar)); }
  "FOR"       { return (new RexxToken(KEYWORD,yytext(),yyline,yycolumn,yychar)); }
  "FOREVER"   { return (new RexxToken(KEYWORD,yytext(),yyline,yycolumn,yychar)); }
  "WHILE"     { return (new RexxToken(KEYWORD,yytext(),yyline,yycolumn,yychar)); }
  "DROP"      { return (new RexxToken(KEYWORD,yytext(),yyline,yycolumn,yychar)); }
  "EXIT"      { return (new RexxToken(KEYWORD,yytext(),yyline,yycolumn,yychar)); }
  "IF"        { return (new RexxToken(KEYWORD,yytext(),yyline,yycolumn,yychar)); }
  "THEN"      { return (new RexxToken(KEYWORD,yytext(),yyline,yycolumn,yychar)); }
  "ELSE"      { return (new RexxToken(KEYWORD,yytext(),yyline,yycolumn,yychar)); }
  "INTERPRET" { return (new RexxToken(KEYWORD,yytext(),yyline,yycolumn,yychar)); }
  "ITERATE"   { return (new RexxToken(KEYWORD,yytext(),yyline,yycolumn,yychar)); }
  "LEAVE"     { return (new RexxToken(KEYWORD,yytext(),yyline,yycolumn,yychar)); }
  "NOP"       { return (new RexxToken(KEYWORD,yytext(),yyline,yycolumn,yychar)); }
  "NUMERIC"   { return (new RexxToken(KEYWORD,yytext(),yyline,yycolumn,yychar)); }
  "OPTIONS"   { return (new RexxToken(KEYWORD,yytext(),yyline,yycolumn,yychar)); }
  "PARSE"     { return (new RexxToken(KEYWORD,yytext(),yyline,yycolumn,yychar)); }
  "PROCEDUR"  { return (new RexxToken(KEYWORD,yytext(),yyline,yycolumn,yychar)); }
  "EXPOSE"    { return (new RexxToken(KEYWORD,yytext(),yyline,yycolumn,yychar)); }
  "PULL"      { return (new RexxToken(KEYWORD,yytext(),yyline,yycolumn,yychar)); }
  "PUSH"      { return (new RexxToken(KEYWORD,yytext(),yyline,yycolumn,yychar)); }
  "QUEUE"     { return (new RexxToken(KEYWORD,yytext(),yyline,yycolumn,yychar)); }
  "RETURN"    { return (new RexxToken(KEYWORD,yytext(),yyline,yycolumn,yychar)); }
  "SAY"       { return (new RexxToken(KEYWORD,yytext(),yyline,yycolumn,yychar)); }
  "SELECT"    { return (new RexxToken(KEYWORD,yytext(),yyline,yycolumn,yychar)); }
  "WHEN"      { return (new RexxToken(KEYWORD,yytext(),yyline,yycolumn,yychar)); }
  "SIGNAL"    { return (new RexxToken(KEYWORD,yytext(),yyline,yycolumn,yychar)); }
  "TRACE"     { return (new RexxToken(KEYWORD,yytext(),yyline,yycolumn,yychar)); }
  "UPPER"     { return (new RexxToken(KEYWORD,yytext(),yyline,yycolumn,yychar)); }
  /* functions */
  "ABBREV"     { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "ABS"        { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "ADDRESS"    { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "ARG"        { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "BITAND"     { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "BITOR"      { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "BITXOR"     { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "B2X"        { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "CENTER"     { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "CENTRE"     { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "COMPARE"    { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "CONDITION"  { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "COPIES"     { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "C2D"        { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "C2X"        { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "DATATYPE"   { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "DATE"       { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "DBCS"       { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "DELSTR"     { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "DELWORD"    { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "DIGITS"     { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "D2C"        { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "D2X"        { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "ERRORTEXT"  { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "EXTERNALS"  { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "FIND"       { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "FORM"       { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "FORMAT"     { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "FUZZ"       { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "GETMSG"     { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "INDEX"      { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "INSERT"     { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "JUSTIFY"    { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "LASTPOS"    { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "LEFT"       { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "LENGTH"     { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "LINESIZE"   { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "LISTDSI"    { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "MAX"        { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "MIN"        { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "MSG"        { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "MVSVAR"     { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "OUTTRAP"    { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "OVERLAY"    { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "POS"        { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "PROMPT"     { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "QUEUED"     { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "RANDOM"     { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "REVERSE"    { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "RIGHT"      { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "SETLANG"    { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "SIGN"       { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "SOURCELINE" { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "SPACE"      { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "STORAGE"    { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "STRIP"      { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "SUBSTR"     { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "SUBWORD"    { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "SYMBOL"     { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "SYSCPUS"    { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "SYSDSN"     { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "SYSVAR"     { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "TIME"       { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "TRACE"      { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "TRANSLATE"  { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "TRUNC"      { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "USERID"     { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "VALUE"      { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "VERIFY"     { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "WORD"       { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "WORDINDEX"  { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "WORDLENGTH" { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "WORDPOS"    { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "WORDS"      { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "XRANGE"     { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "X2B"        { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "X2C"        { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "X2D"        { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  /* TSO/E external functions */
  "GETMSG"     { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "LISTDSI"    { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "MSG"        { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "MVSVAR"     { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "OUTTRAP"    { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "PROMPT"     { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "SETLANG"    { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "STORAGE"    { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "SYSCPUS"    { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "SYSDSN"     { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "SYSVAR"     { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "TRAPMSG"    { return (new RexxToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  /* operators */
  ","   { return (new RexxToken(COMMA,yytext(),yyline,yycolumn,yychar)); }
  ":"   { return (new RexxToken(COLON,yytext(),yyline,yycolumn,yychar)); }
  ";"   { return (new RexxToken(SEMICOLON,yytext(),yyline,yycolumn,yychar)); }
  "("   { return (new RexxToken(LEFT_PARENTHESIS,yytext(),yyline,yycolumn,yychar)); }
  ")"   { return (new RexxToken(RIGHT_PARENTHESIS,yytext(),yyline,yycolumn,yychar)); }
  "."   { return (new RexxToken(DOT,yytext(),yyline,yycolumn,yychar)); }
  "+"   { return (new RexxToken(PLUS,yytext(),yyline,yycolumn,yychar)); }
  "-"   { return (new RexxToken(MINUS,yytext(),yyline,yycolumn,yychar)); }
  "*"   { return (new RexxToken(MULTI,yytext(),yyline,yycolumn,yychar)); }
  "/"   { return (new RexxToken(DIVIDE,yytext(),yyline,yycolumn,yychar)); }
  "//"  { return (new RexxToken(DIVIDE_INTEGER,yytext(),yyline,yycolumn,yychar)); }
  "%"   { return (new RexxToken(DIVIDE_REMAINDER,yytext(),yyline,yycolumn,yychar)); }
  "="   { return (new RexxToken(EQ,yytext(),yyline,yycolumn,yychar)); }
  "^="  { return (new RexxToken(NE,yytext(),yyline,yycolumn,yychar)); }
  "<>"  { return (new RexxToken(NE,yytext(),yyline,yycolumn,yychar)); }
  "><"  { return (new RexxToken(NE,yytext(),yyline,yycolumn,yychar)); }
  "<"   { return (new RexxToken(LT,yytext(),yyline,yycolumn,yychar)); }
  "<="  { return (new RexxToken(LE,yytext(),yyline,yycolumn,yychar)); }
  ">"   { return (new RexxToken(GT,yytext(),yyline,yycolumn,yychar)); }
  ">="  { return (new RexxToken(GE,yytext(),yyline,yycolumn,yychar)); }
  "=="  { return (new RexxToken(STRICT_EQ,yytext(),yyline,yycolumn,yychar)); }
  "^==" { return (new RexxToken(STRICT_NE,yytext(),yyline,yycolumn,yychar)); }
  "<<"  { return (new RexxToken(STRICT_LT,yytext(),yyline,yycolumn,yychar)); }
  "<<=" { return (new RexxToken(STRICT_LE,yytext(),yyline,yycolumn,yychar)); }
  ">>"  { return (new RexxToken(STRICT_GT,yytext(),yyline,yycolumn,yychar)); }
  ">>=" { return (new RexxToken(STRICT_GE,yytext(),yyline,yycolumn,yychar)); }
  "&"   { return (new RexxToken(AND,yytext(),yyline,yycolumn,yychar)); }
  "!"   { return (new RexxToken(OR,yytext(),yyline,yycolumn,yychar)); }
  "^"   { return (new RexxToken(NOT,yytext(),yyline,yycolumn,yychar)); }
  "!!"  { return (new RexxToken(CONCAT,yytext(),yyline,yycolumn,yychar)); }

  {NONNEWLINE_WHITE_SPACE_CHAR}+ { return (new RexxToken(WHITESPACE,yytext(),yyline,yycolumn,yychar)); }

  "/*" { yybegin(COMMENT); _commentLine = yyline; _commentColumn = yycolumn; _commentChar = yychar; _commentText = new StringBuilder("/*"); _commentCount++; }

  \"{DQUOTE_STRING_TEXT}\" {
    return (new RexxToken(DQUOTE_STRING,yytext(),yyline,yycolumn,yychar));
  }

  \"{DQUOTE_STRING_TEXT} {
    addError(E_UNCLOSED_STRING);
    return (new RexxToken(DQUOTE_STRING_UNCLOSED,yytext(),yyline,yycolumn,yychar));
  }

  '{SQUOTE_STRING_TEXT}' {
    return (new RexxToken(SQUOTE_STRING,yytext(),yyline,yycolumn,yychar));
  }

  '{SQUOTE_STRING_TEXT} {
    addError(E_UNCLOSED_STRING);
    return (new RexxToken(SQUOTE_STRING_UNCLOSED,yytext(),yyline,yycolumn,yychar));
  }

  {DIGIT}+ { return (new RexxToken(NUMBER,yytext(),yyline,yycolumn,yychar)); }

  {IDENT} { return (new RexxToken(IDENTIFIER,yytext(),yyline,yycolumn,yychar)); }

  {NEWLINE} { return (new RexxToken(WHITESPACE,yytext(),yyline,yycolumn,yychar,yychar+yylength())); }
}

/*--------------------------------------------------------------------------------------
* State COMMENT
*-------------------------------------------------------------------------------------*/
<COMMENT> {
  "/*" { _commentCount++; _commentText.append(yytext()); }
  "*/" { _commentText.append(yytext()); if (--_commentCount == 0) { yybegin(YYINITIAL); return (new RexxToken(REXX_COMMENT,_commentText.toString(),_commentLine,_commentColumn,_commentChar)); } }
  {COMMENT_TEXT} { _commentText.append(yytext()); }
  {NEWLINE} { _commentText.append(yytext()); }
}

. {
  System.out.println("Illegal character: <" + yytext() + ">");
	addError(E_ILLEGAL_CHAR);
}

