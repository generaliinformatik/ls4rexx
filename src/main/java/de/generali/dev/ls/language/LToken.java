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

/**
 * LToken represents a token returned from the scanner
 */
public class LToken
{
	private final LTokenType _type;
	private final String _text;
	private final int _line;
	private final int _column;
	private final long _charBegin;
	private final long _charEnd;

	public LToken(final LTokenType pType, final String pText, final int pLine, final int pColumn,
			final long pCharBegin) {
		this(pType, pText, pLine, pColumn, pCharBegin, pCharBegin + pText.length());
	}

	public LToken(final LTokenType pType, final String pText, final int pLine, final int pColumn, final long pCharBegin,
			final long pCharEnd) {
		_type = pType;
		_text = pText;
		_line = pLine;
		_column = pColumn;
		_charBegin = pCharBegin;
		_charEnd = pCharEnd;
	}

	public LTokenType getType()
	{
		return _type;
	}

	public String getText()
	{
		return _text;
	}

	public int getLine()
	{
		return _line;
	}

	public int getColumn()
	{
		return _column;
	}

	public int getLength()
	{
		return _text.length();
	}

	public long getCharBegin()
	{
		return _charBegin;
	}

	public long getCharEnd()
	{
		return _charEnd;
	}

	@Override
	public String toString()
	{
		final StringBuilder sb = new StringBuilder();
		sb.append("LToken(");
		sb.append(_type.toString());
		sb.append(":(");
		sb.append(_line);
		sb.append(":");
		sb.append(_column);
		sb.append("):(");
		sb.append(_charBegin);
		sb.append(":");
		sb.append(_charEnd);
		sb.append("):\"");
		sb.append(_text.replaceAll("\n", "\\\\n").replaceAll("\r", "\\\\r").replaceAll("\t", "\\\\t"));
		sb.append("\")");
		return sb.toString();
	}
}
