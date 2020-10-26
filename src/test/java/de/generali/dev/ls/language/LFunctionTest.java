/**
 *  Copyright (c) 2020 Generali Deutschland AG - Team Informatik
 *
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v2.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *  Contributors:
 *  Markus Holzem <markus.holzem@generali.com>
 */
package de.generali.dev.ls.language;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import de.generali.dev.ls.language.testutils.LLexerBuilder;

/**
 * LKeywordTest
 */
class LFunctionTest
{
	@Test
	void testFunction() throws IOException
	{
		final LLexer lexer = new LLexerBuilder() //
				.addln("subword")//
				.addln("identifier")//
				.build();
		LToken stringToken;
		stringToken = lexer.nextRealToken();
		assertThat(stringToken.getType(), is(equalTo(LTokenType.FUNCTION)));
		assertThat(stringToken.getText(), is(equalTo("subword")));
		stringToken = lexer.nextRealToken();
		assertThat(stringToken.getType(), is(equalTo(LTokenType.IDENTIFIER)));
		assertThat(stringToken.getText(), is(equalTo("identifier")));
		assertThat(lexer.nextRealToken(), is(nullValue()));
		assertThat(lexer.getErrors().hasErrors(), is(false));
	}
}
