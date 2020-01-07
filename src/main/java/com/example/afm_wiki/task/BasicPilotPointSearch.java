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
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import com.example.afm_wiki.WikiUI;
import com.example.afm_wiki.data.BasicSearchInput;
import com.example.afm_wiki.data.DownloadInfo;
import com.example.afm_wiki.data.PilotPointInfo;
import com.example.afm_wiki.data.PilotPointInfo.PilotPointInfoType;

import snaq.db.ConnectionPool;

/**
 * Class for basic pilot point search task.
 *
 * @author Murat Artim
 * @date 1 Mar 2017
 * @time 17:54:13
 */
public class BasicPilotPointSearch extends SearchTask {

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
	public BasicPilotPointSearch(WikiUI ui, BasicSearchInput input) {

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
		String sql = "select id, spectrum_name, name, ac_program, ac_section, fat_mission, description, element_type, ";
		sql += "frame_rib_position, stringer_position, data_source, generation_source, delivery_ref_num, issue, ";
		sql += "material_name from pilot_points where ";
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

					// move to last row
					if (resultSet.last()) {

						// get number of results
						int numResults = resultSet.getRow();

						// move to beginning
						resultSet.beforeFirst();

						// loop over segments
						int resultCount = 0;
						while (resultSet.next()) {

							// get pilot point info
							PilotPointInfo info = new PilotPointInfo();
							info.setInfo(PilotPointInfoType.ID, resultSet.getInt("id"));
							info.setInfo(PilotPointInfoType.SPECTRUM_NAME, resultSet.getString("spectrum_name"));
							info.setInfo(PilotPointInfoType.NAME, resultSet.getString("name"));
							info.setInfo(PilotPointInfoType.AC_PROGRAM, resultSet.getString("ac_program"));
							info.setInfo(PilotPointInfoType.AC_SECTION, resultSet.getString("ac_section"));
							info.setInfo(PilotPointInfoType.FAT_MISSION, resultSet.getString("fat_mission"));
							info.setInfo(PilotPointInfoType.DESCRIPTION, resultSet.getString("description"));
							info.setInfo(PilotPointInfoType.ELEMENT_TYPE, resultSet.getString("element_type"));
							info.setInfo(PilotPointInfoType.FRAME_RIB_POSITION, resultSet.getString("frame_rib_position"));
							info.setInfo(PilotPointInfoType.STRINGER_POSITION, resultSet.getString("stringer_position"));
							info.setInfo(PilotPointInfoType.DATA_SOURCE, resultSet.getString("data_source"));
							info.setInfo(PilotPointInfoType.GENERATION_SOURCE, resultSet.getString("generation_source"));
							info.setInfo(PilotPointInfoType.DELIVERY_REF_NUM, resultSet.getString("delivery_ref_num"));
							info.setInfo(PilotPointInfoType.ISSUE, resultSet.getString("issue"));
							info.setInfo(PilotPointInfoType.MATERIAL_NAME, resultSet.getString("material_name"));

							// add info to list
							infos.add(info);

							// set progress
							resultCount++;
							setProgressValue(resultCount / numResults);
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
		sql += buildQueryForStringBasedItem(PilotPointInfoType.SPECTRUM_NAME);
		sql += buildQueryForStringBasedItem(PilotPointInfoType.NAME);
		sql += buildQueryForStringBasedItem(PilotPointInfoType.AC_PROGRAM);
		sql += buildQueryForStringBasedItem(PilotPointInfoType.AC_SECTION);
		sql += buildQueryForStringBasedItem(PilotPointInfoType.FAT_MISSION);
		sql += buildQueryForStringBasedItem(PilotPointInfoType.DESCRIPTION);
		sql += buildQueryForStringBasedItem(PilotPointInfoType.ELEMENT_TYPE);
		sql += buildQueryForStringBasedItem(PilotPointInfoType.FRAME_RIB_POSITION);
		sql += buildQueryForStringBasedItem(PilotPointInfoType.STRINGER_POSITION);
		sql += buildQueryForStringBasedItem(PilotPointInfoType.DATA_SOURCE);
		sql += buildQueryForStringBasedItem(PilotPointInfoType.GENERATION_SOURCE);
		sql += buildQueryForStringBasedItem(PilotPointInfoType.DELIVERY_REF_NUM);
		sql += buildQueryForStringBasedItem(PilotPointInfoType.MATERIAL_NAME);

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
	private String buildQueryForStringBasedItem(PilotPointInfoType type) throws Exception {

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
