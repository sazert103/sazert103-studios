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
package org.spoutcraft.launcher;

import org.spoutcraft.launcher.api.Launcher;
import org.spoutcraft.launcher.api.SpoutcraftDirectories;
import org.spoutcraft.launcher.exceptions.RestfulAPIException;
import org.spoutcraft.launcher.launch.MinecraftLauncher;
import org.spoutcraft.launcher.util.DownloadListener;

public final class GameUpdater extends SpoutcraftDirectories {
	public static final String baseURL = "http://s3.amazonaws.com/MinecraftDownload/";
	public static final String spoutcraftMirrors = "http://get.spout.org/mirrors.yml";

	// Minecraft Updating Arguments
	private String user = "Player";
	private String downloadTicket = "1";
	private String minecraftPass = "";
	private String minecraftSession = "";

	private DownloadListener listener;
	private long validationTime;
	private UpdateThread updateThread;

	public GameUpdater() throws RestfulAPIException {
		updateThread = new UpdateThread();
	}

	public SpoutcraftData getBuild() {
		return updateThread.getBuild();
	}

	public void start() {
		updateThread.start();
	}

	public boolean isFinished() {
		return updateThread.isFinished();
	}

	public void setWaiting(boolean waiting) {
		updateThread.setWaiting(waiting);
	}

	public void onSpoutcraftBuildChange() {
		DownloadListener old = updateThread.getDownloadListener();
		updateThread.setDownloadListener(null);
		updateThread.interrupt();
		MinecraftLauncher.resetClassLoader();
		updateThread = new UpdateThread(updateThread);
		updateThread.setDownloadListener(old);
		start();
	}

	public void setStartValidationTime(long validationTime) {
		this.validationTime = validationTime;
	}

	protected void validationFinished(boolean result) {
		long end = System.currentTimeMillis();
		System.out.println("------------------ Validation Finished  ------------------");
		System.out.println("Finished in " + (end - validationTime) + "ms");
		System.out.println("Result: " + result);
	}

	public void setMinecraftUser(String user) {
		this.user = user;
	}

	public void setDownloadTicket(String downloadTicket) {
		this.downloadTicket = downloadTicket;
	}

	public String getMinecraftUser() {
		return user;
	}

	public String getDownloadTicket() {
		return downloadTicket;
	}

	public String getMinecraftPass() {
		return minecraftPass;
	}

	public String getMinecraftSession() {
		return minecraftSession;
	}

	public void setMinecraftPass(String minecraftPass) {
		this.minecraftPass = minecraftPass;
	}

	public void setMinecraftSession(String minecraftSession) {
		this.minecraftSession = minecraftSession;
	}

	public DownloadListener getDownloadListener() {
		return listener;
	}

	public void setDownloadListener(DownloadListener listener) {
		this.listener = listener;
		updateThread.setDownloadListener(listener);
	}

	public void stateChanged(String message, float progress) {
		if (listener != null) {
			listener.stateChanged(message, progress);
		}
	}

	public void runGame() {
		Launcher.getGameLauncher().runGame(user, minecraftSession, downloadTicket);
	}
}
