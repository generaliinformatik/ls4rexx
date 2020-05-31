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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import de.holzem.ls.language.testutils.LLexerBuilder;

/**
 * LOperatorTest
 */
class LOperatorTest
{
	@Test
	void testMiscOperators() throws IOException
	{
		final LLexer lexer = new LLexerBuilder() //
				.add(",:;().!!").build();
		LToken stringToken;
		stringToken = lexer.nextRealToken();
		assertThat(stringToken.getType(), is(equalTo(LTokenType.COMMA)));
		assertThat(stringToken.getText(), is(equalTo(",")));
		stringToken = lexer.nextRealToken();
		assertThat(stringToken.getType(), is(equalTo(LTokenType.COLON)));
		assertThat(stringToken.getText(), is(equalTo(":")));
		stringToken = lexer.nextRealToken();
		assertThat(stringToken.getType(), is(equalTo(LTokenType.SEMICOLON)));
		assertThat(stringToken.getText(), is(equalTo(";")));
		stringToken = lexer.nextRealToken();
		assertThat(stringToken.getType(), is(equalTo(LTokenType.LEFT_PARENTHESIS)));
		assertThat(stringToken.getText(), is(equalTo("(")));
		stringToken = lexer.nextRealToken();
		assertThat(stringToken.getType(), is(equalTo(LTokenType.RIGHT_PARENTHESIS)));
		assertThat(stringToken.getText(), is(equalTo(")")));
		stringToken = lexer.nextRealToken();
		assertThat(stringToken.getType(), is(equalTo(LTokenType.DOT)));
		assertThat(stringToken.getText(), is(equalTo(".")));
		stringToken = lexer.nextRealToken();
		assertThat(stringToken.getType(), is(equalTo(LTokenType.CONCAT)));
		assertThat(stringToken.getText(), is(equalTo("!!")));
		assertThat(lexer.nextRealToken(), is(nullValue()));
		assertThat(lexer.getLErrors().getErrors(), is(empty()));
	}

	@Test
	void testMathOperators() throws IOException
	{
		final LLexer lexer = new LLexerBuilder() //
				.add("+-*///%").build();
		LToken stringToken;
		stringToken = lexer.nextRealToken();
		assertThat(stringToken.getType(), is(equalTo(LTokenType.PLUS)));
		assertThat(stringToken.getText(), is(equalTo("+")));
		stringToken = lexer.nextRealToken();
		assertThat(stringToken.getType(), is(equalTo(LTokenType.MINUS)));
		assertThat(stringToken.getText(), is(equalTo("-")));
		stringToken = lexer.nextRealToken();
		assertThat(stringToken.getType(), is(equalTo(LTokenType.MULTI)));
		assertThat(stringToken.getText(), is(equalTo("*")));
		stringToken = lexer.nextRealToken();
		assertThat(stringToken.getType(), is(equalTo(LTokenType.DIVIDE_INTEGER)));
		assertThat(stringToken.getText(), is(equalTo("//")));
		stringToken = lexer.nextRealToken();
		assertThat(stringToken.getType(), is(equalTo(LTokenType.DIVIDE)));
		assertThat(stringToken.getText(), is(equalTo("/")));
		stringToken = lexer.nextRealToken();
		assertThat(stringToken.getType(), is(equalTo(LTokenType.DIVIDE_REMAINDER)));
		assertThat(stringToken.getText(), is(equalTo("%")));
		assertThat(lexer.nextRealToken(), is(nullValue()));
		assertThat(lexer.getLErrors().getErrors(), is(empty()));
	}

