package com.example.lib;


    public class MpegFileFormatType extends AudioFileFormat.Type {
        public static final AudioFileFormat.Type MPEG = new MpegFileFormatType("MPEG", "mpeg");
        public static final AudioFileFormat.Type MP3 = new MpegFileFormatType("MP3", "mp3");

        public MpegFileFormatType(String var1, String var2) {
            super(var1, var2);
        }

}
