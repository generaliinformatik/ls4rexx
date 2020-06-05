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
package de.holzem.ls.services;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.lsp4j.CompletionItem;
import org.eclipse.lsp4j.CompletionList;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.ServerCapabilities;
import org.junit.jupiter.api.Test;

import de.holzem.ls.language.LModel;
import de.holzem.ls.language.LParser;
import de.holzem.ls.language.testutils.TestResource;

/**
 * DocumentSymbolServiceTest
 */
class CompletionServiceTest
{
	@Test
	void testCompletionVariables()
	{
		final String uri = "rexx/completion-service-variables.rex";
		final String testResourceContent = TestResource.getContent(uri);
		final LModel lModel = LParser.INSTANCE.parse(uri, testResourceContent);
		final LServices services = new LServices(new ServerCapabilities());
		final Position[] testpositions = { //
				new Position(0, 0), // empty list
				new Position(1, 7), // nothing like var defined yet
				new Position(3, 7), // var1 defined on line before
				new Position(5, 7), // var2 defined on line before
				new Position(7, 7) // var3 defined on line before
		};
		final String[][] varlist = { //
				{}, //
				{}, //
				{ "var1" }, //
				{ "var1", "var2" }, //
				{ "var1", "var2", "var3" } //
		};
		for (int i = 0; i < testpositions.length; ++i) {
			final Position position = testpositions[i];
			final CompletionList list = services.doComplete(null, lModel, position);
			final List<CompletionItem> items = list.getItems();
			final List<String> foundlist = new ArrayList<String>();
			for (final CompletionItem item : items) {
				foundlist.add(item.getInsertText());
			}
			if (varlist[i].length == 0) {
				assertThat(foundlist, is(empty()));
			} else {
				assertThat(foundlist, contains(varlist[i]));
			}
		}
	}
}
