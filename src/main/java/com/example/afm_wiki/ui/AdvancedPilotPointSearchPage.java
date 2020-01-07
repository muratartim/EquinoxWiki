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

import com.example.afm_wiki.data.PilotPointInfo.PilotPointInfoType;
import com.example.afm_wiki.data.PilotPointSearchInput;
import com.example.afm_wiki.data.SearchItem;
import com.example.afm_wiki.task.AdvancedPilotPointSearch;
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
 * Class for advanced pilot point search page.
 *
 * @author Murat Artim
 * @date 23 Feb 2017
 * @time 16:45:39
 */
public class AdvancedPilotPointSearchPage extends VerticalLayout {

	/** Serial ID. */
	private static final long serialVersionUID = 1L;

	/** Search field index. */
	public static final int PILOT_POINT_NAME = 0, SPECTRUM_NAME = 1, PROGRAM = 2, SECTION = 3, MISSION = 4, FRAME_RIB_POS = 5, STRINGER_POS = 6, DATA_SOURCE = 7, GENERATION_SOURCE = 8, DELIVERY_REF = 9, DESCRIPTION = 10, ISSUE = 11, MATERIAL_NAME = 12;

	/** Search fields. */
	private final SearchField[] searchFields_;

	/** The owner page. */
	private final AdvancedSearch owner_;

	/** Shortcut listener. */
	private final ShortcutListener shortcutListener_;

	/**
	 * Creates advanced pilot point search page.
	 *
	 * @param owner
	 *            The owner page.
	 */
	public AdvancedPilotPointSearchPage(AdvancedSearch owner) {

		// set attributes
		owner_ = owner;

		// create page layout
		setSizeFull();
		setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

		// create layout for the search text field
		AbsoluteLayout searchLayout = new AbsoluteLayout();
		searchLayout.addStyleName("centeredPanel2");
		searchLayout.setWidth(675, Unit.PIXELS);
		searchLayout.setHeight(440, Unit.PIXELS);
		addComponent(searchLayout);
		setExpandRatio(searchLayout, 1);

		// create search text field
		searchFields_ = new SearchField[13];
		searchFields_[PILOT_POINT_NAME] = new SearchField("Pilot point name", 0, 0, searchLayout);
		searchFields_[SPECTRUM_NAME] = new SearchField("Spectrum name", 55, 0, searchLayout);
		searchFields_[PROGRAM] = new SearchField("A/C program", 110, 0, searchLayout);
		searchFields_[SECTION] = new SearchField("A/C section", 165, 0, searchLayout);
		searchFields_[MISSION] = new SearchField("Fatigue mission", 220, 0, searchLayout);
		searchFields_[FRAME_RIB_POS] = new SearchField("Frame/rib position", 275, 0, searchLayout);
		searchFields_[STRINGER_POS] = new SearchField("Stringer position", 330, 0, searchLayout);
		searchFields_[DATA_SOURCE] = new SearchField("Data source", 0, 355, searchLayout);
		searchFields_[GENERATION_SOURCE] = new SearchField("Generation source", 55, 355, searchLayout);
		searchFields_[DELIVERY_REF] = new SearchField("Delivery reference", 110, 355, searchLayout);
		searchFields_[DESCRIPTION] = new SearchField("Description", 165, 355, searchLayout);
		searchFields_[ISSUE] = new SearchField("Pilot point issue", 220, 355, searchLayout);
		searchFields_[MATERIAL_NAME] = new SearchField("Material name", 275, 355, searchLayout);

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
		PilotPointSearchInput input = new PilotPointSearchInput();

		// add search items
		String pilotPointName = searchFields_[PILOT_POINT_NAME].getValue();
		if ((pilotPointName != null) && !pilotPointName.isEmpty())
			input.addInput(PilotPointInfoType.NAME, new SearchItem(pilotPointName, searchFields_[PILOT_POINT_NAME].getFilter()));
		String spectrumName = searchFields_[SPECTRUM_NAME].getValue();
		if ((spectrumName != null) && !spectrumName.isEmpty())
			input.addInput(PilotPointInfoType.SPECTRUM_NAME, new SearchItem(spectrumName, searchFields_[SPECTRUM_NAME].getFilter()));
		String program = searchFields_[PROGRAM].getValue();
		if ((program != null) && !program.isEmpty())
			input.addInput(PilotPointInfoType.AC_PROGRAM, new SearchItem(program, searchFields_[PROGRAM].getFilter()));
		String section = searchFields_[SECTION].getValue();
		if ((section != null) && !section.isEmpty())
			input.addInput(PilotPointInfoType.AC_SECTION, new SearchItem(section, searchFields_[SECTION].getFilter()));
		String mission = searchFields_[MISSION].getValue();
		if ((mission != null) && !mission.isEmpty())
			input.addInput(PilotPointInfoType.FAT_MISSION, new SearchItem(mission, searchFields_[MISSION].getFilter()));
		String framePos = searchFields_[FRAME_RIB_POS].getValue();
		if ((framePos != null) && !framePos.isEmpty())
			input.addInput(PilotPointInfoType.FRAME_RIB_POSITION, new SearchItem(framePos, searchFields_[FRAME_RIB_POS].getFilter()));
		String stringerPos = searchFields_[STRINGER_POS].getValue();
		if ((stringerPos != null) && !stringerPos.isEmpty())
			input.addInput(PilotPointInfoType.STRINGER_POSITION, new SearchItem(stringerPos, searchFields_[STRINGER_POS].getFilter()));
		String dataSource = searchFields_[DATA_SOURCE].getValue();
		if ((dataSource != null) && !dataSource.isEmpty())
			input.addInput(PilotPointInfoType.DATA_SOURCE, new SearchItem(dataSource, searchFields_[DATA_SOURCE].getFilter()));
		String genSource = searchFields_[GENERATION_SOURCE].getValue();
		if ((genSource != null) && !genSource.isEmpty())
			input.addInput(PilotPointInfoType.GENERATION_SOURCE, new SearchItem(genSource, searchFields_[GENERATION_SOURCE].getFilter()));
		String deliveryRef = searchFields_[DELIVERY_REF].getValue();
		if ((deliveryRef != null) && !deliveryRef.isEmpty())
			input.addInput(PilotPointInfoType.DELIVERY_REF_NUM, new SearchItem(deliveryRef, searchFields_[DELIVERY_REF].getFilter()));
		String description = searchFields_[DESCRIPTION].getValue();
		if ((description != null) && !description.isEmpty())
			input.addInput(PilotPointInfoType.DESCRIPTION, new SearchItem(description, searchFields_[DESCRIPTION].getFilter()));
		String issue = searchFields_[ISSUE].getValue();
		if ((issue != null) && !issue.isEmpty())
			input.addInput(PilotPointInfoType.ISSUE, new SearchItem(issue, searchFields_[ISSUE].getFilter()));
		String materialName = searchFields_[MATERIAL_NAME].getValue();
		if ((materialName != null) && !materialName.isEmpty())
			input.addInput(PilotPointInfoType.MATERIAL_NAME, new SearchItem(materialName, searchFields_[MATERIAL_NAME].getFilter()));

		// no search items entered
		if (input.isEmpty()) {
			Notification.show("No search criteria entered. Please enter at least 1 search item to proceed.", Notification.Type.WARNING_MESSAGE);
			return;
		}

		// set engine settings
		owner_.getOwner().getSettings().setEngineSettings(input);

		// search
		owner_.getOwner().getOwner().submitTask(new AdvancedPilotPointSearch(owner_.getOwner().getOwner(), input));
	}
}
