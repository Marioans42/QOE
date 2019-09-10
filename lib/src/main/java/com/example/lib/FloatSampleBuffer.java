package com.example.lib;

import java.util.ArrayList;
import java.util.Random;

public class FloatSampleBuffer {
    private static final boolean LAZY_DEFAULT = true;
    private ArrayList channels;
    private int sampleCount;
    private int channelCount;
    private float sampleRate;
    private int originalFormatType;
    public static final int DITHER_MODE_AUTOMATIC = 0;
    public static final int DITHER_MODE_ON = 1;
    public static final int DITHER_MODE_OFF = 2;
    private static Random random = null;
    private float ditherBits;
    private boolean doDither;
    private int ditherMode;
    private static final int F_8 = 1;
    private static final int F_16 = 2;
    private static final int F_24 = 3;
    private static final int F_32 = 4;
    private static final int F_SAMPLE_WIDTH_MASK = 7;
    private static final int F_SIGNED = 8;
    private static final int F_BIGENDIAN = 16;
    private static final int CT_8S = 9;
    private static final int CT_8U = 1;
    private static final int CT_16SB = 26;
    private static final int CT_16SL = 10;
    private static final int CT_24SB = 27;
    private static final int CT_24SL = 11;
    private static final int CT_32SB = 28;
    private static final int CT_32SL = 12;
    private static final float twoPower7 = 128.0F;
    private static final float twoPower15 = 32768.0F;
    private static final float twoPower23 = 8388608.0F;
    private static final float twoPower31 = 2.14748365E9F;
    private static final float invTwoPower7 = 0.0078125F;
    private static final float invTwoPower15 = 3.0517578E-5F;
    private static final float invTwoPower23 = 1.1920929E-7F;
    private static final float invTwoPower31 = 4.656613E-10F;

    public FloatSampleBuffer() {
        this(0, 0, 1.0F);
    }

    public FloatSampleBuffer(int channelCount, int sampleCount, float sampleRate) {
        this.channels = new ArrayList();
        this.sampleCount = 0;
        this.channelCount = 0;
        this.sampleRate = 0.0F;
        this.originalFormatType = 0;
        this.ditherBits = 0.8F;
        this.doDither = false;
        this.ditherMode = 0;
        this.init(channelCount, sampleCount, sampleRate, true);
    }

    public FloatSampleBuffer(byte[] buffer, int offset, int byteCount, AudioFormat format) {
        this(format.getChannels(), byteCount / (format.getSampleSizeInBits() / 8 * format.getChannels()), format.getSampleRate());
        this.initFromByteArray(buffer, offset, byteCount, format);
    }

    protected void init(int channelCount, int sampleCount, float sampleRate) {
        this.init(channelCount, sampleCount, sampleRate, true);
    }

    protected void init(int channelCount, int sampleCount, float sampleRate, boolean lazy) {
        if (channelCount >= 0 && sampleCount >= 0) {
            this.setSampleRate(sampleRate);
            if (this.getSampleCount() != sampleCount || this.getChannelCount() != channelCount) {
                this.createChannels(channelCount, sampleCount, lazy);
            }

        } else {
            throw new IllegalArgumentException("Invalid parameters in initialization of FloatSampleBuffer.");
        }
    }

    private void createChannels(int channelCount, int sampleCount, boolean lazy) {
        this.sampleCount = sampleCount;
        this.channelCount = 0;

        for(int ch = 0; ch < channelCount; ++ch) {
            this.insertChannel(ch, false, lazy);
        }

        if (!lazy) {
            while(this.channels.size() > channelCount) {
                this.channels.remove(this.channels.size() - 1);
            }
        }

    }

    public void initFromByteArray(byte[] buffer, int offset, int byteCount, AudioFormat format) {
        this.initFromByteArray(buffer, offset, byteCount, format, true);
    }

