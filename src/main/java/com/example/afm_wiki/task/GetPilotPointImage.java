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

import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.example.afm_wiki.WikiUI;
import com.example.afm_wiki.data.PilotPointImageType;
import com.example.afm_wiki.ui.PilotPointImagePage;

import snaq.db.ConnectionPool;

/**
 * Class for get pilot point image task.
 *
 * @author Murat Artim
 * @date 7 Mar 2017
 * @time 11:09:30
 */
public class GetPilotPointImage extends WikiTask<byte[]> {

	/** Serial ID. */
	private static final long serialVersionUID = 1L;

	/** Pilot point ID. */
	private final int pilotPointID_;

	/** Requesting panel. */
	private final PilotPointImagePage panel_;

	/** Pilot point image type. */
	private final PilotPointImageType imageType_;

	/**
	 * Creates get pilot point image task.
	 *
	 * @param pilotPointID
	 *            Pilot point ID.
	 * @param imageType
	 *            Pilot point image type.
	 * @param panel
	 *            Requesting panel.
	 * @param ui
	 *            The owner user interface.
	 */
	public GetPilotPointImage(int pilotPointID, PilotPointImageType imageType, PilotPointImagePage panel, WikiUI ui) {

		// create task
		super(ui);

		// set attributes
		pilotPointID_ = pilotPointID;
		imageType_ = imageType;
		panel_ = panel;
	}

	@Override
	protected byte[] run(ConnectionPool databaseConnectionPool) throws Exception {

		// set progress and info
		setProgressInfo("D o w n l o a d i n g");
		setProgressValue(0);

		// initialize byte array
		byte[] imageBytes = null;

		// get connection to database
		try (Connection connection = databaseConnectionPool.getConnection(3000)) {

			// create statement
			try (Statement statement = connection.createStatement()) {

				// create and execute query
				String sql = "select image from " + imageType_.getTableName();
				sql += " where id = " + pilotPointID_;
				try (ResultSet resultSet = statement.executeQuery(sql)) {
					while (resultSet.next()) {

						// set image
						Blob blob = resultSet.getBlob("image");
						if (blob != null) {
							imageBytes = blob.getBytes(1L, (int) blob.length());
							blob.free();
						}
					}
				}
			}
		}

		// return image bytes
		return imageBytes;
	}

	@Override
	protected void succeeded(byte[] result, WikiUI ui) {

		// call super method
		super.succeeded(result, ui);

		// no results found
		if ((result == null) || (result.length == 0))
			panel_.setNoImageMessage();

		// results found
		else
			panel_.setPilotPointImage(result);
	}
}
