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

import com.example.afm_wiki.utility.MyPaperStack.Paper;
import com.vaadin.event.MouseEvents.ClickEvent;
import com.vaadin.event.MouseEvents.ClickListener;
import com.vaadin.server.ClassResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Class for advanced search layout.
 *
 * @author Murat Artim
 * @date 23 Feb 2017
 * @time 11:57:52
 */
public class AdvancedSearch extends VerticalLayout implements Paper {

	/** Serial ID. */
	private static final long serialVersionUID = 1L;

	/** The owner view. */
	private final SearchView owner_;

	/** Search tabs. */
	private final TabSheet searchTabs_;

	/**
	 * Creates advanced search layout.
	 *
	 * @param owner
	 *            The owner view.
	 */
	public AdvancedSearch(SearchView owner) {

		// set owner
		owner_ = owner;

		// create page layout
		addStyleName("pageLayout");
		setSizeFull();

		// create tab sheet
		searchTabs_ = new TabSheet();
		searchTabs_.setVisible(false);
		searchTabs_.addStyleName(ValoTheme.TABSHEET_COMPACT_TABBAR);
		searchTabs_.addStyleName(ValoTheme.TABSHEET_CENTERED_TABS);
		searchTabs_.addStyleName(ValoTheme.TABSHEET_EQUAL_WIDTH_TABS);
		searchTabs_.addStyleName(ValoTheme.TABSHEET_ICONS_ON_TOP);
		searchTabs_.addStyleName("searchTabs");
		searchTabs_.setSizeFull();
		addComponent(searchTabs_);
		setExpandRatio(searchTabs_, 1);

		// create tabs
		Tab spectraTab = searchTabs_.addTab(new AdvancedSpectrumSearchPage(this), "Fatigue Spectra");
		spectraTab.setIcon(FontAwesome.AREA_CHART);
		Tab pilotPointTab = searchTabs_.addTab(new AdvancedPilotPointSearchPage(this), "Pilot Points");
		pilotPointTab.setIcon(FontAwesome.FILE_POWERPOINT_O);
		Tab loadcaseTab = searchTabs_.addTab(new AdvancedLoadcaseFactorSearchPage(this), "Loadcase Factors");
		loadcaseTab.setIcon(FontAwesome.TABLE);
		Tab modelTab = searchTabs_.addTab(new Label("Not implemented yet"), "Aircraft Models");
		modelTab.setIcon(FontAwesome.PAPER_PLANE);

		// create settings image
		Image settingsImage = new Image(null, new ClassResource("image/settings.png"));
		settingsImage.addStyleName("settingsImage");
		settingsImage.setDescription("Search Engine Settings");
		settingsImage.setHeight("42px");
		settingsImage.setWidth("42px");
		addComponent(settingsImage);
		setComponentAlignment(settingsImage, Alignment.BOTTOM_RIGHT);

		// add click listener to settings image
		settingsImage.addClickListener(new ClickListener() {

			/** Serial ID. */
			private static final long serialVersionUID = 1L;

			@Override
			public void click(ClickEvent event) {

				// get settings dialog
				SettingsDialog settings = owner_.getSettings();

				// not shown
				if (!settings.isAttached()) {
					settings.center();
					owner_.getOwner().addWindow(settings);
				}
			}
		});

		// add tab change listener
		searchTabs_.addSelectedTabChangeListener(new SelectedTabChangeListener() {

			/** Serial ID. */
			private static final long serialVersionUID = 1L;

			@Override
			public void selectedTabChange(SelectedTabChangeEvent event) {
				Component selected = event.getTabSheet().getSelectedTab();
				if (!selected.equals(searchTabs_.getTab(0).getComponent()))
					((AdvancedSpectrumSearchPage) searchTabs_.getTab(0).getComponent()).removeListeners();
				if (!selected.equals(searchTabs_.getTab(1).getComponent()))
					((AdvancedPilotPointSearchPage) searchTabs_.getTab(1).getComponent()).removeListeners();
				if (!selected.equals(searchTabs_.getTab(2).getComponent()))
					((AdvancedLoadcaseFactorSearchPage) searchTabs_.getTab(2).getComponent()).removeListeners();
			}
		});
	}

	/**
	 * Returns owner user interface.
	 *
	 * @return The owner user interface.
	 */
	public SearchView getOwner() {
		return owner_;
	}

	/**
	 * Returns the search tabs.
	 *
	 * @return The search tabs.
	 */
	public TabSheet getSearchTabs() {
		return searchTabs_;
	}

	@Override
	public void shown() {
		searchTabs_.setVisible(true);
	}

	@Override
	public void hidden() {
		((AdvancedSpectrumSearchPage) searchTabs_.getTab(0).getComponent()).removeListeners();
		((AdvancedPilotPointSearchPage) searchTabs_.getTab(1).getComponent()).removeListeners();
		((AdvancedLoadcaseFactorSearchPage) searchTabs_.getTab(2).getComponent()).removeListeners();
		searchTabs_.setVisible(false);
	}
}
