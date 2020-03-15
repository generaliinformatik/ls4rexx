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
 * RexxKeywordTest
 */
public class RexxFunctionTest {
	@Test
	public void testFunction() throws IOException
	{
		final RexxLexer lexer = new RexxLexerBuilder() //
				.addln("subword")//
				.addln("identifier")//
				.build();
		RexxToken stringToken;
		stringToken = lexer.nextToken();
		assertThat(stringToken.getType(), is(equalTo(TokenType.FUNCTION)));
		assertThat(stringToken.getText(), is(equalTo("subword")));
		stringToken = lexer.nextToken();
		assertThat(stringToken.getType(), is(equalTo(TokenType.IDENTIFIER)));
		assertThat(stringToken.getText(), is(equalTo("identifier")));
		assertThat(lexer.nextToken(), is(nullValue()));
		assertThat(lexer.getRexxErrors().getErrors(), is(empty()));
	}

}
