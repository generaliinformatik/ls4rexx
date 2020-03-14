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
 * RexxStringTest
 */
public class RexxStringTest {

	@Test
	public void testSimpleDQuoteString() throws IOException
	{
		final RexxLexer lexer = new RexxLexerBuilder() //
				.add("\"string\"").build();
		final RexxToken stringToken = lexer.nextToken();
		System.out.println(stringToken);
		assertThat(stringToken.getType(), is(equalTo(TokenType.STRING)));
		assertThat(stringToken.getText(), is(equalTo("string")));
		assertThat(lexer.nextToken(), is(nullValue()));
		assertThat(lexer.getRexxErrors().getErrors(), is(empty()));
	}

	@Test
	public void testTwoDQuoteStrings() throws IOException
	{
		final RexxLexer lexer = new RexxLexerBuilder() //
				.add("\"string1\" \"string2\"").build();
		final RexxToken string1Token = lexer.nextToken();
		System.out.println(string1Token);
		assertThat(string1Token.getType(), is(equalTo(TokenType.STRING)));
		assertThat(string1Token.getText(), is(equalTo("string1")));
		final RexxToken string2Token = lexer.nextToken();
		System.out.println(string2Token);
		assertThat(string2Token.getType(), is(equalTo(TokenType.STRING)));
		assertThat(string2Token.getText(), is(equalTo("string2")));
		assertThat(lexer.nextToken(), is(nullValue()));
		assertThat(lexer.getRexxErrors().getErrors(), is(empty()));
	}

	@Test
	public void testUnclosedDQuoteString() throws IOException
	{
		final RexxLexer lexer = new RexxLexerBuilder() //
				.add("\"string").build();
		final RexxToken stringToken = lexer.nextToken();
		System.out.println(stringToken);
		assertThat(stringToken.getType(), is(equalTo(TokenType.STRING_UNCLOSED)));
		assertThat(stringToken.getText(), is(equalTo("string")));
		assertThat(lexer.nextToken(), is(nullValue()));
		assertThat(lexer.getRexxErrors().getErrors().get(0), is(equalTo(RexxError.E_UNCLOSED_STRING)));
	}

	@Test
	public void testStringDQuoteDuplication() throws IOException
	{
		final RexxLexer lexer = new RexxLexerBuilder() //
				.add("\"--\"\"--\"").build();
		final RexxToken stringToken = lexer.nextToken();
		System.out.println(stringToken);
		assertThat(stringToken.getType(), is(equalTo(TokenType.STRING)));
		assertThat(stringToken.getText(), is(equalTo("--\"--")));
		assertThat(lexer.nextToken(), is(nullValue()));
		assertThat(lexer.getRexxErrors().getErrors(), is(empty()));
	}

	@Test
	public void testSimpleSQuoteString() throws IOException
	{
		final RexxLexer lexer = new RexxLexerBuilder() //
				.add("'string'").build();
		final RexxToken stringToken = lexer.nextToken();
		System.out.println(stringToken);
		assertThat(stringToken.getType(), is(equalTo(TokenType.STRING)));
		assertThat(stringToken.getText(), is(equalTo("string")));
		assertThat(lexer.nextToken(), is(nullValue()));
		assertThat(lexer.getRexxErrors().getErrors(), is(empty()));
	}

	@Test
	public void testTwoSQuoteStrings() throws IOException
	{
		final RexxLexer lexer = new RexxLexerBuilder() //
				.add("'string1' 'string2'").build();
		final RexxToken string1Token = lexer.nextToken();
		System.out.println(string1Token);
		assertThat(string1Token.getType(), is(equalTo(TokenType.STRING)));
		assertThat(string1Token.getText(), is(equalTo("string1")));
		final RexxToken string2Token = lexer.nextToken();
		System.out.println(string2Token);
		assertThat(string2Token.getType(), is(equalTo(TokenType.STRING)));
		assertThat(string2Token.getText(), is(equalTo("string2")));
		assertThat(lexer.nextToken(), is(nullValue()));
		assertThat(lexer.getRexxErrors().getErrors(), is(empty()));
	}

	@Test
	public void testUnclosedSQuoteString() throws IOException
	{
		final RexxLexer lexer = new RexxLexerBuilder() //
				.add("'string").build();
		final RexxToken stringToken = lexer.nextToken();
		System.out.println(stringToken);
		assertThat(stringToken.getType(), is(equalTo(TokenType.STRING_UNCLOSED)));
		assertThat(stringToken.getText(), is(equalTo("string")));
		assertThat(lexer.nextToken(), is(nullValue()));
		assertThat(lexer.getRexxErrors().getErrors().get(0), is(equalTo(RexxError.E_UNCLOSED_STRING)));
	}

	@Test
	public void testStringSQuoteDuplication() throws IOException
	{
		final RexxLexer lexer = new RexxLexerBuilder() //
				.add("'--''--'").build();
		final RexxToken stringToken = lexer.nextToken();
		System.out.println(stringToken);
		assertThat(stringToken.getType(), is(equalTo(TokenType.STRING)));
		assertThat(stringToken.getText(), is(equalTo("--'--")));
		assertThat(lexer.nextToken(), is(nullValue()));
		assertThat(lexer.getRexxErrors().getErrors(), is(empty()));
	}

	@Test
	public void testMixedQuoteStrings() throws IOException
	{
		final RexxLexer lexer = new RexxLexerBuilder() //
				.add(" '\"' \"'\" ").build();
		final RexxToken string1Token = lexer.nextToken();
		System.out.println(string1Token);
		assertThat(string1Token.getType(), is(equalTo(TokenType.STRING)));
		assertThat(string1Token.getText(), is(equalTo("\"")));
		final RexxToken string2Token = lexer.nextToken();
		System.out.println(string2Token);
		assertThat(string2Token.getType(), is(equalTo(TokenType.STRING)));
		assertThat(string2Token.getText(), is(equalTo("'")));
		assertThat(lexer.nextToken(), is(nullValue()));
		assertThat(lexer.getRexxErrors().getErrors(), is(empty()));
	}

}
