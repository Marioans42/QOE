//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.example.lib;

import org.tritonus.share.TDebug;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import com.example.lib.*;


import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.UnsupportedAudioFileException;


public abstract class TAudioFileReader extends AudioFileReader {
    private int m_nMarkLimit;
    private boolean m_bRereading;

    protected TAudioFileReader(int nMarkLimit) {
        this(nMarkLimit, false);
    }

    protected TAudioFileReader(int nMarkLimit, boolean bRereading) {
        this.m_nMarkLimit = -1;
        this.m_nMarkLimit = nMarkLimit;
        this.m_bRereading = bRereading;
    }

    private int getMarkLimit() {
        return this.m_nMarkLimit;
    }

    private boolean isRereading() {
        return this.m_bRereading;
    }

    public AudioFileFormat getAudioFileFormat(File file) throws UnsupportedAudioFileException, IOException {
        if (TDebug.TraceAudioFileReader) {
            TDebug.out("TAudioFileReader.getAudioFileFormat(File): begin");
        }

        long lFileLengthInBytes = file.length();
        InputStream inputStream = new FileInputStream(file);
        AudioFileFormat audioFileFormat = null;

        try {
            audioFileFormat = this.getAudioFileFormat(inputStream, lFileLengthInBytes);
        } finally {
            inputStream.close();
        }

        if (TDebug.TraceAudioFileReader) {
            TDebug.out("TAudioFileReader.getAudioFileFormat(File): end");
        }

        return audioFileFormat;
    }

    public AudioFileFormat getAudioFileFormat(URL url) throws UnsupportedAudioFileException, IOException {
        if (TDebug.TraceAudioFileReader) {
            TDebug.out("TAudioFileReader.getAudioFileFormat(URL): begin");
        }

        long lFileLengthInBytes = -1L;
        InputStream inputStream = url.openStream();
        AudioFileFormat audioFileFormat = null;

        try {
            audioFileFormat = this.getAudioFileFormat(inputStream, lFileLengthInBytes);
        } finally {
            inputStream.close();
        }

        if (TDebug.TraceAudioFileReader) {
            TDebug.out("TAudioFileReader.getAudioFileFormat(URL): end");
        }

        return audioFileFormat;
    }

    public AudioFileFormat getAudioFileFormat(InputStream inputStream) throws UnsupportedAudioFileException, IOException {
        if (TDebug.TraceAudioFileReader) {
            TDebug.out("TAudioFileReader.getAudioFileFormat(InputStream): begin");
        }

        long lFileLengthInBytes = -1L;
        inputStream.mark(this.getMarkLimit());
        AudioFileFormat audioFileFormat = null;

        try {
            audioFileFormat = this.getAudioFileFormat(inputStream, lFileLengthInBytes);
        } finally {
            inputStream.reset();
        }

        if (TDebug.TraceAudioFileReader) {
            TDebug.out("TAudioFileReader.getAudioFileFormat(InputStream): end");
        }

        return audioFileFormat;
    }

    protected abstract AudioFileFormat getAudioFileFormat(InputStream var1, long var2) throws UnsupportedAudioFileException, IOException;

    public AudioInputStream getAudioInputStream(File file) throws UnsupportedAudioFileException, IOException {
        if (TDebug.TraceAudioFileReader) {
            TDebug.out("TAudioFileReader.getAudioInputStream(File): begin");
        }

        long lFileLengthInBytes = file.length();
        InputStream inputStream = new FileInputStream(file);
        AudioInputStream audioInputStream = null;

        try {
            audioInputStream = this.getAudioInputStream(inputStream, lFileLengthInBytes);
        } catch (UnsupportedAudioFileException var7) {
            inputStream.close();
            throw var7;
        } catch (IOException var8) {
            inputStream.close();
            throw var8;
        }

        if (TDebug.TraceAudioFileReader) {
            TDebug.out("TAudioFileReader.getAudioInputStream(File): end");
        }

        return audioInputStream;
    }

