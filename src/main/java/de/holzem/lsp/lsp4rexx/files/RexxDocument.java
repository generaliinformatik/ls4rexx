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
package de.holzem.lsp.lsp4rexx.files;

import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;

import org.eclipse.lsp4j.TextDocumentContentChangeEvent;
import org.eclipse.lsp4j.TextDocumentItem;
import org.eclipse.lsp4j.jsonrpc.CancelChecker;
import org.eclipse.lsp4j.jsonrpc.CompletableFutures;

import de.holzem.lsp.lsp4rexx.rexxparser.RexxFile;
import lombok.extern.slf4j.Slf4j;

/**
 * RexxDocument
 */
@Slf4j
public class RexxDocument extends TextDocumentItem
{
	final BiFunction<TextDocumentItem, CancelChecker, RexxFile> _parseBiFunction;
	private CompletableFuture<RexxFile> _model;

	/**
	 * Constructor
	 */
	public RexxDocument(final TextDocumentItem pTextDocumentItem,
			final BiFunction<TextDocumentItem, CancelChecker, RexxFile> pParseBiFunction) {
		super.setUri(pTextDocumentItem.getUri());
		super.setText(pTextDocumentItem.getText());
		super.setVersion(pTextDocumentItem.getVersion());
		super.setLanguageId(pTextDocumentItem.getLanguageId());
		_parseBiFunction = pParseBiFunction;
	}

	public CompletableFuture<RexxFile> getModel()
	{
		if (_model == null) {
			_model = CompletableFutures.computeAsync(this::parseDocument);
		}
		return _model;
	}

	private RexxFile parseDocument(final CancelChecker pCancelChecker)
	{
		final long start = System.currentTimeMillis();
		RexxFile rexxFile = null;
		try {
			log.debug("Start parse of {}", getUri());
			rexxFile = _parseBiFunction.apply(this, pCancelChecker);
		} catch (final CancellationException exc) {
			log.debug("Cancel parse of {} in {}ms", getUri(), (System.currentTimeMillis() - start));
		} finally {
			log.debug("End parse of {} in {}ms", getUri(), (System.currentTimeMillis() - start));
		}
		return rexxFile;
	}

	void update(final List<TextDocumentContentChangeEvent> pChanges)
	{
		final int numberOfChanges = pChanges.size();
		if (numberOfChanges > 0) {
			final TextDocumentContentChangeEvent lastChangeEvent = pChanges.get(numberOfChanges - 1);
			setText(lastChangeEvent.getText());
		}
	}

	@Override
	public void setText(final String pText)
	{
		super.setText(pText);
		cancelModel();
	}

	/**
	 * Cancel the completable future which loads the model.
	 */
	private void cancelModel()
	{
		if (_model != null) {
			_model.cancel(true);
			_model = null;
		}
	}
}
