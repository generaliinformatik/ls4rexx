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