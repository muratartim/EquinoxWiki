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
package com.example.afm_wiki.task;

import java.util.ArrayList;

import com.example.afm_wiki.WikiUI;
import com.example.afm_wiki.data.DownloadInfo;
import com.vaadin.server.Page;
import com.vaadin.ui.Notification;

/**
 * Abstract class for search task.
 *
 * @author Murat Artim
 * @date 1 Mar 2017
 * @time 17:15:36
 */
public abstract class SearchTask extends WikiTask<ArrayList<DownloadInfo>> {

	/** Serial ID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates search task.
	 *
	 * @param ui
	 *            The owner user interface.
	 */
	public SearchTask(WikiUI ui) {
		super(ui);
	}

	@Override
	protected void succeeded(ArrayList<DownloadInfo> result, WikiUI ui) {

		// call super method
		super.succeeded(result, ui);

		// no results found
		if ((result == null) || result.isEmpty()) {
			Notification n = new Notification("");
			n.setCaption("OOPS!");
			n.setStyleName("tray");
			n.setDescription("Your search did not match any files.<br>Suggestions:<UL><LI>Make sure all words are spelled correctly.<LI>Try different keywords.<LI>Try more general keywords.</UL>");
			n.setHtmlContentAllowed(true);
			n.show(Page.getCurrent());
		}

		// results found
		else {
			ui.getResultsView().getSearchResults().addSearchResults(result);
			ui.showResultsView();
		}
	}
}
