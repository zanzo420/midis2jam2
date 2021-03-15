package org.wysko.midis2jam2.instrument.percussion.drumset;

import com.jme3.material.Material;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;
import org.wysko.midis2jam2.Midis2jam2;
import org.wysko.midis2jam2.midi.MidiNoteOnEvent;

import java.util.List;

import static org.wysko.midis2jam2.Midis2jam2.rad;
import static org.wysko.midis2jam2.instrument.percussion.drumset.StickDrum.MAX_ANGLE;

public class BassDrum extends PercussionInstrument {
	
	private final static int PEDAL_MAX_ANGLE = 20;
	
	final Spatial bassDrum;
	final Spatial bassDrumBeaterArm;
	final Spatial bassDrumBeaterHolder;
	final Spatial bassDrumPedal;
	
	final Node highLevelNode = new Node();
	final Node drumNode = new Node();
	final Node beaterNode = new Node();
	
	public BassDrum(Midis2jam2 context, List<MidiNoteOnEvent> hits) {
		super(context, hits);
		bassDrum = context.loadModel("DrumSet_BassDrum.obj", "DrumShell.bmp", Midis2jam2.MatType.UNSHADED, 0.9f);
		bassDrumBeaterArm = context.loadModel("DrumSet_BassDrumBeaterArm.fbx", "MetalTexture.bmp",
				Midis2jam2.MatType.UNSHADED, 0.9f);
		
		// Apply materials
		final Node arm = (Node) bassDrumBeaterArm;
		
		final Texture shinySilverTexture = context.getAssetManager().loadTexture("Assets/ShinySilver.bmp");
		final Material shinySilverMaterial = new Material(context.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		shinySilverMaterial.setTexture("ColorMap", shinySilverTexture);
		arm.getChild(0).setMaterial(shinySilverMaterial);
		
		final Texture darkMetalTexture = context.getAssetManager().loadTexture("Assets/MetalTextureDark.bmp");
		final Material darkMetalMaterial = new Material(context.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		darkMetalMaterial.setTexture("ColorMap", darkMetalTexture);
		arm.getChild(1).setMaterial(darkMetalMaterial);
		
		bassDrumBeaterHolder = context.loadModel("DrumSet_BassDrumBeaterHolder.fbx", "MetalTexture.bmp",
				Midis2jam2.MatType.UNSHADED, 0.9f);
		final Node holder = (Node) bassDrumBeaterHolder;
		
		holder.getChild(0).setMaterial(darkMetalMaterial);
		
		
		bassDrumPedal = context.loadModel("DrumSet_BassDrumPedal.obj", "MetalTexture.bmp", Midis2jam2.MatType.UNSHADED, 0.9f);
		
		drumNode.attachChild(bassDrum);
		beaterNode.attachChild(this.bassDrumBeaterArm);
		beaterNode.attachChild(bassDrumBeaterHolder);
		beaterNode.attachChild(bassDrumPedal);
		highLevelNode.attachChild(drumNode);
		highLevelNode.attachChild(beaterNode);
		
		this.bassDrumBeaterArm.setLocalTranslation(0, 5.5f, 1.35f);
		this.bassDrumBeaterArm.setLocalRotation(new Quaternion().fromAngles(rad(MAX_ANGLE), 0, 0));
		bassDrumPedal.setLocalRotation(new Quaternion().fromAngles(rad(PEDAL_MAX_ANGLE), 0, 0));
		bassDrumPedal.setLocalTranslation(0, 0.5f, 7.5f);
		beaterNode.setLocalTranslation(0, 0, 1.5f);
		
		highLevelNode.setLocalTranslation(0, 0, -80);
	}
	
	@Override
	public void tick(double time, float delta) {
		MidiNoteOnEvent nextHit = null;
		while (!hits.isEmpty() && context.file.eventInSeconds(hits.get(0)) <= time)
			nextHit = hits.remove(0);
		
		if (nextHit != null) {
			// We need to strike
			bassDrumBeaterArm.setLocalRotation(new Quaternion().fromAngles(0, 0, 0));
			bassDrumPedal.setLocalRotation(new Quaternion().fromAngles(0, 0, 0));
			drumNode.setLocalTranslation(0, 0, (float) (-3 * velocityRecoilDampening(nextHit.velocity)));
		} else {
			
			// Drum recoil
			Vector3f localTranslation = drumNode.getLocalTranslation();
			if (localTranslation.z < -0.0001) {
				drumNode.setLocalTranslation(0, 0, Math.min(0,
						localTranslation.z + (PercussionInstrument.DRUM_RECOIL_COMEBACK * delta)));
			} else {
				drumNode.setLocalTranslation(0, 0, 0);
			}
			
			// Beater comeback
			float[] beaterAngles = bassDrumBeaterArm.getLocalRotation().toAngles(new float[3]);
			float beaterAngle = beaterAngles[0] + 8f * delta;
			beaterAngle = Math.min(rad((float) MAX_ANGLE), beaterAngle);
			bassDrumBeaterArm.setLocalRotation(new Quaternion().fromAngles(
					beaterAngle, 0, 0
			));
			
			// Pedal comeback
			float[] pedalAngles = bassDrumPedal.getLocalRotation().toAngles(new float[3]);
			float pedalAngle = (float) (pedalAngles[0] + 8f * delta * (PEDAL_MAX_ANGLE / MAX_ANGLE));
			pedalAngle = Math.min(rad((float) PEDAL_MAX_ANGLE), pedalAngle);
			bassDrumPedal.setLocalRotation(new Quaternion().fromAngles(
					pedalAngle, 0, 0
			));
		}
		
		
	}
}