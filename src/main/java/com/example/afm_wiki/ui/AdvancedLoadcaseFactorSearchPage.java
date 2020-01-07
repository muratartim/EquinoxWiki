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

import com.example.afm_wiki.data.LoadcaseFactorInfo.LoadcaseFactorInfoType;
import com.example.afm_wiki.data.LoadcaseFactorSearchInput;
import com.example.afm_wiki.data.SearchItem;
import com.example.afm_wiki.task.AdvancedLoadcaseFactorSearch;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Class for advanced loadcase factor search page.
 *
 * @author Murat Artim
 * @date 23 Feb 2017
 * @time 17:26:31
 */
public class AdvancedLoadcaseFactorSearchPage extends VerticalLayout {

	/** Serial ID. */
	private static final long serialVersionUID = 1L;

	/** Search field index. */
	public static final int FILE_NAME = 0, SPECTRUM_NAME = 1, PILOT_POINT_NAME = 2, PROGRAM = 3, SECTION = 4, MISSION = 5, ISSUE = 6, DELIVERY_REF = 7, DESCRIPTION = 8;

	/** Search fields. */
	private final SearchField[] searchFields_;

	/** The owner page. */
	private final AdvancedSearch owner_;

	/** Shortcut listener. */
	private final ShortcutListener shortcutListener_;

	/**
	 * Creates advanced loadcase factor search page.
	 *
	 * @param owner
	 *            The owner page.
	 */
	public AdvancedLoadcaseFactorSearchPage(AdvancedSearch owner) {

		// set attributes
		owner_ = owner;

		// create page layout
		setSizeFull();
		setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

		// create layout for the search text field
		AbsoluteLayout searchLayout = new AbsoluteLayout();
		searchLayout.addStyleName("centeredPanel1");
		searchLayout.setWidth(675, Unit.PIXELS);
		searchLayout.setHeight(440, Unit.PIXELS);
		addComponent(searchLayout);
		setExpandRatio(searchLayout, 1);

		// create search text field
		searchFields_ = new SearchField[9];
		searchFields_[FILE_NAME] = new SearchField("Loadcase factors file name", 0, 0, searchLayout);
		searchFields_[SPECTRUM_NAME] = new SearchField("Spectrum name", 55, 0, searchLayout);
		searchFields_[PILOT_POINT_NAME] = new SearchField("Pilot point name", 110, 0, searchLayout);
		searchFields_[PROGRAM] = new SearchField("Aircraft program", 165, 0, searchLayout);
		searchFields_[SECTION] = new SearchField("Aircraft section", 220, 0, searchLayout);
		searchFields_[MISSION] = new SearchField("Fatigue mission", 0, 355, searchLayout);
		searchFields_[ISSUE] = new SearchField("Loadcase factors file issue", 55, 355, searchLayout);
		searchFields_[DELIVERY_REF] = new SearchField("Delivery reference", 110, 355, searchLayout);
		searchFields_[DESCRIPTION] = new SearchField("Description", 165, 355, searchLayout);

		// set action listeners to search fields
		shortcutListener_ = new ShortcutListener("", KeyCode.ENTER, null) {

			/** Serial ID. */
			private static final long serialVersionUID = 1L;

			@Override
			public void handleAction(Object sender, Object target) {
				performSearch();
			}
		};
		for (SearchField sf : searchFields_)
			sf.setActionListener(shortcutListener_);

		// create search button
		Button search = new Button("Search", new Button.ClickListener() {

			/** Serial ID. */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
				performSearch();
			}
		});
		search.setIcon(FontAwesome.ROCKET);
		search.addStyleName(ValoTheme.BUTTON_SMALL);
		search.addStyleName(ValoTheme.BUTTON_PRIMARY);
		search.setWidth(120, Unit.PIXELS);

