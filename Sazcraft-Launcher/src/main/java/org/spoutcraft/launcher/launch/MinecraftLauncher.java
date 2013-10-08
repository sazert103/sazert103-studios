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
package org.spoutcraft.launcher.launch;

import java.applet.Applet;
import java.io.File;
import java.util.List;

import org.spoutcraft.launcher.exceptions.CorruptedMinecraftJarException;
import org.spoutcraft.launcher.exceptions.MinecraftVerifyException;
import org.spoutcraft.launcher.exceptions.UnknownMinecraftException;
import org.spoutcraft.launcher.rest.Library;
import org.spoutcraft.launcher.util.Utils;

public class MinecraftLauncher {
	private static MinecraftClassLoader loader = null;
	public static MinecraftClassLoader getClassLoader(List<Library> libraries) {
		if (loader == null) {
			File mcBinFolder = new File(Utils.getWorkingDirectory(), "bin");

			File spoutcraftJar = new File(mcBinFolder, "spoutcraft.jar");
			File minecraftJar = new File(mcBinFolder, "minecraft.jar");
			File jinputJar = new File(mcBinFolder, "jinput.jar");
			File lwglJar = new File(mcBinFolder, "lwjgl.jar");
			File lwjgl_utilJar = new File(mcBinFolder, "lwjgl_util.jar");

			File[] files = new File[4 + libraries.size()];

			int index = 0;
			for (Library lib : libraries) {
				File libraryFile = new File(mcBinFolder, "lib" + File.separator + lib.name() + ".jar");
				files[index] = libraryFile;
				index++;
			}

			try {
				files[index + 0] = minecraftJar;
				files[index + 1] = jinputJar;
				files[index + 2] = lwglJar;
				files[index + 3] = lwjgl_utilJar;

				loader = new MinecraftClassLoader(ClassLoader.getSystemClassLoader(), spoutcraftJar, files);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return loader;
	}

	public static void resetClassLoader() {
		loader = null;
	}

	@SuppressWarnings("rawtypes")
	public static Applet getMinecraftApplet(List<Library> libraries) throws CorruptedMinecraftJarException, MinecraftVerifyException {
		File mcBinFolder = new File(Utils.getWorkingDirectory(), "bin");

		try {
			ClassLoader classLoader = getClassLoader(libraries);

			String nativesPath = new File(mcBinFolder, "natives").getAbsolutePath();
			System.setProperty("org.lwjgl.librarypath", nativesPath);
			System.setProperty("net.java.games.input.librarypath", nativesPath);
			System.setProperty("org.lwjgl.util.Debug", "true");
			System.setProperty("org.lwjgl.util.NoChecks", "false");

			Class minecraftClass = classLoader.loadClass("net.minecraft.client.MinecraftApplet");
			return (Applet) minecraftClass.newInstance();
		} catch (ClassNotFoundException ex) {
			throw new CorruptedMinecraftJarException(ex);
		} catch (IllegalAccessException ex) {
			throw new CorruptedMinecraftJarException(ex);
		} catch (InstantiationException ex) {
			throw new CorruptedMinecraftJarException(ex);
		} catch (VerifyError ex) {
			throw new MinecraftVerifyException(ex);
		} catch (Throwable t) {
			throw new UnknownMinecraftException(t);
		}
	}
}
