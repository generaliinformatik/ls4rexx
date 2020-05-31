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

import org.eclipse.lsp4j.CompletionList;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.jsonrpc.CancelChecker;

import de.holzem.ls.language.LModel;

/**
 * LanguageServices
 */
public class LanguageServices
{
	private final CompletionService _completionService;

	public LanguageServices() {
		_completionService = new CompletionService(this);
	}

	public CompletionList doComplete(final LModel pLModel, final Position pPosition,
			final CancelChecker pCancelChecker)
	{
		return _completionService.doComplete(pLModel, pPosition, pCancelChecker);
	}
}
