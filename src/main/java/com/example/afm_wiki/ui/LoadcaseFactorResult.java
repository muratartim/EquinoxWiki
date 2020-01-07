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

import com.example.afm_wiki.data.DownloadInfo;
import com.example.afm_wiki.data.LoadcaseFactorInfo;
import com.example.afm_wiki.data.LoadcaseFactorInfo.LoadcaseFactorInfoType;
import com.example.afm_wiki.data.PilotPointInfo.PilotPointInfoType;
import com.example.afm_wiki.data.PilotPointSearchInput;
import com.example.afm_wiki.data.SearchItem;
import com.example.afm_wiki.data.SpectrumInfo.SpectrumInfoType;
import com.example.afm_wiki.data.SpectrumSearchInput;
import com.example.afm_wiki.task.AdvancedPilotPointSearch;
import com.example.afm_wiki.task.AdvancedSpectrumSearch;
import com.example.afm_wiki.task.DownloadLoadcaseFactor;
import com.example.afm_wiki.ui.SearchResults.DownloadableResult;
import com.example.afm_wiki.utility.Utility;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Class for loadcase factor result item.
 *
 * @author Murat Artim
 * @date 27 Feb 2017
 * @time 11:19:36
 */
public class LoadcaseFactorResult extends AbsoluteLayout implements DownloadableResult, ClickListener {

	/** Serial ID. */
	private static final long serialVersionUID = 1L;

	/** The owner panel. */
	private final SearchResults owner_;

	/** Loadcase factor info. */
	private final LoadcaseFactorInfo info_;

	/** The title of the result. */
	private final CheckBox titleCheckBox_;

	/** The description of the result. */
	private final Label descriptionLabel_;

	/** Buttons. */
	private final Button pilotPointsButton_, spectrumButton_, infoButton_, downloadButton_;

	/**
	 * Creates loadcase factor result.
	 *
	 * @param owner
	 *            The owner panel.
	 * @param info
	 *            Loadcase factor info.
	 */
	public LoadcaseFactorResult(SearchResults owner, LoadcaseFactorInfo info) {

		// set info
		owner_ = owner;
		info_ = info;

		// setup layout
		addStyleName("searchItem");
		setWidth("100%");
		setHeight(65, Unit.PIXELS);

		// create header check box
		titleCheckBox_ = new CheckBox(Utility.trimResultLine((String) info_.getInfo(LoadcaseFactorInfoType.NAME)));
		titleCheckBox_.addStyleName("searchItemHeader");
		addComponent(titleCheckBox_, "top: 10; left: 10;");

		// create description
		String description = (String) info_.getInfo(LoadcaseFactorInfoType.SPECTRUM_NAME);
		description += ", " + (String) info_.getInfo(LoadcaseFactorInfoType.AC_PROGRAM);
		description += ", " + (String) info_.getInfo(LoadcaseFactorInfoType.AC_SECTION);
		description += ", " + (String) info_.getInfo(LoadcaseFactorInfoType.FAT_MISSION);
		descriptionLabel_ = new Label(Utility.trimResultLine(description));
		descriptionLabel_.addStyleName("searchItemDescription");
		addComponent(descriptionLabel_, "top: 32; left: 35;");

		// create layout for buttons
		CssLayout buttonLayout = new CssLayout();
		buttonLayout.addStyleName("v-component-group");
		addComponent(buttonLayout, "top: 13; right: 10;");

		// create loadcase factors button
		pilotPointsButton_ = new Button(FontAwesome.FILE_POWERPOINT_O);
		pilotPointsButton_.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
		pilotPointsButton_.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		pilotPointsButton_.setDescription("Search connected pilot points");
		buttonLayout.addComponent(pilotPointsButton_);
		pilotPointsButton_.addClickListener(this);

		// create spectrum button
		spectrumButton_ = new Button(FontAwesome.AREA_CHART);
		spectrumButton_.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
		spectrumButton_.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		spectrumButton_.setDescription("Search connected spectrum");
		buttonLayout.addComponent(spectrumButton_);
		spectrumButton_.addClickListener(this);

		// create information button
		infoButton_ = new Button(FontAwesome.INFO_CIRCLE);
		infoButton_.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
		infoButton_.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		infoButton_.setDescription("Show info");
		buttonLayout.addComponent(infoButton_);
		infoButton_.addClickListener(this);

		// create download button
		downloadButton_ = new Button(FontAwesome.CLOUD_DOWNLOAD);
		downloadButton_.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
		downloadButton_.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		downloadButton_.setDescription("Download to disk");
		buttonLayout.addComponent(downloadButton_);
		downloadButton_.addClickListener(this);
	}

