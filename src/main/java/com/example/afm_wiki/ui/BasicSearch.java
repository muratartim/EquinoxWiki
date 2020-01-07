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

import java.util.ArrayList;

import org.vaadin.resetbuttonfortextfield.ResetButtonForTextField;

import com.example.afm_wiki.data.BasicSearchInput;
import com.example.afm_wiki.task.BasicLoadcaseFactorSearch;
import com.example.afm_wiki.task.BasicPilotPointSearch;
import com.example.afm_wiki.task.BasicSpectrumSearch;
import com.example.afm_wiki.utility.MyPaperStack.Paper;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.MouseEvents.ClickEvent;
import com.vaadin.event.MouseEvents.ClickListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.ClassResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Class for basic search page.
 *
 * @author Murat Artim
 * @date 23 Feb 2017
 * @time 15:03:25
 */
public class BasicSearch extends VerticalLayout implements Paper {

	/** Serial ID. */
	private static final long serialVersionUID = 1L;

	/** The owner user interface. */
	private final SearchView owner_;

	/** Search button. */
	private final TextField searchField_;

	/** Shortcut listener. */
	private ShortcutListener shortcutListener_;

	/**
	 * Creates basic search page.
	 *
	 * @param owner
	 *            The owner view.
	 */
	public BasicSearch(SearchView owner) {

		// set owner
		owner_ = owner;

		// create page layout
		addStyleName("pageLayout");
		setSizeFull();

		// create layout for top components
		HorizontalLayout topLayout = new HorizontalLayout();
		topLayout.setWidth("100%");
		topLayout.setDefaultComponentAlignment(Alignment.MIDDLE_RIGHT);
		addComponent(topLayout);

		// create 'advanced search' label
		Label advancedSearchLabel = new Label("Advanced Search");
		advancedSearchLabel.addStyleName("advancedSearchLabel");
		topLayout.addComponent(advancedSearchLabel);
		topLayout.setExpandRatio(advancedSearchLabel, 1f);

		// create finger pointer image
		Image pointerImage = new Image(null, new ClassResource("image/pointRight.png"));
		pointerImage.addStyleName("pointerImage");
		topLayout.addComponent(pointerImage);

		// create center layout
		VerticalLayout centerLayout = new VerticalLayout();
		addComponent(centerLayout);
		setComponentAlignment(centerLayout, Alignment.MIDDLE_CENTER);
		setExpandRatio(centerLayout, 1);

		// create wiki image
		Image wikiImage = new Image(null, new ClassResource("image/wiki.png"));
		wikiImage.addStyleName("wikiImage");
		centerLayout.addComponent(wikiImage);
		centerLayout.setComponentAlignment(wikiImage, Alignment.MIDDLE_CENTER);

		// create layout for the search text field
		AbsoluteLayout searchLayout = new AbsoluteLayout();
		searchLayout.setWidth(600, Unit.PIXELS);
		searchLayout.setHeight(100, Unit.PIXELS);
		centerLayout.addComponent(searchLayout);
		centerLayout.setComponentAlignment(searchLayout, Alignment.MIDDLE_CENTER);

		// create search text field
		searchField_ = new TextField();
		ResetButtonForTextField.extend(searchField_);
		searchField_.setImmediate(true);
		searchField_.addStyleName("searchField");
		searchField_.setInputPrompt("Search AFM database");
		searchField_.setWidth(450, Unit.PIXELS);
		searchField_.setHeight(37, Unit.PIXELS);
		String description = "Enter space or comma separated keywords. Examples:<br>";
		description += "\"<font color=\"steelblue\">A380 FF</font>\" or ";
		description += "\"<font color=\"steelblue\">A310, sect13</font>\" or ";
		description += "\"<font color=\"steelblue\">A310 sect13 MR</font>\"";
		searchField_.setDescription(description);
		searchLayout.addComponent(searchField_, "top: 0; left: 75;");

		// setup search field to listen for Enter key
		setupSearchField(searchField_);

		// create search button
		Button searchButton = new Button("Search", new Button.ClickListener() {

			/** Serial ID. */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
				performSearch(searchField_.getValue());
			}
		});
		searchButton.setIcon(FontAwesome.ROCKET);
		searchButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
		searchButton.addStyleName(ValoTheme.BUTTON_SMALL);
		searchButton.setWidth(100, Unit.PIXELS);

		// create settings button
		Button settings = new Button("Settings", new Button.ClickListener() {

			/** Serial ID. */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {

				// get settings dialog
				SettingsDialog settings = owner_.getSettings();

				// not shown
				if (!settings.isAttached()) {
					settings.center();
					owner_.getOwner().addWindow(settings);
				}
			}
		});
		settings.setIcon(FontAwesome.GEARS);
		settings.addStyleName(ValoTheme.BUTTON_PRIMARY);
		settings.addStyleName(ValoTheme.BUTTON_SMALL);
		settings.setWidth(100, Unit.PIXELS);

