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
package de.holzem.lsp.lsp4rexx;

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

import de.holzem.lsp.lsp4rexx.files.RexxDocument;
import de.holzem.lsp.lsp4rexx.files.RexxDocuments;
import de.holzem.lsp.lsp4rexx.rexxparser.RexxFile;
import de.holzem.lsp.lsp4rexx.rexxparser.RexxParser;
import de.holzem.lsp.lsp4rexx.services.LanguageServices;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RexxTextDocumentService implements TextDocumentService
{
	private final RexxLanguageServer _rexxLanguageServer;
	private final RexxDocuments _rexxDocuments;

	/**
	 * Constructor
	 */
	public RexxTextDocumentService(final RexxLanguageServer pRexxLanguageServer) {
		_rexxLanguageServer = pRexxLanguageServer;
		_rexxDocuments = new RexxDocuments(this::parseBiFunction);
	}

	private RexxFile parseBiFunction(final TextDocumentItem pDocumentItem, final CancelChecker pCancelChecker)
	{
		return RexxParser.INSTANCE.parse(pDocumentItem, pCancelChecker);
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
		final RexxDocument rexxDocument = _rexxDocuments.onDidOpenTextDocument(didOpenTextDocumentParams);
		triggerValidationFor(rexxDocument);
	}

	@Override
	public void didChange(final DidChangeTextDocumentParams didChangeTextDocumentParams)
	{
		final RexxDocument rexxDocument = _rexxDocuments.onDidChangeTextDocument(didChangeTextDocumentParams);
		triggerValidationFor(rexxDocument);
	}

	@Override
	public void didClose(final DidCloseTextDocumentParams didCloseTextDocumentParams)
	{
		_rexxDocuments.onDidCloseTextDocument(didCloseTextDocumentParams);
		final TextDocumentIdentifier document = didCloseTextDocumentParams.getTextDocument();
		final String uri = document.getUri();
		// TODO clean diagnostics
	}

	@Override
	public void didSave(final DidSaveTextDocumentParams didSaveTextDocumentParams)
	{
	}

	private void triggerValidationFor(final RexxDocument pRexxDocument)
	{
		pRexxDocument.getModel().thenAcceptAsync(this::validate);
	}

	private void validate(final RexxFile pRexxFile) throws CancellationException
	{
		final CancelChecker cancelChecker = pRexxFile.getCancelChecker();
		cancelChecker.checkCanceled();
		// TODO publish diagnostics
	}

	private RexxDocument getDocument(final String pUri)
	{
		return _rexxDocuments.getDocument(pUri);
	}

	private LanguageServices getLanguageServices()
	{
		return _rexxLanguageServer.getLanguageServices();
	}

	private <R> CompletableFuture<R> computeRexxFileAsync(final TextDocumentIdentifier pDocumentIdentifier,
			final BiFunction<CancelChecker, RexxFile, R> pBiFunction)
	{
		final RexxDocument rexxDocument = getDocument(pDocumentIdentifier.getUri());
		return computeModelAsync(rexxDocument.getModel(), pBiFunction);
	}

	private static <R> CompletableFuture<R> computeModelAsync(final CompletableFuture<RexxFile> pRexxFile,
			final BiFunction<CancelChecker, RexxFile, R> pBiFunction)
	{
		final CompletableFuture<CancelChecker> start = new CompletableFuture<CancelChecker>();
		final CompletableFuture<R> result = start.thenCombineAsync(pRexxFile, pBiFunction);
		final CancelChecker cancelIndicator = () -> {
			if (result.isCancelled()) {
				throw new CancellationException();
			}
		};
		start.complete(cancelIndicator);
		return result;
	}
}
