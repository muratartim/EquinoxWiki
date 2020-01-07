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

import com.example.afm_wiki.data.SearchInput;
import com.example.afm_wiki.data.SpectrumInfo.SpectrumInfoType;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.server.ClassResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Abstract class for settings dialog.
 *
 * @author Murat Artim
 * @date 17 Mar 2017
 * @time 14:38:17
 */
public class SettingsDialog extends Window {

	/** Serial ID. */
	private static final long serialVersionUID = 1L;

	/** Search target. */
	public static final String FATIGUE_SPECTRA = "Fatigue spectra", PILOT_POINTS = "Pilot points", LOADCASE_FACTORS = "Loadcase factors", AIRCRAFT_MODELS = "Aircraft models";

	/** Logical operator. */
	public static final String AND = "AND", OR = "OR";

	/** Results ordering. */
	public static final String ASCENDING = "Ascending", DESCENDING = "Descending";

	/** The owner panel of this dialog. */
	private final SearchView owner_;

	/** Property set. */
	private final PropertysetItem settings_;

	private final ObjectProperty<String> prop_ = new ObjectProperty<>("10");

	/**
	 * Creates search engine settings dialog.
	 *
	 * @param owner
	 *            The owner user interface.
	 */
	public SettingsDialog(SearchView owner) {

		// set owner
		owner_ = owner;

		// create settings layout
		VerticalLayout settingsLayout = new VerticalLayout();
		settingsLayout.addStyleName("settingsPanel");
		settingsLayout.setMargin(true);
		settingsLayout.setSpacing(true);

		// create search target combobox
		VerticalLayout searchTargetLayout = new VerticalLayout();
		searchTargetLayout.setMargin(true);
		searchTargetLayout.setSpacing(true);

		ComboBox searchTargetCombo = new ComboBox();
		searchTargetCombo.setImmediate(true);
		searchTargetCombo.setWidth("100%");
		searchTargetCombo.setNullSelectionAllowed(false);
		searchTargetCombo.setInvalidAllowed(false);
		searchTargetCombo.setTextInputAllowed(false);
		searchTargetCombo.addItems(FATIGUE_SPECTRA, PILOT_POINTS, LOADCASE_FACTORS, AIRCRAFT_MODELS);
		searchTargetCombo.setItemIcon(FATIGUE_SPECTRA, FontAwesome.AREA_CHART);
		searchTargetCombo.setItemIcon(PILOT_POINTS, FontAwesome.FILE_POWERPOINT_O);
		searchTargetCombo.setItemIcon(LOADCASE_FACTORS, FontAwesome.TABLE);
		searchTargetCombo.setItemIcon(AIRCRAFT_MODELS, FontAwesome.PAPER_PLANE);
		searchTargetCombo.setValue(FATIGUE_SPECTRA);
		searchTargetLayout.addComponent(searchTargetCombo);

		// create search target panel
		Panel searchTargetPanel = new Panel("Search target");
		searchTargetPanel.setIcon(FontAwesome.CROSSHAIRS);
		searchTargetPanel.setWidth("100%");
		searchTargetPanel.setHeightUndefined();
		searchTargetPanel.setContent(searchTargetLayout);
		settingsLayout.addComponent(searchTargetPanel);

		// add listener to search target combobox
		searchTargetCombo.addValueChangeListener(new ValueChangeListener() {

			/** Serial ID. */
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				searchTargetChanged((String) searchTargetCombo.getValue());
			}
		});

		// create filter grid
		GridLayout filterGrid = new GridLayout(2, 3);
		filterGrid.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
		filterGrid.setSpacing(true);
		filterGrid.addComponent(new Label("Maximum hits:"), 0, 0);
		TextField maxHits = new TextField();
		// maxHits.setDecimalAllowed(true);
		// maxHits.setMinValue(1);
		// maxHits.setMaxValue(1000);
		// maxHits.setErrorText("Invalid entry!");
		// maxHits.setNegativeAllowed(false);
		maxHits.setValue("10");
		maxHits.setImmediate(true);
		filterGrid.addComponent(maxHits, 1, 0);
		filterGrid.addComponent(new Label("Logical operator:"), 0, 1);
		ComboBox logicalOperator = new ComboBox();
		logicalOperator.addItems(AND, OR);
		logicalOperator.setValue(AND);
		logicalOperator.setNullSelectionAllowed(false);
		logicalOperator.setInvalidAllowed(false);
		logicalOperator.setTextInputAllowed(false);
		logicalOperator.setImmediate(true);
		filterGrid.addComponent(logicalOperator, 1, 1);

