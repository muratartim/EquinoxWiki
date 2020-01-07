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
package com.example.afm_wiki.data;

/**
 * Class for search item.
 *
 * @author Murat Artim
 * @date May 3, 2014
 * @time 8:03:39 PM
 */
public class SearchItem {

	/** Value of search item. */
	private final Object value_;

	/** Criteria of search item. */
	private final String criteria_;

	/**
	 * Creates search item.
	 *
	 * @param value
	 *            Value of search item.
	 * @param criteria
	 *            Criteria of search item.
	 */
	public SearchItem(Object value, String criteria) {
		value_ = value;
		criteria_ = criteria;
	}

	/**
	 * Returns the value of search item.
	 *
	 * @return The value of search item.
	 */
	public Object getValue() {
		return value_;
	}

	/**
	 * Returns the criteria of search item.
	 *
	 * @return The criteria of search item.
	 */
	public String getCriteria() {
		return criteria_;
	}
}
