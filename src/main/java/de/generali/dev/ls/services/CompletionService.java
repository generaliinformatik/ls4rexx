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
package de.generali.dev.ls.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.lsp4j.CompletionItem;
import org.eclipse.lsp4j.CompletionItemKind;
import org.eclipse.lsp4j.CompletionList;
import org.eclipse.lsp4j.CompletionOptions;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.Range;
import org.eclipse.lsp4j.ServerCapabilities;
import org.eclipse.lsp4j.TextEdit;
import org.eclipse.lsp4j.jsonrpc.CancelChecker;

import de.generali.dev.ls.language.LModel;
import de.generali.dev.ls.language.LToken;
import de.generali.dev.ls.language.LTokenType;
import lombok.extern.slf4j.Slf4j;

/**
 * CompletionService
 */
@Slf4j
public class CompletionService extends LService
{
	public CompletionService(final LServices pLServices, final ServerCapabilities pServerCapabilities) {
		super(pLServices);
		// Set the completion options
		final CompletionOptions completionOptions = new CompletionOptions();
		completionOptions.setTriggerCharacters(Arrays.asList(COMPLETION_TRIGGER_CHARACTERS));
		pServerCapabilities.setCompletionProvider(completionOptions);
	}

	public CompletionList doComplete(final CancelChecker pCancelChecker, final LModel pLModel,
			final Position pCursorPosition)
	{
		final int line = pCursorPosition.getLine();
		final int column = pCursorPosition.getCharacter();
		log.debug("complete for {} at ({},{})", pLModel.getUri(), line, column);
		final int tokenPosition = pLModel.locateCompletionToken(line, column);
		log.debug("match ({},{}) with token {}", line, column, pLModel.getToken(tokenPosition));
		final List<CompletionItem> completionItems = new ArrayList<>();
		final LTokenType tokenType = pLModel.getToken(tokenPosition).getType();
		final boolean isComment = (tokenType == LTokenType.COMMENT || tokenType == LTokenType.COMMENT_UNCLOSED);
		if (!isComment) {
			addVariablesToCompletionItems(pCancelChecker, pLModel, tokenPosition, pCursorPosition, completionItems);
			addLabelsToCompletionItems(pCancelChecker, pLModel, tokenPosition, pCursorPosition, completionItems);
			addKeywordsToCompletionItems(pCancelChecker, pLModel, tokenPosition, pCursorPosition, completionItems);
			addFunctionsToCompletionItems(pCancelChecker, pLModel, tokenPosition, pCursorPosition, completionItems);
		}
		final CompletionList completionList = new CompletionList();
		completionList.setIsIncomplete(true);
		completionList.setItems(completionItems);
		return completionList;
	}

	private void addVariablesToCompletionItems(final CancelChecker pCancelChecker, final LModel pLModel,
			final int pTokenPosition, final Position pCursorPosition, final List<CompletionItem> pCompletionItems)
	{
		final LToken token = pLModel.getToken(pTokenPosition);
		final String tokenText = token.getText();
		final String tokenTextLowerCase = tokenText.toLowerCase();
		final boolean isLikeWhitespace = (token.getType() == LTokenType.LEFT_PARENTHESIS
				|| token.getType() == LTokenType.WHITESPACE);
		final Set<String> collected = new HashSet<String>();
		for (final LToken variable : pLModel.getVariables()) {
			if (pCancelChecker != null)
				pCancelChecker.checkCanceled();
			final String variableText = variable.getText();
			final String variableTextLowerCase = variableText.toLowerCase();
			// take only variables with a similar text
			if (isLikeWhitespace || variableTextLowerCase.startsWith(tokenTextLowerCase)) {
				if (!collected.contains(variableText)) {
					addVariableWithStemParts(pCompletionItems, token, pCursorPosition, variableText, tokenText.length(),
							collected);
				}
			}
		}
		collected.clear();
	}

	private void addVariableWithStemParts(final List<CompletionItem> pCompletionItems, final LToken pToken,
			final Position pCursorPosition, final String variableText, final int pMinLength,
			final Set<String> pCollected)
	{
		int substringEnd = -1;
		do {
			substringEnd = variableText.indexOf('.', substringEnd + 1);
			final String part = ((substringEnd < 0) ? variableText : variableText.substring(0, substringEnd + 1));
			if (!pCollected.contains(part) && part.length() > pMinLength) {
				final CompletionItem completionItem = new CompletionItem(part);
				completionItem.setKind(CompletionItemKind.Variable);
				completionItem.setInsertText(part);
				completionItem.setTextEdit(getTextEditForToken(pToken, pCursorPosition, part));
				completionItem.setDetail("Variable " + part);
				pCompletionItems.add(completionItem);
				pCollected.add(part);
			}
		} while (substringEnd >= 0);
	}

	private void addLabelsToCompletionItems(final CancelChecker pCancelChecker, final LModel pLModel,
			final int pTokenPosition, final Position pCursorPosition, final List<CompletionItem> pCompletionItems)
	{
		final LToken token = pLModel.getToken(pTokenPosition);
		final String tokenText = token.getText();
		final String tokenTextLowerCase = tokenText.toLowerCase();
		final boolean isLikeWhitespace = (token.getType() == LTokenType.LEFT_PARENTHESIS
				|| token.getType() == LTokenType.WHITESPACE);
		for (final LToken label : pLModel.getLabels()) {
			if (pCancelChecker != null)
				pCancelChecker.checkCanceled();
			final String labelText = label.getText();
			final String labelTextLowerCase = labelText.toLowerCase();
			if (isLikeWhitespace || labelTextLowerCase.startsWith(tokenTextLowerCase)) {
				final CompletionItem completionItem = new CompletionItem(labelText);
				completionItem.setKind(CompletionItemKind.Method);
				completionItem.setTextEdit(getTextEditForToken(token, pCursorPosition, labelText));
				completionItem.setDetail("Label " + label.getText());
				pCompletionItems.add(completionItem);
			}
		}
	}