    public void initFromByteArray(byte[] buffer, int offset, int byteCount, AudioFormat format, boolean lazy) {
        if (offset + byteCount > buffer.length) {
            throw new IllegalArgumentException("FloatSampleBuffer.initFromByteArray: buffer too small.");
        } else {
            boolean signed = format.getEncoding().equals(AudioFormat.Encoding.PCM_SIGNED);
            if (!signed && !format.getEncoding().equals(AudioFormat.Encoding.PCM_UNSIGNED)) {
                throw new IllegalArgumentException("FloatSampleBuffer: only PCM samples are possible.");
            } else {
                int bytesPerSample = format.getSampleSizeInBits() / 8;
                int bytesPerFrame = bytesPerSample * format.getChannels();
                int thisSampleCount = byteCount / bytesPerFrame;
                this.init(format.getChannels(), thisSampleCount, format.getSampleRate(), lazy);
                int formatType = this.getFormatType(format.getSampleSizeInBits(), signed, format.isBigEndian());
                this.originalFormatType = formatType;

                for(int ch = 0; ch < format.getChannels(); ++ch) {
                    convertByteToFloat(buffer, offset, bytesPerFrame, formatType, this.getChannel(ch), 0, this.sampleCount);
                    offset += bytesPerSample;
                }

            }
        }
    }

    public void initFromFloatSampleBuffer(FloatSampleBuffer source) {
        this.init(source.getChannelCount(), source.getSampleCount(), source.getSampleRate());

        for(int ch = 0; ch < this.getChannelCount(); ++ch) {
            System.arraycopy(source.getChannel(ch), 0, this.getChannel(ch), 0, this.sampleCount);
        }

    }

    public void reset() {
        this.init(0, 0, 1.0F, false);
    }

    public void reset(int channels, int sampleCount, float sampleRate) {
        this.init(channels, sampleCount, sampleRate, false);
    }

    public int getByteArrayBufferSize(AudioFormat format) {
        if (!format.getEncoding().equals(AudioFormat.Encoding.PCM_SIGNED) && !format.getEncoding().equals(AudioFormat.Encoding.PCM_UNSIGNED)) {
            throw new IllegalArgumentException("FloatSampleBuffer: only PCM samples are possible.");
        } else {
            int bytesPerSample = format.getSampleSizeInBits() / 8;
            int bytesPerFrame = bytesPerSample * format.getChannels();
            return bytesPerFrame * this.getSampleCount();
        }
    }

    public int convertToByteArray(byte[] buffer, int offset, AudioFormat format) {
        int byteCount = this.getByteArrayBufferSize(format);
        if (offset + byteCount > buffer.length) {
            throw new IllegalArgumentException("FloatSampleBuffer.convertToByteArray: buffer too small.");
        } else {
            boolean signed = format.getEncoding().equals(AudioFormat.Encoding.PCM_SIGNED);
            if (!signed && !format.getEncoding().equals(AudioFormat.Encoding.PCM_UNSIGNED)) {
                throw new IllegalArgumentException("FloatSampleBuffer.convertToByteArray: only PCM samples are allowed.");
            } else if (format.getSampleRate() != this.getSampleRate()) {
                throw new IllegalArgumentException("FloatSampleBuffer.convertToByteArray: different samplerates.");
            } else if (format.getChannels() != this.getChannelCount()) {
                throw new IllegalArgumentException("FloatSampleBuffer.convertToByteArray: different channel count.");
            } else {
                int bytesPerSample = format.getSampleSizeInBits() / 8;
                int bytesPerFrame = bytesPerSample * format.getChannels();
                int formatType = this.getFormatType(format.getSampleSizeInBits(), signed, format.isBigEndian());

                for(int ch = 0; ch < format.getChannels(); ++ch) {
                    this.convertFloatToByte(this.getChannel(ch), this.sampleCount, buffer, offset, bytesPerFrame, formatType);
                    offset += bytesPerSample;
                }

                return this.getSampleCount() * bytesPerFrame;
            }
        }
    }

