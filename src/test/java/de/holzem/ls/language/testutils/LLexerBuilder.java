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
package de.holzem.ls.language.testutils;

import java.io.Reader;
import java.io.StringReader;

import de.holzem.ls.language.LLexer;

/**
 * LLexerBuilder creates a {@link LLexer} for a list of lines, that are added with the methods
 * {@link LLexerBuilder#add(String)} and {@link LLexerBuilder#addln(String)}. The only difference between those
 * methods is, that {@link LLexerBuilder#addln(String)} adds <code>\r\n</code>. to the line as a convenience.
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