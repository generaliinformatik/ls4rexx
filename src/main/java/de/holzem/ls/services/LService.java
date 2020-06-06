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
package de.holzem.ls.services;

/**
 * LService
 */
public class LService
{
	protected final LServices _lServices;

	public LService(final LServices pLServices) {
		_lServices = pLServices;
	}

	public LServices getLanguageService()
	{
		return _lServices;
	}
}