    public byte[] convertToByteArray(AudioFormat format) {
        byte[] res = new byte[this.getByteArrayBufferSize(format)];
        this.convertToByteArray(res, 0, format);
        return res;
    }

    public void changeSampleCount(int newSampleCount, boolean keepOldSamples) {
        int oldSampleCount = this.getSampleCount();
        if (oldSampleCount != newSampleCount) {
            Object[] oldChannels = null;
            if (keepOldSamples) {
                oldChannels = this.getAllChannels();
            }

            this.init(this.getChannelCount(), newSampleCount, this.getSampleRate());
            if (keepOldSamples) {
                int copyCount = newSampleCount < oldSampleCount ? newSampleCount : oldSampleCount;

                for(int ch = 0; ch < this.getChannelCount(); ++ch) {
                    float[] oldSamples = (float[])oldChannels[ch];
                    float[] newSamples = (float[])this.getChannel(ch);
                    if (oldSamples != newSamples) {
                        System.arraycopy(oldSamples, 0, newSamples, 0, copyCount);
                    }

                    if (oldSampleCount < newSampleCount) {
                        for(int i = oldSampleCount; i < newSampleCount; ++i) {
                            newSamples[i] = 0.0F;
                        }
                    }
                }
            }

        }
    }

    public void makeSilence() {
        if (this.getChannelCount() > 0) {
            this.makeSilence(0);

            for(int ch = 1; ch < this.getChannelCount(); ++ch) {
                this.copyChannel(0, ch);
            }
        }

    }

    public void makeSilence(int channel) {
        float[] samples = this.getChannel(0);

        for(int i = 0; i < this.getSampleCount(); ++i) {
            samples[i] = 0.0F;
        }

    }

    public void addChannel(boolean silent) {
        this.insertChannel(this.getChannelCount(), silent);
    }

    public void insertChannel(int index, boolean silent) {
        this.insertChannel(index, silent, true);
    }

    public void insertChannel(int index, boolean silent, boolean lazy) {
        int physSize = this.channels.size();
        int virtSize = this.getChannelCount();
        float[] newChannel = null;
        if (physSize > virtSize) {
            for(int ch = virtSize; ch < physSize; ++ch) {
                float[] thisChannel = (float[])this.channels.get(ch);
                if (lazy && thisChannel.length >= this.getSampleCount() || !lazy && thisChannel.length == this.getSampleCount()) {
                    newChannel = thisChannel;
                    this.channels.remove(ch);
                    break;
                }
            }
        }

        if (newChannel == null) {
            newChannel = new float[this.getSampleCount()];
        }

        this.channels.add(index, newChannel);
        ++this.channelCount;
        if (silent) {
            this.makeSilence(index);
        }

    }

    public void removeChannel(int channel) {
        this.removeChannel(channel, true);
    }

    public void removeChannel(int channel, boolean lazy) {
        if (!lazy) {
            this.channels.remove(channel);
        } else if (channel < this.getChannelCount() - 1) {
            this.channels.add(this.channels.remove(channel));
        }

        --this.channelCount;
    }

    public void copyChannel(int sourceChannel, int targetChannel) {
        float[] source = this.getChannel(sourceChannel);
        float[] target = this.getChannel(targetChannel);
        System.arraycopy(source, 0, target, 0, this.getSampleCount());
    }

    public void copy(int sourceIndex, int destIndex, int length) {
        for(int i = 0; i < this.getChannelCount(); ++i) {
            this.copy(i, sourceIndex, destIndex, length);
        }

    }

    public void copy(int channel, int sourceIndex, int destIndex, int length) {
        float[] data = this.getChannel(channel);
        int bufferCount = this.getSampleCount();
        if (sourceIndex + length <= bufferCount && destIndex + length <= bufferCount && sourceIndex >= 0 && destIndex >= 0 && length >= 0) {
            System.arraycopy(data, sourceIndex, data, destIndex, length);
        } else {
            throw new IndexOutOfBoundsException("parameters exceed buffer size");
        }
    }

