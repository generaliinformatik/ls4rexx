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
  private int _commentCount = 0;

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
Ident = {ALPHA}({ALPHA}|{DIGIT}|_)*

%%

/*--------------------------------------------------------------------------------------
* State YYINITIAL
*-------------------------------------------------------------------------------------*/
<YYINITIAL> {
  ","   { return (new RexxToken(COMMA,yytext(),yyline,yycolumn,yychar,yychar+1)); }
  ":"   { return (new RexxToken(COLON,yytext(),yyline,yycolumn,yychar,yychar+1)); }
  ";"   { return (new RexxToken(SEMICOLON,yytext(),yyline,yycolumn,yychar,yychar+1)); }
  "("   { return (new RexxToken(LEFT_PARENTHESIS,yytext(),yyline,yycolumn,yychar,yychar+1)); }
  ")"   { return (new RexxToken(RIGHT_PARENTHESIS,yytext(),yyline,yycolumn,yychar,yychar+1)); }
  "."   { return (new RexxToken(DOT,yytext(),yyline,yycolumn,yychar,yychar+1)); }
  "+"   { return (new RexxToken(PLUS,yytext(),yyline,yycolumn,yychar,yychar+1)); }
  "-"   { return (new RexxToken(MINUS,yytext(),yyline,yycolumn,yychar,yychar+1)); }
  "*"   { return (new RexxToken(MULTI,yytext(),yyline,yycolumn,yychar,yychar+1)); }
  "/"   { return (new RexxToken(DIVIDE,yytext(),yyline,yycolumn,yychar,yychar+1)); }
  "//"  { return (new RexxToken(DIVIDE_INTEGER,yytext(),yyline,yycolumn,yychar,yychar+1)); }
  "%"   { return (new RexxToken(DIVIDE_REMAINDER,yytext(),yyline,yycolumn,yychar,yychar+1)); }
  "="   { return (new RexxToken(EQ,yytext(),yyline,yycolumn,yychar,yychar+1)); }
  "^="  { return (new RexxToken(NE,yytext(),yyline,yycolumn,yychar,yychar+2)); }
  "<>"  { return (new RexxToken(NE,yytext(),yyline,yycolumn,yychar,yychar+2)); }
  "><"  { return (new RexxToken(NE,yytext(),yyline,yycolumn,yychar,yychar+2)); }
  "<"   { return (new RexxToken(LT,yytext(),yyline,yycolumn,yychar,yychar+1)); }
  "<="  { return (new RexxToken(LE,yytext(),yyline,yycolumn,yychar,yychar+2)); }
  ">"   { return (new RexxToken(GT,yytext(),yyline,yycolumn,yychar,yychar+1)); }
  ">="  { return (new RexxToken(GE,yytext(),yyline,yycolumn,yychar,yychar+2)); }
  "=="  { return (new RexxToken(STRICT_EQ,yytext(),yyline,yycolumn,yychar,yychar+1)); }
  "^==" { return (new RexxToken(STRICT_NE,yytext(),yyline,yycolumn,yychar,yychar+1)); }
  "<<"  { return (new RexxToken(STRICT_LT,yytext(),yyline,yycolumn,yychar,yychar+1)); }
  "<<=" { return (new RexxToken(STRICT_LE,yytext(),yyline,yycolumn,yychar,yychar+1)); }
  ">>"  { return (new RexxToken(STRICT_GT,yytext(),yyline,yycolumn,yychar,yychar+1)); }
  ">>=" { return (new RexxToken(STRICT_GE,yytext(),yyline,yycolumn,yychar,yychar+1)); }
  "&"   { return (new RexxToken(AND,yytext(),yyline,yycolumn,yychar,yychar+1)); }
  "!"   { return (new RexxToken(OR,yytext(),yyline,yycolumn,yychar,yychar+1)); }
  "^"   { return (new RexxToken(NOT,yytext(),yyline,yycolumn,yychar,yychar+1)); }
  "!!"  { return (new RexxToken(CONCAT,yytext(),yyline,yycolumn,yychar,yychar+2)); }

  {NONNEWLINE_WHITE_SPACE_CHAR}+ { }

  "/*" { yybegin(COMMENT); _commentCount++; }

  \"{DQUOTE_STRING_TEXT}\" {
    String str =  yytext().substring(1,yylength()-1);
    return (new RexxToken(STRING,str.replaceAll("\"\"", "\""),yyline,yycolumn,yychar,yychar+yylength()));
  }

  \"{DQUOTE_STRING_TEXT} {
    String str =  yytext().substring(1,yytext().length());
    addError(E_UNCLOSED_STRING);
    
    return (new RexxToken(STRING_UNCLOSED,str,yyline,yycolumn,yychar,yychar + str.length()));
  }

  '{SQUOTE_STRING_TEXT}' {
    String str =  yytext().substring(1,yylength()-1);
    return (new RexxToken(STRING,str.replaceAll("''", "'"),yyline,yycolumn,yychar,yychar+yylength()));
  }

  '{SQUOTE_STRING_TEXT} {
    String str =  yytext().substring(1,yytext().length());
    addError(E_UNCLOSED_STRING);
    
    return (new RexxToken(STRING_UNCLOSED,str,yyline,yycolumn,yychar,yychar + str.length()));
  }

  {DIGIT}+ { return (new RexxToken(NUMBER,yytext(),yyline,yycolumn,yychar,yychar+yylength())); }

  {Ident} { return (new RexxToken(IDENTIFIER,yytext(),yyline,yycolumn,yychar,yychar+yylength())); }
}

/*--------------------------------------------------------------------------------------
* State COMMENT
*-------------------------------------------------------------------------------------*/
<COMMENT> {
  "/*" { _commentCount++; }
  "*/" { if (--_commentCount == 0) yybegin(YYINITIAL); }
  {COMMENT_TEXT} { }
}


{NEWLINE} { }

. {
  System.out.println("Illegal character: <" + yytext() + ">");
	addError(E_ILLEGAL_CHAR);
}

