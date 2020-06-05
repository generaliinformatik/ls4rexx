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
package de.holzem.ls.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.eclipse.lsp4j.CompletionItem;
import org.eclipse.lsp4j.CompletionItemKind;
import org.eclipse.lsp4j.CompletionList;
import org.eclipse.lsp4j.CompletionOptions;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.ServerCapabilities;
import org.eclipse.lsp4j.jsonrpc.CancelChecker;

import de.holzem.ls.language.LModel;
import de.holzem.ls.language.LToken;
import de.holzem.ls.language.LTokenType;
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

	public CompletionList doComplete(final CancelChecker pCancelChecker, final LModel pLModel, final Position pPosition)
	{
		final int line = pPosition.getLine();
		final int column = pPosition.getCharacter();
		log.debug("complete for {} at ({},{})", pLModel.getUri(), line, column);
		final int tokenPosition = getTokenPosition(pLModel, line, column);
		log.debug("match with token {}", pLModel.getToken(tokenPosition));
		final List<CompletionItem> completionItems = new ArrayList<>();
		addVariablesToCompletions(pLModel, tokenPosition, completionItems);
		addLabelsToCompletions(pLModel, tokenPosition, completionItems);
		addFunctionsToCompletions(pLModel, tokenPosition, completionItems);
		// addKeywordsToCompletions(pLModel, tokenPosition, completionItems);
		final CompletionList completionList = new CompletionList();
		completionList.setIsIncomplete(true);
		completionList.setItems(completionItems);
		return completionList;
	}

	private int getTokenPosition(final LModel pLModel, final int pLine, final int pColumn)
	{
		int tokenPosition = pLModel.locatePrevToken(pLine, pColumn);
		final LToken token = pLModel.getToken(tokenPosition);
		if (token.getType() == LTokenType.WHITESPACE && token.getLine() == pLine && token.getColumn() == pColumn) {
			if (tokenPosition > 0) {
				--tokenPosition;
			}
		}
		return tokenPosition;
	}

	private void addVariablesToCompletions(final LModel pLModel, final int pTokenPosition,
			final List<CompletionItem> pCompletionItems)
	{
		final LToken token = pLModel.getToken(pTokenPosition);
		final String tokenText = token.getText();
		for (final String variable : pLModel.getVariables()) {
			if (variable.startsWith(tokenText)) {
				if (!Objects.equals(variable, tokenText)) {
					final CompletionItem completionItem = new CompletionItem(variable);
					completionItem.setKind(CompletionItemKind.Variable);
					completionItem.setInsertText(variable);
					completionItem.setDetail("Variable " + variable);
					pCompletionItems.add(completionItem);
				}
			}
		}
	}

	private void addLabelsToCompletions(final LModel pLModel, final int pTokenPosition,
			final List<CompletionItem> pCompletionItems)
	{
		final LToken token = pLModel.getToken(pTokenPosition);
		final String tokenText = token.getText();
		for (final String label : pLModel.getLabels()) {
			if (label.startsWith(tokenText)) {
				if (!Objects.equals(label, tokenText)) {
					final CompletionItem completionItem = new CompletionItem(label);
					completionItem.setKind(CompletionItemKind.Method);
					completionItem.setInsertText(label);
					completionItem.setDetail("Label " + label);
					pCompletionItems.add(completionItem);
				}
			}
		}
	}

	private void addFunctionsToCompletions(final LModel pLModel, final int pTokenPosition,
			final List<CompletionItem> pCompletionItems)
	{
		final LToken token = pLModel.getToken(pTokenPosition);
		final String tokenText = token.getText();
		for (final String function : FUNCTIONS) {
			if (function.startsWith(tokenText)) {
				if (!Objects.equals(function, tokenText)) {
					final CompletionItem completionItem = new CompletionItem(function);
					completionItem.setKind(CompletionItemKind.Function);
					completionItem.setInsertText(function);
					completionItem.setDetail("Function " + function);
					pCompletionItems.add(completionItem);
				}
			}
		}
	}

	private void addKeywordsToCompletions(final LModel pLModel, final int pTokenPosition,
			final List<CompletionItem> pCompletionItems)
	{
		final LToken token = pLModel.getToken(pTokenPosition);
		final String tokenText = token.getText();
		for (final String keyword : KEYWORDS) {
			if (keyword.startsWith(tokenText)) {
				if (!Objects.equals(keyword, tokenText)) {
					final CompletionItem completionItem = new CompletionItem(keyword);
					completionItem.setKind(CompletionItemKind.Keyword);
					completionItem.setInsertText(keyword);
					completionItem.setDetail("Keyword " + keyword);
					pCompletionItems.add(completionItem);
				}
			}
		}
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