	/**
	 * Returns the owner panel.
	 *
	 * @return The owner panel.
	 */
	public SearchResults getOwner() {
		return owner_;
	}

	/**
	 * Returns the loadcase factor info.
	 *
	 * @return The loadcase factor info.
	 */
	public LoadcaseFactorInfo getInfo() {
		return info_;
	}

	@Override
	public boolean isSelected() {
		return titleCheckBox_.getValue();
	}

	@Override
	public DownloadInfo getDownloadInfo() {
		return info_;
	}

	@Override
	public void buttonClick(ClickEvent event) {
		Object source = event.getSource();
		if (source.equals(infoButton_))
			onInfoClicked();
		else if (source.equals(downloadButton_))
			onDownloadClicked();
		else if (source.equals(spectrumButton_))
			onSearchSpectrumClicked();
		else if (source.equals(pilotPointsButton_))
			onSearchPilotPointsClicked();
	}

	/**
	 * Called when spectrum info clicked.
	 */
	private void onInfoClicked() {
		owner_.showLoadcaseFactorInfo(info_);
	}

	/**
	 * Called when download loadcase factor clicked.
	 */
	private void onDownloadClicked() {
		owner_.getOwner().getOwner().submitTask(new DownloadLoadcaseFactor(info_, owner_.getOwner().getOwner()));
	}

	/**
	 * Called when search spectrum clicked.
	 */
	private void onSearchSpectrumClicked() {

		// create search input
		SpectrumSearchInput input = new SpectrumSearchInput();

		// get search info
		String spectrumName = (String) info_.getInfo(LoadcaseFactorInfoType.SPECTRUM_NAME);
		String program = (String) info_.getInfo(LoadcaseFactorInfoType.AC_PROGRAM);
		String section = (String) info_.getInfo(LoadcaseFactorInfoType.AC_SECTION);

		// add inputs
		input.addInput(SpectrumInfoType.NAME, new SearchItem(spectrumName, SearchField.EQUALS));
		input.addInput(SpectrumInfoType.AC_PROGRAM, new SearchItem(program, SearchField.EQUALS));
		input.addInput(SpectrumInfoType.AC_SECTION, new SearchItem(section, SearchField.EQUALS));

		// get settings window
		SettingsDialog settings = owner_.getOwner().getOwner().getSearchView().getSettings();

		// set engine settings
		settings.setEngineSettings(input);

		// search
		owner_.getOwner().getOwner().submitTask(new AdvancedSpectrumSearch(owner_.getOwner().getOwner(), input));
	}

	/**
	 * Called when search pilot points clicked.
	 */
	private void onSearchPilotPointsClicked() {

		// create search input
		PilotPointSearchInput input = new PilotPointSearchInput();

		// get search info
		String spectrumName = (String) info_.getInfo(LoadcaseFactorInfoType.SPECTRUM_NAME);
		String ppName = (String) info_.getInfo(LoadcaseFactorInfoType.PILOT_POINT_NAME);
		String program = (String) info_.getInfo(LoadcaseFactorInfoType.AC_PROGRAM);
		String section = (String) info_.getInfo(LoadcaseFactorInfoType.AC_SECTION);

		// add inputs
		input.addInput(PilotPointInfoType.SPECTRUM_NAME, new SearchItem(spectrumName, SearchField.EQUALS));
		input.addInput(PilotPointInfoType.NAME, new SearchItem(ppName, SearchField.EQUALS));
		input.addInput(PilotPointInfoType.AC_PROGRAM, new SearchItem(program, SearchField.EQUALS));
		input.addInput(PilotPointInfoType.AC_SECTION, new SearchItem(section, SearchField.EQUALS));

		// get settings window
		SettingsDialog settings = owner_.getOwner().getOwner().getSearchView().getSettings();

		// set engine settings
		settings.setEngineSettings(input);

		// search
		owner_.getOwner().getOwner().submitTask(new AdvancedPilotPointSearch(owner_.getOwner().getOwner(), input));
	}
}
