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
 *
 * SocketLauncher for Debugging copied from
 * https://github.com/angelozerr/lemminx/blob/master/org.eclipse.lemminx/src/main/java/org/eclipse/lemminx/XMLServerSocketLauncher.java
 * released under Eclipse Public License v2.0 http://www.eclipse.org/legal/epl-v20.html
 *
 * @author Dennis Huebner <dennis.huebner@gmail.com> - Initial contribution and API
 */
package de.holzem.lsp.lsp4rexx;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.Channels;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.eclipse.lsp4j.jsonrpc.Launcher;
import org.eclipse.lsp4j.jsonrpc.MessageConsumer;
import org.eclipse.lsp4j.services.LanguageClient;

public class RexxServerSocketLauncher {

	private static final int DEFAULT_PORT = 5_008;

	/**
	 * Calls {@link #launch(String[])}
	 *
	 * @param args
	 *            standard launch arguments. may contain <code>--port</code> argument to change the default port 5008
	 * @throws Exception
	 *             any communication exception
	 */
	public static void main(final String[] args) throws Exception
	{
		new RexxServerSocketLauncher().launch(args);
	}

	/**
	 * Launches {@link RexxLanguageServer} using asynchronous server-socket channel and makes it accessible through the
	 * JSON RPC protocol defined by the LSP.
	 *
	 * @param args
	 *            standard launch arguments. may contain <code>--port</code> argument to change the default port 5008
	 * @throws Exception
	 *             any communication exception
	 */
	public void launch(final String[] args) throws Exception
	{
		final AsynchronousServerSocketChannel _open = AsynchronousServerSocketChannel.open();
		final int _port = getPort(args);
		final InetSocketAddress _inetSocketAddress = new InetSocketAddress("0.0.0.0", _port);
		final AsynchronousServerSocketChannel serverSocket = _open.bind(_inetSocketAddress);
		while (true) {
			final AsynchronousSocketChannel socketChannel = serverSocket.accept().get();
			final InputStream in = Channels.newInputStream(socketChannel);
			final OutputStream out = Channels.newOutputStream(socketChannel);
			final ExecutorService executorService = Executors.newCachedThreadPool();
			final RexxLanguageServer languageServer = new RexxLanguageServer();
			final Launcher<LanguageClient> launcher = Launcher.createIoLauncher(languageServer, LanguageClient.class,
					in, out, executorService, (final MessageConsumer it) -> {
						return it;
					});
			languageServer.connect(launcher.getRemoteProxy());
			launcher.startListening();
		}
	}

	private int getPort(final String... args)
	{
		for (int i = 0; (i < (args.length - 1)); i++) {
			final String _get = args[i];
			final boolean _equals = Objects.equals(_get, "--port");
			if (_equals) {
				return Integer.parseInt(args[(i + 1)]);
			}
		}
		return DEFAULT_PORT;
	}
}
