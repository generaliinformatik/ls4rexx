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
package de.holzem.lsp.lsp4rexx.rexxscanner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import de.holzem.lsp.lsp4rexx.rexxscanner.testutils.RexxLexerBuilder;

/**
 * RexxOperatorTest
 */
public class RexxOperatorTest {

	@Test
	public void testMiscOperators() throws IOException
	{
		final RexxLexer lexer = new RexxLexerBuilder() //
				.add(",:;().!!").build();
		RexxToken stringToken;
		stringToken = lexer.nextToken();
		assertThat(stringToken.getType(), is(equalTo(TokenType.COMMA)));
		assertThat(stringToken.getText(), is(equalTo(",")));
		stringToken = lexer.nextToken();
		assertThat(stringToken.getType(), is(equalTo(TokenType.COLON)));
		assertThat(stringToken.getText(), is(equalTo(":")));
		stringToken = lexer.nextToken();
		assertThat(stringToken.getType(), is(equalTo(TokenType.SEMICOLON)));
		assertThat(stringToken.getText(), is(equalTo(";")));
		stringToken = lexer.nextToken();
		assertThat(stringToken.getType(), is(equalTo(TokenType.LEFT_PARENTHESIS)));
		assertThat(stringToken.getText(), is(equalTo("(")));
		stringToken = lexer.nextToken();
		assertThat(stringToken.getType(), is(equalTo(TokenType.RIGHT_PARENTHESIS)));
		assertThat(stringToken.getText(), is(equalTo(")")));
		stringToken = lexer.nextToken();
		assertThat(stringToken.getType(), is(equalTo(TokenType.DOT)));
		assertThat(stringToken.getText(), is(equalTo(".")));
		stringToken = lexer.nextToken();
		assertThat(stringToken.getType(), is(equalTo(TokenType.CONCAT)));
		assertThat(stringToken.getText(), is(equalTo("!!")));
		assertThat(lexer.nextToken(), is(nullValue()));
		assertThat(lexer.getRexxErrors().getErrors(), is(empty()));
	}

	@Test
	public void testMathOperators() throws IOException
	{
		final RexxLexer lexer = new RexxLexerBuilder() //
				.add("+-*///%").build();
		RexxToken stringToken;
		stringToken = lexer.nextToken();
		assertThat(stringToken.getType(), is(equalTo(TokenType.PLUS)));
		assertThat(stringToken.getText(), is(equalTo("+")));
		stringToken = lexer.nextToken();
		assertThat(stringToken.getType(), is(equalTo(TokenType.MINUS)));
		assertThat(stringToken.getText(), is(equalTo("-")));
		stringToken = lexer.nextToken();
		assertThat(stringToken.getType(), is(equalTo(TokenType.MULTI)));
		assertThat(stringToken.getText(), is(equalTo("*")));
		stringToken = lexer.nextToken();
		assertThat(stringToken.getType(), is(equalTo(TokenType.DIVIDE_INTEGER)));
		assertThat(stringToken.getText(), is(equalTo("//")));
		stringToken = lexer.nextToken();
		assertThat(stringToken.getType(), is(equalTo(TokenType.DIVIDE)));
		assertThat(stringToken.getText(), is(equalTo("/")));
		stringToken = lexer.nextToken();
		assertThat(stringToken.getType(), is(equalTo(TokenType.DIVIDE_REMAINDER)));
		assertThat(stringToken.getText(), is(equalTo("%")));
		assertThat(lexer.nextToken(), is(nullValue()));
		assertThat(lexer.getRexxErrors().getErrors(), is(empty()));
	}

