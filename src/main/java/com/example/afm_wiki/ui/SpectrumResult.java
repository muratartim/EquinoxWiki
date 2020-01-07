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
import com.example.afm_wiki.data.LoadcaseFactorInfo.LoadcaseFactorInfoType;
import com.example.afm_wiki.data.LoadcaseFactorSearchInput;
import com.example.afm_wiki.data.PilotPointInfo.PilotPointInfoType;
import com.example.afm_wiki.data.PilotPointSearchInput;
import com.example.afm_wiki.data.SearchItem;
import com.example.afm_wiki.data.SpectrumInfo;
import com.example.afm_wiki.data.SpectrumInfo.SpectrumInfoType;
import com.example.afm_wiki.task.AdvancedLoadcaseFactorSearch;
import com.example.afm_wiki.task.AdvancedPilotPointSearch;
import com.example.afm_wiki.task.DownloadSpectrum;
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
 * Class for spectrum search result item.
 *
 * @author Murat Artim
 * @date 27 Feb 2017
 * @time 10:33:08
 */
public class SpectrumResult extends AbsoluteLayout implements DownloadableResult, ClickListener {

	/** Serial ID. */
	private static final long serialVersionUID = 1L;

	/** The owner panel. */
	private final SearchResults owner_;

	/** Spectrum info. */
	private final SpectrumInfo info_;

	/** The title of the result. */
	private final CheckBox titleCheckBox_;

	/** The description of the result. */
	private final Label descriptionLabel_;

	/** Buttons. */
	private final Button loadcaseFactorsButton_, pilotPointsButton_, infoButton_, downloadButton_;

	/**
	 * Creates spectrum result.
	 *
	 * @param owner
	 *            The owner panel.
	 * @param info
	 *            Spectrum info.
	 */
	public SpectrumResult(SearchResults owner, SpectrumInfo info) {

		// set info
		owner_ = owner;
		info_ = info;

		// setup layout
		addStyleName("searchItem");
		setWidth("100%");
		setHeight(65, Unit.PIXELS);

		// create header check box
		titleCheckBox_ = new CheckBox(Utility.trimResultLine((String) info_.getInfo(SpectrumInfoType.NAME)));
		titleCheckBox_.addStyleName("searchItemHeader");
		addComponent(titleCheckBox_, "top: 10; left: 10;");

		// create description
		String description = (String) info_.getInfo(SpectrumInfoType.AC_PROGRAM);
		description += ", " + (String) info_.getInfo(SpectrumInfoType.AC_SECTION);
		description += ", " + (String) info_.getInfo(SpectrumInfoType.FAT_MISSION);
		description += ", " + (String) info_.getInfo(SpectrumInfoType.DELIVERY_REF);
		descriptionLabel_ = new Label(Utility.trimResultLine(description));
		descriptionLabel_.addStyleName("searchItemDescription");
		addComponent(descriptionLabel_, "top: 32; left: 35;");

		// create layout for buttons
		CssLayout buttonLayout = new CssLayout();
		buttonLayout.addStyleName("v-component-group");
		addComponent(buttonLayout, "top: 13; right: 10;");

		// create loadcase factors button
		loadcaseFactorsButton_ = new Button(FontAwesome.TABLE);
		loadcaseFactorsButton_.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
		loadcaseFactorsButton_.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		loadcaseFactorsButton_.setDescription("Search connected loadcase factor files");
		buttonLayout.addComponent(loadcaseFactorsButton_);
		loadcaseFactorsButton_.addClickListener(this);

		// create pilot point button
		pilotPointsButton_ = new Button(FontAwesome.FILE_POWERPOINT_O);
		pilotPointsButton_.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
		pilotPointsButton_.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		pilotPointsButton_.setDescription("Search connected pilot points");
		buttonLayout.addComponent(pilotPointsButton_);
		pilotPointsButton_.addClickListener(this);

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
		downloadButton_.setDescription("Download");
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
	 * Returns the spectrum info.
	 *
	 * @return The spectrum info.
	 */
	public SpectrumInfo getInfo() {
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
		else if (source.equals(pilotPointsButton_))
			onSearchPilotPointsClicked();
		else if (source.equals(loadcaseFactorsButton_))
			onSearchLoadcaseFactorsClicked();
	}

	/**
	 * Called when spectrum info clicked.
	 */
	private void onInfoClicked() {
		owner_.showSpectrumInfo(info_);
	}

	/**
	 * Called when download spectrum clicked.
	 */
	private void onDownloadClicked() {
		owner_.getOwner().getOwner().submitTask(new DownloadSpectrum(info_, owner_.getOwner().getOwner()));
	}

	/**
	 * Called when search pilot points clicked.
	 */
	private void onSearchPilotPointsClicked() {

		// create search input
		PilotPointSearchInput input = new PilotPointSearchInput();

		// get search info
		String spectrumName = (String) info_.getInfo(SpectrumInfoType.NAME);
		String program = (String) info_.getInfo(SpectrumInfoType.AC_PROGRAM);
		String section = (String) info_.getInfo(SpectrumInfoType.AC_SECTION);

		// add inputs
		input.addInput(PilotPointInfoType.SPECTRUM_NAME, new SearchItem(spectrumName, SearchField.EQUALS));
		input.addInput(PilotPointInfoType.AC_PROGRAM, new SearchItem(program, SearchField.EQUALS));
		input.addInput(PilotPointInfoType.AC_SECTION, new SearchItem(section, SearchField.EQUALS));

		// get settings window
		SettingsDialog settings = owner_.getOwner().getOwner().getSearchView().getSettings();

		// set engine settings
		settings.setEngineSettings(input);

		// search
		owner_.getOwner().getOwner().submitTask(new AdvancedPilotPointSearch(owner_.getOwner().getOwner(), input));
	}

	/**
	 * Called when search loadcase factors clicked.
	 */
	private void onSearchLoadcaseFactorsClicked() {

		// create search input
		LoadcaseFactorSearchInput input = new LoadcaseFactorSearchInput();

		// get search info
		String spectrumName = (String) info_.getInfo(SpectrumInfoType.NAME);
		String program = (String) info_.getInfo(SpectrumInfoType.AC_PROGRAM);
		String section = (String) info_.getInfo(SpectrumInfoType.AC_SECTION);

		// add inputs
		input.addInput(LoadcaseFactorInfoType.SPECTRUM_NAME, new SearchItem(spectrumName, SearchField.EQUALS));
		input.addInput(LoadcaseFactorInfoType.AC_PROGRAM, new SearchItem(program, SearchField.EQUALS));
		input.addInput(LoadcaseFactorInfoType.AC_SECTION, new SearchItem(section, SearchField.EQUALS));

		// get settings window
		SettingsDialog settings = owner_.getOwner().getOwner().getSearchView().getSettings();

		// set engine settings
		settings.setEngineSettings(input);

		// search
		owner_.getOwner().getOwner().submitTask(new AdvancedLoadcaseFactorSearch(owner_.getOwner().getOwner(), input));
	}
}
