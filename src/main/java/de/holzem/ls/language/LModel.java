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

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.lsp4j.jsonrpc.CancelChecker;

import lombok.Builder;
import lombok.Value;

/**
 * LModel accumulates all information gained from the LParser.
 */
@Value
@Builder
public final class LModel
{
	private static final LToken NULL_TOKEN = new LToken(LTokenType.COMMENT, "", 0, 0, 0);
	private final String uri;
	private final List<LToken> tokens;
	private final List<LToken> variables;
	private final List<LToken> labels;
	private final CancelChecker cancelChecker;
	private final LErrors errors;

	public String getText()
	{
		final String fileText = tokens.stream().map(token -> token.getText()).collect(Collectors.joining());
		return fileText;
	}

	public String getFileName()
	{
		try {
			// remove "/file:///" from uri
			final String filePath = uri.startsWith("file:///") ? uri.substring(8) : uri;
			final Path path = Paths.get(filePath);
			return path.getFileName().toString();
		} catch (final InvalidPathException exc) {
			return "not parsed";
		}
	}

	public LToken getToken(final int pIndex)
	{
		if (pIndex < 0 || pIndex > tokens.size() - 1) {
			return NULL_TOKEN;
		}
		return tokens.get(pIndex);
	}

	public int locateToken(final int pLine, final int pColumn)
	{
		final LToken compareToken = new LToken(LTokenType.SYNTHETIC, "", pLine, pColumn, 0);
		int position = Collections.binarySearch(tokens, compareToken, new LTokenPositionComparator());
		if (position < 0) {
			position = tokens.size() - 1;
		}
		return position;
	}

	public int locatePrevToken(final int pLine, final int pColumn)
	{
		int position = locateToken(pLine, pColumn);
		// if found token is a comment do not look any further
		final LTokenType tokenType = getToken(position).getType();
		if (tokenType == LTokenType.COMMENT || tokenType == LTokenType.COMMENT_UNCLOSED
				|| tokenType == LTokenType.DQUOTE_STRING || tokenType == LTokenType.DQUOTE_STRING_UNCLOSED
				|| tokenType == LTokenType.SQUOTE_STRING || tokenType == LTokenType.SQUOTE_STRING_UNCLOSED) {
			return position;
		}
		// if the found token is the last token and not whitespace take this token
		if (position == tokens.size() - 1 && tokenType != LTokenType.WHITESPACE) {
			return position;
		}
		// take previous token
		return (position > 0 ? --position : position);
	}

	private static class LTokenPositionComparator implements Comparator<LToken>
	{
		@Override
		public int compare(final LToken pToken1, final LToken pToken2)
		{
			if (pToken1.getLine() == pToken2.getLine()) {
				if (pToken1.getType() == LTokenType.SYNTHETIC) {
					// pToken1 is compare token
					final int cmp = pToken1.getColumn();
					final int start = pToken2.getColumn();
					final int end = start + pToken2.getText().length();
					if (start <= cmp && cmp < end) {
						return 0;
					}
				} else {
					// pToken2 is compare token
					final int cmp = pToken2.getColumn();
					final int start = pToken1.getColumn();
					final int end = start + pToken1.getText().length();
					if (start <= cmp && cmp < end) {
						return 0;
					}
				}
				return pToken1.getColumn() - pToken2.getColumn();
			}
			return pToken1.getLine() - pToken2.getLine();
		}
	}
}
