/*
 * Copyright 2018 Murat Artim (muratartim@gmail.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.afm_wiki.utility;

/**
 * Enumeration for AFM database connection parameters.
 *
 * @author Murat Artim
 * @date 27 Feb 2017
 * @time 14:54:23
 */
public enum AFMDatabaseConnection {

	/** Parameter. */
	// HOSTNAME("44.199.253.170"), PORT("1527"), PATH("\\\\de-vfiler128.eu.airbus.corp\\ST_fatigue_AIRBUS_ALL_AFM\\Equinox_Spectra_Database"), USERNAME("aurora"), PASSWORD("17891917");
	HOSTNAME("localhost"), PORT("1527"), PATH("/Users/aurora/Documents/Developer/EclipseWorkspace/Equinox/globalDatabase"), USERNAME("aurora"), PASSWORD("17891917");

	/** Parameter value. */
	private final String value_;

	/**
	 * Creates AFM database connection parameter.
	 *
	 * @param value
	 *            Parameter value.
	 */
	AFMDatabaseConnection(String value) {
		value_ = value;
	}

	/**
	 * Returns the value of parameter.
	 *
	 * @return The value of parameter.
	 */
	public String getValue() {
		return value_;
	}
}
