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

import java.io.Serializable;

import org.vaadin.resetbuttonfortextfield.ResetButtonForTextField;

import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.TextField;

/**
 * Class for search field.
 *
 * @author Murat Artim
 * @date 23 Feb 2017
 * @time 14:46:31
 */
public class SearchField implements Serializable {

	/** Serial ID. */
	private static final long serialVersionUID = 1L;

	/** Search filter. */
	public static final String CONTAINS = "Contains", EQUALS = "Equals", STARTS_WITH = "Starts with", ENDS_WITH = "Ends with";

	/** Text field. */
	private final TextField textField_;

	/** Filters. */
	private final MenuItem[] filters_;

	/**
	 * Creates advanced search field.
	 *
	 * @param prompt
	 *            Prompt text.
	 * @param top
	 *            Top position.
	 * @param left
	 *            Left position.
	 * @param layout
	 *            Layout.
	 */
	public SearchField(String prompt, int top, int left, AbsoluteLayout layout) {

		// create text field
		textField_ = new TextField();
		ResetButtonForTextField.extend(textField_);
		textField_.setImmediate(true);
		textField_.addStyleName("advancedSearchField");
		textField_.setInputPrompt(prompt);
		textField_.setWidth(270, Unit.PIXELS);
		textField_.setHeight(37, Unit.PIXELS);
		textField_.setData(CONTAINS);
		layout.addComponent(textField_, "top: " + top + "; left: " + left + ";");

		// create filter button
		MenuBar filterMenu = new MenuBar();
		layout.addComponent(filterMenu, "top: " + top + "; left: " + (left + 278) + ";");

		// add filters
		MenuItem filters = filterMenu.addItem("", null, null);
		filters_ = new MenuItem[] { filters.addItem(CONTAINS, null, null), filters.addItem(EQUALS, null, null), filters.addItem(STARTS_WITH, null, null), filters.addItem(ENDS_WITH, null, null) };

		// setup filters
		for (MenuItem item : filters_) {

			// set checkable
			item.setCheckable(true);

			// set action commend
			item.setCommand(new Command() {

				/** Serial ID. */
				private static final long serialVersionUID = 1L;

				@Override
				public void menuSelected(MenuItem selectedItem) {

					// get state
					boolean isChecked = selectedItem.isChecked();

					// checked
					if (isChecked) {

						// clear all other filters
						for (MenuItem item : filters_)
							if (!item.equals(selectedItem))
								item.setChecked(false);

						// set data to text field
						textField_.setData(item.getText());
					}

					// unchecked (don't allow if no other item is checked)
					else {
						for (MenuItem item : filters_)
							if (item.isChecked())
								return;
						selectedItem.setChecked(true);
					}
				}
			});
		}

		// set contains as checked12
		filters_[0].setChecked(true);
	}

	/**
	 * Returns search value.
	 *
	 * @return Search value.
	 */
	public String getValue() {
		return textField_.getValue();
	}

	/**
	 * Returns search filter.
	 *
	 * @return Search filter.
	 */
	public String getFilter() {
		return (String) textField_.getData();
	}

	/**
	 * Resets this search field.
	 */
	public void reset() {
		textField_.clear();
		filters_[0].setChecked(true);
		filters_[0].getCommand().menuSelected(filters_[0]);
	}

	/**
	 * Sets given action listener to this search field.
	 *
	 * @param listener
	 *            Action listener.
	 */
	public void setActionListener(ShortcutListener listener) {

		// create and add focus listener
		textField_.addFocusListener(new FocusListener() {

			/** Serial ID. */
			private static final long serialVersionUID = 1L;

			@Override
			public void focus(FocusEvent event) {
				textField_.addShortcutListener(listener);
			}
		});

		// create and add blur listener
		textField_.addBlurListener(new BlurListener() {

			/** Serial ID. */
			private static final long serialVersionUID = 1L;

			@Override
			public void blur(BlurEvent event) {
				textField_.removeShortcutListener(listener);
			}
		});
	}

	/**
	 * Removes shortcut listener.
	 * 
	 * @param listener
	 *            Shortcut listener.
	 */
	public void removeShortcutListener(ShortcutListener listener) {
		textField_.removeShortcutListener(listener);
	}
}
