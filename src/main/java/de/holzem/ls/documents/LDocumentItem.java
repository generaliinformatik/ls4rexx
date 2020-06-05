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
import java.util.function.Consumer;

import org.eclipse.lsp4j.TextDocumentContentChangeEvent;
import org.eclipse.lsp4j.TextDocumentItem;
import org.eclipse.lsp4j.jsonrpc.CancelChecker;

import com.google.common.base.Function;

import de.holzem.ls.language.LModel;
import de.holzem.ls.language.LParser;
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
