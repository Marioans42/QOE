package com.example.lib;


    public class MpegFileFormatType extends Type {
        public static final Type MPEG = new MpegFileFormatType("MPEG", "mpeg");
        public static final Type MP3 = new MpegFileFormatType("MP3", "mp3");

        public MpegFileFormatType(String var1, String var2) {
            super(var1, var2);
        }

}
