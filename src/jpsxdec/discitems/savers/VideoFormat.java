/*
 * jPSXdec: PlayStation 1 Media Decoder/Converter in Java
 * Copyright (C) 2012-2014  Michael Sabin
 * All rights reserved.
 *
 * Redistribution and use of the jPSXdec code or any derivative works are
 * permitted provided that the following conditions are met:
 *
 *  * Redistributions may not be sold, nor may they be used in commercial
 *    or revenue-generating business activities.
 *
 *  * Redistributions that are modified from the original source must
 *    include the complete source code, including the source code for all
 *    components used by a binary built from the modified sources. However, as
 *    a special exception, the source code distributed need not include
 *    anything that is normally distributed (in either source or binary form)
 *    with the major components (compiler, kernel, and so on) of the operating
 *    system on which the executable runs, unless that component itself
 *    accompanies the executable.
 *
 *  * Redistributions must reproduce the above copyright notice, this list
 *    of conditions and the following disclaimer in the documentation and/or
 *    other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER
 * OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package jpsxdec.discitems.savers;

import java.util.ArrayList;
import java.util.List;
import jpsxdec.LocalizedMessage;
import jpsxdec.formats.JavaImageFormat;


public enum VideoFormat {
    AVI_MJPG("AVI: Compressed (MJPG)", // I18N
             "avi:mjpg") // I18N
    {
        public String getExtension() { return ".avi"; }
        public boolean isAvi() { return true; }
        public int getDecodeQualityCount() { return 0; }
        public MdecDecodeQuality getMdecDecodeQuality(int i) { return null; }
    },
    AVI_RGB("AVI: Uncompressed RGB", // I18N
            "avi:rgb") // I18N
    {
        public String getExtension() { return ".avi"; }
        public boolean isAvi() { return true; }
    },
    AVI_YUV("AVI: YUV", // I18N
            "avi:yuv") // I18N
    {
        public String getExtension() { return ".avi"; }
        public boolean isAvi() { return true; }
        public int getDecodeQualityCount() { return 1; }
        public MdecDecodeQuality getMdecDecodeQuality(int i) { return MdecDecodeQuality.HIGH_PLUS; }
    },
    AVI_JYUV("AVI: YUV with [0-255] range", // I18N
             "avi:jyuv") // I18N
    {
        public String getExtension() { return ".avi"; }
        public boolean isAvi() { return true; }
        public int getDecodeQualityCount() { return 1; }
        public MdecDecodeQuality getMdecDecodeQuality(int i) { return MdecDecodeQuality.HIGH_PLUS; }
    },
    IMGSEQ_PNG("Image sequence: png", // I18N
               "png", // I18N
               JavaImageFormat.PNG)
    {
        public String getExtension() { return "." + getImgFmt().getExtension(); }
    },
    IMGSEQ_JPG("Image sequence: jpg", // I18N
               "jpg") // I18N
    {
        public String getExtension() { return ".jpg"; }
        public int getDecodeQualityCount() { return 0; }
        public MdecDecodeQuality getMdecDecodeQuality(int i) { return null; }
    },
    IMGSEQ_BMP("Image sequence: bmp", // I18N
               "bmp", // I18N
               JavaImageFormat.BMP)
    {
        public String getExtension() { return "." + getImgFmt().getExtension(); }
    },
    IMGSEQ_BITSTREAM("Image sequence: bitstream", // I18N
                     "bs") // I18N
    {
        public String getExtension() { return ".bs"; }
        public int getDecodeQualityCount() { return 0; }
        public MdecDecodeQuality getMdecDecodeQuality(int i) { return null; }
        public boolean isCroppable() { return false; }
        public boolean needsDims() { return true; }
    },
    IMGSEQ_MDEC ("Image sequence: mdec", // I18N
                 "mdec") // I18N
    {
        public String getExtension() { return ".mdec"; }
        public int getDecodeQualityCount() { return 0; }
        public MdecDecodeQuality getMdecDecodeQuality(int i) { return null; }
        public boolean isCroppable() { return false; }
        public boolean needsDims() { return true; }
    },
    ;

    /** How the format will be displayed in the GUI. */
    private final LocalizedMessage _guiName;
    /** How the format will be displayed on the command line. */
    private final LocalizedMessage _cmdLineId;
    private final JavaImageFormat _eImgFmt;

    private VideoFormat(String sGui, String sCmdLine) {
        this(sGui, sCmdLine, null);
    }

    private VideoFormat(String sGui, String sCmdLine, JavaImageFormat imgFormat) {
        _guiName = new LocalizedMessage(sGui);
        _cmdLineId = new LocalizedMessage(sCmdLine);
        _eImgFmt = imgFormat;
    }

    /** {@inheritDoc}
     *<p>
     *  Must be localized because this object is used directly. */
    public String toString() { return _guiName.getLocalizedMessage(); }
    public LocalizedMessage getCmdLine() { return _cmdLineId; }
    public boolean isAvailable() {
        return _eImgFmt == null ? true : _eImgFmt.isAvailable();
    }

    public boolean isCroppable() { return true; }

    public int getDecodeQualityCount() { return MdecDecodeQuality.values().length; }
    public MdecDecodeQuality getMdecDecodeQuality(int i) { return MdecDecodeQuality.values()[i]; }

    /** If AVI, it means it can save audio, otherwise it is an image sequence. */
    public boolean isAvi() { return false; }
    public boolean isSequence() { return !isAvi(); }

    public JavaImageFormat getImgFmt() { return _eImgFmt; }

    /** If the output filename should include frame dimensions. */
    public boolean needsDims() { return false; }
    /** Filename extension with '.'. */
    abstract public String getExtension();

    /////////////////////////////////////////////////////////

    public static VideoFormat fromCmdLine(String sCmdLine) {
        for (VideoFormat fmt : getAvailable()) {
            if (fmt.getCmdLine().getEnglishMessage().equalsIgnoreCase(sCmdLine) ||
                fmt.getCmdLine().getLocalizedMessage().equalsIgnoreCase(sCmdLine))
                return fmt;
        }
        return null;
    }

    public static String getCmdLineList() {
        StringBuilder sb = new StringBuilder();
        for (VideoFormat fmt : values()) {
            if (fmt.isAvailable()) {
                if (sb.length() > 0)
                    sb.append(", ");
                sb.append(fmt.getCmdLine());
            }
        }
        return sb.toString();
    }

    public static List<VideoFormat> getAvailable() {
        ArrayList<VideoFormat> avalable = new ArrayList<VideoFormat>();
        for (VideoFormat fmt : values()) {
            if (fmt.isAvailable())
                avalable.add(fmt);
        }
        return avalable;
    }
}