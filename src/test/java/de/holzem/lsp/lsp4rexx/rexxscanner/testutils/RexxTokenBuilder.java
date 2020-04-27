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
package de.holzem.lsp.lsp4rexx.rexxscanner.testutils;

import java.util.ArrayList;
import java.util.List;

import de.holzem.lsp.lsp4rexx.rexxscanner.RexxToken;
import de.holzem.lsp.lsp4rexx.rexxscanner.TokenType;

public class RexxTokenBuilder
{
	private final List<RexxToken> _tokens = new ArrayList<>();
	private int _line = 0;
	private int _column = 0;
	private long _charBegin = 0;

	public RexxTokenBuilder createToken(final TokenType pTokenType, final String pText)
	{
		final RexxToken token = new RexxToken(pTokenType, pText, _line, _column, _charBegin);
		_charBegin = token.getCharEnd();
		_column += (token.getCharEnd() - token.getCharBegin());
		_tokens.add(token);
		return this;
	}

	public RexxTokenBuilder createWhiteSpace(final String pText)
	{
		final RexxToken token = new RexxToken(TokenType.WHITESPACE, pText, _line, _column, _charBegin);
		_charBegin = token.getCharEnd();
		_column += (token.getCharEnd() - token.getCharBegin());
		final long newlines = pText.chars().filter(num -> num == '\n').count();
		_line += newlines;
		_tokens.add(token);
		return this;
	}

	public RexxTokenBuilder createComment(final String pText)
	{
		final RexxToken token = new RexxToken(TokenType.REXX_COMMENT, pText, _line, _column, _charBegin);
		_charBegin = token.getCharEnd();
		_column += (token.getCharEnd() - token.getCharBegin());
		final long newlines = pText.chars().filter(num -> num == '\n').count();
		_line += newlines;
		_tokens.add(token);
		return this;
	}

	public List<RexxToken> build()
	{
		return _tokens;
	}
}