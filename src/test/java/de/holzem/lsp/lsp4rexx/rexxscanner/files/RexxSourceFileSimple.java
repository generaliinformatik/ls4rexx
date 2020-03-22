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
package de.holzem.lsp.lsp4rexx.rexxscanner.files;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.io.FileNotFoundException;
import java.util.List;

import org.junit.jupiter.api.Test;

import de.holzem.lsp.lsp4rexx.rexxscanner.RexxSourceFileInfo;
import de.holzem.lsp.lsp4rexx.rexxscanner.testutils.SourceFile;

/**
 * RexxSourceFileSimple tests <code>simple.rex</code>.
 */
public class RexxSourceFileSimple
{
	@Test
	public void testSimple() throws FileNotFoundException
	{
		final SourceFile sourceFile = new SourceFile("simple.rex");
		final RexxSourceFileInfo rexxSourceInfo = new RexxSourceFileInfo();
		rexxSourceInfo.parse(sourceFile.getFileContent());
		final List<String> variables = rexxSourceInfo.getVariables();
		assertThat(variables.size(), is(equalTo(2)));
		assertThat(variables.get(0), is(equalTo("info")));
		assertThat(variables.get(1), is(equalTo("sum")));
	}
}
