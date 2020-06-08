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

/**
 * LErrorType
 */
public enum LErrorType
{
	/** unmatched end comment */
	E_UNMATCHED_ENDCOMMENT("Unmatched end-of-comment punctuation"),
	/** unmatched start comment */
	E_UNCLOSED_COMMENT("Unclosed comment"),
	/** unclosed string */
	E_UNCLOSED_STRING("Unclosed string"),
	/** illegal character */
	E_ILLEGAL_CHAR("Illegal character"),
	/** unclosed do keyword */
	E_UNMATCHED_DO("Unmatched do keyword"),
	/** unmatched end keyword */
	E_UNMATCHED_END("Unmatched end keyword");

	private String _message;

	private LErrorType(final String pMessage) {
		_message = pMessage;
	}

	public String getMessage()
	{
		return _message;
	}
}
