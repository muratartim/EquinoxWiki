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

import java.util.concurrent.ExecutorService;

import javax.servlet.ServletContext;

import com.example.afm_wiki.WikiUI;
import com.example.afm_wiki.task.WikiTask;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.Window;

/**
 * Class for progress dialog.
 *
 * @author Murat Artim
 * @date 1 Mar 2017
 * @time 07:32:26
 */
public class ProgressDialog extends Window {

	/** Serial ID. */
	private static final long serialVersionUID = 1L;

	/** The owner user interface. */
	private final WikiUI owner_;

	/** Progress bar. */
	private final ProgressBar progress_;

	/** Info label. */
	private final Label info_;

	/**
	 * Creates progress dialog.
	 *
	 * @param owner
	 *            The owner user interface.
	 */
	public ProgressDialog(WikiUI owner) {

		// set owner
		owner_ = owner;

		// create settings layout
		AbsoluteLayout layout = new AbsoluteLayout();
		layout.setWidth(200, Unit.PIXELS);
		layout.setHeight(200, Unit.PIXELS);

		// create progress bar
		progress_ = new ProgressBar();
		progress_.setImmediate(true);
		progress_.setWidth(100, Unit.PIXELS);
		layout.addComponent(progress_, "top: 90; left: 50;");

		// create info label
		info_ = new Label("S e a r c h i n g");
		info_.setImmediate(true);
		info_.addStyleName("progressInfo");
		info_.setWidth(200, Unit.PIXELS);
		layout.addComponent(info_, "top: 110; left: 0;");

		// create settings dialog
		setCaption(null);
		setContent(layout);
		setSizeUndefined();
		center();
		setModal(true);
		setResizable(false);
		setDraggable(false);
		setClosable(false);
	}

	/**
	 * Returns owner user interface.
	 *
	 * @return The owner user interface.
	 */
	public WikiUI getOwner() {
		return owner_;
	}

	/**
	 * Sets the value of this progress bar. The value is a float between 0 and 1 where 0 represents no progress at all and 1 represents fully completed.
	 *
	 * @param value
	 *            Progress value.
	 */
	public void setProgressValue(float value) {
		progress_.setValue(value);
	}

	/**
	 * Sets progress info.
	 *
	 * @param info
	 *            Information string.
	 */
	public void setProgressInfo(String info) {
		info_.setValue(info);
	}

	/**
	 * Submits given task to thread pool.
	 *
	 * @param task
	 *            Task to submit.
	 */
	public void submitTask(WikiTask<?> task) {

		// get thread pool
		ServletContext context = VaadinServlet.getCurrent().getServletContext();
		ExecutorService threadPool = (ExecutorService) context.getAttribute("threadPool");

		// submit task
		threadPool.submit(task);
	}
}
