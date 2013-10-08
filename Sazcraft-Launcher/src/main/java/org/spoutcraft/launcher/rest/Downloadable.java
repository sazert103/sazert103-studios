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

import java.io.File;

import org.spoutcraft.launcher.exceptions.DownloadException;
import org.spoutcraft.launcher.util.DownloadListener;

/**
 * Represents a downloadable file
 */
public interface Downloadable {
	/**
	 * Downloads the file from the maven repository and saves it in the given location
	 *
	 * @param file location to save the file
	 * @param listener download listener, optional
	 */
	public void download(File location, DownloadListener listener) throws DownloadException;

	/**
	 * True if the md5 is a valid match
	 *
	 * @param md5
	 * @return matches
	 */
	public boolean valid(String md5);

	/**
	 * Name of the file
	 *
	 * @return file name
	 */
	public String name();
}
