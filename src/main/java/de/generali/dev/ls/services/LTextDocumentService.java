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
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;

import org.eclipse.lsp4j.CodeActionParams;
import org.eclipse.lsp4j.CodeLens;
import org.eclipse.lsp4j.CodeLensParams;
import org.eclipse.lsp4j.Command;
import org.eclipse.lsp4j.CompletionItem;
import org.eclipse.lsp4j.CompletionList;
import org.eclipse.lsp4j.CompletionParams;
import org.eclipse.lsp4j.DidChangeTextDocumentParams;
import org.eclipse.lsp4j.DidCloseTextDocumentParams;
import org.eclipse.lsp4j.DidOpenTextDocumentParams;
import org.eclipse.lsp4j.DidSaveTextDocumentParams;
import org.eclipse.lsp4j.DocumentFormattingParams;
import org.eclipse.lsp4j.DocumentHighlight;
import org.eclipse.lsp4j.DocumentOnTypeFormattingParams;
import org.eclipse.lsp4j.DocumentRangeFormattingParams;
import org.eclipse.lsp4j.DocumentSymbolParams;
import org.eclipse.lsp4j.Hover;
import org.eclipse.lsp4j.Location;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.ReferenceParams;
import org.eclipse.lsp4j.RenameParams;
import org.eclipse.lsp4j.SignatureHelp;
import org.eclipse.lsp4j.SymbolInformation;
import org.eclipse.lsp4j.TextDocumentIdentifier;
import org.eclipse.lsp4j.TextDocumentItem;
import org.eclipse.lsp4j.TextDocumentPositionParams;
import org.eclipse.lsp4j.TextEdit;
import org.eclipse.lsp4j.WorkspaceEdit;
import org.eclipse.lsp4j.jsonrpc.CancelChecker;
import org.eclipse.lsp4j.jsonrpc.messages.Either;
import org.eclipse.lsp4j.services.LanguageClient;
import org.eclipse.lsp4j.services.TextDocumentService;

