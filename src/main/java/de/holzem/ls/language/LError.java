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
 * LError
 */
public enum LError
{
	E_UNMATCHED_ENDCOMMENT("Unmatched end-of-comment punctuation"),
	E_UNMATCHED_STARTCOMMENT("Unmatched start-of-comment punctuation"), E_UNCLOSED_STRING("Unclosed string"),
	E_ILLEGAL_CHAR("Illegal character");

	private String _message;

	private LError(final String pMessage) {
		_message = pMessage;
	}

	public String getMessage()
	{
		return _message;
	}
}
