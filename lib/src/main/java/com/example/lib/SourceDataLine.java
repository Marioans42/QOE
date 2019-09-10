package com.example.lib;

import javax.sound.sampled.LineUnavailableException;

public interface SourceDataLine extends DataLine {
    void open(AudioFormat var1, int var2) throws LineUnavailableException;

    void open(AudioFormat var1) throws LineUnavailableException;

    int write(byte[] var1, int var2, int var3);
}
