package com.example.lib;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;



public class AudioFileFormat {
    private Type type;
    private int byteLength;
    private AudioFormat format;
    private int frameLength;
    private HashMap<String, Object> properties;

    protected AudioFileFormat(Type var1, int var2, AudioFormat var3, int var4) {
        this.type = var1;
        this.byteLength = var2;
        this.format = var3;
        this.frameLength = var4;
        this.properties = null;
    }

    public AudioFileFormat(Type var1, AudioFormat var2, int var3) {
        this(var1, -1, var2, var3);
    }

    public AudioFileFormat(Type var1, AudioFormat var2, int var3, Map<String, Object> var4) {
        this(var1, -1, var2, var3);
        this.properties = new HashMap(var4);
    }

    public Type getType() {
        return this.type;
    }

    public int getByteLength() {
        return this.byteLength;
    }

    public AudioFormat getFormat() {
        return this.format;
    }

    public int getFrameLength() {
        return this.frameLength;
    }

    public Map<String, Object> properties() {
        Object var1;
        if (this.properties == null) {
            var1 = new HashMap(0);
        } else {
            var1 = (Map)((Map)this.properties.clone());
        }

        return Collections.unmodifiableMap((Map)var1);
    }

    public Object getProperty(String var1) {
        return this.properties == null ? null : this.properties.get(var1);
    }

    public String toString() {
        StringBuffer var1 = new StringBuffer();
        if (this.type != null) {
            var1.append(this.type.toString() + " (." + this.type.getExtension() + ") file");
        } else {
            var1.append("unknown file format");
        }

        if (this.byteLength != -1) {
            var1.append(", byte length: " + this.byteLength);
        }

        var1.append(", data format: " + this.format);
        if (this.frameLength != -1) {
            var1.append(", frame length: " + this.frameLength);
        }

        return new String(var1);
    }

    public static class Type {
        public static final com.example.lib.Type WAVE = new com.example.lib.Type("WAVE", "wav");
        public static final com.example.lib.Type AU = new com.example.lib.Type("AU", "au");
        public static final com.example.lib.Type AIFF = new com.example.lib.Type("AIFF", "aif");
        public static final com.example.lib.Type AIFC = new com.example.lib.Type("AIFF-C", "aifc");
        public static final com.example.lib.Type SND = new com.example.lib.Type("SND", "snd");
        private final String name;
        private final String extension;

        public Type(String var1, String var2) {
            this.name = var1;
            this.extension = var2;
        }

        public final boolean equals(Object var1) {
            if (this.toString() != null) {
                return var1 instanceof com.example.lib.Type ? this.toString().equals(var1.toString()) : false;
            } else {
                return var1 != null && var1.toString() == null;
            }
        }

        public final int hashCode() {
            return this.toString() == null ? 0 : this.toString().hashCode();
        }

        public final String toString() {
            return this.name;
        }

        public String getExtension() {
            return this.extension;
        }
    }
}