    public void expandChannel(int targetChannelCount) {
        if (this.getChannelCount() != 1) {
            throw new IllegalArgumentException("FloatSampleBuffer: can only expand channels for mono signals.");
        } else {
            for(int ch = 1; ch < targetChannelCount; ++ch) {
                this.addChannel(false);
                this.copyChannel(0, ch);
            }

        }
    }

    public void mixDownChannels() {
        float[] firstChannel = this.getChannel(0);
        int sampleCount = this.getSampleCount();
        int channelCount = this.getChannelCount();

        for(int ch = channelCount - 1; ch > 0; --ch) {
            float[] thisChannel = this.getChannel(ch);

            for(int i = 0; i < sampleCount; ++i) {
                firstChannel[i] += thisChannel[i];
            }

            this.removeChannel(ch);
        }

    }

    public void setSamplesFromBytes(byte[] srcBuffer, int srcOffset, AudioFormat format, int destOffset, int lengthInSamples) {
        int bytesPerSample = (format.getSampleSizeInBits() + 7) / 8;
        int bytesPerFrame = bytesPerSample * format.getChannels();
        if (srcOffset + lengthInSamples * bytesPerFrame > srcBuffer.length) {
            throw new IllegalArgumentException("FloatSampleBuffer.setSamplesFromBytes: srcBuffer too small.");
        } else if (destOffset + lengthInSamples > this.getSampleCount()) {
            throw new IllegalArgumentException("FloatSampleBuffer.setSamplesFromBytes: destBuffer too small.");
        } else {
            boolean signed = format.getEncoding().equals(AudioFormat.Encoding.PCM_SIGNED);
            boolean unsigned = format.getEncoding().equals(AudioFormat.Encoding.PCM_UNSIGNED);
            if (!signed && !unsigned) {
                throw new IllegalArgumentException("FloatSampleBuffer: only PCM samples are possible.");
            } else {
                int formatType = this.getFormatType(format.getSampleSizeInBits(), signed, format.isBigEndian());

                for(int ch = 0; ch < format.getChannels(); ++ch) {
                    convertByteToFloat(srcBuffer, srcOffset, bytesPerFrame, formatType, this.getChannel(ch), destOffset, lengthInSamples);
                    srcOffset += bytesPerSample;
                }

            }
        }
    }

    public int getChannelCount() {
        return this.channelCount;
    }

    public int getSampleCount() {
        return this.sampleCount;
    }

    public float getSampleRate() {
        return this.sampleRate;
    }

    public void setSampleRate(float sampleRate) {
        if (sampleRate <= 0.0F) {
            throw new IllegalArgumentException("Invalid samplerate for FloatSampleBuffer.");
        } else {
            this.sampleRate = sampleRate;
        }
    }

    public float[] getChannel(int channel) {
        if (channel >= 0 && channel < this.getChannelCount()) {
            return (float[])this.channels.get(channel);
        } else {
            throw new IllegalArgumentException("FloatSampleBuffer: invalid channel number.");
        }
    }

    public Object[] getAllChannels() {
        Object[] res = new Object[this.getChannelCount()];

        for(int ch = 0; ch < this.getChannelCount(); ++ch) {
            res[ch] = this.getChannel(ch);
        }

        return res;
    }

    public void setDitherBits(float ditherBits) {
        if (ditherBits <= 0.0F) {
            throw new IllegalArgumentException("DitherBits must be greater than 0");
        } else {
            this.ditherBits = ditherBits;
        }
    }

    public float getDitherBits() {
        return this.ditherBits;
    }

    public void setDitherMode(int mode) {
        if (mode != 0 && mode != 1 && mode != 2) {
            throw new IllegalArgumentException("Illegal DitherMode");
        } else {
            this.ditherMode = mode;
        }
    }

    public int getDitherMode() {
        return this.ditherMode;
    }

