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

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.example.afm_wiki.data.PilotPointImageType;
import com.example.afm_wiki.data.PilotPointInfo;
import com.example.afm_wiki.data.PilotPointInfo.PilotPointInfoType;
import com.example.afm_wiki.task.GetPilotPointImage;
import com.example.afm_wiki.utility.Utility;
import com.vaadin.server.ClassResource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * Class for pilot point image page.
 *
 * @author Murat Artim
 * @date 6 Mar 2017
 * @time 17:21:10
 */
public class PilotPointImagePage extends VerticalLayout {

	/** Serial ID. */
	private static final long serialVersionUID = 1L;

	/** The owner window of this page. */
	private final PilotPointInfoDialog owner_;

	/** Pilot point image. */
	private final Image image_, noImage_;

	/** Labels. */
	private final Label noImageLabel_;

	/** Pilot point image type. */
	private final PilotPointImageType imageType_;

	/** Image request indicator. */
	private boolean imageRequested_ = false;

	/**
	 * Create pilot point image page.
	 *
	 * @param owner
	 *            The owner window of this page.
	 * @param imageType
	 *            Pilot point image type.
	 */
	public PilotPointImagePage(PilotPointInfoDialog owner, PilotPointImageType imageType) {

		// set owner
		owner_ = owner;
		imageType_ = imageType;

		// setup layout
		if (!imageType_.equals(PilotPointImageType.IMAGE))
			addStyleName("pilotPointImagePage");
		setWidth(501, Unit.PIXELS);
		setHeight(438, Unit.PIXELS);
		setMargin(true);
		setSpacing(true);

		// create components
		image_ = new Image();
		image_.setWidth(477, Unit.PIXELS);
		image_.setHeight(414, Unit.PIXELS);

		// setup no image label and image
		noImage_ = new Image();
		noImage_.setSource(new ClassResource("image/sad.png"));
		noImageLabel_ = new Label(imageType_.getPageName() + " not available.");
		noImageLabel_.addStyleName("noImageLabel");
		noImageLabel_.setWidth("100%");
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
	 * Requests image of this page.
	 *
	 * @param info
	 *            Pilot point info.
	 */
	public void requestImage(PilotPointInfo info) {

		// image already requested
		if (imageRequested_)
			return;

		// remove all components
		removeAllComponents();

		// request pilot point image
		imageRequested_ = true;
		owner_.getOwner().getOwner().getOwner().submitTask(new GetPilotPointImage(info.getID(), imageType_, this, owner_.getOwner().getOwner().getOwner()));
	}

	/**
	 * Sets pilot point image to this panel.
	 *
	 * @param imageBytes
	 *            Image bytes.
	 */
	public void setPilotPointImage(byte[] imageBytes) {

		// filename should be unique in run time
		String fileName = (String) owner_.getInfo().getInfo(PilotPointInfoType.NAME);
		fileName += "_ID" + owner_.getInfo().getID();
		fileName += "_" + imageType_.getTableName();
		fileName = Utility.correctFileName(fileName);

		// create stream resource
		StreamResource resource = new StreamResource(new StreamSource() {

			/** Serial ID. */
			private static final long serialVersionUID = 1L;

			@Override
			public InputStream getStream() {
				return new ByteArrayInputStream(imageBytes);
			}
		}, fileName);

		// set image
		image_.setSource(resource);

		// add image to layout
		removeAllComponents();
		addComponent(image_);
		setComponentAlignment(image_, Alignment.MIDDLE_CENTER);
	}

	/**
	 * Removes all components and sets no image message.
	 */
	public void setNoImageMessage() {
		removeAllComponents();
		addComponent(noImage_);
		setComponentAlignment(noImage_, Alignment.BOTTOM_CENTER);
		addComponent(noImageLabel_);
		setComponentAlignment(noImageLabel_, Alignment.TOP_CENTER);
	}

	/**
	 * Resets this page.
	 */
	public void reset() {
		imageRequested_ = false;
		removeAllComponents();
	}
}
