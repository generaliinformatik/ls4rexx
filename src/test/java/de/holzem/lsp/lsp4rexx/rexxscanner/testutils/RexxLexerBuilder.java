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

import java.io.Reader;
import java.io.StringReader;

import de.holzem.lsp.lsp4rexx.rexxscanner.RexxLexer;

/**
 * RexxLexerBuilder creates a {@link RexxLexer} for a list of lines, that are added with the methods
 * {@link RexxLexerBuilder#add(String)} and {@link RexxLexerBuilder#addln(String)}. The only difference between those
 * methods is, that {@link RexxLexerBuilder#addln(String)} adds <code>\r\n</code>. to the line as a convenience.
 */
public class RexxLexerBuilder
{
	private final StringBuilder _stringBuilder = new StringBuilder();

	public RexxLexerBuilder add(final String pLine)
	{
		_stringBuilder.append(pLine);
		return this;
	}

	public RexxLexerBuilder addln(final String pLine)
	{
		_stringBuilder.append(pLine).append("\r\n");
		return this;
	}

	public RexxLexer build()
	{
		final Reader reader = new StringReader(_stringBuilder.toString());
		final RexxLexer lexer = new RexxLexer(reader);
		return lexer;
	}
}