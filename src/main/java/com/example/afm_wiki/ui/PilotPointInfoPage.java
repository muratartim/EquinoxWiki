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

import com.example.afm_wiki.data.PilotPointInfo;
import com.example.afm_wiki.data.PilotPointInfo.PilotPointInfoType;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * Class for pilot point info page.
 *
 * @author Murat Artim
 * @date 6 Mar 2017
 * @time 15:08:00
 */
public class PilotPointInfoPage extends VerticalLayout {

	/** Serial ID. */
	private static final long serialVersionUID = 1L;

	/** The owner window of this page. */
	private final PilotPointInfoDialog owner_;

	/** Info labels. */
	private final HashMap<PilotPointInfoType, Label> labels_;

	/**
	 * Create pilot point info page.
	 *
	 * @param owner
	 *            The owner window of this page.
	 */
	public PilotPointInfoPage(PilotPointInfoDialog owner) {

		// set owner
		owner_ = owner;

		// setup layout
		setSizeUndefined();
		setMargin(true);

		// setup labels
		labels_ = new HashMap<>();
		for (PilotPointInfoType type : PilotPointInfoType.values()) {

			// ID or spectrum name (skip)
			if (type.equals(PilotPointInfoType.ID) || type.equals(PilotPointInfoType.NAME))
				continue;

			// create caption
			Label caption = new Label(type.getLabel() + ":");
			caption.setWidth(145, Unit.PIXELS);
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
			addComponent(rowLayout);
		}
	}

	/**
	 * Returns the owner of this page.
	 *
	 * @return The owner of this page.
	 */
	public PilotPointInfoDialog getOwner() {
		return owner_;
	}

	/**
	 * Sets pilot point info to this page.
	 *
	 * @param info
	 *            Spectrum info to set.
	 */
	public void setInfo(PilotPointInfo info) {

		// set info to labels
		for (PilotPointInfoType type : PilotPointInfoType.values()) {

			// ID or spectrum name (skip)
			if (type.equals(PilotPointInfoType.ID) || type.equals(PilotPointInfoType.NAME))
				continue;

			// get value
			String value = (String) info.getInfo(type);

			// set to label
			labels_.get(type).setValue(((value == null) || value.trim().isEmpty()) ? "-" : value);
		}
	}
}
