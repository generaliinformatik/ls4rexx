/**
 *  Copyright (c) 2020 Generali Deutschland AG - Team Informatik
 *
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v2.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *  Contributors:
 *  Markus Holzem <markus.holzem@generali.com>
 */
package de.generali.dev.ls.services;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.eclipse.lsp4j.DidChangeConfigurationParams;
import org.eclipse.lsp4j.DidChangeWatchedFilesParams;
import org.eclipse.lsp4j.SymbolInformation;
import org.eclipse.lsp4j.WorkspaceSymbolParams;
import org.eclipse.lsp4j.services.WorkspaceService;

public class LWorkspaceService implements WorkspaceService
{
	@Override
	public CompletableFuture<List<? extends SymbolInformation>> symbol(
			final WorkspaceSymbolParams workspaceSymbolParams)
	{
		return null;
	}

	@Override
	public void didChangeConfiguration(final DidChangeConfigurationParams didChangeConfigurationParams)
	{
	}

	@Override
	public void didChangeWatchedFiles(final DidChangeWatchedFilesParams didChangeWatchedFilesParams)
	{
	}
}
