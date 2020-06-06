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
package de.holzem.ls.language;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import de.holzem.ls.language.testutils.TestResource;

/**
 * LParserTest
 */
class LParserTest
{
	@Test
	void testSimple()
	{
		final String uri = "rexx/simple.rex";
		final String testResourceContent = TestResource.getContent(uri);
		final LModel lModel = LParser.INSTANCE.parse(uri, testResourceContent);
		assertThat(lModel.getText(), is(equalTo(testResourceContent)));
		final List<String> variableTexts = new ArrayList<String>();
		for (final LToken variable : lModel.getVariables()) {
			variableTexts.add(variable.getText());
		}
		assertThat(variableTexts, contains("info", "infoSum"));
		assertThat(variableTexts.size(), is(equalTo(2)));
	}

	@Test
	void testCall()
	{
		final String uri = "rexx/call.rex";
		final String testResourceContent = TestResource.getContent(uri);
		final LModel lModel = LParser.INSTANCE.parse(uri, testResourceContent);
		assertThat(lModel.getLabels().size(), is(equalTo(1)));
		assertThat(lModel.getLabels().get(0).getText(), is(equalTo("TESTPROC")));
	}
}