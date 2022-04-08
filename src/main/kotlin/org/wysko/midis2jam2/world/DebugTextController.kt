/*
 * Copyright (C) 2022 Jacob Wysko
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

package org.wysko.midis2jam2.world

import com.jme3.font.BitmapText
import com.jme3.scene.Spatial
import org.lwjgl.opengl.GL11
import org.wysko.midis2jam2.Midis2jam2
import org.wysko.midis2jam2.util.cullHint

private val GL_RENDERER: String by lazy { GL11.glGetString(GL11.GL_RENDERER) }

/**
 * Draws debug text on the screen.
 *
 * @param context Context to the main class.
 */
class DebugTextController(val context: Midis2jam2) {

    private val text = BitmapText(context.assetManager.loadFont("Interface/Fonts/Console.fnt")).apply {
        context.app.guiNode.attachChild(this)
        cullHint = Spatial.CullHint.Always
    }

    /**
     * Enables and disables the display of the debug text.
     */
    var enabled: Boolean = true
        set(value) {
            text.cullHint = enabled.cullHint()
            field = value
        }

    /**
     * Toggles the visibility of the debug text.
     */
    fun toggle(): Unit = run { enabled = !enabled }

    /**
     * Updates the debug text.
     *
     * @param tpf the time per frame
     */
    fun tick(tpf: Float) {
        if (enabled) {
            with(text) {
                setLocalTranslation(0f, context.app.viewPort.camera.height.toFloat(), 0f)
                text = context.debugText(tpf, context.timeSinceStart)
            }
        }
    }
}

/**
 * Generates debug text.
 */
private fun Midis2jam2.debugText(tpf: Float, time: Double): String {
    return buildString {
        /* midis2jam2 version and build */
        append("midis2jam2 v${this@debugText.version} (${this@debugText.build})\n")

        /* computer operating system and renderer */
        append("$operatingSystem\n")
        append("$GL_RENDERER\n")

        /* settings */
        append(
            "F: ${this@debugText.properties.getProperty("fullscreen")} / LDE: ${
                this@debugText.properties.getProperty(
                    "legacy_display_engine"
                )
            }\n"
        )
        append(
            "D: ${this@debugText.properties.getProperty("midi_device")} / SF: ${
                this@debugText.properties.getProperty(
                    "soundfont"
                ) ?: "N/A"
            }\n"
        )
        append("LF: ${this@debugText.properties.getProperty("latency_fix")}\n")

        /* fps and time */
        append("${String.format("%.0f", 1 / tpf)} fps\n")
        append("${String.format("%.2f", time)}s / ${String.format("%.2f", this@debugText.file.length)}s\n")

        /* instruments strings */
        append("${this@debugText.instruments.joinToString("")}\n")
    }
}

private val operatingSystem = "${System.getProperty("os.name")} / ${System.getProperty("os.version")}"