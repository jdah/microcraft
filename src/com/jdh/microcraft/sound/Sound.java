package com.jdh.microcraft.sound;

import com.jdh.microcraft.Main;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Sound {
    public static final Sound CRAFT = new Sound("/sound/craft.wav");
    public static final Sound HIT = new Sound("/sound/hit.wav");
    public static final Sound HURT = new Sound("/sound/hurt.wav");
    public static final Sound LOSE = new Sound("/sound/lose.wav");
    public static final Sound PICKUP = new Sound("/sound/pickup.wav");
    public static final Sound PLAYER_HURT = new Sound("/sound/player_hurt.wav");
    public static final Sound START = new Sound("/sound/start.wav");
    public static final Sound WIZARD_DEATH = new Sound("/sound/wizard_death.wav");
    public static final Sound EQUIP = new Sound("/sound/equip.wav");
    public static final Sound MISS = new Sound("/sound/miss.wav");
    public static final Sound WIZARD_ATTACK = new Sound("/sound/wizard_attack.wav");

    private final Set<Clip> clips = Collections.synchronizedSet(new HashSet<>());

    private final AudioFormat format;
    private final byte[] bytes;

    public Sound(String path) {
        try {
            AudioInputStream stream = AudioSystem.getAudioInputStream(
                new BufferedInputStream(Main.class.getResourceAsStream(path)));

            this.format = stream.getFormat();
            this.bytes = stream.readAllBytes();

            for (int i = 0; i < 4; i++) {
                this.createNewClip();
            }
        } catch (IOException | UnsupportedAudioFileException e) {
            throw new Error(e);
        }
    }

    private Clip createNewClip() {
        try {
            Clip clip = AudioSystem.getClip();
            clip.open(this.format, this.bytes, 0, this.bytes.length);
            clips.add(clip);
            return clip;
        } catch (LineUnavailableException e) {
            throw new Error(e);
        }
    }

    public void play() {
        new Thread(() -> {
            Clip clip = clips.stream()
                .filter(c ->
                    c.getFramePosition() == 0 ||
                        c.getFramePosition() == c.getFrameLength())
                .findFirst()
                .orElseGet(this::createNewClip);

            clip.setFramePosition(0);
            clip.start();
        }).start();
    }
}
