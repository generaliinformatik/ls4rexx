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
package de.holzem.lsp.lsp4rexx.services;

/**
 * LanguageService
 */
public class LanguageService
{
	protected final LanguageServices _languageServices;

	public LanguageService(final LanguageServices pLanguageServices) {
		_languageServices = pLanguageServices;
	}

	public LanguageServices getLanguageService()
	{
		return _languageServices;
	}
}