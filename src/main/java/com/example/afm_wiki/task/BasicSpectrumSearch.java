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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import com.example.afm_wiki.WikiUI;
import com.example.afm_wiki.data.BasicSearchInput;
import com.example.afm_wiki.data.DownloadInfo;
import com.example.afm_wiki.data.SpectrumInfo;
import com.example.afm_wiki.data.SpectrumInfo.SpectrumInfoType;

import snaq.db.ConnectionPool;

/**
 * Class for basic spectrum search.
 *
 * @author Murat Artim
 * @date 27 Feb 2017
 * @time 20:29:31
 */
public class BasicSpectrumSearch extends SearchTask {

	/** Serial ID. */
	private static final long serialVersionUID = 1L;

	/** Search input. */
	private final BasicSearchInput input_;

	/**
	 * Creates basic spectrum search task.
	 *
	 * @param ui
	 *            The owner user interface.
	 * @param input
	 *            Search input.
	 */
	public BasicSpectrumSearch(WikiUI ui, BasicSearchInput input) {

		// create search task
		super(ui);

		// set input
		input_ = input;
	}

	@Override
	protected ArrayList<DownloadInfo> run(ConnectionPool databaseConnectionPool) throws Exception {

		// create array of results
		ArrayList<DownloadInfo> infos = new ArrayList<>();

		// create SQL query
		String sql = "select id, name, ac_program, ac_section, fat_mission, fat_mission_issue, flp_issue, iflp_issue, cdf_issue, delivery_ref, description, data_size from spectra where ";
		sql += buildQuery();

		// set progress and info
		setProgressInfo("S e a r c h i n g");
		setProgressValue(0);

		// get connection to database
		try (Connection connection = databaseConnectionPool.getConnection(3000)) {

			// create statement
			try (Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {

				// set max hits
				statement.setMaxRows(input_.getMaxHits());

				// execute query
				try (ResultSet resultSet = statement.executeQuery(sql)) {

					// prepare statement to count connected pilot points
					sql = "select count(id) as numpps from pilot_points where spectrum_name = ?";
					try (PreparedStatement countPPs = connection.prepareStatement(sql)) {

						// prepare statement to count connected multiplication tables
						sql = "select count(id) as nummults from mult_tables where spectrum_name = ?";
						try (PreparedStatement countMultTables = connection.prepareStatement(sql)) {

							// move to last row
							if (resultSet.last()) {

								// get number of results
								int numResults = resultSet.getRow();

								// move to beginning
								resultSet.beforeFirst();

								// loop over segments
								int resultCount = 0;
								while (resultSet.next()) {

									// get spectrum info
									SpectrumInfo info = new SpectrumInfo();
									info.setInfo(SpectrumInfoType.ID, resultSet.getInt("id"));
									info.setInfo(SpectrumInfoType.NAME, resultSet.getString("name"));
									info.setInfo(SpectrumInfoType.DATA_SIZE, resultSet.getLong("data_size"));
									info.setInfo(SpectrumInfoType.AC_PROGRAM, resultSet.getString("ac_program"));
									info.setInfo(SpectrumInfoType.AC_SECTION, resultSet.getString("ac_section"));
									info.setInfo(SpectrumInfoType.FAT_MISSION, resultSet.getString("fat_mission"));
									info.setInfo(SpectrumInfoType.FAT_MISSION_ISSUE, resultSet.getString("fat_mission_issue"));
									info.setInfo(SpectrumInfoType.FLP_ISSUE, resultSet.getString("flp_issue"));
									info.setInfo(SpectrumInfoType.IFLP_ISSUE, resultSet.getString("iflp_issue"));
									info.setInfo(SpectrumInfoType.CDF_ISSUE, resultSet.getString("cdf_issue"));
									info.setInfo(SpectrumInfoType.DELIVERY_REF, resultSet.getString("delivery_ref"));
									info.setInfo(SpectrumInfoType.DESCRIPTION, resultSet.getString("description"));

									// get number of connected pilot points
									int numPPs = 0;
									countPPs.setString(1, resultSet.getString("name"));
									try (ResultSet resultSet2 = countPPs.executeQuery()) {
										if (resultSet2.next())
											numPPs = resultSet2.getInt("numpps");
									}
									info.setInfo(SpectrumInfoType.PILOT_POINTS, numPPs);

									// get number of connected multiplication tables
									int numMult = 0;
									countMultTables.setString(1, resultSet.getString("name"));
									try (ResultSet resultSet2 = countMultTables.executeQuery()) {
										if (resultSet2.next())
											numMult = resultSet2.getInt("nummults");
									}
									info.setInfo(SpectrumInfoType.MULT_TABLES, numMult);

									// add info to list
									infos.add(info);

									// set progress
									resultCount++;
									setProgressValue(resultCount / numResults);
								}
							}
						}
					}
				}

				// reset statement
				statement.setMaxRows(0);
			}
		}

		// return results
		return infos;
	}

	/**
	 * Builds and returns a new SQL query according to search items.
	 *
	 * @return A new SQL query.
	 * @throws Exception
	 *             If exception occurs during process.
	 */
	private String buildQuery() throws Exception {

		// initialize parameters
		String sql = "";

		// add search items
		sql += buildQueryForStringBasedItem(SpectrumInfoType.NAME);
		sql += buildQueryForStringBasedItem(SpectrumInfoType.AC_PROGRAM);
		sql += buildQueryForStringBasedItem(SpectrumInfoType.AC_SECTION);
		sql += buildQueryForStringBasedItem(SpectrumInfoType.FAT_MISSION);
		sql += buildQueryForStringBasedItem(SpectrumInfoType.DELIVERY_REF);
		sql += buildQueryForStringBasedItem(SpectrumInfoType.DESCRIPTION);

		// remove trailing operator
		sql = sql.substring(0, sql.lastIndexOf(" or "));

		// return query
		return sql;
	}

	/**
	 * Builds and returns SQL query for the given type of string based search item.
	 *
	 * @param type
	 *            Type of search item.
	 * @return SQL query.
	 * @throws Exception
	 *             If exception occurs during process.
	 */
	private String buildQueryForStringBasedItem(SpectrumInfoType type) throws Exception {

		// get database column name
		String columnName = type.getColumnName();

		// create query
		String sql = "(";
		for (String keyword : input_.getKeywords()) {
			sql += input_.getCase() ? "upper(" + columnName + ") like upper('%" + keyword + "%')" : columnName + " like '%" + keyword + "%'";
			sql += input_.getOperator() ? " and " : " or ";
		}

		// remove trailing operator
		sql = input_.getOperator() ? sql.substring(0, sql.lastIndexOf(" and ")) : sql.substring(0, sql.lastIndexOf(" or "));
		sql += ")";

		// add or operator
		sql += " or ";

		// return query
		return sql;
	}
}