		// create filter layout
		VerticalLayout filterLayout = new VerticalLayout();
		filterLayout.setMargin(true);
		filterLayout.setSizeUndefined();
		filterLayout.addComponent(filterGrid);
		CheckBox ignoreCase = new CheckBox("Ignore case");
		ignoreCase.setValue(true);
		ignoreCase.setImmediate(true);
		filterLayout.addComponent(ignoreCase);

		// create filter panel
		Panel filterPanel = new Panel("Results filtering");
		filterPanel.setIcon(new ClassResource("image/filter.png"));
		filterPanel.setWidth("100%");
		filterPanel.setHeightUndefined();
		filterPanel.setContent(filterLayout);
		settingsLayout.addComponent(filterPanel);

		// create order grid
		GridLayout orderGrid = new GridLayout(2, 2);
		orderGrid.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
		orderGrid.setSpacing(true);
		orderGrid.addComponent(new Label("Order by:"), 0, 0);

		ComboBox spectrumOrderByCombo = new ComboBox();
		spectrumOrderByCombo.setWidth("100%");
		spectrumOrderByCombo.setNullSelectionAllowed(false);
		spectrumOrderByCombo.setInvalidAllowed(false);
		spectrumOrderByCombo.setTextInputAllowed(false);
		spectrumOrderByCombo.setPageLength(0);
		spectrumOrderByCombo.setImmediate(true);
		orderGrid.addComponent(spectrumOrderByCombo, 1, 0);

		for (SpectrumInfoType info : SpectrumInfoType.values())
			if (!info.equals(SpectrumInfoType.ID) && !info.equals(SpectrumInfoType.MULT_TABLES) && !info.equals(SpectrumInfoType.PILOT_POINTS))
				spectrumOrderByCombo.addItem(info);

		ComboBox pilotPointOrderByCombo = new ComboBox();
		pilotPointOrderByCombo.setWidth("100%");
		pilotPointOrderByCombo.setNullSelectionAllowed(false);
		pilotPointOrderByCombo.setInvalidAllowed(false);
		pilotPointOrderByCombo.setTextInputAllowed(false);
		pilotPointOrderByCombo.setPageLength(0);
		pilotPointOrderByCombo.setImmediate(true);

		ComboBox loadcaseFactorOrderByCombo = new ComboBox();
		loadcaseFactorOrderByCombo.setWidth("100%");
		loadcaseFactorOrderByCombo.setNullSelectionAllowed(false);
		loadcaseFactorOrderByCombo.setInvalidAllowed(false);
		loadcaseFactorOrderByCombo.setTextInputAllowed(false);
		loadcaseFactorOrderByCombo.setPageLength(0);
		loadcaseFactorOrderByCombo.setImmediate(true);

		orderGrid.addComponent(new Label("Results ordering:"), 0, 1);
		ComboBox orderingCombo = new ComboBox();
		orderingCombo.addItems(ASCENDING, DESCENDING);
		orderingCombo.setItemIcon(ASCENDING, FontAwesome.SORT_ALPHA_ASC);
		orderingCombo.setItemIcon(DESCENDING, FontAwesome.SORT_ALPHA_DESC);
		orderingCombo.setValue(ASCENDING);
		orderingCombo.setNullSelectionAllowed(false);
		orderingCombo.setInvalidAllowed(false);
		orderingCombo.setTextInputAllowed(false);
		orderingCombo.setImmediate(true);
		orderGrid.addComponent(orderingCombo, 1, 1);