    public int getFormatType(int ssib, boolean signed, boolean bigEndian) {
        int bytesPerSample = ssib / 8;
        int res = 0;
        if (ssib == 8) {
            res = 1;
        } else if (ssib == 16) {
            res = 2;
        } else if (ssib == 24) {
            res = 3;
        } else if (ssib == 32) {
            res = 4;
        }

        if (res == 0) {
            throw new IllegalArgumentException("FloatSampleBuffer: unsupported sample size of " + ssib + " bits per sample.");
        } else if (!signed && bytesPerSample > 1) {
            throw new IllegalArgumentException("FloatSampleBuffer: unsigned samples larger than 8 bit are not supported");
        } else {
            if (signed) {
                res |= 8;
            }

            if (bigEndian && ssib != 8) {
                res |= 16;
            }

            return res;
        }
    }

    private static void convertByteToFloat(byte[] input, int inputOffset, int bytesPerFrame, int formatType, float[] output, int outputOffset, int sampleCount) {
        int endCount = outputOffset + sampleCount;

        for(int sample = outputOffset; sample < endCount; ++sample) {
            switch(formatType) {
                case 1:
                    output[sample] = (float)((input[inputOffset] & 255) - 128) * 0.0078125F;
                    break;
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 13:
                case 14:
                case 15:
                case 16:
                case 17:
                case 18:
                case 19:
                case 20:
                case 21:
                case 22:
                case 23:
                case 24:
                case 25:
                default:
                    throw new IllegalArgumentException("Unsupported formatType=" + formatType);
                case 9:
                    output[sample] = (float)input[inputOffset] * 0.0078125F;
                    break;
                case 10:
                    output[sample] = (float)(input[inputOffset + 1] << 8 | input[inputOffset] & 255) * 3.0517578E-5F;
                    break;
                case 11:
                    output[sample] = (float)(input[inputOffset + 2] << 16 | (input[inputOffset + 1] & 255) << 8 | input[inputOffset] & 255) * 1.1920929E-7F;
                    break;
                case 12:
                    output[sample] = (float)(input[inputOffset + 3] << 24 | (input[inputOffset + 2] & 255) << 16 | (input[inputOffset + 1] & 255) << 8 | input[inputOffset] & 255) * 4.656613E-10F;
                    break;
                case 26:
                    output[sample] = (float)(input[inputOffset] << 8 | input[inputOffset + 1] & 255) * 3.0517578E-5F;
                    break;
                case 27:
                    output[sample] = (float)(input[inputOffset] << 16 | (input[inputOffset + 1] & 255) << 8 | input[inputOffset + 2] & 255) * 1.1920929E-7F;
                    break;
                case 28:
                    output[sample] = (float)(input[inputOffset] << 24 | (input[inputOffset + 1] & 255) << 16 | (input[inputOffset + 2] & 255) << 8 | input[inputOffset + 3] & 255) * 4.656613E-10F;
            }

            inputOffset += bytesPerFrame;
        }

    }

    protected byte quantize8(float sample) {
        if (this.doDither) {
            sample += random.nextFloat() * this.ditherBits;
        }

        if (sample >= 127.0F) {
            return 127;
        } else {
            return sample <= -128.0F ? -128 : (byte)((int)(sample < 0.0F ? sample - 0.5F : sample + 0.5F));
        }
    }

    protected int quantize16(float sample) {
        if (this.doDither) {
            sample += random.nextFloat() * this.ditherBits;
        }

        if (sample >= 32767.0F) {
            return 32767;
        } else {
            return sample <= -32768.0F ? -32768 : (int)(sample < 0.0F ? sample - 0.5F : sample + 0.5F);
        }
    }

    protected int quantize24(float sample) {
        if (this.doDither) {
            sample += random.nextFloat() * this.ditherBits;
        }

        if (sample >= 8388607.0F) {
            return 8388607;
        } else {
            return sample <= -8388608.0F ? -8388608 : (int)(sample < 0.0F ? sample - 0.5F : sample + 0.5F);
        }
    }

