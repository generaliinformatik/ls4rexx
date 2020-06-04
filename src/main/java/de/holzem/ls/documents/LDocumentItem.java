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
package de.holzem.ls.documents;

import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;

import org.eclipse.lsp4j.TextDocumentContentChangeEvent;
import org.eclipse.lsp4j.TextDocumentItem;
import org.eclipse.lsp4j.jsonrpc.CancelChecker;
import org.eclipse.lsp4j.jsonrpc.CompletableFutures;

import de.holzem.ls.language.LModel;
import lombok.extern.slf4j.Slf4j;

/**
 * LDocumentItem
 */
@Slf4j
public class LDocumentItem extends TextDocumentItem
{
	final BiFunction<TextDocumentItem, CancelChecker, LModel> _parseBiFunction;
	private CompletableFuture<LModel> _model;

	/**
	 * Constructor
	 */
	public LDocumentItem(final TextDocumentItem pTextDocumentItem,
			final BiFunction<TextDocumentItem, CancelChecker, LModel> pParseBiFunction) {
		super.setUri(pTextDocumentItem.getUri());
		super.setText(pTextDocumentItem.getText());
		super.setVersion(pTextDocumentItem.getVersion());
		super.setLanguageId(pTextDocumentItem.getLanguageId());
		_parseBiFunction = pParseBiFunction;
	}

	public CompletableFuture<LModel> getModel()
	{
		if (_model == null) {
			_model = CompletableFutures.computeAsync(this::parseDocument);
		}
		return _model;
	}

	private LModel parseDocument(final CancelChecker pCancelChecker)
	{
		final long startTime = System.currentTimeMillis();
		LModel lModel = null;
		try {
			log.debug("Start parse of {}", getUri());
			lModel = _parseBiFunction.apply(this, pCancelChecker);
		} catch (final CancellationException exc) {
			log.debug("CANCEL {}ms parse of {}", (System.currentTimeMillis() - startTime), getUri());
		} finally {
			log.debug("END {}ms parse of {} ", (System.currentTimeMillis() - startTime), getUri());
		}
		return lModel;
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