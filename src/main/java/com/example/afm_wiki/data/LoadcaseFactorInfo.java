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

import java.util.HashMap;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Class for loadcase factor info.
 *
 * @author Murat Artim
 * @date Feb 29, 2016
 * @time 11:32:49 AM
 */
public class LoadcaseFactorInfo implements DownloadInfo {

	/** Serial ID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Enumeration for loadcase factor info types.
	 *
	 * @author Murat Artim
	 * @date May 4, 2014
	 * @time 9:41:10 AM
	 */
	public enum LoadcaseFactorInfoType {

		/** Loadcase factor info type. */
		ID("Loadcase factor ID", "id"), NAME("Loadcase factor name", "name"), SPECTRUM_NAME("Spectrum name", "spectrum_name"), PILOT_POINT_NAME("Pilot point name", "pilot_point_name"), AC_PROGRAM("A/C program", "ac_program"), AC_SECTION("A/C section", "ac_section"), FAT_MISSION("Fatigue mission", "fat_mission"), ISSUE("Issue", "issue"), DELIVERY_REF("Delivery reference", "delivery_ref_num"), DESCRIPTION("Description", "description");

		/** Database column name. */
		private final String label_, columnName_;

		/**
		 * Creates loadcase factor info constant.
		 *
		 * @param label
		 *            The label of info type.
		 * @param columnName
		 *            Database column name.
		 */
		LoadcaseFactorInfoType(String label, String columnName) {
			label_ = label;
			columnName_ = columnName;
		}

		/**
		 * Returns the database column name of the info.
		 *
		 * @return Database column name.
		 */
		public String getColumnName() {
			return columnName_;
		}

		/**
		 * Returns the label of the info type.
		 *
		 * @return The label of the info type.
		 */
		public String getLabel() {
			return label_;
		}

		@Override
		public String toString() {
			return label_;
		}
	}

	/** Map containing the info. */
	private final HashMap<LoadcaseFactorInfoType, Object> info_ = new HashMap<>();

	@Override
	public int getID() {
		return (int) getInfo(LoadcaseFactorInfoType.ID);
	}

	/**
	 * Returns the demanded loadcase factor info.
	 *
	 * @param type
	 *            Info type.
	 * @return The demanded loadcase factor info.
	 */
	public Object getInfo(LoadcaseFactorInfoType type) {
		return info_.get(type);
	}

	/**
	 * Sets loadcase factor info.
	 *
	 * @param type
	 *            Info type.
	 * @param info
	 *            Info to set.
	 */
	public void setInfo(LoadcaseFactorInfoType type, Object info) {
		info_.put(type, info);
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(77, 87).append(info_).toHashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof LoadcaseFactorInfo))
			return false;
		if (o == this)
			return true;
		LoadcaseFactorInfo info = (LoadcaseFactorInfo) o;
		return new EqualsBuilder().append(info_, info.info_).isEquals();
	}
}
