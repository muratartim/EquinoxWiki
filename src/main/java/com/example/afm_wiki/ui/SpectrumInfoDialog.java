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

import java.util.HashMap;

import com.example.afm_wiki.data.SpectrumInfo;
import com.example.afm_wiki.data.SpectrumInfo.SpectrumInfoType;
import com.example.afm_wiki.utility.Utility;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * Class for spectrum info dialog.
 *
 * @author Murat Artim
 * @date 5 Mar 2017
 * @time 14:30:57
 */
public class SpectrumInfoDialog extends Window {

	/** Serial ID. */
	private static final long serialVersionUID = 1L;

	/** The owner panel of this dialog. */
	private final SearchResults owner_;

	/** Info labels. */
	private final HashMap<SpectrumInfoType, Label> labels_;

	/**
	 * Creates spectrum info dialog.
	 *
	 * @param owner
	 *            The owner panel of this dialog.
	 */
	public SpectrumInfoDialog(SearchResults owner) {

		// set owner
		owner_ = owner;

		// create layout
		VerticalLayout layout = new VerticalLayout();
		layout.setSizeUndefined();
		layout.setMargin(true);
		layout.addStyleName("infoPanel");

		// setup labels
		labels_ = new HashMap<>();
		for (SpectrumInfoType type : SpectrumInfoType.values()) {

			// ID or spectrum name (skip)
			if (type.equals(SpectrumInfoType.ID) || type.equals(SpectrumInfoType.NAME))
				continue;

			// create caption
			Label caption = new Label(type.getLabel() + ":");
			caption.setWidth(165, Unit.PIXELS);
			caption.addStyleName("searchItemCaption");

			// create label
			Label label = new Label();
			label.setWidth(320, Unit.PIXELS);
			label.addStyleName("searchItemLabel");
			labels_.put(type, label);

			// create layout for row
			HorizontalLayout rowLayout = new HorizontalLayout();
			rowLayout.setSizeUndefined();
			rowLayout.setSpacing(true);

			// add labels to layout
			rowLayout.addComponents(caption, label);
			layout.addComponent(rowLayout);
		}

		// create settings dialog
		setCaption("   Spectrum name goes here..");
		setIcon(FontAwesome.AREA_CHART);
		setContent(layout);
		setSizeUndefined();
		center();
		setModal(true);
		setResizable(false);
		setDraggable(false);
	}

	/**
	 * Returns the owner of this dialog.
	 *
	 * @return The owner of this dialog.
	 */
	public SearchResults getOwner() {
		return owner_;
	}

	/**
	 * Sets spectrum info to this dialog.
	 *
	 * @param info
	 *            Spectrum info to set.
	 */
	public void setInfo(SpectrumInfo info) {

		// set caption
		setCaption("  " + (String) info.getInfo(SpectrumInfoType.NAME));

		// set info to labels
		for (SpectrumInfoType type : SpectrumInfoType.values()) {

			// ID or spectrum name (skip)
			if (type.equals(SpectrumInfoType.ID) || type.equals(SpectrumInfoType.NAME))
				continue;

			// get value
			String value = null;
			if (type.equals(SpectrumInfoType.DATA_SIZE))
				value = Utility.readableFileSize((long) info.getInfo(SpectrumInfoType.DATA_SIZE));
			else if (type.equals(SpectrumInfoType.PILOT_POINTS))
				value = Integer.toString((int) info.getInfo(SpectrumInfoType.PILOT_POINTS));
			else if (type.equals(SpectrumInfoType.MULT_TABLES))
				value = Integer.toString((int) info.getInfo(SpectrumInfoType.MULT_TABLES));
			else
				value = (String) info.getInfo(type);

			// set to label
			labels_.get(type).setValue(((value == null) || value.trim().isEmpty()) ? "-" : value);
		}
	}
}
