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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * LErrors
 */
public class LErrors
{
	private final List<LError> _errors = new ArrayList<>();

	public void addError(final LError pLError)
	{
		_errors.add(pLError);
	}

	public List<LError> getErrors()
	{
		return Collections.unmodifiableList(_errors);
	}
}
