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
import com.example.afm_wiki.data.DownloadInfo;
import com.example.afm_wiki.data.LoadcaseFactorInfo;
import com.example.afm_wiki.data.LoadcaseFactorInfo.LoadcaseFactorInfoType;
import com.example.afm_wiki.data.LoadcaseFactorSearchInput;
import com.example.afm_wiki.data.SearchItem;
import com.example.afm_wiki.ui.SearchField;

import snaq.db.ConnectionPool;

/**
 * Class for advanced loadcase factor search task.
 *
 * @author Murat Artim
 * @date 5 Mar 2017
 * @time 13:38:09
 */
public class AdvancedLoadcaseFactorSearch extends SearchTask {

	/** Serial ID. */
	private static final long serialVersionUID = 1L;

	/** Search input. */
	private final LoadcaseFactorSearchInput input_;

	/**
	 * Creates advanced loadcase factor search task.
	 *
	 * @param ui
	 *            The owner user interface.
	 * @param input
	 *            Search input.
	 */
	public AdvancedLoadcaseFactorSearch(WikiUI ui, LoadcaseFactorSearchInput input) {

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
		String sql = "select id, spectrum_name, pilot_point_name, name, ac_program, ac_section, fat_mission, issue, delivery_ref_num, description from mult_tables where ";
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

							// get spectrum info
							LoadcaseFactorInfo info = new LoadcaseFactorInfo();
							info.setInfo(LoadcaseFactorInfoType.ID, resultSet.getInt("id"));
							info.setInfo(LoadcaseFactorInfoType.SPECTRUM_NAME, resultSet.getString("spectrum_name"));
							info.setInfo(LoadcaseFactorInfoType.PILOT_POINT_NAME, resultSet.getString("pilot_point_name"));
							info.setInfo(LoadcaseFactorInfoType.NAME, resultSet.getString("name"));
							info.setInfo(LoadcaseFactorInfoType.AC_PROGRAM, resultSet.getString("ac_program"));
							info.setInfo(LoadcaseFactorInfoType.AC_SECTION, resultSet.getString("ac_section"));
							info.setInfo(LoadcaseFactorInfoType.FAT_MISSION, resultSet.getString("fat_mission"));
							info.setInfo(LoadcaseFactorInfoType.ISSUE, resultSet.getString("issue"));
							info.setInfo(LoadcaseFactorInfoType.DELIVERY_REF, resultSet.getString("delivery_ref_num"));
							info.setInfo(LoadcaseFactorInfoType.DESCRIPTION, resultSet.getString("description"));

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
		sql += buildQueryForStringBasedItem(LoadcaseFactorInfoType.NAME);
		sql += buildQueryForStringBasedItem(LoadcaseFactorInfoType.SPECTRUM_NAME);
		sql += buildQueryForStringBasedItem(LoadcaseFactorInfoType.PILOT_POINT_NAME);
		sql += buildQueryForStringBasedItem(LoadcaseFactorInfoType.AC_PROGRAM);
		sql += buildQueryForStringBasedItem(LoadcaseFactorInfoType.AC_SECTION);
		sql += buildQueryForStringBasedItem(LoadcaseFactorInfoType.FAT_MISSION);
		sql += buildQueryForStringBasedItem(LoadcaseFactorInfoType.ISSUE);
		sql += buildQueryForStringBasedItem(LoadcaseFactorInfoType.DELIVERY_REF);
		sql += buildQueryForStringBasedItem(LoadcaseFactorInfoType.DESCRIPTION);

		// remove trailing operator
		if (sql.endsWith(" and "))
			sql = sql.substring(0, sql.lastIndexOf(" and "));
		else if (sql.endsWith(" or "))
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
	private String buildQueryForStringBasedItem(LoadcaseFactorInfoType type) throws Exception {

		// get search item
		SearchItem item = input_.getInput(type);

		// search item exists
		String sql = "";
		if (item != null) {

			// get database column name
			String columnName = type.getColumnName();

			// create query
			sql += input_.getCase() ? "upper(" + columnName + ") like " : columnName + " like ";
			String filter = item.getCriteria();
			String value = item.getValue().toString();
			if (filter.equals(SearchField.CONTAINS))
				sql += input_.getCase() ? "upper('%" + value + "%')" : "'%" + value + "%'"; // contains
			else if (filter.equals(SearchField.EQUALS))
				sql += input_.getCase() ? "upper('" + value + "')" : "'" + value + "'"; // equals
			else if (filter.equals(SearchField.STARTS_WITH))
				sql += input_.getCase() ? "upper('" + value + "%')" : "'" + value + "%'"; // starts with
			else if (filter.equals(SearchField.ENDS_WITH))
				sql += input_.getCase() ? "upper('%" + value + "')" : "'%" + value + "'"; // ends with
			sql += input_.getOperator() ? " and " : " or ";
		}

		// return query
		return sql;
	}
}
