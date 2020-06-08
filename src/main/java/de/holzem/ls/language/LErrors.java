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
import java.util.List;

/**
 * LErrors
 */
public class LErrors
{
	private final List<LError> _errors = new ArrayList<LError>();

	public void addError(final LErrorType pLErrorType, final LToken pToken)
	{
		final LError error = new LError(pLErrorType, pToken);
		_errors.add(error);
	}

	public void addAllErrors(final LErrors pLErrors)
	{
		_errors.addAll(pLErrors._errors);
	}

	public LError getError(final int pIndex)
	{
		return _errors.get(pIndex);
	}

	public int getNumberOfErrors()
	{
		return _errors.size();
	}

	public boolean hasErrors()
	{
		return (getNumberOfErrors() != 0);
	}

	@Override
	public String toString()
	{
		return "LErrors [_errors=" + _errors + "]";
	}
}
