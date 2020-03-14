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
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import de.holzem.lsp.lsp4rexx.rexxscanner.testutils.RexxLexerBuilder;

/**
 * RexxCommentTest
 */
public class RexxCommentTest {

	@Test
	public void testCommentSingleLine() throws IOException
	{
		final RexxLexer lexer = new RexxLexerBuilder() //
				.addln("/* comment */").build();
		assertThat(lexer.nextToken(), is(nullValue()));
	}

	@Test
	public void testNestedCommentSingleLine() throws IOException
	{
		final RexxLexer lexer = new RexxLexerBuilder() //
				.addln("/* outer comment /* inner comment */ outer comment*/").build();
		assertThat(lexer.nextToken(), is(nullValue()));
	}

	@Test
	public void testCommentMultiLine() throws IOException
	{
		final RexxLexer lexer = new RexxLexerBuilder() //
				.addln("/* First Line") //
				.addln("   Second Line") //
				.addln("   Third Line */").build();
		assertThat(lexer.nextToken(), is(nullValue()));
	}

	@Test
	public void testNestedCommentMultiLine() throws IOException
	{
		final RexxLexer lexer = new RexxLexerBuilder() //
				.addln("/* First Line") //
				.addln("   /* Second Line */") //
				.addln("   Third Line */").build();
		assertThat(lexer.nextToken(), is(nullValue()));
	}

}
