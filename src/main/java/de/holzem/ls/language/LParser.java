/**
*  Copyright (c) 2020 Markus Holzem
*
*  All rights reserved. This program and the accompanying materials
*  are made available under the terms of the Eclipse Public License v2.0
*  which accompanies this distribution, and is available at
*  http://www.eclipse.org/legal/epl-v20.html
*
* SPDX-License-Identifier: EPL-2.0
*
*  Contributors:
*  Markus Holzem <markus@holzem.de>
*/
package de.holzem.ls.language;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CancellationException;

import org.eclipse.lsp4j.TextDocumentItem;
import org.eclipse.lsp4j.jsonrpc.CancelChecker;

import lombok.extern.slf4j.Slf4j;

/**
 * LParser parses a REXX file
 */
@Slf4j
public enum LParser
{
	INSTANCE;

	public LModel parse(final String pUri, final String pText)
	{
		return parse(pUri, pText, null);
	}

	public LModel parse(final TextDocumentItem pDocumentItem, final CancelChecker pCancelChecker)
			throws CancellationException
	{
		final String uri = pDocumentItem.getUri();
		final String text = pDocumentItem.getText();
		return parse(uri, text, pCancelChecker);
	}

	public LModel parse(final String pUri, final String pText, final CancelChecker pCancelChecker)
			throws CancellationException
	{
		log.debug("parse {}", pUri);
		final LParserHandler handler = new LParserHandler();
		final Reader reader = new StringReader(pText);
		final LLexer lexer = new LLexer(reader);
		final List<LToken> tokens = new ArrayList<LToken>();
		// correct interpretation usually needs three tokens
		// Initialize triple
		LToken prev = null;
		LToken current = getNextNonSkipToken(lexer, tokens);
		LToken next = getNextNonSkipToken(lexer, tokens);
		//
		while (current != null) {
			final LTokenType lTokenType = current.getType();
			switch (lTokenType) {
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
		final LModel lModel = new LModel.LModelBuilder().uri(pUri).tokens(tokens).variables(handler.getVariables())
				.labels(handler.getLabels()).cancelChecker(pCancelChecker).build();
		log.debug("parsing done {}: {} variables, {} labels", pUri, handler.getVariables().size(),
				handler.getLabels().size());
		return lModel;
	}

	private LToken getNextNonSkipToken(final LLexer pLexer, final List<LToken> pCompleteTokenList)
	{
		LToken token = null;
		boolean tokenFound = false;
		while ((!tokenFound) && ((token = getNextToken(pLexer)) != null)) {
			final LTokenType lTokenType = token.getType();
			switch (lTokenType) {
			case WHITESPACE:
			case COMMENT:
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

	private LToken getNextToken(final LLexer pLexer)
	{
		LToken token = null;
		try {
			token = pLexer.nextToken();
		} catch (final IOException exc) {
			log.error("error while reading next token", exc);
		}
		return token;
	}
}