    protected int quantize32(float sample) {
        if (this.doDither) {
            sample += random.nextFloat() * this.ditherBits;
        }

        if (sample >= 2.14748365E9F) {
            return 2147483647;
        } else {
            return sample <= -2.14748365E9F ? -2147483648 : (int)(sample < 0.0F ? sample - 0.5F : sample + 0.5F);
        }
    }

    private void convertFloatToByte(float[] input, int sampleCount, byte[] output, int offset, int bytesPerFrame, int formatType) {
        switch(this.ditherMode) {
            case 0:
                this.doDither = (this.originalFormatType & 7) > (formatType & 7);
                break;
            case 1:
                this.doDither = true;
                break;
            case 2:
                this.doDither = false;
        }

        if (this.doDither && random == null) {
            random = new Random();
        }

        for(int inIndex = 0; inIndex < sampleCount; ++inIndex) {
            int iSample;
            switch(formatType) {
                case 1:
                    output[offset] = (byte)(this.quantize8(input[inIndex] * 128.0F) + 128);
                    break;
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 13:
                case 14:
                case 15:
                case 16:
                case 17:
                case 18:
                case 19:
                case 20:
                case 21:
                case 22:
                case 23:
                case 24:
                case 25:
                default:
                    throw new IllegalArgumentException("Unsupported formatType=" + formatType);
                case 9:
                    output[offset] = this.quantize8(input[inIndex] * 128.0F);
                    break;
                case 10:
                    iSample = this.quantize16(input[inIndex] * 32768.0F);
                    output[offset + 1] = (byte)(iSample >> 8);
                    output[offset] = (byte)(iSample & 255);
                    break;
                case 11:
                    iSample = this.quantize24(input[inIndex] * 8388608.0F);
                    output[offset + 2] = (byte)(iSample >> 16);
                    output[offset + 1] = (byte)(iSample >>> 8 & 255);
                    output[offset] = (byte)(iSample & 255);
                    break;
                case 12:
                    iSample = this.quantize32(input[inIndex] * 2.14748365E9F);
                    output[offset + 3] = (byte)(iSample >> 24);
                    output[offset + 2] = (byte)(iSample >>> 16 & 255);
                    output[offset + 1] = (byte)(iSample >>> 8 & 255);
                    output[offset] = (byte)(iSample & 255);
                    break;
                case 26:
                    iSample = this.quantize16(input[inIndex] * 32768.0F);
                    output[offset] = (byte)(iSample >> 8);
                    output[offset + 1] = (byte)(iSample & 255);
                    break;
                case 27:
                    iSample = this.quantize24(input[inIndex] * 8388608.0F);
                    output[offset] = (byte)(iSample >> 16);
                    output[offset + 1] = (byte)(iSample >>> 8 & 255);
                    output[offset + 2] = (byte)(iSample & 255);
                    break;
                case 28:
                    iSample = this.quantize32(input[inIndex] * 2.14748365E9F);
                    output[offset] = (byte)(iSample >> 24);
                    output[offset + 1] = (byte)(iSample >>> 16 & 255);
                    output[offset + 2] = (byte)(iSample >>> 8 & 255);
                    output[offset + 3] = (byte)(iSample & 255);
            }

            offset += bytesPerFrame;
        }

    }

    private static String formatType2Str(int formatType) {
        String res = "" + formatType + ": ";
        switch(formatType & 7) {
            case 1:
                res = res + "8bit";
                break;
            case 2:
                res = res + "16bit";
                break;
            case 3:
                res = res + "24bit";
                break;
            case 4:
                res = res + "32bit";
        }

        res = res + ((formatType & 8) == 8 ? " signed" : " unsigned");
        if ((formatType & 7) != 1) {
            res = res + ((formatType & 16) == 16 ? " big endian" : " little endian");
        }

        return res;
    }
}
