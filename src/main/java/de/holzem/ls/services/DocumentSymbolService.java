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

	private void addVariable(final LModel pLModel, final List<SymbolInformation> list)
	{
		final SymbolInformation si = new SymbolInformation();
		si.setKind(SymbolKind.Variable);
		si.setName("Variable");
		final Range range = new Range(new Position(3, 0), new Position(3, 1));
		final Location location = new Location(pLModel.getUri(), range);
		si.setLocation(location);
		list.add(si);
	}

	private void addFunction(final LModel pLModel, final List<SymbolInformation> list)
	{
		final SymbolInformation si = new SymbolInformation();
		si.setKind(SymbolKind.Function);
		si.setName("Function");
		final Range range = new Range(new Position(3, 0), new Position(3, 1));
		final Location location = new Location(pLModel.getUri(), range);
		si.setLocation(location);
		list.add(si);
	}
}
