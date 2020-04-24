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
 * RexxIdentifierTest
 */
public class RexxIdentifierTest
{
	@Test
	public void testIdentifier() throws IOException
	{
		final RexxLexer lexer = new RexxLexerBuilder() //
				.addln("myVar")//
				.addln("_my_variable_5")//
				.addln("$global$")//
				.addln("#misc")//
				.addln("§atsign")//
				.build();
		RexxToken stringToken;
		stringToken = lexer.nextRealToken();
		assertThat(stringToken.getType(), is(equalTo(TokenType.IDENTIFIER)));
		assertThat(stringToken.getText(), is(equalTo("myVar")));
		stringToken = lexer.nextRealToken();
		assertThat(stringToken.getType(), is(equalTo(TokenType.IDENTIFIER)));
		assertThat(stringToken.getText(), is(equalTo("_my_variable_5")));
		stringToken = lexer.nextRealToken();
		assertThat(stringToken.getType(), is(equalTo(TokenType.IDENTIFIER)));
		assertThat(stringToken.getText(), is(equalTo("$global$")));
		stringToken = lexer.nextRealToken();
		assertThat(stringToken.getType(), is(equalTo(TokenType.IDENTIFIER)));
		assertThat(stringToken.getText(), is(equalTo("#misc")));
		stringToken = lexer.nextRealToken();
		assertThat(stringToken.getType(), is(equalTo(TokenType.IDENTIFIER)));
		assertThat(stringToken.getText(), is(equalTo("§atsign")));
		assertThat(lexer.nextRealToken(), is(nullValue()));
		assertThat(lexer.getRexxErrors().getErrors(), is(empty()));
	}

	@Test
	public void testStemVar() throws IOException
	{
		final RexxLexer lexer = new RexxLexerBuilder() //
				.addln("stem.1.firstname")//
				.addln("stem.1.lastname")//
				.addln("stem.0")//
				.addln("stem.")//
				.build();
		RexxToken stringToken;
		stringToken = lexer.nextRealToken();
		assertThat(stringToken.getType(), is(equalTo(TokenType.IDENTIFIER)));
		assertThat(stringToken.getText(), is(equalTo("stem.1.firstname")));
		stringToken = lexer.nextRealToken();
		assertThat(stringToken.getType(), is(equalTo(TokenType.IDENTIFIER)));
		assertThat(stringToken.getText(), is(equalTo("stem.1.lastname")));
		stringToken = lexer.nextRealToken();
		assertThat(stringToken.getType(), is(equalTo(TokenType.IDENTIFIER)));
		assertThat(stringToken.getText(), is(equalTo("stem.0")));
		stringToken = lexer.nextRealToken();
		assertThat(stringToken.getType(), is(equalTo(TokenType.IDENTIFIER)));
		assertThat(stringToken.getText(), is(equalTo("stem.")));
		assertThat(lexer.nextRealToken(), is(nullValue()));
		assertThat(lexer.getRexxErrors().getErrors(), is(empty()));
	}
}
