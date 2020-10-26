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

import java.io.Reader;
import java.io.StringReader;

import de.generali.dev.ls.language.LLexer;

/**
 * LLexerBuilder creates a {@link LLexer} for a list of lines, that are added with the methods
 * {@link LLexerBuilder#add(String)} and {@link LLexerBuilder#addln(String)}. The only difference between those methods
 * is, that {@link LLexerBuilder#addln(String)} adds <code>\r\n</code>. to the line as a convenience.
 */
public class LLexerBuilder
{
	private final StringBuilder _stringBuilder = new StringBuilder();

	public LLexerBuilder add(final String pLine)
	{
		_stringBuilder.append(pLine);
		return this;
	}

	public LLexerBuilder addln(final String pLine)
	{
		_stringBuilder.append(pLine).append("\r\n");
		return this;
	}

	public LLexer build()
	{
		final Reader reader = new StringReader(_stringBuilder.toString());
		final LLexer lexer = new LLexer(reader);
		return lexer;
	}
}