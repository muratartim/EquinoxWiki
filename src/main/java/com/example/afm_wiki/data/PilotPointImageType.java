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
package com.example.afm_wiki.data;

/**
 * Enumeration for pilot point image type.
 *
 * @author Murat Artim
 * @date May 8, 2016
 * @time 4:52:41 PM
 */
public enum PilotPointImageType {

	/** Pilot point image type. */
	IMAGE("ppImage.png", "pilot_point_image", "Pilot Point Image"), MISSION_PROFILE("missionProfile.png", "pilot_point_mp", "Mission Profile"), LONGEST_FLIGHT("longestFlight.png", "pilot_point_tf_l", "Longest Typical Flight"), FLIGHT_WITH_HIGHEST_OCCURRENCE("flightWithHighestOccurrence.png", "pilot_point_tf_ho", "Flight With Highest Occurrence"), FLIGHT_WITH_MAX_TOTAL_STRESS("flightWithHighestTotalStress.png", "pilot_point_tf_hs", "Flight With Highest Total Stress"), LEVEL_CROSSING("levelCrossing.png", "pilot_point_lc", "Level Crossings"), DAMAGE_ANGLE("damageAngle.png", "pilot_point_da", "Damage Angles"), NUMBER_OF_PEAKS("numberOfPeaks.png", "pilot_point_st_nop", "Typical Flight Number Of Peaks"), FLIGHT_OCCURRENCE("flightOccurrence.png", "pilot_point_st_fo", "Typical Flight Occurrences"), RAINFLOW_HISTOGRAM("rainflowHistogram.png", "pilot_point_st_rh", "Rainflow Histogram"), LOADCASE_DAMAGE_CONTRIBUTION("loadcaseDamageContribution.png", "pilot_point_dc", "Loadcase Damage Contributions"), FLIGHT_DAMAGE_CONTRIBUTION("flightDamageContribution.png", "pilot_point_tf_dc", "Flight Damage Contributions");

	/** Attributes of image type. */
	private final String fileName_, tableName_, pageName_;

	/** Maximum image file size. */
	public static final long MAX_IMAGE_SIZE = 2000000L;

	/**
	 * Creates pilot point image type.
	 *
	 * @param fileName
	 *            File name.
	 * @param tableName
	 *            Table name.
	 * @param pageName
	 *            Page name.
	 */
	PilotPointImageType(String fileName, String tableName, String pageName) {
		fileName_ = fileName;
		tableName_ = tableName;
		pageName_ = pageName;
	}

	/**
	 * Returns file name.
	 *
	 * @return File name.
	 */
	public String getFileName() {
		return fileName_;
	}

	/**
	 * Returns table name.
	 *
	 * @return Table name.
	 */
	public String getTableName() {
		return tableName_;
	}

	/**
	 * Returns page name.
	 *
	 * @return Page name.
	 */
	public String getPageName() {
		return pageName_;
	}

	@Override
	public String toString() {
		return pageName_;
	}
}
