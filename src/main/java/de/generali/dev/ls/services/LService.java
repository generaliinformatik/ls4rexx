/**
 *  Copyright (c) 2020 Generali Deutschland AG - Team Informatik
 *
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v2.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *  Contributors:
 *  Markus Holzem <markus.holzem@generali.com>
 */
package de.generali.dev.ls.services;

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