	private void addFunctionsToCompletionItems(final CancelChecker pCancelChecker, final LModel pLModel,
			final int pTokenPosition, final Position pCursorPosition, final List<CompletionItem> pCompletionItems)
	{
		final LToken token = pLModel.getToken(pTokenPosition);
		final String tokenText = token.getText();
		final String tokenTextLowerCase = tokenText.toLowerCase();
		for (final String function : FUNCTIONS) {
			if (pCancelChecker != null)
				pCancelChecker.checkCanceled();
			final String functionLowerCase = function.toLowerCase();
			if (functionLowerCase.startsWith(tokenTextLowerCase)) {
				final CompletionItem completionItem = new CompletionItem(function);
				completionItem.setKind(CompletionItemKind.Function);
				completionItem.setTextEdit(getTextEditForToken(token, pCursorPosition, function));
				completionItem.setDetail("Function " + function);
				pCompletionItems.add(completionItem);
			}
		}
	}

	private void addKeywordsToCompletionItems(final CancelChecker pCancelChecker, final LModel pLModel,
			final int pTokenPosition, final Position pCursorPosition, final List<CompletionItem> pCompletionItems)
	{
		final LToken token = pLModel.getToken(pTokenPosition);
		final String tokenText = token.getText();
		final String tokenTextLowerCase = tokenText.toLowerCase();
		for (final String keyword : KEYWORDS) {
			if (pCancelChecker != null)
				pCancelChecker.checkCanceled();
			final String keyWordLowerCase = keyword.toLowerCase();
			if (keyWordLowerCase.startsWith(tokenTextLowerCase)) {
				final CompletionItem completionItem = new CompletionItem(keyword);
				completionItem.setKind(CompletionItemKind.Keyword);
				completionItem.setInsertText(keyword);
				completionItem.setTextEdit(getTextEditForToken(token, pCursorPosition, keyword));
				completionItem.setDetail("Keyword " + keyword);
				pCompletionItems.add(completionItem);
			}
		}
	}

	private TextEdit getTextEditForToken(final LToken pToken, final Position pCursorPosition, final String pNewText)
	{
		final boolean isLikeWhitespace = (pToken.getType() == LTokenType.LEFT_PARENTHESIS
				|| pToken.getType() == LTokenType.WHITESPACE);
		final Range range = new Range();
		if (isLikeWhitespace) {
			range.setStart(pCursorPosition);
			range.setEnd(pCursorPosition);
		} else {
			final int line = pToken.getLine();
			final int startColumn = pToken.getColumn();
			final int length = pToken.getText().length();
			final int endColumn = startColumn + length;
			range.setStart(new Position(line, startColumn));
			range.setEnd(new Position(line, endColumn));
		}
		final TextEdit textEdit = new TextEdit(range, pNewText);
		return textEdit;
	}

	private static final String[] COMPLETION_TRIGGER_CHARACTERS = { "_", "$", ".", //
			"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "o", "p", "q", "r", "s", "t", "u", "v",
			"w", "x", "y", "z", //
			"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "O", "P", "Q", "R", "S", "T", "U", "V",
			"W", "X", "Y", "Z", //
	};
	private static String[] FUNCTIONS = { //
			"abbrev", //
			"abs", //
			"address", //
			"arg", //
			"b2x", //
			"bitand", //
			"bitor", //
			"bitxor", //
			"c2d", //
			"c2x", //
			"center", //
			"centre", //
			"compare", //
			"condition", //
			"copies", //
			"d2c", //
			"d2x", //
			"datatype", //
			"date", //
			"dbcs", //
			"delstr", //
			"delword", //
			"digits", //
			"errortext", //
			"externals", //
			"find", //
			"form", //
			"format", //
			"fuzz", //
			"getmsg", //
			"getmsg", //
			"index", //
			"insert", //
			"justify", //
			"lastpos", //
			"left", //
			"length", //
			"linesize", //
			"listdsi", //
			"listdsi", //
			"max", //
			"min", //
			"msg", //
			"msg", //
			"mvsvar", //
			"mvsvar", //
			"outtrap", //
			"outtrap", //
			"overlay", //
			"pos", //
			"prompt", //
			"prompt", //
			"queued", //
			"random", //
			"reverse", //
			"right", //
			"setlang", //
			"setlang", //
			"sign", //
			"sourceline", //
			"space", //
			"storage", //
			"storage", //
			"strip", //
			"substr", //
			"subword", //
			"symbol", //
			"syscpus", //
			"syscpus", //
			"sysdsn", //
			"sysdsn", //
			"sysvar", //
			"sysvar", //
			"time", //
			"trace", //
			"translate", //
			"trapmsg", //
			"trunc", //
			"userid", //
			"value", //
			"verify", //
			"word", //
			"wordindex", //
			"wordlength", //
			"wordpos", //
			"words", //
			"x2b", //
			"x2c", //
			"x2d", //
			"xrange", //
	};
	private static String[] KEYWORDS = { //
			"address", //
			"arg", //
			"by", //
			"call", //
			"do", //
			"drop", //
			"else", //
			"end", //
			"exit", //
			"expose", //
			"for", //
			"forever", //
			"if", //
			"interpret", //
			"iterate", //
			"leave", //
			"nop", //
			"numeric", //
			"options", //
			"parse", //
			"procedure", //
			"pull", //
			"push", //
			"queue", //
			"return", //
			"say", //
			"select", //
			"signal", //
			"then", //
			"to", //
			"trace", //
			"upper", //
			"when", //
			"while", //
	};
}
