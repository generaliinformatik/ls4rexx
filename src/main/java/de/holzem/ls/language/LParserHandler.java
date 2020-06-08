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

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * LParserHandler
 */
class LParserHandler
{
	private static final List<String> PARSE_SUBKEYWORDS = Arrays.asList("arg", "value", "source", "with");
	private final Set<String> _registeredVariables = new HashSet<String>();
	private final Set<String> _registeredLabels = new HashSet<String>();
	private final List<LToken> _variables = new ArrayList<LToken>();
	private final List<LToken> _labels = new ArrayList<LToken>();
	private boolean _inParseStatement = false;
	private final Deque<LToken> _doStack = new ArrayDeque<LToken>();
	private final LErrors _errors = new LErrors();

	void handleKeyword(final LToken pPrevToken, final LToken pCurrentToken, final LToken pNextToken)
	{
		final String keyword = pCurrentToken.getText().toLowerCase();
		if (Objects.equals("parse", keyword)) {
			_inParseStatement = true;
		} else if (_inParseStatement) {
			if (!PARSE_SUBKEYWORDS.contains(keyword)) {
				_inParseStatement = false;
			}
		}
		if (Objects.equals("do", keyword)) {
			_doStack.push(pCurrentToken);
		}
		if (Objects.equals("end", keyword)) {
			if (_doStack.isEmpty()) {
				_errors.addError(LErrorType.E_UNMATCHED_END, pCurrentToken);
			} else {
				_doStack.pop();
			}
		}
	}

	void handleIdentifier(final LToken pPrevToken, final LToken pCurrentToken, final LToken pNextToken)
	{
		final LTokenType nextTokenType = (pNextToken != null ? pNextToken.getType() : null);
		final boolean isNextEqual = (nextTokenType == LTokenType.EQ);
		if (isNextEqual) {
			final String tokenText = pCurrentToken.getText();
			final String tokenTextLowerCase = tokenText.toLowerCase();
			if (!_registeredVariables.contains(tokenTextLowerCase)) {
				_variables.add(pCurrentToken);
				_registeredVariables.add(tokenTextLowerCase);
			}
		}
		if (_inParseStatement) {
			final String tokenText = pCurrentToken.getText();
			final String tokenTextLowerCase = tokenText.toLowerCase();
			if (!_registeredVariables.contains(tokenTextLowerCase) && !PARSE_SUBKEYWORDS.contains(tokenTextLowerCase)) {
				_variables.add(pCurrentToken);
				_registeredVariables.add(tokenTextLowerCase);
			}
		}
	}

	void handleColon(final LToken pPrevToken, final LToken pCurrentToken, final LToken pNextToken)
	{
		final String labelText = pPrevToken.getText();
		final String labelTextUpperCase = labelText.toUpperCase();
		if (!_registeredLabels.contains(labelTextUpperCase)) {
			_labels.add(pPrevToken);
			_registeredLabels.add(labelTextUpperCase);
		}
	}

	void finishParsing()
	{
		while (!_doStack.isEmpty()) {
			final LToken errorToken = _doStack.pop();
			_errors.addError(LErrorType.E_UNMATCHED_DO, errorToken);
		}
	}

	List<LToken> getVariables()
	{
		_variables.sort((s1, s2) -> s1.getText().compareToIgnoreCase(s2.getText()));
		return _variables;
	}

	List<LToken> getLabels()
	{
		_labels.sort((s1, s2) -> s1.getText().compareToIgnoreCase(s2.getText()));
		return _labels;
	}

	LErrors getErrors()
	{
		return _errors;
	}
}
