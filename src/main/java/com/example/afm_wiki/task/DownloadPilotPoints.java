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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.example.afm_wiki.WikiUI;
import com.example.afm_wiki.data.PilotPointInfo;
import com.example.afm_wiki.data.PilotPointInfo.PilotPointInfoType;
import com.example.afm_wiki.utility.Utility;

import snaq.db.ConnectionPool;

/**
 * Class for download pilot points task.
 *
 * @author Murat Artim
 * @date 10 Mar 2017
 * @time 09:42:48
 */
public class DownloadPilotPoints extends WikiTask<File> {

	/** Serial ID. */
	private static final long serialVersionUID = 1L;

	/** Pilot point info. */
	private final ArrayList<PilotPointInfo> info_;

	/**
	 * Creates download pilot point task.
	 *
	 * @param ui
	 *            The owner user interface.
	 */
	public DownloadPilotPoints(WikiUI ui) {

		// create task
		super(ui);

		// create list of info
		info_ = new ArrayList<>();
	}

	/**
	 * Adds pilot point info to be downloaded.
	 *
	 * @param info
	 *            Pilot point info.
	 */
	public void addPilotPointInfo(PilotPointInfo info) {
		info_.add(info);
	}

	@Override
	protected File run(ConnectionPool databaseConnectionPool) throws Exception {

		// set progress and info
		setProgressInfo("D o w n l o a d i n g");
		setProgressValue(0);

		// create list store download paths
		ArrayList<Path> downloads = new ArrayList<>();

		// get connection to database
		try (Connection connection = databaseConnectionPool.getConnection(3000)) {

			// prepare statement
			try (PreparedStatement statement = connection.prepareStatement("select data from pilot_point_data where id = ?")) {

				// loop over spectrum files
				for (int i = 0; i < info_.size(); i++) {

					// get info
					PilotPointInfo info = info_.get(i);

					// create download path
					Path downloadPath = Utility.createDownloadFilePath();

					// get CDF set ID
					int ppID = (int) info.getInfo(PilotPointInfoType.ID);
					statement.setInt(1, ppID);

					// download archive
					downloadArchive(statement, downloadPath);

					// add to list
					downloads.add(downloadPath);

					// set progress
					setProgressValue((i + 1) / info_.size());
				}
			}
		}

		// zip all downloads
		File downloadPath = Utility.createDownloadFilePath().toFile();
		Utility.zipFiles(downloads, downloadPath);
		return downloadPath;
	}

	@Override
	protected void succeeded(File result, WikiUI ui) {

		// call super method
		super.succeeded(result, ui);

		// create download file name
		String fileName = "download_" + new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(new Date());
		fileName = Utility.correctFileName(fileName + ".zip");

		// download
		ui.download(result, fileName);
	}

	/**
	 * Downloads pilot point archive from the global database.
	 *
	 * @param statement
	 *            Database statement.
	 * @param downloadPath
	 *            Path to download file.
	 * @throws Exception
	 *             If exception occurs during process.
	 */
	private static void downloadArchive(PreparedStatement statement, Path downloadPath) throws Exception {
		try (ResultSet resultSet = statement.executeQuery()) {
			if (resultSet.next()) {
				Blob blob = resultSet.getBlob("data");
				Files.copy(blob.getBinaryStream(), downloadPath, StandardCopyOption.REPLACE_EXISTING);
				blob.free();
			}
		}
	}
}
