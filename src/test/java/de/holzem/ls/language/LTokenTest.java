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
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

/**
 * LTokenTest
 */
class LTokenTest
{
	@Test
	void test()
	{
		final LToken token = new LToken(LTokenType.IDENTIFIER, "ident", 5, 10, 60L);
		assertThat(token.getText(), is(equalTo("ident")));
		assertThat(token.getLine(), is(equalTo(5)));
		assertThat(token.getColumn(), is(equalTo(10)));
		assertThat(token.getCharBegin(), is(equalTo(60L)));
		assertThat(token.getCharEnd(), is(equalTo(65L)));
		assertThat(token.toString(), containsString("ident"));
	}
}
