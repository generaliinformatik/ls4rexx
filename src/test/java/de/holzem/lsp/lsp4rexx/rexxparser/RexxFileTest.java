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
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.holzem.lsp.lsp4rexx.rexxscanner.RexxToken;
import de.holzem.lsp.lsp4rexx.rexxscanner.TokenType;

/**
 * RexxFileTest
 */
class RexxFileTest
{
	private int _line;
	private int _column;
	private long _charBegin;

	@BeforeEach
	void initCounters()
	{
		_line = 0;
		_column = 0;
		_charBegin = 0;
	}

	@Test
	void testUri()
	{
		final RexxFile file = new RexxFile("test.rex");
		assertThat(file.getUri(), is(equalTo("test.rex")));
	}

	@Test
	void testSingleComment()
	{
		final RexxFile file = new RexxFile("test.rex");
		file.add(createComment("/* REXX */"));
		assertThat(file.getText(), is(equalTo("/* REXX */")));
	}

	@Test
	void testSmallRexx()
	{
		final RexxFile file = new RexxFile("test.rex");
		file.add(createComment("/* REXX */"));
		file.add(createWhiteSpace("\r\n"));
		file.add(createToken(TokenType.FUNCTION, "exit"));
		file.add(createWhiteSpace(" "));
		file.add(createToken(TokenType.NUMBER, "0"));
		assertThat(file.getText(), is(equalTo("/* REXX */\r\nexit 0")));
	}

	private RexxToken createToken(final TokenType pTokenType, final String pText)
	{
		final RexxToken token = new RexxToken(pTokenType, pText, _line, _column, _charBegin);
		_charBegin = token.getCharEnd();
		_column += (token.getCharEnd() - token.getCharBegin());
		return token;
	}

	private RexxToken createWhiteSpace(final String pText)
	{
		final RexxToken token = new RexxToken(TokenType.WHITESPACE, pText, _line, _column, _charBegin);
		_charBegin = token.getCharEnd();
		_column += (token.getCharEnd() - token.getCharBegin());
		final long newlines = pText.chars().filter(num -> num == '\n').count();
		_line += newlines;
		return token;
	}

	private RexxToken createComment(final String pText)
	{
		final RexxToken token = new RexxToken(TokenType.REXX_COMMENT, pText, _line, _column, _charBegin);
		_charBegin = token.getCharEnd();
		_column += (token.getCharEnd() - token.getCharBegin());
		final long newlines = pText.chars().filter(num -> num == '\n').count();
		_line += newlines;
		return token;
	}
}