		// create reset button
		Button reset = new Button("Reset", new Button.ClickListener() {

			/** Serial ID. */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
				for (SearchField s : searchFields_)
					s.reset();
			}
		});
		reset.setIcon(FontAwesome.FILE_O);
		reset.addStyleName(ValoTheme.BUTTON_SMALL);
		reset.addStyleName(ValoTheme.BUTTON_PRIMARY);
		reset.setWidth(120, Unit.PIXELS);

		// add buttons to layout
		searchLayout.addComponent(reset, "bottom: 3; left: 212;");
		searchLayout.addComponent(search, "bottom: 3; left: 342;");

	}

	/**
	 * Returns owner page.
	 *
	 * @return The owner page.
	 */
	public AdvancedSearch getOwner() {
		return owner_;
	}

	/**
	 * Removes all search field shortcut listeners.
	 */
	public void removeListeners() {
		for (SearchField sf : searchFields_)
			sf.removeShortcutListener(shortcutListener_);
	}

	/**
	 * Performs search.
	 */
	private void performSearch() {

		// create search input
		LoadcaseFactorSearchInput input = new LoadcaseFactorSearchInput();

		// add search items
		String name = searchFields_[FILE_NAME].getValue();
		if ((name != null) && !name.isEmpty())
			input.addInput(LoadcaseFactorInfoType.NAME, new SearchItem(name, searchFields_[FILE_NAME].getFilter()));
		String spectrumName = searchFields_[SPECTRUM_NAME].getValue();
		if ((spectrumName != null) && !spectrumName.isEmpty())
			input.addInput(LoadcaseFactorInfoType.SPECTRUM_NAME, new SearchItem(spectrumName, searchFields_[SPECTRUM_NAME].getFilter()));
		String ppName = searchFields_[PILOT_POINT_NAME].getValue();
		if ((ppName != null) && !ppName.isEmpty())
			input.addInput(LoadcaseFactorInfoType.PILOT_POINT_NAME, new SearchItem(ppName, searchFields_[PILOT_POINT_NAME].getFilter()));
		String acProgram = searchFields_[PROGRAM].getValue();
		if ((acProgram != null) && !acProgram.isEmpty())
			input.addInput(LoadcaseFactorInfoType.AC_PROGRAM, new SearchItem(acProgram, searchFields_[PROGRAM].getFilter()));
		String acSection = searchFields_[SECTION].getValue();
		if ((acSection != null) && !acSection.isEmpty())
			input.addInput(LoadcaseFactorInfoType.AC_SECTION, new SearchItem(acSection, searchFields_[SECTION].getFilter()));
		String fatMission = searchFields_[MISSION].getValue();
		if ((fatMission != null) && !fatMission.isEmpty())
			input.addInput(LoadcaseFactorInfoType.FAT_MISSION, new SearchItem(fatMission, searchFields_[MISSION].getFilter()));
		String issue = searchFields_[ISSUE].getValue();
		if ((issue != null) && !issue.isEmpty())
			input.addInput(LoadcaseFactorInfoType.ISSUE, new SearchItem(issue, searchFields_[ISSUE].getFilter()));
		String deliveryRef = searchFields_[DELIVERY_REF].getValue();
		if ((deliveryRef != null) && !deliveryRef.isEmpty())
			input.addInput(LoadcaseFactorInfoType.DELIVERY_REF, new SearchItem(deliveryRef, searchFields_[DELIVERY_REF].getFilter()));
		String description = searchFields_[DESCRIPTION].getValue();
		if ((description != null) && !description.isEmpty())
			input.addInput(LoadcaseFactorInfoType.DESCRIPTION, new SearchItem(description, searchFields_[DESCRIPTION].getFilter()));

		// no search items entered
		if (input.isEmpty()) {
			Notification.show("No search criteria entered. Please enter at least 1 search item to proceed.", Notification.Type.WARNING_MESSAGE);
			return;
		}

		// set engine settings
		owner_.getOwner().getSettings().setEngineSettings(input);

		// search
		owner_.getOwner().getOwner().submitTask(new AdvancedLoadcaseFactorSearch(owner_.getOwner().getOwner(), input));
	}
}
