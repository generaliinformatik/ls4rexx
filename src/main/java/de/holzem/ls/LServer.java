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
 *  Markus Holzem <markus@holzem.de> - merge server and launcher, cleanup
 *
 *  Originated from
 *  https://github.com/angelozerr/lemminx/blob/master/org.eclipse.lemminx/src/main/java/org/eclipse/lemminx/XMLServerLauncher.java
 *  https://github.com/angelozerr/lemminx/blob/master/org.eclipse.lemminx/src/main/java/org/eclipse/lemminx/XMLLanguageServer.java
 */
package de.holzem.ls;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.eclipse.lsp4j.InitializeParams;
import org.eclipse.lsp4j.InitializeResult;
import org.eclipse.lsp4j.ServerCapabilities;
import org.eclipse.lsp4j.jsonrpc.Launcher;
import org.eclipse.lsp4j.launch.LSPLauncher;
import org.eclipse.lsp4j.services.LanguageClient;
import org.eclipse.lsp4j.services.LanguageClientAware;
import org.eclipse.lsp4j.services.LanguageServer;
import org.eclipse.lsp4j.services.TextDocumentService;
import org.eclipse.lsp4j.services.WorkspaceService;

import de.holzem.ls.services.LServices;
import de.holzem.ls.services.LTextDocumentService;
import de.holzem.ls.services.LWorkspaceService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LServer implements LanguageServer, LanguageClientAware
{
	private final TextDocumentService _textDocumentService;
	private final WorkspaceService _workspaceService;
	private final LServices _lServices;
	private final ServerCapabilities _serverCapabilities;
	private LanguageClient _languageClient;
	private int errorCode = 1;

	public LServer() {
		_workspaceService = new LWorkspaceService();
		_serverCapabilities = new ServerCapabilities();
		_lServices = new LServices(_serverCapabilities);
		_textDocumentService = new LTextDocumentService(this);
	}

	@Override
	public CompletableFuture<InitializeResult> initialize(final InitializeParams initializeParams)
	{
		// ServerCapabilities is initialized by the registered LServices
		final InitializeResult initializeResult = new InitializeResult(_serverCapabilities);
		return CompletableFuture.supplyAsync(() -> initializeResult);
	}

	@Override
	public CompletableFuture<Object> shutdown()
	{
		// If shutdown request comes from client, set the error code to 0.
		errorCode = 0;
		return null;
	}

	@Override
	public void exit()
	{
		// Kill the LS on exit request from client.
		System.exit(errorCode);
	}

	@Override
	public TextDocumentService getTextDocumentService()
	{
		// Return the endpoint for language features.
		return _textDocumentService;
	}

	@Override
	public WorkspaceService getWorkspaceService()
	{
		// Return the endpoint for workspace functionality.
		return _workspaceService;
	}

	@Override
	public void connect(final LanguageClient languageClient)
	{
		// Get the client which started this LS.
		_languageClient = languageClient;
	}

	public LServices getLServices()
	{
		return _lServices;
	}

	public LanguageClient getLanguageClient()
	{
		return _languageClient;
	}

	/**
	 * Start the server
	 *
	 * @param args no arguments
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	public static void main(final String[] args)
	{
		// As we are using system std io channels
		// we need to reset and turn off the logging globally
		// So our client->server communication doesn't get interrupted.
		LogManager.getLogManager().reset();
		final Logger globalLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
		globalLogger.setLevel(Level.OFF);
		// Initialize the LServer
		final LServer languageServer = new LServer();
		// Create JSON RPC launcher for LServer instance.
		final Launcher<LanguageClient> launcher = LSPLauncher.createServerLauncher(languageServer, System.in,
				System.out);
		// Get the client that request to launch the LS.
		final LanguageClient client = launcher.getRemoteProxy();
		// Set the client to language server
		languageServer.connect(client);
		// Start the listener for JsonRPC
		final Future<?> startListening = launcher.startListening();
		// Get the computed result from LS.
		try {
			log.info("Server start listening");
			startListening.get();
		} catch (InterruptedException | ExecutionException exc) {
			// do nothing (end of asynch processing)
		}
		log.info("Server stopped");
	}
}
