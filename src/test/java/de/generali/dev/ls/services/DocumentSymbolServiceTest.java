/**
 *  Copyright (c) 2020 Generali Deutschland AG - Team Informatik
 *
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v2.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *  Contributors:
 *  Markus Holzem <markus.holzem@generali.com>
 */
package de.generali.dev.ls.services;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.util.List;

import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.Range;
import org.eclipse.lsp4j.ServerCapabilities;
import org.eclipse.lsp4j.SymbolInformation;
import org.junit.jupiter.api.Test;

import de.generali.dev.ls.language.LModel;
import de.generali.dev.ls.language.LParser;
import de.generali.dev.ls.language.testutils.TestResource;

/**
 * DocumentSymbolServiceTest
 */
class DocumentSymbolServiceTest
{
	@Test
	void testDocumentSymbolLabels()
	{
		final String[] expectedLabels = { //
				"document-symbol-service-labels.rex", //
				"TESTPROC1", "TESTPROC2", "TESTPROC3", "TESTPROC4" };
		final int[] expectedLines = { //
				0, //
				4, 7, 10, 13 };
		final int[] expectedEndColumn = { //
				0, //
				9, 9, 9, 9 };
		final String uri = "rexx/document-symbol-service-labels.rex";
		final String testResourceContent = TestResource.getContent(uri);
		final LModel lModel = LParser.INSTANCE.parse(uri, testResourceContent);
		final LServices services = new LServices(new ServerCapabilities());
		final List<SymbolInformation> list = services.doDocumentSymbol(null, lModel);
		for (int i = 0; i < list.size(); ++i) {
			assertThat(list.get(i).getName(), is(equalTo(expectedLabels[i])));
			final Range range = list.get(i).getLocation().getRange();
			final Position start = range.getStart();
			final Position end = range.getEnd();
			assertThat(list.get(i).getName(), is(equalTo(expectedLabels[i])));
			assertThat(start.getLine(), is(equalTo(expectedLines[i])));
			assertThat(end.getLine(), is(equalTo(expectedLines[i])));
			assertThat(start.getCharacter(), is(equalTo(0)));
			assertThat(end.getCharacter(), is(equalTo(expectedEndColumn[i])));
		}
	}
}
