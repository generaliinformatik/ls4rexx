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
