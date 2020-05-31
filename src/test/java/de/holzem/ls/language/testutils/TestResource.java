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
package de.holzem.ls.language.testutils;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import lombok.extern.slf4j.Slf4j;

/**
 * TestResource is a helper class to convert a source file to a String.
 */
@Slf4j
public class TestResource
{
	/** For the parser it does not matter how the line break is represented */
	private static final String LINE_BREAK = "\n";
	private final String _filePath;

	private TestResource(final String pFileName) {
		_filePath = "src/test/resources/" + pFileName;
	}

	private String getContent()
	{
		final StringBuilder contentBuilder = new StringBuilder();
		try (Stream<String> stream = Files.lines(Paths.get(_filePath), StandardCharsets.UTF_8)) {
			stream.forEach(s -> contentBuilder.append(s).append(LINE_BREAK));
		} catch (final FileNotFoundException exc) {
			log.error("test resource not found {}", _filePath, exc);
			fail("test resource not found " + _filePath);
		} catch (final IOException exc) {
			log.error("IOException on {}", _filePath, exc);
			fail("IOException on " + _filePath + ": " + exc.getMessage());
		}
		return contentBuilder.toString();
	}

	public static String getContent(final String pTestResource)
	{
		final TestResource testResource = new TestResource(pTestResource);
		final String content = testResource.getContent();
		return content;
	}
}
