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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.lsp4j.Diagnostic;
import org.eclipse.lsp4j.DiagnosticSeverity;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.PublishDiagnosticsParams;
import org.eclipse.lsp4j.Range;
import org.eclipse.lsp4j.ServerCapabilities;
import org.eclipse.lsp4j.TextDocumentIdentifier;
import org.eclipse.lsp4j.jsonrpc.CancelChecker;
import org.eclipse.lsp4j.services.LanguageClient;

import de.generali.dev.ls.language.LError;
import de.generali.dev.ls.language.LErrors;
import de.generali.dev.ls.language.LModel;
import de.generali.dev.ls.language.LToken;

/**
 * DocumentSymbolService
 */
public class PublishDiagnosticsService extends LService
{
	/**
	 * Constructor
	 */
	public PublishDiagnosticsService(final LServices pLServices, final ServerCapabilities pServerCapabilities) {
		super(pLServices);
	}

	public void publishDiagnostics(final LanguageClient pLanguageClient, final CancelChecker pCancelChecker,
			final LModel pLModel)
	{
		final String uri = pLModel.getUri();
		final List<Diagnostic> diagnosticList = new ArrayList<Diagnostic>();
		final LErrors errors = pLModel.getErrors();
		if (errors.hasErrors()) {
			final int numberOfErrors = errors.getNumberOfErrors();
			for (int i = 0; i < numberOfErrors; ++i) {
				final LError error = errors.getError(i);
				final LToken errorToken = error.getToken();
				final int length = errorToken.getText().length();
				final Position startPos = new Position(errorToken.getLine(), errorToken.getColumn());
				final Position endPos = new Position(errorToken.getLine(), errorToken.getColumn() + length);
				final Range range = new Range(startPos, endPos);
				final String message = error.getErrorType().getMessage();
				final Diagnostic diagnostic = new Diagnostic(range, message, DiagnosticSeverity.Error,
						errorToken.getText());
				diagnosticList.add(diagnostic);
			}
		}
		final PublishDiagnosticsParams diagnostics = new PublishDiagnosticsParams(uri, diagnosticList);
		pLanguageClient.publishDiagnostics(diagnostics);
	}

	public void publishCleanDiagnostics(final LanguageClient pLanguageClient,
			final TextDocumentIdentifier pTextDocumentIdentifier)
	{
		final String uri = pTextDocumentIdentifier.getUri();
		final PublishDiagnosticsParams diagnostics = new PublishDiagnosticsParams(uri, Collections.emptyList());
		pLanguageClient.publishDiagnostics(diagnostics);
	}
}
