package com.example.lib;



public  class Type {
    public static final Type WAVE = new Type("WAVE", "wav");
    public static final Type AU = new Type("AU", "au");
    public static final Type AIFF = new Type("AIFF", "aif");
    public static final Type AIFC = new Type("AIFF-C", "aifc");
    public static final Type SND = new Type("SND", "snd");
    private final String name;
    private final String extension;

    public Type(String var1, String var2) {
        this.name = var1;
        this.extension = var2;
    }

    public final boolean equals(Object var1) {
        if (this.toString() != null) {
            return var1 instanceof Type ? this.toString().equals(var1.toString()) : false;
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