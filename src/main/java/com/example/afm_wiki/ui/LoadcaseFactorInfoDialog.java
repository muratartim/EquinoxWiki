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

import com.example.afm_wiki.data.LoadcaseFactorInfo;
import com.example.afm_wiki.data.LoadcaseFactorInfo.LoadcaseFactorInfoType;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * Class for loadcase factor info dialog.
 *
 * @author Murat Artim
 * @date 9 Mar 2017
 * @time 11:17:33
 */
public class LoadcaseFactorInfoDialog extends Window {

	/** Serial ID. */
	private static final long serialVersionUID = 1L;

	/** The owner panel of this dialog. */
	private final SearchResults owner_;

	/** Info labels. */
	private final HashMap<LoadcaseFactorInfoType, Label> labels_;

	/**
	 * Creates loadcase factor info dialog.
	 *
	 * @param owner
	 *            The owner panel of this dialog.
	 */
	public LoadcaseFactorInfoDialog(SearchResults owner) {

		// set owner
		owner_ = owner;

		// create layout
		VerticalLayout layout = new VerticalLayout();
		layout.setSizeUndefined();
		layout.setMargin(true);
		layout.addStyleName("infoPanel");

		// setup labels
		labels_ = new HashMap<>();
		for (LoadcaseFactorInfoType type : LoadcaseFactorInfoType.values()) {

			// ID or spectrum name (skip)
			if (type.equals(LoadcaseFactorInfoType.ID) || type.equals(LoadcaseFactorInfoType.NAME))
				continue;

			// create caption
			Label caption = new Label(type.getLabel() + ":");
			caption.setWidth(140, Unit.PIXELS);
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
		setCaption("   Loadcase factor name goes here..");
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
	 * Sets loadcase factor info to this dialog.
	 *
	 * @param info
	 *            Loadcase factor info to set.
	 */
	public void setInfo(LoadcaseFactorInfo info) {

		// set caption
		setCaption("  " + (String) info.getInfo(LoadcaseFactorInfoType.NAME));

		// set info to labels
		for (LoadcaseFactorInfoType type : LoadcaseFactorInfoType.values()) {

			// ID or loadcase factor name (skip)
			if (type.equals(LoadcaseFactorInfoType.ID) || type.equals(LoadcaseFactorInfoType.NAME))
				continue;

			// get value
			String value = (String) info.getInfo(type);

			// set to label
			labels_.get(type).setValue(((value == null) || value.trim().isEmpty()) ? "-" : value);
		}
	}
}
