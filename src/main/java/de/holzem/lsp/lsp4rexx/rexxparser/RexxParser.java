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
import java.util.concurrent.CancellationException;

import org.eclipse.lsp4j.TextDocumentItem;
import org.eclipse.lsp4j.jsonrpc.CancelChecker;

import de.holzem.lsp.lsp4rexx.rexxscanner.RexxLexer;
import de.holzem.lsp.lsp4rexx.rexxscanner.RexxToken;
import de.holzem.lsp.lsp4rexx.rexxscanner.TokenType;
import lombok.extern.slf4j.Slf4j;

/**
 * RexxParser parses a REXX file
 */
@Slf4j
public enum RexxParser
{
	INSTANCE;

	RexxFile parse(final String pUri, final String pText)
	{
		return parse(pUri, pText, null);
	}

	public RexxFile parse(final TextDocumentItem pDocumentItem, final CancelChecker pCancelChecker)
			throws CancellationException
	{
		final String uri = pDocumentItem.getUri();
		final String text = pDocumentItem.getText();
		return parse(uri, text, pCancelChecker);
	}

	public RexxFile parse(final String pUri, final String pText, final CancelChecker pCancelChecker)
			throws CancellationException
	{
		log.info("parse {}", pUri);
		final RexxHandler handler = new RexxHandler();
		final Reader reader = new StringReader(pText);
		final RexxLexer lexer = new RexxLexer(reader);
		final List<RexxToken> tokens = new ArrayList<RexxToken>();
		// correct interpretation usually needs three tokens
		// Initialize triple
		RexxToken prev = null;
		RexxToken current = getNextNonSkipToken(lexer, tokens);
		RexxToken next = getNextNonSkipToken(lexer, tokens);
		//
		while (current != null) {
			final TokenType tokenType = current.getType();
			switch (tokenType) {
			case KEYWORD:
				handler.handleKeyword(prev, current, next);
				break;
			case IDENTIFIER:
				handler.handleIdentifier(prev, current, next);
				break;
			case COLON:
				handler.handleColon(prev, current, next);
				break;
			default:
				break;
			}
			// Next Token
			prev = current;
			current = next;
			next = getNextNonSkipToken(lexer, tokens);
			// Check whether parsing is canceled
			if (pCancelChecker != null) {
				pCancelChecker.checkCanceled();
			}
		}
		final RexxFile rexxFile = new RexxFile.RexxFileBuilder().uri(pUri).tokens(tokens)
				.variables(handler.getVariables()).labels(handler.getLabels()).cancelChecker(pCancelChecker).build();
		log.info("parsing done {}: {} variables, {} labels", pUri, handler.getVariables().size(),
				handler.getLabels().size());
		return rexxFile;
	}

	private RexxToken getNextNonSkipToken(final RexxLexer pLexer, final List<RexxToken> pCompleteTokenList)
	{
		RexxToken token = null;
		boolean tokenFound = false;
		while ((!tokenFound) && ((token = getNextToken(pLexer)) != null)) {
			final TokenType tokenType = token.getType();
			switch (tokenType) {
			case WHITESPACE:
			case REXX_COMMENT:
				pCompleteTokenList.add(token);
				break;
			default:
				pCompleteTokenList.add(token);
				tokenFound = true;
				break;
			}
		}
		return token;
	}

	private RexxToken getNextToken(final RexxLexer pLexer)
	{
		RexxToken token = null;
		try {
			token = pLexer.nextToken();
		} catch (final IOException exc) {
			log.error("error while reading next token", exc);
		}
		return token;
	}
}
