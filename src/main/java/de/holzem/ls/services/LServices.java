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

import java.util.List;

import org.eclipse.lsp4j.CompletionList;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.ServerCapabilities;
import org.eclipse.lsp4j.SymbolInformation;
import org.eclipse.lsp4j.TextDocumentSyncKind;
import org.eclipse.lsp4j.jsonrpc.CancelChecker;

import de.holzem.ls.language.LModel;

/**
 * LServices
 */
public class LServices
{
	private final CompletionService _completionService;
	private final DocumentSymbolService _documentSymbolService;

	public LServices(final ServerCapabilities pServerCapabilities) {
		pServerCapabilities.setTextDocumentSync(TextDocumentSyncKind.Full);
		_completionService = new CompletionService(this, pServerCapabilities);
		_documentSymbolService = new DocumentSymbolService(this, pServerCapabilities);
	}

	public CompletionList doComplete(final CancelChecker pCancelChecker, final LModel pLModel, final Position pPosition)
	{
		return _completionService.doComplete(pCancelChecker, pLModel, pPosition);
	}

	public List<SymbolInformation> doDocumentSymbol(final CancelChecker pCancelChecker, final LModel pLModel)
	{
		return _documentSymbolService.doDocumentSymbol(pCancelChecker, pLModel);
	}
}
