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

/**
 * RexxToken represents a token returned from the scanner
 */
public class RexxToken
{
	private final TokenType _type;
	private final String _text;
	private final int _line;
	private final int _column;
	private final long _charBegin;
	private final long _charEnd;

	public RexxToken(final TokenType pType, final String pText, final int pLine, final int pColumn,
			final long pCharBegin) {
		this(pType, pText, pLine, pColumn, pCharBegin, pCharBegin + pText.length());
	}

	public RexxToken(final TokenType pType, final String pText, final int pLine, final int pColumn,
			final long pCharBegin, final long pCharEnd) {
		_type = pType;
		_text = pText;
		_line = pLine;
		_column = pColumn;
		_charBegin = pCharBegin;
		_charEnd = pCharEnd;
	}

	public TokenType getType()
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
		sb.append("RexxToken(");
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
