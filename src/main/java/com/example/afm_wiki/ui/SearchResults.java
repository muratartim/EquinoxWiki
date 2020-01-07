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
import java.util.Iterator;

import com.example.afm_wiki.data.AircraftModelInfo;
import com.example.afm_wiki.data.DownloadInfo;
import com.example.afm_wiki.data.LoadcaseFactorInfo;
import com.example.afm_wiki.data.PilotPointInfo;
import com.example.afm_wiki.data.SpectrumInfo;
import com.example.afm_wiki.task.DownloadLoadcaseFactors;
import com.example.afm_wiki.task.DownloadPilotPoints;
import com.example.afm_wiki.task.DownloadSpectra;
import com.example.afm_wiki.task.WikiTask;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Class for search results layout.
 *
 * @author Murat Artim
 * @date 24 Feb 2017
 * @time 14:35:01
 */
public class SearchResults extends VerticalLayout {

	/** Serial ID. */
	private static final long serialVersionUID = 1L;

	/** The owner view. */
	private final ResultsView owner_;

	/** Search tabs. */
	private final TabSheet searchTabs_;

	/** Tab index. */
	private volatile int tabIndex_ = 1;

	/** Spectrum info dialog. */
	private final SpectrumInfoDialog spectrumInfoDialog_;

	/** Pilot point info dialog. */
	private final PilotPointInfoDialog pilotPointInfoDialog_;

	/** Loadcase factor info dialog. */
	private final LoadcaseFactorInfoDialog loadcaseFactorInfoDialog_;

	/**
	 * Creates basic search page.
	 *
	 * @param owner
	 *            The owner view.
	 */
	public SearchResults(ResultsView owner) {

		// set owner
		owner_ = owner;

		// create page layout
		addStyleName("pageLayout");
		setSizeFull();

		// add tab sheet
		searchTabs_ = new TabSheet();
		searchTabs_.addStyleName(ValoTheme.TABSHEET_COMPACT_TABBAR);
		searchTabs_.addStyleName("searchTabs");
		searchTabs_.setSizeFull();
		addComponent(searchTabs_);
		setExpandRatio(searchTabs_, 1);

		// create layout for buttons
		AbsoluteLayout buttonLayout = new AbsoluteLayout();
		buttonLayout.setWidth("100%");
		buttonLayout.setHeight(60, Unit.PIXELS);
		buttonLayout.addStyleName("searchControls");
		addComponent(buttonLayout);

		// create back button
		Button search = new Button("Back to search", FontAwesome.SEARCH);
		search.setHtmlContentAllowed(true);
		search.setWidth(180, Unit.PIXELS);
		search.addStyleName(ValoTheme.BUTTON_PRIMARY);
		search.addStyleName(ValoTheme.BUTTON_SMALL);
		buttonLayout.addComponent(search, "top: 13; left: 215;");

		// add click listener to search button
		search.addClickListener(new ClickListener() {

			/** Serial ID. */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				owner_.getOwner().showSearchView();
			}
		});

		// create download button
		Button download = new Button("Download selected", FontAwesome.CLOUD_DOWNLOAD);
		download.setHtmlContentAllowed(true);
		download.setWidth(180, Unit.PIXELS);
		download.addStyleName(ValoTheme.BUTTON_PRIMARY);
		download.addStyleName(ValoTheme.BUTTON_SMALL);
		buttonLayout.addComponent(download, "top: 13; left: 405;");

