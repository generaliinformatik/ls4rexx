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
				{}, // completion in comment
				{ "var1", "var2", "var3" }, // show all variables anyway
				{ "var1", "var2", "var3" }, // show all variables anyway
				{ "var1", "var2", "var3" }, // show all variables anyway
				{ "var1", "var2", "var3" } // show all variables anyway
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
