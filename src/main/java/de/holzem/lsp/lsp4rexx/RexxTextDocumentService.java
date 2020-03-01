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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.eclipse.lsp4j.CodeActionParams;
import org.eclipse.lsp4j.CodeLens;
import org.eclipse.lsp4j.CodeLensParams;
import org.eclipse.lsp4j.Command;
import org.eclipse.lsp4j.CompletionItem;
import org.eclipse.lsp4j.CompletionItemKind;
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
import org.eclipse.lsp4j.ReferenceParams;
import org.eclipse.lsp4j.RenameParams;
import org.eclipse.lsp4j.SignatureHelp;
import org.eclipse.lsp4j.SymbolInformation;
import org.eclipse.lsp4j.TextDocumentPositionParams;
import org.eclipse.lsp4j.TextEdit;
import org.eclipse.lsp4j.WorkspaceEdit;
import org.eclipse.lsp4j.jsonrpc.messages.Either;
import org.eclipse.lsp4j.services.TextDocumentService;

public class RexxTextDocumentService implements TextDocumentService {
	@Override
	public CompletableFuture<Either<List<CompletionItem>, CompletionList>> completion(
			final CompletionParams completionParams)
	{
		// Provide completion item.
		return CompletableFuture.supplyAsync(() -> {
			final List<CompletionItem> completionItems = new ArrayList<>();
			try {
				final CompletionItem completionItem = new CompletionItem();
				completionItem.setInsertText("say \"Hello\"\n");
				completionItem.setLabel("say \"Hello\"");
				completionItem.setKind(CompletionItemKind.Snippet);
				completionItem.setDetail("sayHello()\n this will say hello to the people");
				completionItems.add(completionItem);
			} catch (final Exception e) {
				// TODO: Handle the exception.
			}
			return Either.forLeft(completionItems);
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
		System.out.println(didOpenTextDocumentParams);
	}

	@Override
	public void didChange(final DidChangeTextDocumentParams didChangeTextDocumentParams)
	{
		System.out.println(didChangeTextDocumentParams);
	}

	@Override
	public void didClose(final DidCloseTextDocumentParams didCloseTextDocumentParams)
	{

	}

	@Override
	public void didSave(final DidSaveTextDocumentParams didSaveTextDocumentParams)
	{

	}
}
