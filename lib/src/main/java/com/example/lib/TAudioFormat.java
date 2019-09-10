package com.example.lib;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TAudioFormat extends AudioFormat {
    private Map m_properties;
    private Map m_unmodifiableProperties;

    public TAudioFormat(AudioFormat.Encoding encoding, float sampleRate, int sampleSizeInBits, int channels, int frameSize, float frameRate, boolean bigEndian, Map properties) {
        super(encoding, sampleRate, sampleSizeInBits, channels, frameSize, frameRate, bigEndian);
        this.initMaps(properties);
    }

    public TAudioFormat(float sampleRate, int sampleSizeInBits, int channels, boolean signed, boolean bigEndian, Map properties) {
        super(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
        this.initMaps(properties);
    }


    private void initMaps(Map properties) {
        this.m_properties = new HashMap();
        this.m_properties.putAll(properties);
        this.m_unmodifiableProperties = Collections.unmodifiableMap(this.m_properties);
    }

    public Map properties() {
        return this.m_unmodifiableProperties;
    }

    protected void setProperty(String key, Object value) {
        this.m_properties.put(key, value);
    }
}