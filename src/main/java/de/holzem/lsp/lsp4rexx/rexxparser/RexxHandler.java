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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.holzem.lsp.lsp4rexx.rexxscanner.RexxToken;
import de.holzem.lsp.lsp4rexx.rexxscanner.TokenType;

/**
 * RexxHandler
 */
class RexxHandler
{
	Set<String> _registeredVariables = new HashSet<String>();
	Set<String> _registeredLabels = new HashSet<String>();
	List<String> _variables = new ArrayList<String>();
	List<String> _labels = new ArrayList<String>();

	void handleKeyword(final RexxToken pPrevToken, final RexxToken pCurrentToken, final RexxToken pNextToken)
	{
	}

	void handleIdentifier(final RexxToken pPrevToken, final RexxToken pCurrentToken, final RexxToken pNextToken)
	{
		final TokenType nextTokenType = pNextToken.getType();
		if (nextTokenType == TokenType.EQ) {
			final String tokenText = pCurrentToken.getText();
			final String tokenTextLowerCase = tokenText.toLowerCase();
			if (!_registeredVariables.contains(tokenTextLowerCase)) {
				_variables.add(tokenText);
				_registeredVariables.add(tokenTextLowerCase);
			}
		}
	}

	void handleColon(final RexxToken pPrevToken, final RexxToken pCurrentToken, final RexxToken pNextToken)
	{
		final String labelText = pPrevToken.getText();
		final String labelTextUpperCase = labelText.toUpperCase();
		if (!_registeredLabels.contains(labelTextUpperCase)) {
			_labels.add(labelText);
			_registeredLabels.add(labelTextUpperCase);
		}
	}

	List<String> getVariables()
	{
		return _variables;
	}

	List<String> getLabels()
	{
		return _labels;
	}
}
