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

import com.example.afm_wiki.data.PilotPointImageType;
import com.example.afm_wiki.data.PilotPointInfo;
import com.example.afm_wiki.data.PilotPointInfo.PilotPointInfoType;
import com.vaadin.server.ClassResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Class for pilot point info dialog.
 *
 * @author Murat Artim
 * @date 6 Mar 2017
 * @time 11:25:51
 */
public class PilotPointInfoDialog extends Window {

	/** Serial ID. */
	private static final long serialVersionUID = 1L;

	/** The owner panel of this dialog. */
	private final SearchResults owner_;

	/** Info tabs. */
	private final TabSheet tabs_;

	/** Pilot point info. */
	private PilotPointInfo info_;

	/**
	 * Creates spectrum info dialog.
	 *
	 * @param owner
	 *            The owner panel of this dialog.
	 */
	public PilotPointInfoDialog(SearchResults owner) {

		// set owner
		owner_ = owner;

		// create layout
		VerticalLayout layout = new VerticalLayout();
		layout.setSizeUndefined();

		// create tab sheet
		tabs_ = new TabSheet();
		tabs_.addStyleName(ValoTheme.TABSHEET_COMPACT_TABBAR);
		tabs_.addStyleName(ValoTheme.TABSHEET_FRAMED);
		tabs_.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
		tabs_.setHeightUndefined();
		tabs_.setWidth(503, Unit.PIXELS);

		// create and add info tab
		Tab infoTab = tabs_.addTab(new PilotPointInfoPage(this));
		infoTab.setIcon(FontAwesome.LIST_ALT);
		infoTab.setCaption("Pilot point info");
		tabs_.setSelectedTab(infoTab);

		// create and add image tab
		Tab imageTab = tabs_.addTab(new PilotPointImagePage(this, PilotPointImageType.IMAGE));
		imageTab.setIcon(FontAwesome.PHOTO);
		imageTab.setCaption("Pilot point image");

		// create and add mission profile tab
		Tab profileTab = tabs_.addTab(new PilotPointImagePage(this, PilotPointImageType.MISSION_PROFILE));
		profileTab.setIcon(FontAwesome.AREA_CHART);
		profileTab.setCaption("Mission profile");

		// create and add level crossings tab
		Tab levelCrossingsTab = tabs_.addTab(new PilotPointImagePage(this, PilotPointImageType.LEVEL_CROSSING));
		levelCrossingsTab.setIcon(new ClassResource("image/levelCrossing.png"));
		levelCrossingsTab.setCaption("Level crossings");

		// create and add longest typical flights tab
		Tab longestFlightTab = tabs_.addTab(new PilotPointImagePage(this, PilotPointImageType.LONGEST_FLIGHT));
		longestFlightTab.setIcon(new ClassResource("image/flight.png"));
		longestFlightTab.setCaption("Longest flight");

		// create and add highest occurring typical flights tab
		Tab highestOccurringFlight = tabs_.addTab(new PilotPointImagePage(this, PilotPointImageType.FLIGHT_WITH_HIGHEST_OCCURRENCE));
		highestOccurringFlight.setIcon(new ClassResource("image/flight.png"));
		highestOccurringFlight.setCaption("Flight with highest occurrence");

		// create and add typical flights with highest stress tab
		Tab highestStressFlightTab = tabs_.addTab(new PilotPointImagePage(this, PilotPointImageType.FLIGHT_WITH_MAX_TOTAL_STRESS));
		highestStressFlightTab.setIcon(new ClassResource("image/flight.png"));
		highestStressFlightTab.setCaption("Flight with highest stress");

		// create and add damage angles tab
		Tab damageAnglesTab = tabs_.addTab(new PilotPointImagePage(this, PilotPointImageType.DAMAGE_ANGLE));
		damageAnglesTab.setIcon(new ClassResource("image/angle.png"));
		damageAnglesTab.setCaption("Damage angles");

		// create and add number of peaks tab
		Tab numberOfPeaksTab = tabs_.addTab(new PilotPointImagePage(this, PilotPointImageType.NUMBER_OF_PEAKS));
		numberOfPeaksTab.setIcon(FontAwesome.BAR_CHART_O);
		numberOfPeaksTab.setCaption("Number of peaks");

		// create and add load case damage contributions tab
		Tab loadcaseDamageContributionsTab = tabs_.addTab(new PilotPointImagePage(this, PilotPointImageType.LOADCASE_DAMAGE_CONTRIBUTION));
		loadcaseDamageContributionsTab.setIcon(FontAwesome.PIE_CHART);
		loadcaseDamageContributionsTab.setCaption("Loadcase damage contributions");

		// create and add flight damage contributions tab
		Tab flightDamageContributionsTab = tabs_.addTab(new PilotPointImagePage(this, PilotPointImageType.FLIGHT_DAMAGE_CONTRIBUTION));
		flightDamageContributionsTab.setIcon(FontAwesome.PIE_CHART);
		flightDamageContributionsTab.setCaption("Flight damage contributions");

		// add listener to tab selections
		tabs_.addSelectedTabChangeListener(new SelectedTabChangeListener() {

			/** Serial ID. */
			private static final long serialVersionUID = 1L;

			@Override
			public void selectedTabChange(SelectedTabChangeEvent event) {

				// get selected tab
				Component selectedTab = event.getTabSheet().getSelectedTab();

				// no tab selected
				if (selectedTab == null)
					return;

				// image tab selected (request image)
				if (selectedTab instanceof PilotPointImagePage)
					((PilotPointImagePage) selectedTab).requestImage(info_);
			}
		});

		// create settings dialog
		layout.addComponent(tabs_);
		layout.setComponentAlignment(tabs_, Alignment.MIDDLE_CENTER);
		setCaption("   Pilot point name goes here..");
		setIcon(FontAwesome.FILE_POWERPOINT_O);
		setContent(layout);
		setSizeUndefined();
		center();
		setModal(true);
		setResizable(false);
		setDraggable(false);
	}

	/**
	 * Returns the owner of this popup.
	 *
	 * @return The owner of this popup.
	 */
	public SearchResults getOwner() {
		return owner_;
	}

	/**
	 * Sets pilot point info to this dialog.
	 *
	 * @param info
	 *            Spectrum info to set.
	 */
	public void setInfo(PilotPointInfo info) {

		// set info
		info_ = info;

		// set caption
		setCaption("  " + (String) info.getInfo(PilotPointInfoType.NAME));

		// set pilot point info
		((PilotPointInfoPage) tabs_.getTab(0).getComponent()).setInfo(info);

		// reset image pages
		for (int i = 1; i < 11; i++)
			((PilotPointImagePage) tabs_.getTab(i).getComponent()).reset();

		// select first tab
		tabs_.setSelectedTab(0);
	}

	/**
	 * Returns the pilot point info of this dialog.
	 *
	 * @return The pilot point info of this dialog.
	 */
	public PilotPointInfo getInfo() {
		return info_;
	}
}