		// create results order panel
		VerticalLayout orderLayout = new VerticalLayout();
		orderLayout.setMargin(true);
		orderLayout.setSizeUndefined();
		orderLayout.addComponent(orderGrid);
		Panel orderPanel = new Panel("Results ordering");
		orderPanel.setIcon(FontAwesome.SORT_AMOUNT_ASC);
		orderPanel.setWidth("100%");
		orderPanel.setHeightUndefined();
		orderPanel.setContent(orderLayout);
		settingsLayout.addComponent(orderPanel);

		// create button layout
		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setDefaultComponentAlignment(Alignment.MIDDLE_RIGHT);
		buttonLayout.setWidth("100%");
		buttonLayout.setHeightUndefined();
		buttonLayout.setSpacing(true);
		Button resetButton = new Button("Reset");
		resetButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
		resetButton.addStyleName(ValoTheme.BUTTON_SMALL);
		Button closeButton = new Button("Close");
		closeButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
		closeButton.addStyleName(ValoTheme.BUTTON_SMALL);
		buttonLayout.addComponent(resetButton);
		buttonLayout.addComponent(closeButton);
		buttonLayout.setExpandRatio(resetButton, 1);
		settingsLayout.addComponent(buttonLayout);

		// create settings dialog
		setCaption(" Search Engine Settings");
		setIcon(new ClassResource("image/settingsSmall.png"));
		setContent(settingsLayout);
		setSizeUndefined();
		center();
		setModal(true);
		setResizable(false);
		setDraggable(false);