    public AudioInputStream getAudioInputStream(URL url) throws UnsupportedAudioFileException, IOException {
        if (TDebug.TraceAudioFileReader) {
            TDebug.out("TAudioFileReader.getAudioInputStream(URL): begin");
        }

        long lFileLengthInBytes = -1L;
        InputStream inputStream = url.openStream();
        AudioInputStream audioInputStream = null;

        try {
            audioInputStream = this.getAudioInputStream(inputStream, lFileLengthInBytes);
        } catch (UnsupportedAudioFileException var7) {
            inputStream.close();
            throw var7;
        } catch (IOException var8) {
            inputStream.close();
            throw var8;
        }

        if (TDebug.TraceAudioFileReader) {
            TDebug.out("TAudioFileReader.getAudioInputStream(URL): end");
        }

        return audioInputStream;
    }

    public AudioInputStream getAudioInputStream(InputStream inputStream) throws UnsupportedAudioFileException, IOException {
        if (TDebug.TraceAudioFileReader) {
            TDebug.out("TAudioFileReader.getAudioInputStream(InputStream): begin");
        }

        long lFileLengthInBytes = -1L;
        AudioInputStream audioInputStream = null;
        inputStream.mark(this.getMarkLimit());

        try {
            audioInputStream = this.getAudioInputStream(inputStream, lFileLengthInBytes);
        } catch (UnsupportedAudioFileException var6) {
            inputStream.reset();
            throw var6;
        } catch (IOException var7) {
            inputStream.reset();
            throw var7;
        }

        if (TDebug.TraceAudioFileReader) {
            TDebug.out("TAudioFileReader.getAudioInputStream(InputStream): end");
        }

        return audioInputStream;
    }

    protected AudioInputStream getAudioInputStream(InputStream inputStream, long lFileLengthInBytes) throws UnsupportedAudioFileException, IOException {
        if (TDebug.TraceAudioFileReader) {
            TDebug.out("TAudioFileReader.getAudioInputStream(InputStream, long): begin");
        }

        if (this.isRereading()) {
            inputStream = new BufferedInputStream((InputStream)inputStream, this.getMarkLimit());
            ((InputStream)inputStream).mark(this.getMarkLimit());
        }

        AudioFileFormat audioFileFormat = this.getAudioFileFormat((InputStream)inputStream, lFileLengthInBytes);
        if (this.isRereading()) {
            ((InputStream)inputStream).reset();
        }

        AudioInputStream audioInputStream = new AudioInputStream((InputStream)inputStream, audioFileFormat.getFormat(), (long)audioFileFormat.getFrameLength());
        if (TDebug.TraceAudioFileReader) {
            TDebug.out("TAudioFileReader.getAudioInputStream(InputStream, long): end");
        }

        return audioInputStream;
    }

    protected static int calculateFrameSize(int nSampleSize, int nNumChannels) {
        return (nSampleSize + 7) / 8 * nNumChannels;
    }

    public static int readLittleEndianInt(InputStream is) throws IOException {
        int b0 = is.read();
        int b1 = is.read();
        int b2 = is.read();
        int b3 = is.read();
        if ((b0 | b1 | b2 | b3) < 0) {
            throw new EOFException();
        } else {
            return (b3 << 24) + (b2 << 16) + (b1 << 8) + (b0 << 0);
        }
    }

    public static short readLittleEndianShort(InputStream is) throws IOException {
        int b0 = is.read();
        int b1 = is.read();
        if ((b0 | b1) < 0) {
            throw new EOFException();
        } else {
            return (short)((b1 << 8) + (b0 << 0));
        }
    }

    public static double readIeeeExtended(DataInputStream dis) throws IOException {
        double f = 0.0D;
        long hiMant = 0L;
        long loMant = 0L;
        double HUGE = 3.4028234663852886E38D;
        int expon = dis.readUnsignedShort();
        long t1 = (long)dis.readUnsignedShort();
        long t2 = (long)dis.readUnsignedShort();
        hiMant = t1 << 16 | t2;
        t1 = (long)dis.readUnsignedShort();
        t2 = (long)dis.readUnsignedShort();
        loMant = t1 << 16 | t2;
        if (expon == 0 && hiMant == 0L && loMant == 0L) {
            f = 0.0D;
        } else if (expon == 32767) {
            f = HUGE;
        } else {
            expon -= 16383;
            expon -= 31;
            f = (double)hiMant * Math.pow(2.0D, (double)expon);
            expon -= 32;
            f += (double)loMant * Math.pow(2.0D, (double)expon);
        }

        return f;
    }
}