		// add buttons to layout
		searchLayout.addComponent(searchButton, "top: 60; left: 195;");
		searchLayout.addComponent(settings, "top: 60; left: 305;");

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
	}

	/**
	 * Returns owner view.
	 *
	 * @return The owner view.
	 */
	public SearchView getOwner() {
		return owner_;
	}

	@Override
	public void shown() {
		// no implementation
	}

	@Override
	public void hidden() {
		searchField_.removeShortcutListener(shortcutListener_);
	}

	/**
	 * Sets up search field to listen for Enter key.
	 *
	 * @param searchField
	 *            Search field to set up.
	 */
	private void setupSearchField(TextField searchField) {

		// create shortcut listener
		shortcutListener_ = new ShortcutListener("", KeyCode.ENTER, null) {

			/** Serial ID. */
			private static final long serialVersionUID = 1L;

			@Override
			public void handleAction(Object sender, Object target) {
				performSearch(searchField.getValue());
			}
		};

		// create and add focus listener
		searchField.addFocusListener(new FocusListener() {

			/** Serial ID. */
			private static final long serialVersionUID = 1L;

			@Override
			public void focus(FocusEvent event) {
				searchField.addShortcutListener(shortcutListener_);
			}
		});

		// create and add blur listener
		searchField.addBlurListener(new BlurListener() {

			/** Serial ID. */
			private static final long serialVersionUID = 1L;

			@Override
			public void blur(BlurEvent event) {
				searchField.removeShortcutListener(shortcutListener_);
			}
		});
	}

	/**
	 * Performs search.
	 *
	 * @param input
	 *            User input text.
	 */
	private void performSearch(String input) {

		// check inputs
		ArrayList<String> keywords = checkInputs(input);
		if (keywords == null)
			return;

		// create input
		BasicSearchInput searchInput = new BasicSearchInput();
		searchInput.setKeywords(keywords);

		// get settings dialog
		SettingsDialog settings = owner_.getSettings();

		// set engine settings
		settings.setEngineSettings(searchInput);

		// search
		String searchTarget = settings.getSearchTarget();
		if (searchTarget.equals(SettingsDialog.FATIGUE_SPECTRA))
			owner_.getOwner().submitTask(new BasicSpectrumSearch(owner_.getOwner(), searchInput));
		else if (searchTarget.equals(SettingsDialog.PILOT_POINTS))
			owner_.getOwner().submitTask(new BasicPilotPointSearch(owner_.getOwner(), searchInput));
		else if (searchTarget.equals(SettingsDialog.LOADCASE_FACTORS))
			owner_.getOwner().submitTask(new BasicLoadcaseFactorSearch(owner_.getOwner(), searchInput));
	}

	/**
	 * Checks inputs and shows warning message if invalid.
	 *
	 * @param input
	 *            User input text.
	 * @return List of extracted keywords, or null if invalid inputs are supplied.
	 */
	private static ArrayList<String> checkInputs(String input) {

		try {

			// no input supplied
			if (input == null) {
				Notification.show("Please enter keywords to search", Notification.Type.WARNING_MESSAGE);
				return null;
			}

			// get keywords
			final String keywords = input.trim();

			// no keyword entered
			if ((keywords == null) || keywords.isEmpty()) {
				Notification.show("Please enter keywords to search", Notification.Type.WARNING_MESSAGE);
				return null;
			}

			// create input list
			final ArrayList<String> inputs = new ArrayList<>();

			// comma separated keywords
			if (keywords.contains(",")) {

				// split
				final String[] split = keywords.split(",");

				// no keywords
				if (split.length == 0) {
					Notification.show("Please enter keywords to search", Notification.Type.WARNING_MESSAGE);
					return null;
				}

				// add words to inputs
				for (String word : split) {
					word = word.trim();
					if (!word.isEmpty())
						inputs.add(word);
				}

				// no keywords
				if (inputs.isEmpty()) {
					Notification.show("Please enter keywords to search", Notification.Type.WARNING_MESSAGE);
					return null;
				}
			}

			// space separated keywords
			else if (keywords.contains(" ")) {

				// split
				final String[] split = keywords.split(" ");

				// no keywords
				if (split.length == 0) {
					Notification.show("Please enter keywords to search", Notification.Type.WARNING_MESSAGE);
					return null;
				}

				// add words to inputs
				for (String word : split) {
					word = word.trim();
					if (!word.isEmpty())
						inputs.add(word);
				}

				// no keywords
				if (inputs.isEmpty()) {
					Notification.show("Please enter keywords to search", Notification.Type.WARNING_MESSAGE);
					return null;
				}
			}

			// single keyword
			else
				inputs.add(keywords);

			// return inputs
			return inputs;
		}

		// exception occurred during processing user inputs
		catch (Exception e) {
			Notification.show("Please enter valid keywords to search", Notification.Type.WARNING_MESSAGE);
			return null;
		}
	}
}
