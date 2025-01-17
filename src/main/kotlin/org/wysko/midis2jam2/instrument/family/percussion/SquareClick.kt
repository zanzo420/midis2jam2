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
package org.wysko.midis2jam2.instrument.family.percussion

import com.jme3.math.Quaternion
import com.jme3.scene.Node
import com.jme3.scene.Spatial
import org.wysko.midis2jam2.Midis2jam2
import org.wysko.midis2jam2.instrument.family.percussion.drumset.NonDrumSetPercussion
import org.wysko.midis2jam2.instrument.family.percussive.Stick
import org.wysko.midis2jam2.midi.MidiNoteOnEvent
import org.wysko.midis2jam2.util.Utils.rad

/**
 * The Square Click.
 */
class SquareClick(context: Midis2jam2, hits: MutableList<MidiNoteOnEvent>) : NonDrumSetPercussion(context, hits) {

    /** Contains the square click pad. */
    private val squareClickNode = Node().apply {
        attachChild(context.loadModel("SquareShaker.obj", "Wood.bmp").apply {
            setLocalTranslation(0f, -2f, -2f)
        })
    }.also {
        instrumentNode.attachChild(it)
    }

    /** Contains the stick. */
    private val stickNode = Node().apply {
        attachChild(context.loadModel("DrumSet_Stick.obj", "StickSkin.bmp"))
    }.also {
        instrumentNode.attachChild(it)
    }

    override fun tick(time: Double, delta: Float) {
        super.tick(time, delta)
        Stick.handleStick(context, stickNode, time, delta, hits, actualStick = false).run {
            squareClickNode.localRotation = Quaternion().fromAngles(-this.rotationAngle, 0f, 0f)
        }
        stickNode.cullHint = Spatial.CullHint.Dynamic
    }

    init {
        instrumentNode.localRotation = Quaternion().fromAngles(rad(-90.0), rad(-90.0), rad(-135.0))
        instrumentNode.setLocalTranslation(-42f, 44f, -79f)
    }
}