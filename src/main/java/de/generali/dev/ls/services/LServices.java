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

import java.util.List;

import org.eclipse.lsp4j.CompletionList;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.ServerCapabilities;
import org.eclipse.lsp4j.SymbolInformation;
import org.eclipse.lsp4j.TextDocumentIdentifier;
import org.eclipse.lsp4j.TextDocumentSyncKind;
import org.eclipse.lsp4j.jsonrpc.CancelChecker;
import org.eclipse.lsp4j.services.LanguageClient;

import de.generali.dev.ls.language.LModel;

/**
 * LServices
 */
public class LServices
{
	private final CompletionService _completionService;
	private final DocumentSymbolService _documentSymbolService;
	private final PublishDiagnosticsService _publishDiagnosticsService;

	public LServices(final ServerCapabilities pServerCapabilities) {
		pServerCapabilities.setTextDocumentSync(TextDocumentSyncKind.Full);
		_completionService = new CompletionService(this, pServerCapabilities);
		_documentSymbolService = new DocumentSymbolService(this, pServerCapabilities);
		_publishDiagnosticsService = new PublishDiagnosticsService(this, pServerCapabilities);
	}

	public CompletionList doComplete(final CancelChecker pCancelChecker, final LModel pLModel, final Position pPosition)
	{
		return _completionService.doComplete(pCancelChecker, pLModel, pPosition);
	}

	public List<SymbolInformation> doDocumentSymbol(final CancelChecker pCancelChecker, final LModel pLModel)
	{
		return _documentSymbolService.doDocumentSymbol(pCancelChecker, pLModel);
	}

	public void publishDiagnostics(final LanguageClient pLanguageClient, final CancelChecker pCancelChecker,
			final LModel pLModel)
	{
		_publishDiagnosticsService.publishDiagnostics(pLanguageClient, pCancelChecker, pLModel);
	}

	public void cleanDiagnostics(final LanguageClient pLanguageClient,
			final TextDocumentIdentifier pTextDocumentIdentifier)
	{
		_publishDiagnosticsService.publishCleanDiagnostics(pLanguageClient, pTextDocumentIdentifier);
	}
}
