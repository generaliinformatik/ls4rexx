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

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import de.holzem.lsp.lsp4rexx.rexxscanner.RexxLexer;
import de.holzem.lsp.lsp4rexx.rexxscanner.RexxToken;
import de.holzem.lsp.lsp4rexx.rexxscanner.TokenType;

/**
 * RexxParser parses a REXX file
 */
public enum RexxParser
{
	INSTANCE;

	public RexxFile parse(final String pUri, final String pText)
	{
		final RexxHandler handler = new RexxHandler();
		final Reader reader = new StringReader(pText);
		final RexxLexer lexer = new RexxLexer(reader);
		final List<RexxToken> tokens = new ArrayList<RexxToken>();
		try {
			for (RexxToken rexxToken = lexer.nextToken(); rexxToken != null; rexxToken = lexer.nextToken()) {
				tokens.add(rexxToken);
				final TokenType tokenType = rexxToken.getType();
				switch (tokenType) {
				case FUNCTION:
					handler.handleFunction(rexxToken);
					break;
				case IDENTIFIER:
					handler.handleIdentifier(rexxToken);
					break;
				case COLON:
					handler.handleColon(rexxToken);
					break;
				case LEFT_PARENTHESIS:
					handler.handleLeftParenthesis(rexxToken);
					break;
				default:
					break;
				}
			}
		} catch (final IOException exc) {
			// should never happen on a string
		}
		final RexxFile rexxFile = new RexxFile.RexxFileBuilder().uri(pUri).tokens(tokens)
				.variables(handler.getVariables()).labels(handler.getLabels()).build();
		return rexxFile;
	}
}
