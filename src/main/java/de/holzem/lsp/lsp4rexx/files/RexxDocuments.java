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

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import org.eclipse.lsp4j.DidChangeTextDocumentParams;
import org.eclipse.lsp4j.DidCloseTextDocumentParams;
import org.eclipse.lsp4j.DidOpenTextDocumentParams;
import org.eclipse.lsp4j.TextDocumentIdentifier;
import org.eclipse.lsp4j.TextDocumentItem;
import org.eclipse.lsp4j.jsonrpc.CancelChecker;

import de.holzem.lsp.lsp4rexx.rexxparser.RexxFile;
import lombok.extern.slf4j.Slf4j;

/**
 * RexxDocuments
 */
@Slf4j
public class RexxDocuments
{
	private final BiFunction<TextDocumentItem, CancelChecker, RexxFile> _parseBiFunction;
	private final Map<String, RexxDocument> _documents;

	public RexxDocuments(final BiFunction<TextDocumentItem, CancelChecker, RexxFile> pParseBiFunction) {
		_parseBiFunction = pParseBiFunction;
		_documents = new HashMap<String, RexxDocument>();
	}

	public RexxDocument getDocument(final String pUri)
	{
		synchronized (_documents) {
			return _documents.get(pUri);
		}
	}

	public RexxDocument createDocument(final TextDocumentItem pTextDocumentItem)
	{
		final RexxDocument document = new RexxDocument(pTextDocumentItem, _parseBiFunction);
		return document;
	}

	public RexxDocument onDidChangeTextDocument(final DidChangeTextDocumentParams params)
	{
		RexxDocument document = null;
		synchronized (_documents) {
			document = getDocument(params.getTextDocument());
			log.debug("change of {}", document.getUri());
			if (document != null) {
				document.setVersion(params.getTextDocument().getVersion());
				document.update(params.getContentChanges());
			}
		}
		return document;
	}

	public RexxDocument onDidOpenTextDocument(final DidOpenTextDocumentParams params)
	{
		RexxDocument document = null;
		final TextDocumentItem item = params.getTextDocument();
		synchronized (_documents) {
			document = createDocument(item);
			log.debug("open of {}", document.getUri());
			_documents.put(document.getUri(), document);
		}
		return document;
	}

	public RexxDocument onDidCloseTextDocument(final DidCloseTextDocumentParams params)
	{
		RexxDocument document = null;
		synchronized (_documents) {
			document = getDocument(params.getTextDocument());
			log.debug("close of {}", document.getUri());
			if (document != null) {
				_documents.remove(params.getTextDocument().getUri());
			}
		}
		return document;
	}

	private RexxDocument getDocument(final TextDocumentIdentifier pTextDocumentIdentifier)
	{
		return _documents.get(pTextDocumentIdentifier.getUri());
	}
}
