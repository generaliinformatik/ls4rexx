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
		if (Paths.get(pFileName).isAbsolute()) {
			_filePath = pFileName;
		} else {
			_filePath = "src/test/resources/" + pFileName;
		}
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
