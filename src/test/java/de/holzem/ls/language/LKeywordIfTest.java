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
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import de.holzem.ls.language.testutils.LLexerBuilder;

/**
 * LKeywordIfTest
 */
class LKeywordIfTest
{
	@Test
	void testIfStatement() throws IOException
	{
		final LLexer lexer = new LLexerBuilder() //
				.addln("if a=15 then")//
				.addln("  say 'a=15'")//
				.addln("else")//
				.addln("  say 'a^=15'")//
				.build();
		LToken stringToken;
		stringToken = lexer.nextRealToken();
		assertThat(stringToken.getType(), is(equalTo(LTokenType.KEYWORD)));
		assertThat(stringToken.getText(), is(equalTo("if")));
		stringToken = lexer.nextRealToken();
		assertThat(stringToken.getType(), is(equalTo(LTokenType.IDENTIFIER)));
		assertThat(stringToken.getText(), is(equalTo("a")));
		stringToken = lexer.nextRealToken();
		assertThat(stringToken.getType(), is(equalTo(LTokenType.EQ)));
		assertThat(stringToken.getText(), is(equalTo("=")));
		stringToken = lexer.nextRealToken();
		assertThat(stringToken.getType(), is(equalTo(LTokenType.NUMBER)));
		assertThat(stringToken.getText(), is(equalTo("15")));
		stringToken = lexer.nextRealToken();
		assertThat(stringToken.getType(), is(equalTo(LTokenType.KEYWORD)));
		assertThat(stringToken.getText(), is(equalTo("then")));
		stringToken = lexer.nextRealToken();
		assertThat(stringToken.getType(), is(equalTo(LTokenType.KEYWORD)));
		assertThat(stringToken.getText(), is(equalTo("say")));
		stringToken = lexer.nextRealToken();
		assertThat(stringToken.getType(), is(equalTo(LTokenType.SQUOTE_STRING)));
		assertThat(stringToken.getText(), is(equalTo("'a=15'")));
		stringToken = lexer.nextRealToken();
		assertThat(stringToken.getType(), is(equalTo(LTokenType.KEYWORD)));
		assertThat(stringToken.getText(), is(equalTo("else")));
		stringToken = lexer.nextRealToken();
		assertThat(stringToken.getType(), is(equalTo(LTokenType.KEYWORD)));
		assertThat(stringToken.getText(), is(equalTo("say")));
		stringToken = lexer.nextRealToken();
		assertThat(stringToken.getType(), is(equalTo(LTokenType.SQUOTE_STRING)));
		assertThat(stringToken.getText(), is(equalTo("'a^=15'")));
		assertThat(lexer.nextRealToken(), is(nullValue()));
		assertThat(lexer.getErrors().hasErrors(), is(false));
	}
}
