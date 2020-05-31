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

/**
 * LError
 */
public enum LError {
	E_UNMATCHED_ENDCOMMENT("Unmatched end-of-comment punctuation"),
	E_UNMATCHED_STARTCOMMENT("Unmatched start-of-comment punctuation"),
	E_UNCLOSED_STRING("Unclosed string"),
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
