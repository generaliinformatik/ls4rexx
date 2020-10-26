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
package de.generali.dev.ls.language.testutils;

import java.util.ArrayList;
import java.util.List;

import de.generali.dev.ls.language.LToken;
import de.generali.dev.ls.language.LTokenType;

public class LTokenBuilder
{
	private final List<LToken> _tokens = new ArrayList<>();
	private int _line = 0;
	private int _column = 0;
	private long _charBegin = 0;

	public LTokenBuilder createToken(final LTokenType pLTokenType, final String pText)
	{
		final LToken token = new LToken(pLTokenType, pText, _line, _column, _charBegin);
		_charBegin = token.getCharEnd();
		_column += (token.getCharEnd() - token.getCharBegin());
		_tokens.add(token);
		return this;
	}

	public LTokenBuilder createWhiteSpace(final String pText)
	{
		final LToken token = new LToken(LTokenType.WHITESPACE, pText, _line, _column, _charBegin);
		_charBegin = token.getCharEnd();
		_column += (token.getCharEnd() - token.getCharBegin());
		final long newlines = pText.chars().filter(num -> num == '\n').count();
		_line += newlines;
		_tokens.add(token);
		return this;
	}

	public LTokenBuilder createComment(final String pText)
	{
		final LToken token = new LToken(LTokenType.COMMENT, pText, _line, _column, _charBegin);
		_charBegin = token.getCharEnd();
		_column += (token.getCharEnd() - token.getCharBegin());
		final long newlines = pText.chars().filter(num -> num == '\n').count();
		_line += newlines;
		_tokens.add(token);
		return this;
	}

	public List<LToken> build()
	{
		return _tokens;
	}
}