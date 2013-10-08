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
package org.spoutcraft.launcher.skin.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JProgressBar;

public class LiteProgressBar extends JProgressBar implements Transparent {
	private static final long serialVersionUID = 1L;
	private final TransparentComponent transparency = new TransparentComponent(this, false);

	public LiteProgressBar() {
		setFocusable(false);
		setOpaque(true);
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) transparency.setup(g);
		
		g2d.clearRect(0, 0, getWidth(), getHeight());

		// Draw bar
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, getWidth(), getHeight());

		// Draw progress
		g2d.setColor(Color.BLUE);
		int x = (int) (getWidth() * getPercentComplete());
		g2d.fillRect(0, 0, x, getHeight());

		transparency.cleanup(g2d);
		g2d = (Graphics2D) g;

		if (this.isStringPainted() && getString().length() > 0) {
			g2d.setFont(getFont());

			final int startWidth = (getWidth() - g2d.getFontMetrics().stringWidth(getString())) / 2;
			String white = "";
			int whiteWidth = 0;
			int chars = 0;
			for (int i = 0; i < getString().length(); i++) {
				white += getString().charAt(i);
				whiteWidth = g2d.getFontMetrics().stringWidth(white);
				if (startWidth + whiteWidth > x) {
					break;
				}
				chars++;
			}
			if (chars != getString().length()) {
				white = white.substring(0, white.length() - 1);
				whiteWidth = g2d.getFontMetrics().stringWidth(white);
			}
			float height = getFont().getSize();
			g2d.setColor(Color.WHITE);
			g2d.drawString(white, startWidth, height * 1.5F);
			g2d.setColor(Color.BLACK);
			g2d.drawString(this.getString().substring(chars), whiteWidth + startWidth, height * 1.5F);
		}

		transparency.cleanup(g2d);
	}

	public float getTransparency() {
		return transparency.getTransparency();
	}

	public void setTransparency(float t) {
		transparency.setTransparency(t);
	}

	public float getHoverTransparency() {
		return transparency.getHoverTransparency();
	}

	public void setHoverTransparency(float t) {
		transparency.setHoverTransparency(t);
	}
}
