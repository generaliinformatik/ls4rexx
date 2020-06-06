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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import de.holzem.ls.language.testutils.LLexerBuilder;

/**
 * LCommentTest
 */
class LCommentTest
{
	@Test
	void testCommentSingleLine() throws IOException
	{
		final LLexer lexer = new LLexerBuilder() //
				.addln("/* comment */").build();
		LToken stringToken;
		stringToken = lexer.nextToken();
		assertThat(stringToken.getType(), is(equalTo(LTokenType.COMMENT)));
		assertThat(stringToken.getText(), is(equalTo("/* comment */")));
		assertThat(stringToken.getLine(), is(equalTo(0)));
		assertThat(stringToken.getColumn(), is(equalTo(0)));
		assertThat(stringToken.getCharBegin(), is(equalTo(0L)));
		assertThat(stringToken.getCharEnd(), is(equalTo(13L)));
		stringToken = lexer.nextToken();
		assertThat(stringToken.getType(), is(equalTo(LTokenType.WHITESPACE)));
		assertThat(stringToken.getText(), is(equalTo("\r\n")));
		assertThat(stringToken.getLine(), is(equalTo(0)));
		assertThat(stringToken.getColumn(), is(equalTo(13)));
		assertThat(stringToken.getCharBegin(), is(equalTo(13L)));
		assertThat(stringToken.getCharEnd(), is(equalTo(15L)));
		assertThat(lexer.nextToken(), is(nullValue()));
		assertThat(lexer.getLErrors().getErrors(), is(empty()));
	}

	@Test
	void testNestedCommentSingleLine() throws IOException
	{
		final LLexer lexer = new LLexerBuilder() //
				.add("/* outer comment /* inner comment */ outer comment*/").build();
		LToken stringToken;
		stringToken = lexer.nextToken();
		assertThat(stringToken.getType(), is(equalTo(LTokenType.COMMENT)));
		assertThat(stringToken.getText(), is(equalTo("/* outer comment /* inner comment */ outer comment*/")));
		assertThat(lexer.nextToken(), is(nullValue()));
		assertThat(lexer.getLErrors().getErrors(), is(empty()));
	}

	@Test
	void testCommentMultiLine() throws IOException
	{
		final LLexer lexer = new LLexerBuilder() //
				.addln("/* First Line") //
				.addln("   Second Line") //
				.add("   Third Line */").build();
		LToken token;
		token = lexer.nextToken();
		assertThat(token.getType(), is(equalTo(LTokenType.COMMENT)));
		assertThat(token.getText(), is(equalTo("/* First Line\r\n   Second Line\r\n   Third Line */")));
		assertThat(lexer.nextToken(), is(nullValue()));
		assertThat(lexer.getLErrors().getErrors(), is(empty()));
	}

	@Test
	void testNestedCommentMultiLine() throws IOException
	{
		final LLexer lexer = new LLexerBuilder() //
				.addln("/* First Line") //
				.addln("   /* Second Line */") //
				.add("   Third Line */").build();
		LToken token;
		token = lexer.nextToken();
		assertThat(token.getType(), is(equalTo(LTokenType.COMMENT)));
		assertThat(token.getText(), is(equalTo("/* First Line\r\n   /* Second Line */\r\n   Third Line */")));
		assertThat(lexer.nextToken(), is(nullValue()));
		assertThat(lexer.getLErrors().getErrors(), is(empty()));
	}

	@Test
	void testCommentWithStar() throws IOException
	{
		final LLexer lexer = new LLexerBuilder() //
				.addln("/*REXX******************************************************************") //
				.addln("*") //
				.addln("* REXX-Name")//
				.addln("*") //
				.add("***********************************************************************/") //
				.build();
		LToken token;
		token = lexer.nextToken();
		assertThat(token.getType(), is(equalTo(LTokenType.COMMENT)));
		assertThat(token.getText(), is(equalTo(
				"/*REXX******************************************************************\r\n*\r\n* REXX-Name\r\n*\r\n***********************************************************************/")));
		assertThat(lexer.nextToken(), is(nullValue()));
		assertThat(lexer.getLErrors().getErrors(), is(empty()));
	}
}
