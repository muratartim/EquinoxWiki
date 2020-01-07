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
 * Class for pilot point download info.
 *
 * @author Murat Artim
 * @date Feb 12, 2016
 * @time 4:33:18 PM
 */
public class PilotPointInfo implements DownloadInfo {

	/** Serial ID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Enumeration for pilot point info types.
	 *
	 * @author Murat Artim
	 * @date May 4, 2014
	 * @time 9:41:10 AM
	 */
	public enum PilotPointInfoType {

		/** Pilot point info type. */
		ID("Pilot point ID", "id"), SPECTRUM_NAME("Spectrum name", "spectrum_name"), AC_PROGRAM("A/C program", "ac_program"), AC_SECTION("A/C section", "ac_section"), NAME("Pilot point name", "name"), FAT_MISSION("Fatigue mission", "fat_mission"), DESCRIPTION("Description", "description"), ELEMENT_TYPE("Element type", "element_type"), FRAME_RIB_POSITION("Frame/rib position", "frame_rib_position"), STRINGER_POSITION("Stringer position", "stringer_position"), DATA_SOURCE("Data source", "data_source"), GENERATION_SOURCE("Generation source", "generation_source"), DELIVERY_REF_NUM("Delivery reference", "delivery_ref_num"), ISSUE("Pilot point issue", "issue"), MATERIAL_NAME("Material name", "material_name");

		/** Database column name. */
		private final String label_, columnName_;

		/**
		 * Creates pilot point info constant.
		 *
		 * @param label
		 *            The label of info.
		 * @param columnName
		 *            Database column name.
		 */
		PilotPointInfoType(String label, String columnName) {
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
		 * Returns the label of the info.
		 *
		 * @return The label of the info.
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
	private final HashMap<PilotPointInfoType, Object> info_ = new HashMap<>();

	@Override
	public int getID() {
		return (int) getInfo(PilotPointInfoType.ID);
	}

	/**
	 * Returns the demanded pilot point info.
	 *
	 * @param type
	 *            Info type.
	 * @return The demanded pilot point info.
	 */
	public Object getInfo(PilotPointInfoType type) {
		return info_.get(type);
	}

	/**
	 * Sets pilot point info.
	 *
	 * @param type
	 *            Info type.
	 * @param info
	 *            Info to set.
	 */
	public void setInfo(PilotPointInfoType type, Object info) {
		info_.put(type, info);
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(45, 89).append(info_).toHashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof PilotPointInfo))
			return false;
		if (o == this)
			return true;
		PilotPointInfo info = (PilotPointInfo) o;
		return new EqualsBuilder().append(info_, info.info_).isEquals();
	}
}