		// add listener to close button
		closeButton.addClickListener(new Button.ClickListener() {

			/** Serial ID. */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
				owner.getOwner().removeWindow(SettingsDialog.this);
			}
		});

		// add listener to reset button
		resetButton.addClickListener(new Button.ClickListener() {

			/** Serial ID. */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {

				// reset general settings
				// maxHits.setValue("10");
				// logicalOperator.setValue("AND");
				// ignoreCase.setValue(true);
				// orderingCombo.setValue(ASCENDING);

				// reset specific settings
				onResetClicked();
			}
		});

		// create property set
		settings_ = new PropertysetItem();
		// settings_.addItemProperty("searchTarget", new ObjectProperty<>(FATIGUE_SPECTRA));
		// settings_.addItemProperty("maxHits", new ObjectProperty<>("10"));
		// settings_.addItemProperty("logicalOperator", new ObjectProperty<>(AND));
		// settings_.addItemProperty("ignoreCase", new ObjectProperty<>(true));
		// settings_.addItemProperty("spectrumOrderBy", new ObjectProperty<>(SpectrumInfoType.NAME.getLabel()));
		// settings_.addItemProperty("pilotPointOrderBy", new ObjectProperty<>(PilotPointInfoType.NAME.getLabel()));
		// settings_.addItemProperty("loadcaseFactorOrderBy", new ObjectProperty<>(LoadcaseFactorInfoType.NAME.getLabel()));
		// settings_.addItemProperty("order", new ObjectProperty<>(ASCENDING));
		//
		// // bind property set to components
		// FieldGroup binder = new FieldGroup(settings_);
		// binder.bind(searchTargetCombo, "searchTarget");
		// binder.bind(maxHits, "maxHits");
		// binder.bind(logicalOperator, "logicalOperator");
		// binder.bind(ignoreCase, "ignoreCase");
		// binder.bind(spectrumOrderByCombo, "spectrumOrderBy");
		// binder.bind(pilotPointOrderByCombo, "pilotPointOrderBy");
		// binder.bind(loadcaseFactorOrderByCombo, "loadcaseFactorOrderBy");
		// binder.bind(orderingCombo, "order");

		maxHits.setPropertyDataSource(prop_);
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
	 * Returns the search target. Note that, this is only useful for basic search.
	 *
	 * @return The search target.
	 */
	public String getSearchTarget() {
		return (String) settings_.getItemProperty("searchTarget").getValue();
	}

	/**
	 * Returns maximum number of hits or 0 if there is no limit.
	 *
	 * @return Maximum number of hits or 0 if there is no limit.
	 */
	public int getMaximumHits() {
		return (int) settings_.getItemProperty("maxHits").getValue();
	}

	/**
	 * Returns logical operator.
	 *
	 * @return Logical operator.
	 */
	public String getLogicalOperator() {
		return (String) settings_.getItemProperty("logicalOperator").getValue();
	}

	/**
	 * Returns true if case should be ignored.
	 *
	 * @return True if case should be ignored.
	 */
	public boolean isIgnoreCase() {
		return (boolean) settings_.getItemProperty("ignoreCase").getValue();
	}

	/**
	 * Sets search engine settings.
	 *
	 * @param input
	 *            Search input.
	 */
	public void setEngineSettings(SearchInput input) {
		input.setCase(isIgnoreCase());
		input.setMaxHits(getMaximumHits());
		input.setOperator(getLogicalOperator().equals(AND));
	}

	/**
	 * Sets order by combo according to search target selection.
	 *
	 * @param searchTarget
	 *            Selected search target.
	 */
	private void searchTargetChanged(String searchTarget) {

		// // remove all items
		// orderByCombo_.removeAllItems();
		//
		// // spectrum
		// if (searchTarget.equals(FATIGUE_SPECTRA)) {
		// for (SpectrumInfoType info : SpectrumInfoType.values())
		// if (!info.equals(SpectrumInfoType.ID) && !info.equals(SpectrumInfoType.MULT_TABLES) && !info.equals(SpectrumInfoType.PILOT_POINTS))
		// orderByCombo_.addItem(info);
		// orderByCombo_.setValue(SpectrumInfoType.NAME);
		// }
		//
		// // pilot points
		// else if (searchTarget.equals(PILOT_POINTS)) {
		// for (PilotPointInfoType info : PilotPointInfoType.values())
		// if (!info.equals(PilotPointInfoType.ID))
		// orderByCombo_.addItem(info);
		// orderByCombo_.setValue(PilotPointInfoType.NAME);
		// }
		//
		// // loadcase factors
		// else if (searchTarget.equals(LOADCASE_FACTORS)) {
		// for (LoadcaseFactorInfoType info : LoadcaseFactorInfoType.values())
		// if (!info.equals(LoadcaseFactorInfoType.ID))
		// orderByCombo_.addItem(info);
		// orderByCombo_.setValue(LoadcaseFactorInfoType.NAME);
		// }
		//
		// // aircraft models
		// else if (searchTarget.equals(AIRCRAFT_MODELS)) {
		// for (AircraftModelInfoType info : AircraftModelInfoType.values())
		// if (!info.equals(AircraftModelInfoType.ID))
		// orderByCombo_.addItem(info);
		// orderByCombo_.setValue(AircraftModelInfoType.MODEL_NAME);
		// }
	}

	/**
	 * Called when reset button clicked.
	 */
	private void onResetClicked() {

		System.out.println("value: " + prop_.getValue());

		for (Object id : settings_.getItemPropertyIds())
			System.out.println(id + " = " + settings_.getItemProperty(id).getValue());

		// settings_.getItemProperty("searchTarget").setValue(FATIGUE_SPECTRA);
		// settings_.addItemProperty("maxHits", new ObjectProperty<>(10));
		// settings_.addItemProperty("logicalOperator", new ObjectProperty<>(AND));
		// settings_.addItemProperty("ignoreCase", new ObjectProperty<>(true));
		// settings_.addItemProperty("spectrumOrderBy", new ObjectProperty<>(SpectrumInfoType.NAME.getLabel()));
		// settings_.addItemProperty("pilotPointOrderBy", new ObjectProperty<>(PilotPointInfoType.NAME.getLabel()));
		// settings_.addItemProperty("loadcaseFactorOrderBy", new ObjectProperty<>(LoadcaseFactorInfoType.NAME.getLabel()));
		// settings_.addItemProperty("order", new ObjectProperty<>(ASCENDING));
	}
}
