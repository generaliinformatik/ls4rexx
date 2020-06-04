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
 */
package de.holzem.ls.language;

/**
 * LTokenType
 */
public enum LTokenType {
	SYNTHETIC,
	COMMENT,
	WHITESPACE,
	COMMA,
	COLON,
	SEMICOLON,
	LEFT_PARENTHESIS,
	RIGHT_PARENTHESIS,
	DOT,
	PLUS,
	MINUS,
	MULTI,
	DIVIDE,
	DIVIDE_INTEGER,
	DIVIDE_REMAINDER,
	EQ,
	NE,
	LT,
	LE,
	GT,
	GE,
	STRICT_EQ,
	STRICT_NE,
	STRICT_LT,
	STRICT_LE,
	STRICT_GT,
	STRICT_GE,
	AND,
	OR,
	NOT,
	CONCAT,
	SQUOTE_STRING,
	SQUOTE_STRING_UNCLOSED,
	DQUOTE_STRING,
	DQUOTE_STRING_UNCLOSED,
	NUMBER,
	IDENTIFIER,
	KEYWORD,
	FUNCTION

}