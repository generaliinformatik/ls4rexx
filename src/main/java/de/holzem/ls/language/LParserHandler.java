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
package de.holzem.ls.language;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * LParserHandler
 */
class LParserHandler
{
	Set<String> _registeredVariables = new HashSet<String>();
	Set<String> _registeredLabels = new HashSet<String>();
	List<String> _variables = new ArrayList<String>();
	List<LToken> _labels = new ArrayList<LToken>();

	void handleKeyword(final LToken pPrevToken, final LToken pCurrentToken, final LToken pNextToken)
	{
	}

	void handleIdentifier(final LToken pPrevToken, final LToken pCurrentToken, final LToken pNextToken)
	{
		final LTokenType prevTokenType = (pPrevToken != null ? pPrevToken.getType() : null);
		final LTokenType nextTokenType = (pNextToken != null ? pNextToken.getType() : null);
		final boolean isNextColon = (nextTokenType == LTokenType.COLON);
		final boolean isNextLeftParen = (nextTokenType == LTokenType.LEFT_PARENTHESIS);
		final boolean isPrevCall = (prevTokenType == LTokenType.KEYWORD
				&& pPrevToken.getText().toLowerCase().contentEquals("call"));
		if (!isNextColon && !isPrevCall && !isNextLeftParen) {
			final String tokenText = pCurrentToken.getText();
			final String tokenTextLowerCase = tokenText.toLowerCase();
			if (!_registeredVariables.contains(tokenTextLowerCase)) {
				_variables.add(tokenText);
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

	List<String> getVariables()
	{
		_variables.sort((s1, s2) -> s1.compareToIgnoreCase(s2));
		return _variables;
	}

	List<LToken> getLabels()
	{
		_labels.sort((s1, s2) -> s1.getText().compareToIgnoreCase(s2.getText()));
		return _labels;
	}
}
