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

/**
 * RexxHandler
 */
class RexxHandler
{
	Set<String> _registeredVariables = new HashSet<String>();
	List<String> _variables = new ArrayList<String>();
	List<String> _labels = new ArrayList<String>();

	void handleFunction(final RexxToken pRexxToken)
	{
		// TODO Auto-generated method stub
	}

	void handleIdentifier(final RexxToken pRexxToken)
	{
		final String tokenText = pRexxToken.getText();
		final String tokenTextLowerCase = tokenText.toLowerCase();
		if (!_registeredVariables.contains(tokenTextLowerCase)) {
			_variables.add(tokenText);
			_registeredVariables.add(tokenTextLowerCase);
		}
	}

	void handleLeftParenthesis(final RexxToken pRexxToken)
	{
		// TODO Auto-generated method stub
	}

	void handleColon(final RexxToken pRexxToken)
	{
		// TODO Auto-generated method stub
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
