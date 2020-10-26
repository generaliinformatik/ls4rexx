/**
 *  Copyright (c) 2020 Generali Deutschland AG - Team Informatik
 *
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v2.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *  Contributors:
 *  Markus Holzem <markus.holzem@generali.com>
 *
 * Based on the jflex-1.8.1/examples/simple
 */
package de.generali.dev.ls.language;

import static de.generali.dev.ls.language.LTokenType.*;
import static de.generali.dev.ls.language.LErrorType.*;

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
   * or an I/O-Error occurs. WHITESPACE and COMMENT Tokens are skipped.
   *
   * @return the next token.
   * @exception java.io.IOException if any I/O-Error occurs.
   */
  public LToken nextRealToken() throws java.io.IOException {
    LToken nextToken = null;
    do {
      nextToken = nextToken();
    } while (nextToken != null && (nextToken.getType() == COMMENT || nextToken.getType() == COMMENT_UNCLOSED || nextToken.getType() == WHITESPACE));
    return nextToken;
  }
  
  /**
   * Resumes scanning until the next regular expression is matched, the end of input is encountered
   * or an I/O-Error occurs. In addition to yylex this method caters for unclosed comments.
   *
   * @return the next token.
   * @exception java.io.IOException if any I/O-Error occurs.
   */
  public LToken nextToken() throws java.io.IOException
  {
    LToken token = yylex();
    if (token == null && yyatEOF() && yystate() == COMMENT_STATE) {
      yybegin(YYINITIAL);
      token = new LToken(COMMENT_UNCLOSED, _commentText.toString(), _commentLine, _commentColumn, _commentChar);
      addError(E_UNCLOSED_COMMENT, token);
    }
    return token;
  }

  private LErrors _lErrors = new LErrors();

  private void addError(LErrorType pLErrorType, LToken pLToken) {
    _lErrors.addError(pLErrorType, pLToken);
  }

  public LErrors getErrors() {
    return _lErrors;
  }
%}

%char
%line
%column

%state COMMENT_STATE

%unicode
%ignorecase

%class LLexer
%type LToken

/*
** for debugging purposes
%debug
**
*/

