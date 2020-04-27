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
package de.holzem.lsp.lsp4rexx.rexxparser;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import java.util.List;

import org.junit.jupiter.api.Test;

import de.holzem.lsp.lsp4rexx.rexxparser.RexxFile;
import de.holzem.lsp.lsp4rexx.rexxparser.RexxFile.RexxFileBuilder;
import de.holzem.lsp.lsp4rexx.rexxscanner.RexxToken;
import de.holzem.lsp.lsp4rexx.rexxscanner.TokenType;
import de.holzem.lsp.lsp4rexx.rexxscanner.testutils.RexxTokenBuilder;
import nl.jqno.equalsverifier.EqualsVerifier;

/**
 * RexxFileTest
 */
class RexxFileTest
{
	@Test
	void testBuilder()
	{
		final RexxFile.RexxFileBuilder rexxFileBuilder = RexxFile.builder()//
				.uri("test.rex")//
				.labels(null) //
				.variables(null);
		assertThat(rexxFileBuilder.toString(), containsString("test.rex"));
	}

	@Test
	void testUri()
	{
		final RexxFile file = RexxFile.builder()//
				.uri("test.rex")//
				.labels(null) //
				.variables(null) //
				.build();
		assertThat(file.getUri(), is(equalTo("test.rex")));
		assertThat(file.getLabels(), is(nullValue()));
		assertThat(file.getVariables(), is(nullValue()));
		assertThat(file.getTokens(), is(nullValue()));
		assertThat(file.toString(), containsString("test.rex"));
	}

	@Test
	void testSingleComment()
	{
		final List<RexxToken> tokens = new RexxTokenBuilder() //
				.createComment("/* REXX */") //
				.build();
		final RexxFile file = new RexxFile.RexxFileBuilder() //
				.uri("test.rex") //
				.tokens(tokens) //
				.build();
		assertThat(file.getText(), is(equalTo("/* REXX */")));
	}

	@Test
	void testSmallRexx()
	{
		final List<RexxToken> tokens = new RexxTokenBuilder() //
				.createComment("/* REXX */") //
				.createWhiteSpace("\r\n") //
				.createToken(TokenType.FUNCTION, "exit") //
				.createWhiteSpace(" ") //
				.createToken(TokenType.NUMBER, "0") //
				.build();
		final RexxFile file = new RexxFile.RexxFileBuilder() //
				.uri("test.rex") //
				.tokens(tokens) //
				.build();
		assertThat(file.getText(), is(equalTo("/* REXX */\r\nexit 0")));
	}

	@Test
	void testEquals()
	{
		EqualsVerifier.forClass(RexxFile.class).verify();
	}
}
