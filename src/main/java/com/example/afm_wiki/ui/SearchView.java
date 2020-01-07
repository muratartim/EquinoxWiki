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

import org.vaadin.virkki.paperstack.PaperStack.PageChangeEvent;
import org.vaadin.virkki.paperstack.PaperStack.PageChangeListener;

import com.example.afm_wiki.WikiUI;
import com.example.afm_wiki.utility.MyPaperStack;
import com.example.afm_wiki.utility.MyPaperStack.Paper;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.VerticalLayout;

/**
 * Class for search view.
 *
 * @author Murat Artim
 * @date 8 Mar 2017
 * @time 10:53:12
 */
public class SearchView extends VerticalLayout implements View {

	/** Serial ID. */
	private static final long serialVersionUID = 1L;

	/** The owner UI. */
	private final WikiUI owner_;

	/** Advanced search page. */
	private final AdvancedSearch advancedSearch_;

	/** Basic search page. */
	private final BasicSearch basicSearch_;

	/** Settings dialog. */
	private final SettingsDialog settings_;

	/**
	 * Creates search view.
	 *
	 * @param owner
	 *            The owner user interface.
	 */
	public SearchView(WikiUI owner) {

		// set owner
		owner_ = owner;

		// create settings dialog
		settings_ = new SettingsDialog(this);

		// setup layout
		addStyleName("masterLayout");
		setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		setSizeFull();

		// create paper stack
		MyPaperStack paperStack = new MyPaperStack();
		paperStack.addStyleName("paperStack");
		paperStack.setWidth("800px");
		paperStack.setHeight("560px");
		addComponent(paperStack);

		// create start page and add it to paper stack
		basicSearch_ = new BasicSearch(this);
		paperStack.addPaper(basicSearch_, "black");

		// create advanced search page and add it to paper stack
		advancedSearch_ = new AdvancedSearch(this);
		paperStack.addPaper(advancedSearch_, "black");

		// add page listener to paper stack
		paperStack.addListener(new PageChangeListener() {

			/** Serial ID. */
			private static final long serialVersionUID = 1L;

			@Override
			public void pageChange(PageChangeEvent event) {
				Paper current = paperStack.getCurrentPaper();
				if (current.equals(advancedSearch_)) {
					basicSearch_.hidden();
					advancedSearch_.shown();
				}
				else if (current.equals(basicSearch_)) {
					advancedSearch_.hidden();
					basicSearch_.shown();
				}
			}
		});
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
	 * Returns the settings dialog.
	 * 
	 * @return The settings dialog.
	 */
	public SettingsDialog getSettings() {
		return settings_;
	}

	/**
	 * Returns the basic search page.
	 *
	 * @return The basic search page.
	 */
	public BasicSearch getBasicSearchPage() {
		return basicSearch_;
	}

	/**
	 * Returns the advanced search page.
	 *
	 * @return The advanced search page.
	 */
	public AdvancedSearch getAdvancedSearchPage() {
		return advancedSearch_;
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// no implementation
	}
}
