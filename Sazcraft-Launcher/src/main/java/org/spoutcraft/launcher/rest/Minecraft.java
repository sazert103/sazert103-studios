/*
 * This file is part of Spoutcraft Launcher.
 *
 * Copyright (c) 2011 Spout LLC <http://www.spout.org/>
 * Spoutcraft Launcher is licensed under the Spout License Version 1.
 *
 * Spoutcraft Launcher is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the Spout License Version 1.
 *
 * Spoutcraft Launcher is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the Spout License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://spout.in/licensev1> for the full license,
 * including the MIT license.
 */
package org.spoutcraft.launcher.rest;

import java.util.Arrays;
import java.util.List;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import org.spoutcraft.launcher.VersionComparator;

public final class Minecraft implements Comparable<Minecraft>{
	private final VersionComparator comparator = new VersionComparator();
	private final String version;
	private final String md5;
	private final Library[] libraries;
	@JsonCreator
	public Minecraft(@JsonProperty("minecraft_version") String version, @JsonProperty("minecraft_hash") String md5, @JsonProperty("libraries") Library[] libraries) {
		this.version = version;
		this.md5 = md5;
		this.libraries = libraries;
	}

	public String getVersion() {
		return version;
	}

	public String getMd5() {
		return md5;
	}

	public List<Library> getLibraries() {
		return Arrays.asList(libraries);
	}

	public int compareTo(Minecraft other) {
		return comparator.compare(version, other.version);
	}

	@Override
	public String toString() {
		return "{Minecraft v" + getVersion() + ", md5: " + getMd5() + "}";
	}

	@Override
	public int hashCode() {
		return getVersion().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Minecraft)) {
			return false;
		}
		Minecraft other = (Minecraft) obj;
		return other.getVersion().equals(getVersion());
	}
}
