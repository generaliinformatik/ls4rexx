/**
 *  Copyright (c) 2018 Angelo ZERR.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v2.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *  Contributors:
 *  Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 *  Markus Holzem <markus.holzem@generali.com> - async processing of model
 *
 *  Originated from
 *  https://github.com/angelozerr/lemminx/blob/master/org.eclipse.lemminx/src/main/java/org/eclipse/lemminx/commons/TextDocument.java
 */
package de.generali.dev.ls.documents;

import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import org.eclipse.lsp4j.TextDocumentContentChangeEvent;
import org.eclipse.lsp4j.TextDocumentItem;
import org.eclipse.lsp4j.jsonrpc.CancelChecker;

import com.google.common.base.Function;

import de.generali.dev.ls.language.LModel;
import de.generali.dev.ls.language.LParser;
import lombok.extern.slf4j.Slf4j;

/**
 * LDocumentItem
 */
@Slf4j
public class LDocumentItem extends TextDocumentItem
{
	private CompletableFuture<LModel> _model;

	/**
	 * Constructor
	 */
	public LDocumentItem(final TextDocumentItem pTextDocumentItem) {
		super.setUri(pTextDocumentItem.getUri());
		super.setText(pTextDocumentItem.getText());
		super.setVersion(pTextDocumentItem.getVersion());
		super.setLanguageId(pTextDocumentItem.getLanguageId());
	}

	/**
	 * Run an action on the model of this LDocumentItem.
	 *
	 * @param pAction the action to run
	 */
	public void runActionAsync(final Consumer<LModel> pAction)
	{
		getModel().thenAcceptAsync(pAction);
	}

	/**
	 * Compute a result on the model of this LDocumentItem.
	 *
	 * @param <Result>                 the generic <code>Result</code>
	 * @param pComputeResultBiFunction the function taking a cancel checker and the model to produce the
	 *                                 <code>Result</code>
	 * @return the computed <code>Result</code>
	 */
	public <Result> CompletableFuture<Result> computeResultAsync(
			final BiFunction<CancelChecker, LModel, Result> pComputeResultBiFunction)
	{
		final CompletableFuture<LModel> lModel = getModel();
		return computeResultForModelAsync(lModel, pComputeResultBiFunction);
	}

	/**
	 * Create a {@link CompletableFuture} of the {@link LModel} for this LDocumentItem
	 *
	 * @return the {@link CompletableFuture} fo the {@link LModel}
	 */
	private CompletableFuture<LModel> getModel()
	{
		if (_model == null) {
			_model = computeModelAsync(this::parseDocument);
		}
		return _model;
	}

	private LModel parseDocument(final CancelChecker pCancelChecker)
	{
		final long startTime = System.currentTimeMillis();
		LModel lModel = null;
		try {
			log.debug("Start parse of {}", getUri());
			lModel = LParser.INSTANCE.parse(this, pCancelChecker);
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
	 * Helper method to create a {@link CompletableFuture} of a generic <code>Result</code> that can be cancelled
	 *
	 * @param pComputeResultBiFunction function to compute the result using the {@link LModel}
	 * @return the {@link CompletableFuture} of the generic <Result>
	 */
	private static <Result> CompletableFuture<Result> computeResultForModelAsync(
			final CompletableFuture<LModel> pLModel,
			final BiFunction<CancelChecker, LModel, Result> pComputeResultBiFunction)
	{
		final CompletableFuture<CancelChecker> start = new CompletableFuture<CancelChecker>();
		final CompletableFuture<Result> result = start.thenCombineAsync(pLModel, pComputeResultBiFunction);
		final CancelChecker cancelIndicator = // checkCancel
				() -> {
					if (result.isCancelled()) {
						throw new CancellationException();
					}
				};
		start.complete(cancelIndicator);
		return result;
	}

	/**
	 * Helper method to create a {@link CompletableFuture} of a {@link LModel} that can be cancelled
	 *
	 * @param pComputeModelFunction function to compute the model
	 * @return the {@link CompletableFuture} of the {@link LModel}
	 */
	private static CompletableFuture<LModel> computeModelAsync(
			final Function<CancelChecker, LModel> pComputeModelFunction)
	{
		final CompletableFuture<CancelChecker> start = new CompletableFuture<>();
		final CompletableFuture<LModel> model = start.thenApplyAsync(pComputeModelFunction);
		final CancelChecker cancelIndicator = // checkCancel
				() -> {
					if (model.isCancelled())
						throw new CancellationException();
				};
		start.complete(cancelIndicator);
		return model;
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
