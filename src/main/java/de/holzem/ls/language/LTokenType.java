/**
 *  Copyright (c) 2020 Markus Holzem
 *
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v2.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *  Contributors:
 *  Markus Holzem <markus@holzem.de>
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
