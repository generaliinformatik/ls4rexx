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
import org.eclipse.lsp4j.services.TextDocumentService;

import de.holzem.ls.LServer;
import de.holzem.ls.documents.LDocumentItem;
import de.holzem.ls.documents.LDocuments;
import de.holzem.ls.language.LModel;
import de.holzem.ls.language.LParser;
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
		_lDocuments = new LDocuments(this::parseBiFunction);
	}

	private LModel parseBiFunction(final TextDocumentItem pDocumentItem, final CancelChecker pCancelChecker)
	{
		return LParser.INSTANCE.parse(pDocumentItem, pCancelChecker);
	}

	@Override
	public CompletableFuture<Either<List<CompletionItem>, CompletionList>> completion(
			final CompletionParams completionParams)
	{
		final TextDocumentIdentifier textDocumentIdentifier = completionParams.getTextDocument();
		final Position position = completionParams.getPosition();
		return computeRexxFileAsync(textDocumentIdentifier, (cancelChecker, rexxFile) -> {
			final CompletionList list = getLanguageServices().doComplete(rexxFile, position, cancelChecker);
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
		return null;
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
		final LDocumentItem lDocumentItem = _lDocuments.onDidOpenTextDocument(didOpenTextDocumentParams);
		triggerValidationFor(lDocumentItem);
	}

	@Override
	public void didChange(final DidChangeTextDocumentParams didChangeTextDocumentParams)
	{
		final LDocumentItem lDocumentItem = _lDocuments.onDidChangeTextDocument(didChangeTextDocumentParams);
		triggerValidationFor(lDocumentItem);
	}

	@Override
	public void didClose(final DidCloseTextDocumentParams didCloseTextDocumentParams)
	{
		_lDocuments.onDidCloseTextDocument(didCloseTextDocumentParams);
		final TextDocumentIdentifier document = didCloseTextDocumentParams.getTextDocument();
		final String uri = document.getUri();
		// TODO clean diagnostics
	}

	@Override
	public void didSave(final DidSaveTextDocumentParams didSaveTextDocumentParams)
	{
	}

	private void triggerValidationFor(final LDocumentItem pLDocumentItem)
	{
		pLDocumentItem.getModel().thenAcceptAsync(this::validate);
	}

	private void validate(final LModel pLModel) throws CancellationException
	{
		final CancelChecker cancelChecker = pLModel.getCancelChecker();
		cancelChecker.checkCanceled();
		// TODO publish diagnostics
	}

	private LDocumentItem getDocument(final String pUri)
	{
		return _lDocuments.getDocument(pUri);
	}

	private LanguageServices getLanguageServices()
	{
		return _lServer.getLanguageServices();
	}

	private <R> CompletableFuture<R> computeRexxFileAsync(final TextDocumentIdentifier pDocumentIdentifier,
			final BiFunction<CancelChecker, LModel, R> pBiFunction)
	{
		final LDocumentItem lDocumentItem = getDocument(pDocumentIdentifier.getUri());
		return computeModelAsync(lDocumentItem.getModel(), pBiFunction);
	}

	private static <R> CompletableFuture<R> computeModelAsync(final CompletableFuture<LModel> pLModel,
			final BiFunction<CancelChecker, LModel, R> pBiFunction)
	{
		final CompletableFuture<CancelChecker> start = new CompletableFuture<CancelChecker>();
		final CompletableFuture<R> result = start.thenCombineAsync(pLModel, pBiFunction);
		final CancelChecker cancelIndicator = () -> {
			if (result.isCancelled()) {
				throw new CancellationException();
			}
		};
		start.complete(cancelIndicator);
		return result;
	}
}
