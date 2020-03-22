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

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * RexxSourceFileInfo is used to parse a REXX source file that is passed as a string.
 */
public class RexxSourceFileInfo
{
	private List<String> _variables = new ArrayList<String>();

	/**
	 * Constructor
	 */
	public RexxSourceFileInfo() {
	}

	public void parse(final String pSourceText)
	{
		final Reader reader = new StringReader(pSourceText);
		final RexxLexer lexer = new RexxLexer(reader);
		final Set<String> variables = new HashSet<String>();
		try {
			for (RexxToken rexxToken = lexer.nextToken(); rexxToken != null; rexxToken = lexer.nextToken()) {
				if (rexxToken.getType() == TokenType.IDENTIFIER) {
					variables.add(rexxToken.getText());
				}
			}
		} catch (final Exception exc) {
			// should never happen on a string
		}
		_variables = variables.stream().collect(Collectors.toList());
		Collections.sort(_variables);
	}

	public List<String> getVariables()
	{
		return Collections.unmodifiableList(_variables);
	}
}
