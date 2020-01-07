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
package com.example.afm_wiki.ui;

import com.example.afm_wiki.WikiUI;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.VerticalLayout;

/**
 * Class for results view.
 *
 * @author Murat Artim
 * @date 8 Mar 2017
 * @time 11:26:05
 */
public class ResultsView extends VerticalLayout implements View {

	/** Serial ID. */
	private static final long serialVersionUID = 1L;

	/** The owner UI. */
	private final WikiUI owner_;

	/** Search results page. */
	private final SearchResults searchResults_;

	/**
	 * Creates results view.
	 *
	 * @param owner
	 *            The owner user interface.
	 */
	public ResultsView(WikiUI owner) {

		// set owner
		owner_ = owner;

		// setup layout
		addStyleName("masterLayout");
		setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		setSizeFull();

		// create search results
		searchResults_ = new SearchResults(this);
		searchResults_.addStyleName("paperStack");
		searchResults_.setWidth("800px");
		searchResults_.setHeight("560px");
		addComponent(searchResults_);
	}

	/**
	 * Returns the owner UI.
	 *
	 * @return The owner UI.
	 */
	public WikiUI getOwner() {
		return owner_;
	}

	/**
	 * Returns the search results page.
	 *
	 * @return The search results page.
	 */
	public SearchResults getSearchResults() {
		return searchResults_;
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// no implementation
	}
}
