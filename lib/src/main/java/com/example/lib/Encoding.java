package com.example.lib;

public  class Encoding {
    public static final javax.sound.sampled.AudioFormat.Encoding PCM_SIGNED = new javax.sound.sampled.AudioFormat.Encoding("PCM_SIGNED");
    public static final javax.sound.sampled.AudioFormat.Encoding PCM_UNSIGNED = new javax.sound.sampled.AudioFormat.Encoding("PCM_UNSIGNED");
    public static final javax.sound.sampled.AudioFormat.Encoding PCM_FLOAT = new javax.sound.sampled.AudioFormat.Encoding("PCM_FLOAT");
    public static final javax.sound.sampled.AudioFormat.Encoding ULAW = new javax.sound.sampled.AudioFormat.Encoding("ULAW");
    public static final javax.sound.sampled.AudioFormat.Encoding ALAW = new javax.sound.sampled.AudioFormat.Encoding("ALAW");
    private String name;

    public Encoding(String var1) {
        this.name = var1;
    }

    public final boolean equals(Object var1) {
        if (this.toString() != null) {
            return var1 instanceof javax.sound.sampled.AudioFormat.Encoding ? this.toString().equals(var1.toString()) : false;
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
}