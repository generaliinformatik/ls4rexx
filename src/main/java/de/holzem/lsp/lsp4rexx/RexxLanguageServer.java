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
package de.holzem.lsp.lsp4rexx;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

import org.eclipse.lsp4j.CompletionOptions;
import org.eclipse.lsp4j.InitializeParams;
import org.eclipse.lsp4j.InitializeResult;
import org.eclipse.lsp4j.ServerCapabilities;
import org.eclipse.lsp4j.TextDocumentSyncKind;
import org.eclipse.lsp4j.services.LanguageClient;
import org.eclipse.lsp4j.services.LanguageClientAware;
import org.eclipse.lsp4j.services.LanguageServer;
import org.eclipse.lsp4j.services.TextDocumentService;
import org.eclipse.lsp4j.services.WorkspaceService;

import de.holzem.lsp.lsp4rexx.services.LanguageServices;

public class RexxLanguageServer implements LanguageServer, LanguageClientAware
{
	private final TextDocumentService _textDocumentService;
	private final WorkspaceService _workspaceService;
	private final LanguageServices _languageServices;
	private LanguageClient _languageClient;
	private int errorCode = 1;

	public RexxLanguageServer() {
		_workspaceService = new RexxWorkspaceService();
		_languageServices = new LanguageServices();
		_textDocumentService = new RexxTextDocumentService(this);
	}

	private static final String[] COMPLETION_TRIGGER_CHARACTERS = { "_", "$", //
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
}
