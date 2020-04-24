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
import java.util.List;
import java.util.stream.Collectors;

import de.holzem.lsp.lsp4rexx.rexxscanner.RexxToken;

/**
 * RexxFile
 */
public class RexxFile
{
	private final String _uri;
	private final List<RexxToken> _tokens = new ArrayList<RexxToken>();

	public RexxFile(final String pUri) {
		_uri = pUri;
	}

	public String getUri()
	{
		return _uri;
	}

	public void add(final RexxToken pRexxToken)
	{
		_tokens.add(pRexxToken);
	}

	public String getText()
	{
		final String fileText = _tokens.stream().map(token -> token.getText()).collect(Collectors.joining());
		return fileText;
	}
}
