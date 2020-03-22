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
package de.holzem.lsp.lsp4rexx.rexxscanner.testutils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * SourceFile is a helper class to convert a source file to a String.
 */
public class SourceFile
{
	/** For the parser it does not matter how the line break is represented */
	private static final String LINE_BREAK = "\n";
	private final String _filePath;

	public SourceFile(final String pFileName) {
		_filePath = "src/test/resources/" + pFileName;
	}

	public String getFileContent() throws FileNotFoundException
	{
		final StringBuilder contentBuilder = new StringBuilder();
		try (Stream<String> stream = Files.lines(Paths.get(_filePath), StandardCharsets.UTF_8)) {
			stream.forEach(s -> contentBuilder.append(s).append(LINE_BREAK));
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return contentBuilder.toString();
	}
}