	@Test
	public void testCompareOperators() throws IOException
	{
		final RexxLexer lexer = new RexxLexerBuilder() //
				.add("= ^= <> >< < <= > >=").build();
		RexxToken stringToken;
		stringToken = lexer.nextToken();
		assertThat(stringToken.getType(), is(equalTo(TokenType.EQ)));
		assertThat(stringToken.getText(), is(equalTo("=")));
		stringToken = lexer.nextToken();
		assertThat(stringToken.getType(), is(equalTo(TokenType.NE)));
		assertThat(stringToken.getText(), is(equalTo("^=")));
		stringToken = lexer.nextToken();
		assertThat(stringToken.getType(), is(equalTo(TokenType.NE)));
		assertThat(stringToken.getText(), is(equalTo("<>")));
		stringToken = lexer.nextToken();
		assertThat(stringToken.getType(), is(equalTo(TokenType.NE)));
		assertThat(stringToken.getText(), is(equalTo("><")));
		stringToken = lexer.nextToken();
		assertThat(stringToken.getType(), is(equalTo(TokenType.LT)));
		assertThat(stringToken.getText(), is(equalTo("<")));
		stringToken = lexer.nextToken();
		assertThat(stringToken.getType(), is(equalTo(TokenType.LE)));
		assertThat(stringToken.getText(), is(equalTo("<=")));
		stringToken = lexer.nextToken();
		assertThat(stringToken.getType(), is(equalTo(TokenType.GT)));
		assertThat(stringToken.getText(), is(equalTo(">")));
		stringToken = lexer.nextToken();
		assertThat(stringToken.getType(), is(equalTo(TokenType.GE)));
		assertThat(stringToken.getText(), is(equalTo(">=")));
		assertThat(lexer.nextToken(), is(nullValue()));
		assertThat(lexer.getRexxErrors().getErrors(), is(empty()));
	}

	@Test
	public void testStrictCompareOperators() throws IOException
	{
		final RexxLexer lexer = new RexxLexerBuilder() //
				.add("== ^== << <<= >> >>=").build();
		RexxToken stringToken;
		stringToken = lexer.nextToken();
		assertThat(stringToken.getType(), is(equalTo(TokenType.STRICT_EQ)));
		assertThat(stringToken.getText(), is(equalTo("==")));
		stringToken = lexer.nextToken();
		assertThat(stringToken.getType(), is(equalTo(TokenType.STRICT_NE)));
		assertThat(stringToken.getText(), is(equalTo("^==")));
		stringToken = lexer.nextToken();
		assertThat(stringToken.getType(), is(equalTo(TokenType.STRICT_LT)));
		assertThat(stringToken.getText(), is(equalTo("<<")));
		stringToken = lexer.nextToken();
		assertThat(stringToken.getType(), is(equalTo(TokenType.STRICT_LE)));
		assertThat(stringToken.getText(), is(equalTo("<<=")));
		stringToken = lexer.nextToken();
		assertThat(stringToken.getType(), is(equalTo(TokenType.STRICT_GT)));
		assertThat(stringToken.getText(), is(equalTo(">>")));
		stringToken = lexer.nextToken();
		assertThat(stringToken.getType(), is(equalTo(TokenType.STRICT_GE)));
		assertThat(stringToken.getText(), is(equalTo(">>=")));
		assertThat(lexer.nextToken(), is(nullValue()));
		assertThat(lexer.getRexxErrors().getErrors(), is(empty()));
	}

	@Test
	public void testLogicalOperators() throws IOException
	{
		final RexxLexer lexer = new RexxLexerBuilder() //
				.add("& ! ^").build();
		RexxToken stringToken;
		stringToken = lexer.nextToken();
		assertThat(stringToken.getType(), is(equalTo(TokenType.AND)));
		assertThat(stringToken.getText(), is(equalTo("&")));
		stringToken = lexer.nextToken();
		assertThat(stringToken.getType(), is(equalTo(TokenType.OR)));
		assertThat(stringToken.getText(), is(equalTo("!")));
		stringToken = lexer.nextToken();
		assertThat(stringToken.getType(), is(equalTo(TokenType.NOT)));
		assertThat(stringToken.getText(), is(equalTo("^")));
		assertThat(lexer.nextToken(), is(nullValue()));
		assertThat(lexer.getRexxErrors().getErrors(), is(empty()));
	}

}
