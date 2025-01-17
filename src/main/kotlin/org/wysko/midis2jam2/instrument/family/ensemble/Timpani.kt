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
package org.wysko.midis2jam2.instrument.family.ensemble

import com.jme3.math.Quaternion
import com.jme3.scene.Node
import org.wysko.midis2jam2.Midis2jam2
import org.wysko.midis2jam2.instrument.family.percussive.OneDrumOctave
import org.wysko.midis2jam2.midi.MidiChannelSpecificEvent
import org.wysko.midis2jam2.util.Utils.rad

/** The timpani. */
class Timpani(context: Midis2jam2, eventList: List<MidiChannelSpecificEvent>) : OneDrumOctave(context, eventList) {

    override fun moveForMultiChannel(delta: Float) {
        highestLevel.localRotation =
            Quaternion().fromAngles(0f, rad((-27 + updateInstrumentIndex(delta) * -18).toDouble()), 0f)
    }

    init {
        malletNodes.forEachIndexed { index, malletNode ->
            malletNode.attachChild(context.loadModel("XylophoneMalletWhite.obj", "XylophoneBar.bmp").apply {
                setLocalTranslation(0f, 0f, -5f)
            })
            malletNode.setLocalTranslation(1.8f * (index - 5.5f), 31f, 15f)
        }

        animNode.run {
            attachChild(context.loadModel("TimpaniBody.obj", "HornSkin.bmp", 0.9f).apply {
                (this as Node).getChild(0).setMaterial(context.reflectiveMaterial("Assets/HornSkinGrey.bmp"))
            })
            attachChild(context.loadModel("TimpaniHead.obj", "TimpaniSkin.bmp"))
        }

        instrumentNode.run {
            attachChild(animNode)
            setLocalTranslation(0f, 0f, -120f)
        }
    }
}