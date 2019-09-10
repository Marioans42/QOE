package com.example.lib;



public abstract class MixerProvider {
    public MixerProvider() {
    }

    public boolean isMixerSupported(Mixer.Info var1) {
        Mixer.Info[] var2 = this.getMixerInfo();

        for(int var3 = 0; var3 < var2.length; ++var3) {
            if (var1.equals(var2[var3])) {
                return true;
            }
        }

        return false;
    }

    public abstract com.example.lib.Mixer.Info[] getMixerInfo();

    public abstract Mixer getMixer(Mixer.Info var1);
}
