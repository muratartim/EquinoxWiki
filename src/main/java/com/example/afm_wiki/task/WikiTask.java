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

import java.io.Serializable;
import java.util.concurrent.Callable;

import javax.servlet.ServletContext;

import org.apache.commons.lang3.text.WordUtils;

import com.example.afm_wiki.WikiUI;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Notification;

import snaq.db.ConnectionPool;

/**
 * Abstract class for wiki task.
 *
 * @author Murat Artim
 * @date 27 Feb 2017
 * @time 20:19:15
 * @param <V>
 *            Result type of this task.
 */
public abstract class WikiTask<V> implements Callable<V>, Serializable {

	/** Serial ID. */
	private static final long serialVersionUID = 1L;

	/** The owner user interface. */
	private final WikiUI ui_;

	/** Database connection pool. */
	private final ConnectionPool databaseConnectionPool_;

	/**
	 * Creates wiki task.
	 *
	 * @param ui
	 *            The owner user interface.
	 */
	public WikiTask(WikiUI ui) {

		// set owner user interface
		ui_ = ui;

		// set database connection pool
		ServletContext servletContext = VaadinServlet.getCurrent().getServletContext();
		databaseConnectionPool_ = (ConnectionPool) servletContext.getAttribute("databaseConnectionPool");
	}

	@Override
	public final V call() throws Exception {

		try {

			// execute task
			V result = run(databaseConnectionPool_);

			// call succeeded code within access
			ui_.access(new Runnable() {

				@Override
				public void run() {
					succeeded(result, ui_);
				}
			});

			// return result
			return result;
		}

		// exception occurred during execution
		catch (Exception e) {

			// call failure code within access
			ui_.access(new Runnable() {

				@Override
				public void run() {
					failed(e, ui_);
				}
			});

			// return null
			return null;
		}
	}

	/**
	 * Runs this task and returns the result. Note that, no UI instance should be accessed from within this method.
	 *
	 * @param databaseConnectionPool
	 *            Database connection pool.
	 * @return The result of this task, or null no result can be computed.
	 * @throws Exception
	 *             If exception occurs during process.
	 */
	protected abstract V run(ConnectionPool databaseConnectionPool) throws Exception;

	/**
	 * Called after the task execution is completed. Note that, UI instances can be accessed from within this method.
	 *
	 * @param result
	 *            The result of this task.
	 * @param ui
	 *            The owner user interface.
	 */
	protected void succeeded(V result, WikiUI ui) {

		// remove progress window
		if (ui.getProgressDialog().isAttached()) {
			ui.removeWindow(ui.getProgressDialog());
			ui.getProgressDialog().setProgressValue(0);
		}
	}

	/**
	 * Called if the task execution fails. Note that, UI instances can be accessed from within this method.
	 *
	 * @param e
	 *            Exception occurred.
	 * @param ui
	 *            The owner user interface.
	 */
	protected void failed(Exception e, WikiUI ui) {

		// remove progress window
		if (ui.getProgressDialog().isAttached()) {
			ui.removeWindow(ui.getProgressDialog());
			ui.getProgressDialog().setProgressValue(0);
		}

		// show notification
		Notification n = new Notification("");
		n.setCaption("OOPS!");
		n.setStyleName("tray error");
		n.setDescription("A server exception occurred during processing your request.<br><br><i>Description:</i><br>" + WordUtils.wrap(e.getMessage(), 60));
		n.setHtmlContentAllowed(true);
		n.show(Page.getCurrent());

		// print stack trace
		e.printStackTrace();
	}

	/**
	 * Sets the value of this progress bar. The value is a float between 0 and 1 where 0 represents no progress at all and 1 represents fully completed.
	 *
	 * @param value
	 *            Progress value.
	 */
	protected void setProgressValue(float value) {

		// set progress within access
		ui_.access(new Runnable() {

			@Override
			public void run() {
				ui_.getProgressDialog().setProgressValue(value);
			}
		});
	}

	/**
	 * Sets progress info text.
	 *
	 * @param info
	 *            Information string.
	 */
	protected void setProgressInfo(String info) {

		// set progress within access
		ui_.access(new Runnable() {

			@Override
			public void run() {
				ui_.getProgressDialog().setProgressInfo(info);
			}
		});
	}
}
