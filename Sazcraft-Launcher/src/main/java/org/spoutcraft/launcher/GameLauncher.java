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

import java.applet.Applet;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.logging.Level;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.spoutcraft.launcher.api.Event;
import org.spoutcraft.launcher.api.Launcher;
import org.spoutcraft.launcher.entrypoint.SpoutcraftLauncher;
import org.spoutcraft.launcher.exceptions.CorruptedMinecraftJarException;
import org.spoutcraft.launcher.exceptions.MinecraftVerifyException;
import org.spoutcraft.launcher.launch.MinecraftAppletEnglober;
import org.spoutcraft.launcher.launch.MinecraftLauncher;
import org.spoutcraft.launcher.skin.components.LoginFrame;
import org.spoutcraft.launcher.util.Utils;

public class GameLauncher extends JFrame implements WindowListener {
	private static final long serialVersionUID = 1L;
	private MinecraftAppletEnglober minecraft;

	public static final int RETRYING_LAUNCH = -1;
	public static final int ERROR_IN_LAUNCH = 0;
	public static final int SUCCESSFUL_LAUNCH = 1;

	public GameLauncher() {
		super("Sazcraft");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(true);
		this.addWindowListener(this);
		setIconImage(Toolkit.getDefaultToolkit().getImage(LoginFrame.spoutcraftIcon));
	}

	public void runGame(String user, String session, String downloadTicket) {
		Dimension size = WindowMode.getModeById(Settings.getWindowModeId()).getDimension(this);
		Point centeredLoc = WindowMode.getModeById(Settings.getWindowModeId()).getCenteredLocation(Launcher.getLoginFrame());

		this.setLocation(centeredLoc);
		this.setSize(size);

		Launcher.getGameUpdater().setWaiting(true);
		while (!Launcher.getGameUpdater().isFinished()) {
			try {
				Thread.sleep(100);
			}
			catch (InterruptedException ignore) { }
		}

		Applet applet = null;
		try {
			applet = MinecraftLauncher.getMinecraftApplet(Launcher.getGameUpdater().getBuild().getLibraries());
		} catch (CorruptedMinecraftJarException corruption) {
			corruption.printStackTrace();
		} catch (MinecraftVerifyException verify) {
			Launcher.clearCache();
			JOptionPane.showMessageDialog(getParent(), "Your installation is corrupt, and has been cleaned. \nTry to login again.\n\n If that fails, close and restart this launcher.");
			this.setVisible(false);
			this.dispose();
			Launcher.getLoginFrame().enableForm();
			return;
		}
		if (applet == null) {
			String message = "Failed to launch Spoutcraft!";
			this.setVisible(false);
			JOptionPane.showMessageDialog(getParent(), message);
			this.dispose();
			Launcher.getLoginFrame().enableForm();
			return;
		}

		StartupParameters params = Utils.getStartupParameters();

		minecraft = new MinecraftAppletEnglober(applet);
		minecraft.addParameter("username", user);
		minecraft.addParameter("sessionid", session);
		minecraft.addParameter("downloadticket", downloadTicket);
		minecraft.addParameter("spoutcraftlauncher", "true");
		minecraft.addParameter("portable", params.isPortable() + "");
		minecraft.addParameter("working_directory", Utils.getWorkingDirectory().getAbsolutePath());
		if (params.getServer() != null) {
			minecraft.addParameter("server", params.getServer());
			if (params.getPort() != null) {
				minecraft.addParameter("port", params.getPort());
			} else {
				minecraft.addParameter("port", "25565");
			}
		} else if (Settings.getDirectJoin() != null && Settings.getDirectJoin().length() > 0) {
			String address = Settings.getDirectJoin();
			String port = "25565";
			if (address.contains(":")) {
				String[] s = address.split(":");
				address = s[0];
				port = s[1];
			}
			minecraft.addParameter("server", address);
			minecraft.addParameter("port", port);
		}
		if (params.getProxyHost() != null) {
			minecraft.addParameter("proxy_host", params.getProxyHost());
		}
		if (params.getProxyPort() != null) {
			minecraft.addParameter("proxy_port", params.getProxyPort());
		}
		if (params.getProxyUser() != null) {
			minecraft.addParameter("proxy_user", params.getProxyUser());
		}
		if (params.getProxyPassword() != null) {
			minecraft.addParameter("proxy_pass", params.getProxyPassword());
		}
		//minecraft.addParameter("fullscreen", WindowMode.getModeById(Settings.getWindowModeId()) == WindowMode.FULL_SCREEN ? "true" : "false");

		applet.setStub(minecraft);
		this.add(minecraft);

		validate();
		this.setVisible(true);
		// TODO: Incredibly hacky fix for grey screens due to Minecraft not making the resources directory.
		// TODO: Someone fix this better (or not, no harm as it just works)
		new File(Utils.getWorkingDirectory(), "resources").mkdirs();
		try {
			minecraft.init();
			minecraft.setSize(getWidth(), getHeight());
			minecraft.start();
			Launcher.getLoginFrame().onEvent(Event.GAME_LAUNCH);
		} catch (Throwable t) {
			Launcher.getLogger().log(Level.SEVERE, "Unable to launch Sazcraft", t);
			SpoutcraftLauncher.flush();
		}
		return;
	}

	public void windowOpened(WindowEvent e) {
	}

	public void windowClosing(WindowEvent e) {
		SpoutcraftLauncher.destroyConsole();
		if (this.minecraft != null) {
			this.minecraft.stop();
			this.minecraft.destroy();

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) { }
		}
		System.out.println("Exiting Sazcraft");
		this.dispose();
		System.exit(0);
	}

	public void windowClosed(WindowEvent e) {
	}

	public void windowIconified(WindowEvent e) {
	}

	public void windowDeiconified(WindowEvent e) {
	}

	public void windowActivated(WindowEvent e) {
	}

	public void windowDeactivated(WindowEvent e) {
	}
}
