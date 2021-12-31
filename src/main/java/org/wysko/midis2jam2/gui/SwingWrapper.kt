/*
 * Copyright (C) 2021 Jacob Wysko
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see https://www.gnu.org/licenses/.
 */

package org.wysko.midis2jam2.gui

import java.awt.BorderLayout
import java.awt.Canvas
import javax.swing.JFrame
import javax.swing.JPanel

/**
 * A wrapper for a [Canvas] that can be used in a Swing application.
 * @param canvas the canvas to wrap
 */
class SwingWrapper(
    canvas: Canvas,
    title: String
) : JFrame(title) {
    init {
        contentPane.layout = BorderLayout()
        JPanel().also {
            it.add(canvas, BorderLayout.CENTER)
        }
    }
}