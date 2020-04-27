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
package de.holzem.lsp.lsp4rexx.rexxscanner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

/**
 * RexxTokenTest
 */
class RexxTokenTest
{
	@Test
	void test()
	{
		final RexxToken token = new RexxToken(TokenType.IDENTIFIER, "ident", 5, 10, 60L);
		assertThat(token.getText(), is(equalTo("ident")));
		assertThat(token.getLine(), is(equalTo(5)));
		assertThat(token.getColumn(), is(equalTo(10)));
		assertThat(token.getCharBegin(), is(equalTo(60L)));
		assertThat(token.getCharEnd(), is(equalTo(65L)));
		assertThat(token.toString(), containsString("ident"));
	}
}
