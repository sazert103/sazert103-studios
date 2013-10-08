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
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class LiteTextBox extends JTextField implements FocusListener {
	private static final long serialVersionUID = 1L;
	protected final JLabel label;
	public LiteTextBox(JFrame parent, String label) {
		this.label = new JLabel(label);
		addFocusListener(this);
		parent.getContentPane().add(this.label);
		this.setBackground(new Color(220, 220, 220));
		this.setBorder(new LiteBorder(5, getBackground()));
		this.label.setForeground(Color.BLACK);
	}

	@Override
	public void setFont(Font font) {
		super.setFont(font);
		if (label != null) {
			label.setFont(font);
		}
	}

	@Override
	public void setBounds(int x, int y, int w, int h) {
		super.setBounds(x, y, w, h);
		label.setBounds(x + 5, y + 3, w - 5, h - 5);
	}

	@Override
	public void setText(String text) {
		super.setText(text);
		label.setVisible((text != null && text.length() > 0) ? false : true);
	}

	public void focusGained(FocusEvent e) {
		label.setVisible(false);
	}

	public void focusLost(FocusEvent e) {
		if (getText().length() == 0) {
			label.setVisible(true);
		}
	}
}
