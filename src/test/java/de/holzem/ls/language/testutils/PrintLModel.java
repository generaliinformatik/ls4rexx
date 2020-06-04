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
package de.holzem.ls.language.testutils;

import de.holzem.ls.language.LModel;
import de.holzem.ls.language.LParser;

/**
 * PrintLModel
 */
public class PrintLModel
{
	public static void main(final String[] arg)
	{
		if (arg.length == 0) {
			System.out.println("Usage: PrintLModel <filename>");
			System.exit(1);
		}
		final String filename = arg[0];
		final String content = TestResource.getContent(filename);
		final LModel lModel = LParser.INSTANCE.parse(filename, content, null);
		System.out.println(lModel);
	}
}
