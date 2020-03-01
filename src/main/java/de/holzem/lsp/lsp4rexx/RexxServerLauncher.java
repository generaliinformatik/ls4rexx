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

import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.eclipse.lsp4j.jsonrpc.Launcher;
import org.eclipse.lsp4j.launch.LSPLauncher;
import org.eclipse.lsp4j.services.LanguageClient;

/**
 * Launcher for REXX language server.
 */
public class RexxServerLauncher {
	public static void main(final String[] args) throws ExecutionException, InterruptedException
	{
		// As we are using system std io channels
		// we need to reset and turn off the logging globally
		// So our client->server communication doesn't get interrupted.
		LogManager.getLogManager().reset();
		final Logger globalLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
		globalLogger.setLevel(Level.OFF);
		// start the language server
		startServer(System.in, System.out);
	}

	/**
	 * Start the language server.
	 *
	 * @param in
	 *            System Standard input stream
	 * @param out
	 *            System standard output stream
	 * @throws ExecutionException
	 *             Unable to start the server
	 * @throws InterruptedException
	 *             Unable to start the server
	 */
	private static void startServer(final InputStream in, final OutputStream out)
			throws ExecutionException, InterruptedException
	{
		// Initialize the RexxLanguageServer
		final RexxLanguageServer helloLanguageServer = new RexxLanguageServer();
		// Create JSON RPC launcher for RexxLanguageServer instance.
		final Launcher<LanguageClient> launcher = LSPLauncher.createServerLauncher(helloLanguageServer, in, out);
		// Get the client that request to launch the LS.
		final LanguageClient client = launcher.getRemoteProxy();
		// Set the client to language server
		helloLanguageServer.connect(client);
		// Start the listener for JsonRPC
		final Future<?> startListening = launcher.startListening();
		// Get the computed result from LS.
		startListening.get();
	}
}
