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
package de.holzem.lsp.lsp4rexx.rexxscanner.strings;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import de.holzem.lsp.lsp4rexx.rexxscanner.RexxLexer;
import de.holzem.lsp.lsp4rexx.rexxscanner.RexxToken;
import de.holzem.lsp.lsp4rexx.rexxscanner.TokenType;
import de.holzem.lsp.lsp4rexx.rexxscanner.testutils.RexxLexerBuilder;

/**
 * RexxKeywordIfTest
 */
public class RexxKeywordIfTest
{
	@Test
	public void testIfStatement() throws IOException
	{
		final RexxLexer lexer = new RexxLexerBuilder() //
				.addln("if a=15 then")//
				.addln("  say 'a=15'")//
				.addln("else")//
				.addln("  say 'a^=15'")//
				.build();
		RexxToken stringToken;
		stringToken = lexer.nextRealToken();
		assertThat(stringToken.getType(), is(equalTo(TokenType.KEYWORD)));
		assertThat(stringToken.getText(), is(equalTo("if")));
		stringToken = lexer.nextRealToken();
		assertThat(stringToken.getType(), is(equalTo(TokenType.IDENTIFIER)));
		assertThat(stringToken.getText(), is(equalTo("a")));
		stringToken = lexer.nextRealToken();
		assertThat(stringToken.getType(), is(equalTo(TokenType.EQ)));
		assertThat(stringToken.getText(), is(equalTo("=")));
		stringToken = lexer.nextRealToken();
		assertThat(stringToken.getType(), is(equalTo(TokenType.NUMBER)));
		assertThat(stringToken.getText(), is(equalTo("15")));
		stringToken = lexer.nextRealToken();
		assertThat(stringToken.getType(), is(equalTo(TokenType.KEYWORD)));
		assertThat(stringToken.getText(), is(equalTo("then")));
		stringToken = lexer.nextRealToken();
		assertThat(stringToken.getType(), is(equalTo(TokenType.KEYWORD)));
		assertThat(stringToken.getText(), is(equalTo("say")));
		stringToken = lexer.nextRealToken();
		assertThat(stringToken.getType(), is(equalTo(TokenType.SQUOTE_STRING)));
		assertThat(stringToken.getText(), is(equalTo("'a=15'")));
		stringToken = lexer.nextRealToken();
		assertThat(stringToken.getType(), is(equalTo(TokenType.KEYWORD)));
		assertThat(stringToken.getText(), is(equalTo("else")));
		stringToken = lexer.nextRealToken();
		assertThat(stringToken.getType(), is(equalTo(TokenType.KEYWORD)));
		assertThat(stringToken.getText(), is(equalTo("say")));
		stringToken = lexer.nextRealToken();
		assertThat(stringToken.getType(), is(equalTo(TokenType.SQUOTE_STRING)));
		assertThat(stringToken.getText(), is(equalTo("'a^=15'")));
		assertThat(lexer.nextRealToken(), is(nullValue()));
		assertThat(lexer.getRexxErrors().getErrors(), is(empty()));
	}
}
