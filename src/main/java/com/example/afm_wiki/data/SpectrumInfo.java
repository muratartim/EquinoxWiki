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
 * Class for spectrum info.
 *
 * @author Murat Artim
 * @date May 4, 2014
 * @time 9:28:10 AM
 */
public class SpectrumInfo implements DownloadInfo {

	/** Serial ID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Enumeration for spectrum info types.
	 *
	 * @author Murat Artim
	 * @date May 4, 2014
	 * @time 9:41:10 AM
	 */
	public enum SpectrumInfoType {

		/** Spectrum info type. */
		ID("Spectrum ID", "id"), NAME("Spectrum name", "name"), AC_PROGRAM("A/C program", "ac_program"), AC_SECTION("A/C section", "ac_section"), FAT_MISSION("Fatigue mission", "fat_mission"), FAT_MISSION_ISSUE("Fatigue mission issue", "fat_mission_issue"), FLP_ISSUE("FLP issue", "flp_issue"), IFLP_ISSUE("IFLP issue", "iflp_issue"), CDF_ISSUE("CDF issue", "cdf_issue"), DELIVERY_REF("Delivery reference", "delivery_ref"), DATA_SIZE("Download size", "data_size"), PILOT_POINTS("Pilot points", ""), MULT_TABLES("Loadcase factor files", ""), DESCRIPTION("Description", "description");

		/** Database column name. */
		private final String label_, columnName_;

		/**
		 * Creates spectrum info constant.
		 *
		 * @param label
		 *            Label of info type.
		 * @param columnName
		 *            Database column name.
		 */
		SpectrumInfoType(String label, String columnName) {
			label_ = label;
			columnName_ = columnName;
		}

		/**
		 * Returns label of info type.
		 *
		 * @return Label of info type.
		 */
		public String getLabel() {
			return label_;
		}

		/**
		 * Returns the database column name of the info.
		 *
		 * @return Database column name.
		 */
		public String getColumnName() {
			return columnName_;
		}

		@Override
		public String toString() {
			return label_;
		}
	}

	/** Map containing the info. */
	private final HashMap<SpectrumInfoType, Object> info_ = new HashMap<>();

	@Override
	public int getID() {
		return (int) getInfo(SpectrumInfoType.ID);
	}

	/**
	 * Returns the demanded spectrum info.
	 *
	 * @param type
	 *            Info type.
	 * @return The demanded spectrum info.
	 */
	public Object getInfo(SpectrumInfoType type) {
		return info_.get(type);
	}

	/**
	 * Sets spectrum info.
	 *
	 * @param type
	 *            Info type.
	 * @param info
	 *            Info to set.
	 */
	public void setInfo(SpectrumInfoType type, Object info) {
		info_.put(type, info);
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(95, 127).append(info_).toHashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof SpectrumInfo))
			return false;
		if (o == this)
			return true;
		SpectrumInfo info = (SpectrumInfo) o;
		return new EqualsBuilder().append(info_, info.info_).isEquals();
	}
}
