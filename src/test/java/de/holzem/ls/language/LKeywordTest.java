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
 * LKeywordTest
 */
class LKeywordTest
{
	@Test
	void testKeywordIgnoreCase() throws IOException
	{
		final LLexer lexer = new LLexerBuilder() //
				.addln("address")//
				.addln("ADDRESS")//
				.addln("aDDress")//
				.build();
		LToken stringToken;
		stringToken = lexer.nextRealToken();
		assertThat(stringToken.getType(), is(equalTo(LTokenType.KEYWORD)));
		assertThat(stringToken.getText(), is(equalTo("address")));
		stringToken = lexer.nextRealToken();
		assertThat(stringToken.getType(), is(equalTo(LTokenType.KEYWORD)));
		assertThat(stringToken.getText(), is(equalTo("ADDRESS")));
		assertThat(stringToken.getCharBegin(), is(equalTo(9L)));
		assertThat(stringToken.getCharEnd(), is(equalTo(16L)));
		assertThat(stringToken.getLine(), is(equalTo(1)));
		assertThat(stringToken.getColumn(), is(equalTo(0)));
		stringToken = lexer.nextRealToken();
		assertThat(stringToken.getType(), is(equalTo(LTokenType.KEYWORD)));
		assertThat(stringToken.getText(), is(equalTo("aDDress")));
		assertThat(lexer.nextRealToken(), is(nullValue()));
		assertThat(lexer.getErrors().hasErrors(), is(false));
	}
}
