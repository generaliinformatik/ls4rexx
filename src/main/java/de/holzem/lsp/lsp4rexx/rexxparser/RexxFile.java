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
package de.holzem.lsp.lsp4rexx.rexxparser;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.lsp4j.jsonrpc.CancelChecker;

import de.holzem.lsp.lsp4rexx.rexxscanner.RexxToken;
import lombok.Builder;
import lombok.Value;

/**
 * RexxFile accumulates all information gained from the RexxParser.
 */
@Value
@Builder
public final class RexxFile
{
	private final String uri;
	private final List<RexxToken> tokens;
	private final List<String> variables;
	private final List<String> labels;
	private final CancelChecker cancelChecker;

	public String getText()
	{
		final String fileText = tokens.stream().map(token -> token.getText()).collect(Collectors.joining());
		return fileText;
	}
}
