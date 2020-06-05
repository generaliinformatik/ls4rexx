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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.lsp4j.DidChangeTextDocumentParams;
import org.eclipse.lsp4j.DidCloseTextDocumentParams;
import org.eclipse.lsp4j.DidOpenTextDocumentParams;
import org.eclipse.lsp4j.TextDocumentIdentifier;
import org.eclipse.lsp4j.TextDocumentItem;

/**
 * LDocuments
 */
public class LDocuments
{
	private final Map<String, LDocumentItem> _documents;

	public LDocuments() {
		_documents = new HashMap<String, LDocumentItem>();
	}

	public LDocumentItem getDocument(final String pUri)
	{
		synchronized (_documents) {
			return _documents.get(pUri);
		}
	}

	public LDocumentItem createDocument(final TextDocumentItem pTextDocumentItem)
	{
		final LDocumentItem document = new LDocumentItem(pTextDocumentItem);
		return document;
	}

	public LDocumentItem onDidChangeTextDocument(final DidChangeTextDocumentParams params)
	{
		LDocumentItem document = null;
		synchronized (_documents) {
			document = getDocument(params.getTextDocument());
			if (document != null) {
				document.setVersion(params.getTextDocument().getVersion());
				document.update(params.getContentChanges());
			}
		}
		return document;
	}

	public LDocumentItem onDidOpenTextDocument(final DidOpenTextDocumentParams params)
	{
		LDocumentItem document = null;
		final TextDocumentItem item = params.getTextDocument();
		synchronized (_documents) {
			document = createDocument(item);
			_documents.put(document.getUri(), document);
		}
		return document;
	}

	public LDocumentItem onDidCloseTextDocument(final DidCloseTextDocumentParams params)
	{
		LDocumentItem document = null;
		synchronized (_documents) {
			document = getDocument(params.getTextDocument());
			if (document != null) {
				_documents.remove(params.getTextDocument().getUri());
			}
		}
		return document;
	}

	private LDocumentItem getDocument(final TextDocumentIdentifier pTextDocumentIdentifier)
	{
		return _documents.get(pTextDocumentIdentifier.getUri());
	}
}
