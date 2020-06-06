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
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.startsWith;

import java.util.List;

import org.junit.jupiter.api.Test;

import de.holzem.ls.language.testutils.LTokenBuilder;
import de.holzem.ls.language.testutils.TestResource;
import nl.jqno.equalsverifier.EqualsVerifier;

/**
 * LModelTest
 */
class LModelTest
{
	@Test
	void testBuilder()
	{
		final LModel.LModelBuilder modelBuilder = LModel.builder()//
				.uri("test.rex")//
				.labels(null) //
				.variables(null);
		assertThat(modelBuilder.toString(), containsString("test.rex"));
	}

	@Test
	void testUri()
	{
		final LModel file = LModel.builder()//
				.uri("test.rex")//
				.labels(null) //
				.variables(null) //
				.build();
		assertThat(file.getUri(), is(equalTo("test.rex")));
		assertThat(file.getLabels(), is(nullValue()));
		assertThat(file.getVariables(), is(nullValue()));
		assertThat(file.getTokens(), is(nullValue()));
		assertThat(file.toString(), containsString("test.rex"));
	}

	@Test
	void testSingleComment()
	{
		final List<LToken> tokens = new LTokenBuilder() //
				.createComment("/* REXX */") //
				.build();
		final LModel file = new LModel.LModelBuilder() //
				.uri("test.rex") //
				.tokens(tokens) //
				.build();
		assertThat(file.getText(), is(equalTo("/* REXX */")));
	}

	@Test
	void testSmallRexx()
	{
		final List<LToken> tokens = new LTokenBuilder() //
				.createComment("/* REXX */") //
				.createWhiteSpace("\r\n") //
				.createToken(LTokenType.FUNCTION, "exit") //
				.createWhiteSpace(" ") //
				.createToken(LTokenType.NUMBER, "0") //
				.build();
		final LModel file = new LModel.LModelBuilder() //
				.uri("test.rex") //
				.tokens(tokens) //
				.build();
		assertThat(file.getText(), is(equalTo("/* REXX */\r\nexit 0")));
	}

	@Test
	void testEquals()
	{
		EqualsVerifier.forClass(LModel.class).verify();
	}

	@Test
	void testLocate()
	{
		final String uri = "rexx/simple.rex";
		final String testResourceContent = TestResource.getContent(uri);
		final LModel lModel = LParser.INSTANCE.parse(uri, testResourceContent);
		/* First token on line 1 */
		final int pos_0_0 = lModel.locateToken(0, 0);
		assertThat(lModel.getTokens().get(pos_0_0).getText(), is(equalTo("/* REXX */")));
		/* First token on line 3 */
		final int pos_2_0 = lModel.locateToken(2, 0);
		assertThat(lModel.getTokens().get(pos_2_0).getText(), is(equalTo("/* Initialize */")));
		/* First token on line 15 */
		final int pos_14_0 = lModel.locateToken(14, 0);
		assertThat(lModel.getTokens().get(pos_14_0).getText(), is(equalTo("exit")));
		/* First token on line 15 */
		final int pos_14_6 = lModel.locateToken(14, 6);
		assertThat(lModel.getTokens().get(pos_14_6).getType(), is(equalTo(LTokenType.WHITESPACE)));
		/* Past last token on line 16 */
		final int pos_15_0 = lModel.locateToken(15, 0);
		assertThat(lModel.getTokens().get(pos_15_0).getType(), is(equalTo(LTokenType.WHITESPACE)));
		/* check boundaries and inside of infoSum */
		final int pos_11_10 = lModel.locateToken(11, 10);
		assertThat(lModel.getTokens().get(pos_11_10).getText(), is(equalTo("infoSum")));
		final int pos_11_13 = lModel.locateToken(11, 13);
		assertThat(lModel.getTokens().get(pos_11_13).getText(), is(equalTo("infoSum")));
		final int pos_11_16 = lModel.locateToken(11, 16);
		assertThat(lModel.getTokens().get(pos_11_16).getText(), is(equalTo("infoSum")));
		final int pos_11_17 = lModel.locateToken(11, 17);
		assertThat(lModel.getTokens().get(pos_11_17).getType(), is(equalTo(LTokenType.WHITESPACE)));
	}

	@Test
	void testErrors()
	{
		final String uri = "rexx/errors.rex";
		final String testResourceContent = TestResource.getContent(uri);
		final LModel lModel = LParser.INSTANCE.parse(uri, testResourceContent);
		// Basic error informatio
		final LErrors errors = lModel.getErrors();
		assertThat(errors.hasErrors(), is(true));
		assertThat(errors.getNumberOfErrors(), is(equalTo(5)));
		// first error is unclosed string
		final LError error1 = errors.getError(0);
		assertThat(error1.getErrorType(), is(equalTo(LErrorType.E_ILLEGAL_CHAR)));
		assertThat(error1.getToken().getText(), is(equalTo("{")));
		// second error is unclosed string
		final LError error2 = errors.getError(1);
		assertThat(error2.getErrorType(), is(equalTo(LErrorType.E_UNMATCHED_ENDCOMMENT)));
		assertThat(error2.getToken().getText(), is(equalTo("*/")));
		// first error is unclosed string
		final LError error3 = errors.getError(2);
		assertThat(error3.getErrorType(), is(equalTo(LErrorType.E_UNCLOSED_STRING)));
		assertThat(error3.getToken().getText(), is(equalTo("\"open string")));
		// second error is unclosed string
		final LError error4 = errors.getError(3);
		assertThat(error4.getErrorType(), is(equalTo(LErrorType.E_UNCLOSED_STRING)));
		assertThat(error4.getToken().getText(), is(equalTo("'open string")));
		// third error is unclosed comment
		final LError error5 = errors.getError(4);
		assertThat(error5.getErrorType(), is(equalTo(LErrorType.E_UNCLOSED_COMMENT)));
		assertThat(error5.getToken().getText(), startsWith("/* comment without end"));
	}
}
