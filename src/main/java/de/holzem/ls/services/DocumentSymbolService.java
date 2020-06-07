/**
 *  Copyright (c) 2020 Markus Holzem
 *
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v2.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *  Contributors:
 *  Markus Holzem <markus@holzem.de>
 */
package de.holzem.ls.services;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.lsp4j.Location;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.Range;
import org.eclipse.lsp4j.ServerCapabilities;
import org.eclipse.lsp4j.SymbolInformation;
import org.eclipse.lsp4j.SymbolKind;
import org.eclipse.lsp4j.jsonrpc.CancelChecker;

import de.holzem.ls.language.LModel;
import de.holzem.ls.language.LToken;

/**
 * DocumentSymbolService
 */
public class DocumentSymbolService extends LService
{
	/**
	 * Constructor
	 */
	public DocumentSymbolService(final LServices pLServices, final ServerCapabilities pServerCapabilities) {
		super(pLServices);
		// provide document symbols
		pServerCapabilities.setDocumentSymbolProvider(true);
	}

	public List<SymbolInformation> doDocumentSymbol(final CancelChecker pCancelChecker, final LModel pLModel)
	{
		final List<SymbolInformation> list = new ArrayList<SymbolInformation>();
		addFileName(pLModel, list);
		addMethods(pCancelChecker, pLModel, list);
		return list;
	}

	private void addFileName(final LModel pLModel, final List<SymbolInformation> list)
	{
		final SymbolInformation si = new SymbolInformation();
		si.setKind(SymbolKind.File);
		si.setName(pLModel.getFileName());
		final Range range = new Range(new Position(0, 0), new Position(0, 0));
		final Location location = new Location(pLModel.getUri(), range);
		si.setLocation(location);
		list.add(si);
	}

	private void addMethods(final CancelChecker pCancelChecker, final LModel pLModel,
			final List<SymbolInformation> list)
	{
		for (final LToken label : pLModel.getLabels()) {
			if (pCancelChecker != null)
				pCancelChecker.checkCanceled();
			final SymbolInformation si = new SymbolInformation();
			si.setKind(SymbolKind.Method);
			si.setName(label.getText());
			final int line = label.getLine();
			final int column = label.getColumn();
			final int columnEnd = label.getColumn() + label.getLength();
			final Range range = new Range(new Position(line, column), new Position(line, columnEnd));
			final Location location = new Location(pLModel.getUri(), range);
			si.setLocation(location);
			list.add(si);
		}
	}
}
