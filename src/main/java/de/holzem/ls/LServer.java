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
package de.holzem.ls;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.eclipse.lsp4j.CompletionOptions;
import org.eclipse.lsp4j.InitializeParams;
import org.eclipse.lsp4j.InitializeResult;
import org.eclipse.lsp4j.ServerCapabilities;
import org.eclipse.lsp4j.TextDocumentSyncKind;
import org.eclipse.lsp4j.jsonrpc.Launcher;
import org.eclipse.lsp4j.launch.LSPLauncher;
import org.eclipse.lsp4j.services.LanguageClient;
import org.eclipse.lsp4j.services.LanguageClientAware;
import org.eclipse.lsp4j.services.LanguageServer;
import org.eclipse.lsp4j.services.TextDocumentService;
import org.eclipse.lsp4j.services.WorkspaceService;

import de.holzem.ls.services.LTextDocumentService;
import de.holzem.ls.services.LWorkspaceService;
import de.holzem.ls.services.LanguageServices;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LServer implements LanguageServer, LanguageClientAware
{
	private final TextDocumentService _textDocumentService;
	private final WorkspaceService _workspaceService;
	private final LanguageServices _languageServices;
	private LanguageClient _languageClient;
	private int errorCode = 1;

	public LServer() {
		_workspaceService = new LWorkspaceService();
		_languageServices = new LanguageServices();
		_textDocumentService = new LTextDocumentService(this);
	}

	private static final String[] COMPLETION_TRIGGER_CHARACTERS = { "_", "$", ".", //
			"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "o", "p", "q", "r", "s", "t", "u", "v",
			"w", "x", "y", "z", //
			"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "O", "P", "Q", "R", "S", "T", "U", "V",
			"W", "X", "Y", "Z", //
	};

	@Override
	public CompletableFuture<InitializeResult> initialize(final InitializeParams initializeParams)
	{
		// Initialize the InitializeResult for this LS.
		final InitializeResult initializeResult = new InitializeResult(new ServerCapabilities());
		// Set the capabilities of the LS to inform the client.
		initializeResult.getCapabilities().setTextDocumentSync(TextDocumentSyncKind.Full);
		final CompletionOptions completionOptions = new CompletionOptions();
		completionOptions.setTriggerCharacters(Arrays.asList(COMPLETION_TRIGGER_CHARACTERS));
		initializeResult.getCapabilities().setCompletionProvider(completionOptions);
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

	public LanguageServices getLanguageServices()
	{
		return _languageServices;
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