		// add click listener to download button
		download.addClickListener(new ClickListener() {

			/** Serial ID. */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				downloadSelected();
			}
		});

		// create info dialogs
		spectrumInfoDialog_ = new SpectrumInfoDialog(this);
		pilotPointInfoDialog_ = new PilotPointInfoDialog(this);
		loadcaseFactorInfoDialog_ = new LoadcaseFactorInfoDialog(this);
	}

	/**
	 * Returns owner view.
	 *
	 * @return The owner view.
	 */
	public ResultsView getOwner() {
		return owner_;
	}

	/**
	 * Adds given search results to this page.
	 *
	 * @param results
	 *            Search results to add.
	 */
	synchronized public void addSearchResults(ArrayList<DownloadInfo> results) {

		// create vertical layout
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.setSpacing(true);

		// create panel set layout as content
		Panel panel = new Panel(layout);
		panel.addStyleName("searchPanel");
		panel.setSizeFull();

		// add new tab
		Tab tab = searchTabs_.addTab(panel, "Search " + tabIndex_++);
		tab.setClosable(true);
		searchTabs_.setSelectedTab(tab);

		// set tab icon
		DownloadInfo firstItem = results.get(0);
		if (firstItem instanceof SpectrumInfo)
			tab.setIcon(FontAwesome.AREA_CHART);
		else if (firstItem instanceof PilotPointInfo)
			tab.setIcon(FontAwesome.FILE_POWERPOINT_O);
		else if (firstItem instanceof LoadcaseFactorInfo)
			tab.setIcon(FontAwesome.TABLE);
		else if (firstItem instanceof AircraftModelInfo)
			tab.setIcon(FontAwesome.PAPER_PLANE);

		// add results to layout
		for (DownloadInfo result : results)

			// spectrum info
			if (result instanceof SpectrumInfo)
				layout.addComponent(new SpectrumResult(this, (SpectrumInfo) result));

			// pilot point info
			else if (result instanceof PilotPointInfo)
				layout.addComponent(new PilotPointResult(this, (PilotPointInfo) result));

			// loadcase factors info
			else if (result instanceof LoadcaseFactorInfo)
				layout.addComponent(new LoadcaseFactorResult(this, (LoadcaseFactorInfo) result));

			// aircraft model info
			else if (result instanceof AircraftModelInfo) {
				// TODO aircraft model info
			}
	}

	/**
	 * Shows spectrum information.
	 *
	 * @param info
	 *            Spectrum information to show.
	 */
	public void showSpectrumInfo(SpectrumInfo info) {
		if (!spectrumInfoDialog_.isAttached()) {
			spectrumInfoDialog_.setInfo(info);
			owner_.getOwner().addWindow(spectrumInfoDialog_);
		}
	}

	/**
	 * Shows spectrum information.
	 *
	 * @param info
	 *            Spectrum information to show.
	 */
	public void showPilotPointInfo(PilotPointInfo info) {
		if (!pilotPointInfoDialog_.isAttached()) {
			pilotPointInfoDialog_.setInfo(info);
			owner_.getOwner().addWindow(pilotPointInfoDialog_);
		}
	}

	/**
	 * Shows loadcase factor information.
	 *
	 * @param info
	 *            Loadcase factor information to show.
	 */
	public void showLoadcaseFactorInfo(LoadcaseFactorInfo info) {
		if (!loadcaseFactorInfoDialog_.isAttached()) {
			loadcaseFactorInfoDialog_.setInfo(info);
			owner_.getOwner().addWindow(loadcaseFactorInfoDialog_);
		}
	}

	/**
	 * Starts downloading selected results.
	 */
	private void downloadSelected() {

		// create list to store all downloads
		ArrayList<DownloadInfo> downloads = new ArrayList<>();

		// get downloads
		Panel panel = (Panel) searchTabs_.getSelectedTab();
		VerticalLayout layout = (VerticalLayout) panel.getContent();
		Iterator<Component> iterator = layout.iterator();
		while (iterator.hasNext()) {
			Component c = iterator.next();
			if (c instanceof DownloadableResult) {
				DownloadableResult result = (DownloadableResult) c;
				if (result.isSelected())
					downloads.add(result.getDownloadInfo());
			}
		}

		// no downloads
		if (downloads.isEmpty())
			return;

		// initialize download task
		WikiTask<?> task = null;

		// download spectra
		if (downloads.get(0) instanceof SpectrumInfo) {
			task = new DownloadSpectra(owner_.getOwner());
			for (DownloadInfo info : downloads)
				((DownloadSpectra) task).addSpectrumInfo((SpectrumInfo) info);
		}

		// download pilot points
		else if (downloads.get(0) instanceof PilotPointInfo) {
			task = new DownloadPilotPoints(owner_.getOwner());
			for (DownloadInfo info : downloads)
				((DownloadPilotPoints) task).addPilotPointInfo((PilotPointInfo) info);
		}

		// download loadcase factors
		else if (downloads.get(0) instanceof LoadcaseFactorInfo) {
			task = new DownloadLoadcaseFactors(owner_.getOwner());
			for (DownloadInfo info : downloads)
				((DownloadLoadcaseFactors) task).addLoadcaseFactorInfo((LoadcaseFactorInfo) info);
		}

		// submit task
		owner_.getOwner().submitTask(task);
	}

	/**
	 * Interface for downloadable result.
	 *
	 * @author Murat Artim
	 * @date 9 Mar 2017
	 * @time 13:35:07
	 */
	public interface DownloadableResult {

		/**
		 * Returns true if this result is selected to be downloaded.
		 *
		 * @return True if this result is selected to be downloaded.
		 */
		boolean isSelected();

		/**
		 * Returns the download info.
		 *
		 * @return The download info.
		 */
		DownloadInfo getDownloadInfo();
	}
}