ALPHA=[A-Za-z#$§_]
DIGIT=[0-9]
NONNEWLINE_WHITE_SPACE_CHAR=[\ \b\012]
NEWLINE=\r|\n|\r\n
WHITE_SPACE_CHAR=[\n\r\ \b\012]
DQUOTE_STRING_TEXT=(\"\"|[^\n\r\"]|\\{WHITE_SPACE_CHAR}+\\)*
SQUOTE_STRING_TEXT=(''|[^\n\r']|\\{WHITE_SPACE_CHAR}+\\)*
COMMENT_TEXT=([^*/\n\r]|([*][^/]))+
IDENT = {ALPHA}(\.|{ALPHA}|{DIGIT})*

%%

/*--------------------------------------------------------------------------------------
* State YYINITIAL
*-------------------------------------------------------------------------------------*/
<YYINITIAL> {
  /* keywords */
  "ADDRESS"   { return (new LToken(KEYWORD,yytext(),yyline,yycolumn,yychar)); }
  "ARG"       { return (new LToken(KEYWORD,yytext(),yyline,yycolumn,yychar)); }
  "CALL"      { return (new LToken(KEYWORD,yytext(),yyline,yycolumn,yychar)); }
  "DO"        { return (new LToken(KEYWORD,yytext(),yyline,yycolumn,yychar)); }
  "END"       { return (new LToken(KEYWORD,yytext(),yyline,yycolumn,yychar)); }
  "TO"        { return (new LToken(KEYWORD,yytext(),yyline,yycolumn,yychar)); }
  "BY"        { return (new LToken(KEYWORD,yytext(),yyline,yycolumn,yychar)); }
  "FOR"       { return (new LToken(KEYWORD,yytext(),yyline,yycolumn,yychar)); }
  "FOREVER"   { return (new LToken(KEYWORD,yytext(),yyline,yycolumn,yychar)); }
  "WHILE"     { return (new LToken(KEYWORD,yytext(),yyline,yycolumn,yychar)); }
  "DROP"      { return (new LToken(KEYWORD,yytext(),yyline,yycolumn,yychar)); }
  "EXIT"      { return (new LToken(KEYWORD,yytext(),yyline,yycolumn,yychar)); }
  "IF"        { return (new LToken(KEYWORD,yytext(),yyline,yycolumn,yychar)); }
  "THEN"      { return (new LToken(KEYWORD,yytext(),yyline,yycolumn,yychar)); }
  "ELSE"      { return (new LToken(KEYWORD,yytext(),yyline,yycolumn,yychar)); }
  "INTERPRET" { return (new LToken(KEYWORD,yytext(),yyline,yycolumn,yychar)); }
  "ITERATE"   { return (new LToken(KEYWORD,yytext(),yyline,yycolumn,yychar)); }
  "LEAVE"     { return (new LToken(KEYWORD,yytext(),yyline,yycolumn,yychar)); }
  "NOP"       { return (new LToken(KEYWORD,yytext(),yyline,yycolumn,yychar)); }
  "NUMERIC"   { return (new LToken(KEYWORD,yytext(),yyline,yycolumn,yychar)); }
  "OPTIONS"   { return (new LToken(KEYWORD,yytext(),yyline,yycolumn,yychar)); }
  "PARSE"     { return (new LToken(KEYWORD,yytext(),yyline,yycolumn,yychar)); }
  "PROCEDURE" { return (new LToken(KEYWORD,yytext(),yyline,yycolumn,yychar)); }
  "EXPOSE"    { return (new LToken(KEYWORD,yytext(),yyline,yycolumn,yychar)); }
  "PULL"      { return (new LToken(KEYWORD,yytext(),yyline,yycolumn,yychar)); }
  "PUSH"      { return (new LToken(KEYWORD,yytext(),yyline,yycolumn,yychar)); }
  "QUEUE"     { return (new LToken(KEYWORD,yytext(),yyline,yycolumn,yychar)); }
  "RETURN"    { return (new LToken(KEYWORD,yytext(),yyline,yycolumn,yychar)); }
  "SAY"       { return (new LToken(KEYWORD,yytext(),yyline,yycolumn,yychar)); }
  "SELECT"    { return (new LToken(KEYWORD,yytext(),yyline,yycolumn,yychar)); }
  "WHEN"      { return (new LToken(KEYWORD,yytext(),yyline,yycolumn,yychar)); }
  "SIGNAL"    { return (new LToken(KEYWORD,yytext(),yyline,yycolumn,yychar)); }
  "TRACE"     { return (new LToken(KEYWORD,yytext(),yyline,yycolumn,yychar)); }
  "UPPER"     { return (new LToken(KEYWORD,yytext(),yyline,yycolumn,yychar)); }
  /* functions */
  "ABBREV"     { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "ABS"        { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "ADDRESS"    { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "ARG"        { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "BITAND"     { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "BITOR"      { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "BITXOR"     { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "B2X"        { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "CENTER"     { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "CENTRE"     { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "COMPARE"    { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "CONDITION"  { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "COPIES"     { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "C2D"        { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "C2X"        { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "DATATYPE"   { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "DATE"       { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "DBCS"       { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "DELSTR"     { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "DELWORD"    { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "DIGITS"     { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "D2C"        { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "D2X"        { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "ERRORTEXT"  { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "EXTERNALS"  { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "FIND"       { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "FORM"       { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "FORMAT"     { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "FUZZ"       { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "GETMSG"     { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "INDEX"      { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "INSERT"     { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "JUSTIFY"    { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "LASTPOS"    { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "LEFT"       { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "LENGTH"     { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "LINESIZE"   { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "LISTDSI"    { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "MAX"        { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "MIN"        { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "MSG"        { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "MVSVAR"     { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "OUTTRAP"    { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "OVERLAY"    { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "POS"        { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "PROMPT"     { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "QUEUED"     { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "RANDOM"     { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "REVERSE"    { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "RIGHT"      { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "SETLANG"    { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "SIGN"       { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "SOURCELINE" { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "SPACE"      { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "STORAGE"    { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "STRIP"      { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "SUBSTR"     { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "SUBWORD"    { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "SYMBOL"     { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "SYSCPUS"    { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "SYSDSN"     { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "SYSVAR"     { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "TIME"       { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "TRACE"      { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "TRANSLATE"  { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "TRUNC"      { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "USERID"     { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "VALUE"      { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "VERIFY"     { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "WORD"       { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "WORDINDEX"  { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "WORDLENGTH" { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "WORDPOS"    { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "WORDS"      { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "XRANGE"     { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "X2B"        { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "X2C"        { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "X2D"        { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  /* TSO/E external functions */
  "GETMSG"     { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "LISTDSI"    { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "MSG"        { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "MVSVAR"     { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "OUTTRAP"    { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "PROMPT"     { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "SETLANG"    { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "STORAGE"    { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "SYSCPUS"    { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "SYSDSN"     { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "SYSVAR"     { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  "TRAPMSG"    { return (new LToken(FUNCTION,yytext(),yyline,yycolumn,yychar)); }
  /* operators */
  ","   { return (new LToken(COMMA,yytext(),yyline,yycolumn,yychar)); }
  ":"   { return (new LToken(COLON,yytext(),yyline,yycolumn,yychar)); }
  ";"   { return (new LToken(SEMICOLON,yytext(),yyline,yycolumn,yychar)); }
  "("   { return (new LToken(LEFT_PARENTHESIS,yytext(),yyline,yycolumn,yychar)); }
  ")"   { return (new LToken(RIGHT_PARENTHESIS,yytext(),yyline,yycolumn,yychar)); }
  "."   { return (new LToken(DOT,yytext(),yyline,yycolumn,yychar)); }
  "+"   { return (new LToken(PLUS,yytext(),yyline,yycolumn,yychar)); }
  "-"   { return (new LToken(MINUS,yytext(),yyline,yycolumn,yychar)); }
  "*"   { return (new LToken(MULTI,yytext(),yyline,yycolumn,yychar)); }
  "/"   { return (new LToken(DIVIDE,yytext(),yyline,yycolumn,yychar)); }
  "//"  { return (new LToken(DIVIDE_INTEGER,yytext(),yyline,yycolumn,yychar)); }
  "%"   { return (new LToken(DIVIDE_REMAINDER,yytext(),yyline,yycolumn,yychar)); }
  "="   { return (new LToken(EQ,yytext(),yyline,yycolumn,yychar)); }
  "^="  { return (new LToken(NE,yytext(),yyline,yycolumn,yychar)); }
  "<>"  { return (new LToken(NE,yytext(),yyline,yycolumn,yychar)); }
  "><"  { return (new LToken(NE,yytext(),yyline,yycolumn,yychar)); }
  "<"   { return (new LToken(LT,yytext(),yyline,yycolumn,yychar)); }
  "<="  { return (new LToken(LE,yytext(),yyline,yycolumn,yychar)); }
  ">"   { return (new LToken(GT,yytext(),yyline,yycolumn,yychar)); }
  ">="  { return (new LToken(GE,yytext(),yyline,yycolumn,yychar)); }
  "=="  { return (new LToken(STRICT_EQ,yytext(),yyline,yycolumn,yychar)); }
  "^==" { return (new LToken(STRICT_NE,yytext(),yyline,yycolumn,yychar)); }
  "<<"  { return (new LToken(STRICT_LT,yytext(),yyline,yycolumn,yychar)); }
  "<<=" { return (new LToken(STRICT_LE,yytext(),yyline,yycolumn,yychar)); }
  ">>"  { return (new LToken(STRICT_GT,yytext(),yyline,yycolumn,yychar)); }
  ">>=" { return (new LToken(STRICT_GE,yytext(),yyline,yycolumn,yychar)); }
  "&"   { return (new LToken(AND,yytext(),yyline,yycolumn,yychar)); }
  "!"   { return (new LToken(OR,yytext(),yyline,yycolumn,yychar)); }
  "^"   { return (new LToken(NOT,yytext(),yyline,yycolumn,yychar)); }
  "!!"  { return (new LToken(CONCAT,yytext(),yyline,yycolumn,yychar)); }

  {NONNEWLINE_WHITE_SPACE_CHAR}+ { return (new LToken(WHITESPACE,yytext(),yyline,yycolumn,yychar)); }

  "/*" { yybegin(COMMENT_STATE); _commentLine = yyline; _commentColumn = yycolumn; _commentChar = yychar; _commentText = new StringBuilder("/*"); _commentCount++; }

  "*/" {
    LToken token = new LToken(ILLEGAL,yytext(),yyline,yycolumn,yychar);
    addError(E_UNMATCHED_ENDCOMMENT, token);
    return token;
  }

  \"{DQUOTE_STRING_TEXT}\" {
    return (new LToken(DQUOTE_STRING,yytext(),yyline,yycolumn,yychar));
  }

  \"{DQUOTE_STRING_TEXT} {
    LToken token = new LToken(DQUOTE_STRING_UNCLOSED,yytext(),yyline,yycolumn,yychar);
    addError(E_UNCLOSED_STRING, token);
    return token;
  }

  '{SQUOTE_STRING_TEXT}' {
    return (new LToken(SQUOTE_STRING,yytext(),yyline,yycolumn,yychar));
  }

  '{SQUOTE_STRING_TEXT} {
    LToken token = new LToken(SQUOTE_STRING_UNCLOSED,yytext(),yyline,yycolumn,yychar);
    addError(E_UNCLOSED_STRING, token);
    return token;
  }

  {DIGIT}+ { return (new LToken(NUMBER,yytext(),yyline,yycolumn,yychar)); }

  {IDENT} { return (new LToken(IDENTIFIER,yytext(),yyline,yycolumn,yychar)); }

  {NEWLINE} { return (new LToken(WHITESPACE,yytext(),yyline,yycolumn,yychar,yychar+yylength())); }
}

/*--------------------------------------------------------------------------------------
* State COMMENT_STATE
*-------------------------------------------------------------------------------------*/
<COMMENT_STATE> {
  "/*"           { _commentCount++; _commentText.append(yytext()); }
  "*/"           { _commentText.append(yytext()); if (--_commentCount == 0) { yybegin(YYINITIAL); return (new LToken(COMMENT,_commentText.toString(),_commentLine,_commentColumn,_commentChar)); } }
  "/"            { _commentText.append(yytext()); if (_commentText.toString().endsWith("*/")) { if (--_commentCount == 0) { yybegin(YYINITIAL); return (new LToken(COMMENT,_commentText.toString(),_commentLine,_commentColumn,_commentChar)); } } }
  {COMMENT_TEXT} { _commentText.append(yytext()); }
  {NEWLINE}      { _commentText.append(yytext()); }
}

. {
    /*System.out.println("Illegal character: <" + yytext() + "> at (" + yyline + "," + yycolumn + ")");*/
    LToken token = new LToken(ILLEGAL,yytext(),yyline,yycolumn,yychar); 
	addError(E_ILLEGAL_CHAR, token);
	return token; 
  }

