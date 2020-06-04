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
 * LStringTest
 */
class LStringTest
{
	@Test
	void testSimpleDQuoteString() throws IOException
	{
		final LLexer lexer = new LLexerBuilder() //
				.add("\"string\"").build();
		final LToken stringToken = lexer.nextToken();
		assertThat(stringToken.getType(), is(equalTo(LTokenType.DQUOTE_STRING)));
		assertThat(stringToken.getText(), is(equalTo("\"string\"")));
		assertThat(lexer.nextToken(), is(nullValue()));
		assertThat(lexer.getLErrors().getErrors(), is(empty()));
	}

	@Test
	void testTwoDQuoteStrings() throws IOException
	{
		final LLexer lexer = new LLexerBuilder() //
				.add("\"string1\" \"string2\"").build();
		final LToken string1Token = lexer.nextToken();
		assertThat(string1Token.getType(), is(equalTo(LTokenType.DQUOTE_STRING)));
		assertThat(string1Token.getText(), is(equalTo("\"string1\"")));
		final LToken whitespaceToken = lexer.nextToken();
		assertThat(whitespaceToken.getType(), is(equalTo(LTokenType.WHITESPACE)));
		assertThat(whitespaceToken.getText(), is(equalTo(" ")));
		final LToken string2Token = lexer.nextToken();
		assertThat(string2Token.getType(), is(equalTo(LTokenType.DQUOTE_STRING)));
		assertThat(string2Token.getText(), is(equalTo("\"string2\"")));
		assertThat(lexer.nextToken(), is(nullValue()));
		assertThat(lexer.getLErrors().getErrors(), is(empty()));
	}

	@Test
	void testUnclosedDQuoteString() throws IOException
	{
		final LLexer lexer = new LLexerBuilder() //
				.add("\"string").build();
		final LToken stringToken = lexer.nextToken();
		assertThat(stringToken.getType(), is(equalTo(LTokenType.DQUOTE_STRING_UNCLOSED)));
		assertThat(stringToken.getText(), is(equalTo("\"string")));
		assertThat(lexer.nextToken(), is(nullValue()));
		assertThat(lexer.getLErrors().getErrors().get(0), is(equalTo(LError.E_UNCLOSED_STRING)));
	}

	@Test
	void testStringDQuoteDuplication() throws IOException
	{
		final LLexer lexer = new LLexerBuilder() //
				.add("\"--\"\"--\"").build();
		final LToken stringToken = lexer.nextToken();
		assertThat(stringToken.getType(), is(equalTo(LTokenType.DQUOTE_STRING)));
		assertThat(stringToken.getText(), is(equalTo("\"--\"\"--\"")));
		assertThat(lexer.nextToken(), is(nullValue()));
		assertThat(lexer.getLErrors().getErrors(), is(empty()));
	}

	@Test
	void testSimpleSQuoteString() throws IOException
	{
		final LLexer lexer = new LLexerBuilder() //
				.add("'string'").build();
		final LToken stringToken = lexer.nextToken();
		assertThat(stringToken.getType(), is(equalTo(LTokenType.SQUOTE_STRING)));
		assertThat(stringToken.getText(), is(equalTo("'string'")));
		assertThat(lexer.nextToken(), is(nullValue()));
		assertThat(lexer.getLErrors().getErrors(), is(empty()));
	}

	@Test
	void testTwoSQuoteStrings() throws IOException
	{
		final LLexer lexer = new LLexerBuilder() //
				.add("'string1' 'string2'").build();
		final LToken string1Token = lexer.nextToken();
		assertThat(string1Token.getType(), is(equalTo(LTokenType.SQUOTE_STRING)));
		assertThat(string1Token.getText(), is(equalTo("'string1'")));
		final LToken whitespaceToken = lexer.nextToken();
		assertThat(whitespaceToken.getType(), is(equalTo(LTokenType.WHITESPACE)));
		assertThat(whitespaceToken.getText(), is(equalTo(" ")));
		final LToken string2Token = lexer.nextToken();
		assertThat(string2Token.getType(), is(equalTo(LTokenType.SQUOTE_STRING)));
		assertThat(string2Token.getText(), is(equalTo("'string2'")));
		assertThat(lexer.nextToken(), is(nullValue()));
		assertThat(lexer.getLErrors().getErrors(), is(empty()));
	}

	@Test
	void testUnclosedSQuoteString() throws IOException
	{
		final LLexer lexer = new LLexerBuilder() //
				.add("'string").build();
		final LToken stringToken = lexer.nextToken();
		assertThat(stringToken.getType(), is(equalTo(LTokenType.SQUOTE_STRING_UNCLOSED)));
		assertThat(stringToken.getText(), is(equalTo("'string")));
		assertThat(lexer.nextToken(), is(nullValue()));
		assertThat(lexer.getLErrors().getErrors().get(0), is(equalTo(LError.E_UNCLOSED_STRING)));
	}

	@Test
	void testStringSQuoteDuplication() throws IOException
	{
		final LLexer lexer = new LLexerBuilder() //
				.add("'--''--'").build();
		final LToken stringToken = lexer.nextToken();
		assertThat(stringToken.getType(), is(equalTo(LTokenType.SQUOTE_STRING)));
		assertThat(stringToken.getText(), is(equalTo("'--''--'")));
		assertThat(lexer.nextToken(), is(nullValue()));
		assertThat(lexer.getLErrors().getErrors(), is(empty()));
	}

	@Test
	void testMixedQuoteStrings() throws IOException
	{
		final LLexer lexer = new LLexerBuilder() //
				.add(" '\"' \"'\" ").build();
		final LToken whitespaceToken1 = lexer.nextToken();
		assertThat(whitespaceToken1.getType(), is(equalTo(LTokenType.WHITESPACE)));
		assertThat(whitespaceToken1.getText(), is(equalTo(" ")));
		final LToken string1Token = lexer.nextToken();
		assertThat(string1Token.getType(), is(equalTo(LTokenType.SQUOTE_STRING)));
		assertThat(string1Token.getText(), is(equalTo("'\"'")));
		final LToken whitespaceToken2 = lexer.nextToken();
		assertThat(whitespaceToken2.getType(), is(equalTo(LTokenType.WHITESPACE)));
		assertThat(whitespaceToken2.getText(), is(equalTo(" ")));
		final LToken string2Token = lexer.nextToken();
		assertThat(string2Token.getType(), is(equalTo(LTokenType.DQUOTE_STRING)));
		assertThat(string2Token.getText(), is(equalTo("\"'\"")));
		final LToken whitespaceToken3 = lexer.nextToken();
		assertThat(whitespaceToken3.getType(), is(equalTo(LTokenType.WHITESPACE)));
		assertThat(whitespaceToken3.getText(), is(equalTo(" ")));
		assertThat(lexer.nextToken(), is(nullValue()));
		assertThat(lexer.getLErrors().getErrors(), is(empty()));
	}
}