	@Test
	void testCompareOperators() throws IOException
	{
		final LLexer lexer = new LLexerBuilder() //
				.add("= ^= <> >< < <= > >=").build();
		LToken stringToken;
		stringToken = lexer.nextRealToken();
		assertThat(stringToken.getType(), is(equalTo(LTokenType.EQ)));
		assertThat(stringToken.getText(), is(equalTo("=")));
		stringToken = lexer.nextRealToken();
		assertThat(stringToken.getType(), is(equalTo(LTokenType.NE)));
		assertThat(stringToken.getText(), is(equalTo("^=")));
		stringToken = lexer.nextRealToken();
		assertThat(stringToken.getType(), is(equalTo(LTokenType.NE)));
		assertThat(stringToken.getText(), is(equalTo("<>")));
		stringToken = lexer.nextRealToken();
		assertThat(stringToken.getType(), is(equalTo(LTokenType.NE)));
		assertThat(stringToken.getText(), is(equalTo("><")));
		stringToken = lexer.nextRealToken();
		assertThat(stringToken.getType(), is(equalTo(LTokenType.LT)));
		assertThat(stringToken.getText(), is(equalTo("<")));
		stringToken = lexer.nextRealToken();
		assertThat(stringToken.getType(), is(equalTo(LTokenType.LE)));
		assertThat(stringToken.getText(), is(equalTo("<=")));
		stringToken = lexer.nextRealToken();
		assertThat(stringToken.getType(), is(equalTo(LTokenType.GT)));
		assertThat(stringToken.getText(), is(equalTo(">")));
		stringToken = lexer.nextRealToken();
		assertThat(stringToken.getType(), is(equalTo(LTokenType.GE)));
		assertThat(stringToken.getText(), is(equalTo(">=")));
		assertThat(lexer.nextRealToken(), is(nullValue()));
		assertThat(lexer.getLErrors().getErrors(), is(empty()));
	}

	@Test
	void testStrictCompareOperators() throws IOException
	{
		final LLexer lexer = new LLexerBuilder() //
				.add("== ^== << <<= >> >>=").build();
		LToken stringToken;
		stringToken = lexer.nextRealToken();
		assertThat(stringToken.getType(), is(equalTo(LTokenType.STRICT_EQ)));
		assertThat(stringToken.getText(), is(equalTo("==")));
		stringToken = lexer.nextRealToken();
		assertThat(stringToken.getType(), is(equalTo(LTokenType.STRICT_NE)));
		assertThat(stringToken.getText(), is(equalTo("^==")));
		stringToken = lexer.nextRealToken();
		assertThat(stringToken.getType(), is(equalTo(LTokenType.STRICT_LT)));
		assertThat(stringToken.getText(), is(equalTo("<<")));
		stringToken = lexer.nextRealToken();
		assertThat(stringToken.getType(), is(equalTo(LTokenType.STRICT_LE)));
		assertThat(stringToken.getText(), is(equalTo("<<=")));
		stringToken = lexer.nextRealToken();
		assertThat(stringToken.getType(), is(equalTo(LTokenType.STRICT_GT)));
		assertThat(stringToken.getText(), is(equalTo(">>")));
		stringToken = lexer.nextRealToken();
		assertThat(stringToken.getType(), is(equalTo(LTokenType.STRICT_GE)));
		assertThat(stringToken.getText(), is(equalTo(">>=")));
		assertThat(lexer.nextRealToken(), is(nullValue()));
		assertThat(lexer.getLErrors().getErrors(), is(empty()));
	}

	@Test
	void testLogicalOperators() throws IOException
	{
		final LLexer lexer = new LLexerBuilder() //
				.add("& ! ^").build();
		LToken stringToken;
		stringToken = lexer.nextRealToken();
		assertThat(stringToken.getType(), is(equalTo(LTokenType.AND)));
		assertThat(stringToken.getText(), is(equalTo("&")));
		stringToken = lexer.nextRealToken();
		assertThat(stringToken.getType(), is(equalTo(LTokenType.OR)));
		assertThat(stringToken.getText(), is(equalTo("!")));
		stringToken = lexer.nextRealToken();
		assertThat(stringToken.getType(), is(equalTo(LTokenType.NOT)));
		assertThat(stringToken.getText(), is(equalTo("^")));
		assertThat(lexer.nextRealToken(), is(nullValue()));
		assertThat(lexer.getLErrors().getErrors(), is(empty()));
	}
}
