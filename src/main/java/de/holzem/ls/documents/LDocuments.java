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
 *  Markus Holzem <markus@holzem.de> - refactoring to code style
 *
 *  Originated from
 *  https://github.com/angelozerr/lemminx/blob/master/org.eclipse.lemminx/src/main/java/org/eclipse/lemminx/commons/TextDocuments.java
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
