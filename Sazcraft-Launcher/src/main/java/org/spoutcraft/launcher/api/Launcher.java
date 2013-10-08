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
package org.spoutcraft.launcher.api;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;

import org.spoutcraft.launcher.GameLauncher;
import org.spoutcraft.launcher.GameUpdater;
import org.spoutcraft.launcher.Settings;
import org.spoutcraft.launcher.skin.components.LoginFrame;
import org.spoutcraft.launcher.util.FileUtils;

public class Launcher {
	private static Launcher instance;
	private final Logger logger = Logger.getLogger("org.spoutcraft.launcher.Main");
	private final GameUpdater updater;
	private final GameLauncher launcher;
	private final LoginFrame loginFrame;

	public Launcher(final GameUpdater updater, final GameLauncher launcher, final LoginFrame frame) {
		if (Launcher.instance != null) {
			throw new IllegalArgumentException("You can't have a duplicate launcher");
		}
		this.updater = updater;
		this.launcher = launcher;
		this.loginFrame = frame;

		logger.addHandler(new ConsoleHandler());
		instance = this;
	}

	public static GameUpdater getGameUpdater() {
		if (instance == null) {
			System.out.println("instance is null");
		}
		if (instance.updater == null) {
			System.out.println("updater is null");
		}
		return instance.updater;
	}

	public static Logger getLogger() {
		return instance.logger;
	}

	public static void debug(Object obj) {
		if (Settings.isDebugMode()) {
			System.out.println(obj);
		}
	}

	public static void err(Object obj) {
		System.err.println(obj);
	}

	public static GameLauncher getGameLauncher() {
		return instance.launcher;
	}

	public static boolean clearCache() {
		try {
			FileUtils.deleteDirectory(instance.updater.getUpdateDir());
			FileUtils.deleteDirectory(instance.updater.getBinDir());
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static LoginFrame getLoginFrame() {
		return instance.loginFrame;
	}
}