import de.generali.dev.ls.LServer;
import de.generali.dev.ls.documents.LDocumentItem;
import de.generali.dev.ls.documents.LDocuments;
import de.generali.dev.ls.language.LModel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LTextDocumentService implements TextDocumentService
{
	private final LServer _lServer;
	private final LDocuments _lDocuments;

	/**
	 * Constructor
	 */
	public LTextDocumentService(final LServer pLServer) {
		_lServer = pLServer;
		_lDocuments = new LDocuments();
	}

	@Override
	public CompletableFuture<Either<List<CompletionItem>, CompletionList>> completion(
			final CompletionParams completionParams)
	{
		final TextDocumentIdentifier textDocumentIdentifier = completionParams.getTextDocument();
		final Position position = completionParams.getPosition();
		log("completion", textDocumentIdentifier, position);
		return computeResultAsync(textDocumentIdentifier, //
				// BiFunction taking a CancelChecker and a LModel to create a CompletionList
				(cancelChecker, model) -> {
					final CompletionList list = getServices().doComplete(cancelChecker, model, position);
					return Either.forRight(list);
				});
	}

	@Override
	public CompletableFuture<CompletionItem> resolveCompletionItem(final CompletionItem completionItem)
	{
		return null;
	}

	@Override
	public CompletableFuture<Hover> hover(final TextDocumentPositionParams textDocumentPositionParams)
	{
		return null;
	}

	@Override
	public CompletableFuture<SignatureHelp> signatureHelp(final TextDocumentPositionParams textDocumentPositionParams)
	{
		return null;
	}

	@Override
	public CompletableFuture<List<? extends Location>> definition(
			final TextDocumentPositionParams textDocumentPositionParams)
	{
		return null;
	}

	@Override
	public CompletableFuture<List<? extends Location>> references(final ReferenceParams referenceParams)
	{
		return null;
	}

	@Override
	public CompletableFuture<List<? extends DocumentHighlight>> documentHighlight(
			final TextDocumentPositionParams textDocumentPositionParams)
	{
		return null;
	}

	@Override
	public CompletableFuture<List<? extends SymbolInformation>> documentSymbol(
			final DocumentSymbolParams documentSymbolParams)
	{
		final TextDocumentIdentifier textDocumentIdentifier = documentSymbolParams.getTextDocument();
		log("documentSymbol", textDocumentIdentifier);
		return computeResultAsync(textDocumentIdentifier, //
				// BiFunction taking a CancelChecker and a LModel to create a list with SymbolInformation
				(cancelChecker, model) -> {
					final List<SymbolInformation> list = getServices().doDocumentSymbol(cancelChecker, model);
					return list;
				});
	}

	@Override
	public CompletableFuture<List<? extends Command>> codeAction(final CodeActionParams codeActionParams)
	{
		return null;
	}

	@Override
	public CompletableFuture<List<? extends CodeLens>> codeLens(final CodeLensParams codeLensParams)
	{
		return null;
	}

	@Override
	public CompletableFuture<CodeLens> resolveCodeLens(final CodeLens codeLens)
	{
		return null;
	}

	@Override
	public CompletableFuture<List<? extends TextEdit>> formatting(
			final DocumentFormattingParams documentFormattingParams)
	{
		return null;
	}

	@Override
	public CompletableFuture<List<? extends TextEdit>> rangeFormatting(
			final DocumentRangeFormattingParams documentRangeFormattingParams)
	{
		return null;
	}

	@Override
	public CompletableFuture<List<? extends TextEdit>> onTypeFormatting(
			final DocumentOnTypeFormattingParams documentOnTypeFormattingParams)
	{
		return null;
	}

	@Override
	public CompletableFuture<WorkspaceEdit> rename(final RenameParams renameParams)
	{
		return null;
	}

	@Override
	public void didOpen(final DidOpenTextDocumentParams didOpenTextDocumentParams)
	{
		final TextDocumentItem textDocumentItem = didOpenTextDocumentParams.getTextDocument();
		log("open", textDocumentItem);
		final LDocumentItem lDocumentItem = _lDocuments.onDidOpenTextDocument(didOpenTextDocumentParams);
		lDocumentItem.runActionAsync(this::validate);
	}

	@Override
	public void didChange(final DidChangeTextDocumentParams didChangeTextDocumentParams)
	{
		final TextDocumentIdentifier textDocumentIdentifier = didChangeTextDocumentParams.getTextDocument();
		log("change", textDocumentIdentifier);
		final LDocumentItem lDocumentItem = _lDocuments.onDidChangeTextDocument(didChangeTextDocumentParams);
		lDocumentItem.runActionAsync(this::validate);
	}

	@Override
	public void didClose(final DidCloseTextDocumentParams didCloseTextDocumentParams)
	{
		final TextDocumentIdentifier textDocumentIdentifier = didCloseTextDocumentParams.getTextDocument();
		log("close", textDocumentIdentifier);
		_lDocuments.onDidCloseTextDocument(didCloseTextDocumentParams);
		getServices().cleanDiagnostics(getLanguageClient(), textDocumentIdentifier);
	}

	@Override
	public void didSave(final DidSaveTextDocumentParams didSaveTextDocumentParams)
	{
		final TextDocumentIdentifier textDocumentIdentifier = didSaveTextDocumentParams.getTextDocument();
		log("save", textDocumentIdentifier);
	}

	private void validate(final LModel pLModel) throws CancellationException
	{
		log("validate", pLModel);
		final CancelChecker cancelChecker = pLModel.getCancelChecker();
		cancelChecker.checkCanceled();
		getServices().publishDiagnostics(getLanguageClient(), cancelChecker, pLModel);
	}
	// ------------------------------------------------------------------------
	// convenience methods
	// ------------------------------------------------------------------------

	/**
	 * Run the BiFunction for the given TextDocument
	 *
	 * @param <Result>                 the generic <code>Result</code>
	 * @param pTextDocumentIdentifier  the identifier for the text document
	 * @param pComputeResultBiFunction the BiFunction to compute the <code>Result</code>
	 * @return a {@link CompletableFuture} of the <code>Result</code>
	 */
	private <Result> CompletableFuture<Result> computeResultAsync(final TextDocumentIdentifier pTextDocumentIdentifier,
			final BiFunction<CancelChecker, LModel, Result> pComputeResultBiFunction)
	{
		final LDocumentItem lDocumentItem = _lDocuments.getDocument(pTextDocumentIdentifier.getUri());
		return lDocumentItem.computeResultAsync(pComputeResultBiFunction);
	}

	private LServices getServices()
	{
		return _lServer.getLServices();
	}

	private LanguageClient getLanguageClient()
	{
		return _lServer.getLanguageClient();
	}

	private static void log(final String pFunction, final TextDocumentIdentifier pTextDocumentIdentifier,
			final Position pPosition)
	{
		log.debug("{} for {} at ({},{})", pFunction, pTextDocumentIdentifier.getUri(), pPosition.getLine(),
				pPosition.getCharacter());
	}

	private static void log(final String pFunction, final TextDocumentIdentifier pTextDocumentIdentifier)
	{
		log.debug("{} for {}", pFunction, pTextDocumentIdentifier.getUri());
	}

	private static void log(final String pFunction, final TextDocumentItem pTextDocumentItem)
	{
		log.debug("{} for {}", pFunction, pTextDocumentItem.getUri());
	}

	private static void log(final String pFunction, final LModel pLModel)
	{
		log.debug("{} for {}", pFunction, pLModel.getUri());
	}
}
