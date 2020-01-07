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

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.example.afm_wiki.WikiUI;
import com.example.afm_wiki.data.PilotPointInfo;
import com.example.afm_wiki.data.PilotPointInfo.PilotPointInfoType;
import com.example.afm_wiki.utility.Utility;

import snaq.db.ConnectionPool;

/**
 * Class for download pilot point task.
 *
 * @author Murat Artim
 * @date 7 Mar 2017
 * @time 17:44:04
 */
public class DownloadPilotPoint extends WikiTask<File> {

	/** Serial ID. */
	private static final long serialVersionUID = 1L;

	/** Pilot point info. */
	private final PilotPointInfo info_;

	/**
	 * Creates download pilot point task.
	 *
	 * @param info
	 *            Download info.
	 * @param ui
	 *            The owner user interface.
	 */
	public DownloadPilotPoint(PilotPointInfo info, WikiUI ui) {

		// create task
		super(ui);

		// set download info
		info_ = info;
	}

	@Override
	protected File run(ConnectionPool databaseConnectionPool) throws Exception {

		// set progress and info
		setProgressInfo("D o w n l o a d i n g");
		setProgressValue(0);

		// create download path
		Path downloadPath = Utility.createDownloadFilePath();

		// get connection to database
		try (Connection connection = databaseConnectionPool.getConnection(3000)) {

			// create statement
			try (Statement statement = connection.createStatement()) {

				// get pilot point ID
				int ppID = (int) info_.getInfo(PilotPointInfoType.ID);

				// download
				downloadArchive(statement, ppID, downloadPath);
			}
		}

		// return download path
		return downloadPath.toFile();
	}

	@Override
	protected void succeeded(File result, WikiUI ui) {

		// call super method
		super.succeeded(result, ui);

		// create download file name
		String fileName = (String) info_.getInfo(PilotPointInfoType.NAME);
		fileName = Utility.correctFileName(fileName + ".zip");

		// download
		ui.download(result, fileName);
	}

	/**
	 * Downloads pilot point from the global database.
	 *
	 * @param statement
	 *            Database statement.
	 * @param ppID
	 *            Pilot point ID to download.
	 * @param downloadPath
	 *            Path to download file.
	 * @throws Exception
	 *             If exception occurs during process.
	 */
	private static void downloadArchive(Statement statement, int ppID, Path downloadPath) throws Exception {
		try (ResultSet resultSet = statement.executeQuery("select data from pilot_point_data where id = " + ppID)) {
			if (resultSet.next()) {
				Blob blob = resultSet.getBlob("data");
				Files.copy(blob.getBinaryStream(), downloadPath, StandardCopyOption.REPLACE_EXISTING);
				blob.free();
			}
		}
	}
}
