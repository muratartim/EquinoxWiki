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
package com.example.afm_wiki.utility;

import java.util.ArrayList;

import org.vaadin.virkki.paperstack.PaperStack;

import com.vaadin.ui.Component;

/**
 * Class for paper stack with component list.
 *
 * @author Murat Artim
 * @date 8 Mar 2017
 * @time 12:47:25
 */
public class MyPaperStack extends PaperStack {

	/** Serial ID. */
	private static final long serialVersionUID = 1L;

	/** Component list. */
	private final ArrayList<Paper> papers_ = new ArrayList<>();

	/** Current component index. */
	private int currentIndex_ = 0;

	/**
	 * Creates paper stack with component list.
	 */
	public MyPaperStack() {

		// add page listener to paper stack
		addListener(new PageChangeListener() {

			/** Serial ID. */
			private static final long serialVersionUID = 1L;

			@Override
			public void pageChange(PageChangeEvent event) {

				// set current index
				currentIndex_++;
				currentIndex_ %= 2;
			}
		});
	}

	/**
	 * Adds given paper to this stack.
	 *
	 * @param paper
	 *            Paper to add.
	 * @param bg
	 *            Background color.
	 */
	public void addPaper(Paper paper, String bg) {

		// call ancestor
		super.addComponent((Component) paper, bg);

		// add paper to list
		papers_.add(paper);
	}

	/**
	 * Returns the current component.
	 *
	 * @return The current component.
	 */
	public Paper getCurrentPaper() {
		return papers_.get(currentIndex_);
	}

	/**
	 * Interface for paper stack papers.
	 *
	 * @author Murat Artim
	 * @date 8 Mar 2017
	 * @time 13:13:21
	 */
	public interface Paper {

		/**
		 * Called when this paper is shown.
		 */
		void shown();

		/**
		 * Called when this paper is hidden.
		 */
		void hidden();
	}
}
