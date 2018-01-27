package cn.dazhou.sip;

/**
 * @author Hooyee on 2017/12/5.
 */

public class VideoParam {
    private long videoInWidth;
    private long videoInHeight;
    private long videoOutWidth;
    private long videoOutHeight;
    private long bandwidthDownKbps;
    private long videoInAvgFps;
    private long videoDecAvgTime;
    private long videoEncAvgTime;

    private VideoParam(long videoInWidth, long videoInHeight, long videoOutWidht, long videoOutHieght, long bandwidthDownKbps, long videoInavgFps, long videoDecAvgTime, long videoEncAvgTime) {
        this.videoInWidth = videoInWidth;
        this.videoInHeight = videoInHeight;
        this.videoOutWidth = videoOutWidht;
        this.videoOutHeight = videoOutHieght;
        this.bandwidthDownKbps = bandwidthDownKbps;
        this.videoInAvgFps = videoInavgFps;
        this.videoDecAvgTime = videoDecAvgTime;
        this.videoEncAvgTime = videoEncAvgTime;
    }

    public VideoParam() {}

    public long getVideoInWidth() {
        return videoInWidth;
    }

    public void setVideoInWidth(long videoInWidth) {
        this.videoInWidth = videoInWidth;
    }

    public long getVideoInHeight() {
        return videoInHeight;
    }

    public void setVideoInHeight(long videoInHeight) {
        this.videoInHeight = videoInHeight;
    }

    public long getVideoOutWidth() {
        return videoOutWidth;
    }

    public void setVideoOutWidth(long videoOutWidth) {
        this.videoOutWidth = videoOutWidth;
    }

    public long getVideoOutHeight() {
        return videoOutHeight;
    }

    public void setVideoOutHeight(long videoOutHeight) {
        this.videoOutHeight = videoOutHeight;
    }

    public long getBandwidthDownKbps() {
        return bandwidthDownKbps;
    }

    public void setBandwidthDownKbps(long bandwidthDownKbps) {
        this.bandwidthDownKbps = bandwidthDownKbps;
    }

    public long getVideoInAvgFps() {
        return videoInAvgFps;
    }

    public void setVideoInAvgFps(long videoInAvgFps) {
        this.videoInAvgFps = videoInAvgFps;
    }

    public long getVideoDecAvgTime() {
        return videoDecAvgTime;
    }

    public void setVideoDecAvgTime(long videoDecAvgTime) {
        this.videoDecAvgTime = videoDecAvgTime;
    }

    public long getVideoEncAvgTime() {
        return videoEncAvgTime;
    }

    public void setVideoEncAvgTime(long videoEncAvgTime) {
        this.videoEncAvgTime = videoEncAvgTime;
    }

    public static class Builder {
        long videoInWidth;
        long videoInHeight;
        long videoOutWidth;
        long videoOutHeight;
        long bandwidthDownKbps;
        long videoInavgFps;
        long videoDecAvgTime;
        long videoEncAvgTime;

        public Builder videoInWidth(long videoInWidth) {
            this.videoInWidth = videoInWidth;
            return this;
        }

        public Builder videoInHeight(long videoInHeight) {
            this.videoInHeight = videoInHeight;
            return this;
        }

        public Builder videoOutWidth(long videoOutWidht) {
            this.videoOutWidth = videoOutWidht;
            return this;
        }

        public Builder videoOutHeight(long videoOutHieght) {
            this.videoOutHeight = videoOutHieght;
            return this;
        }

        public Builder bandwidthDownKbps(long bandwidthDownKbps) {
            this.bandwidthDownKbps = bandwidthDownKbps;
            return this;
        }

        public Builder videoInavgFps(long videoInavgFps) {
            this.videoInavgFps = videoInavgFps;
            return this;
        }

        public Builder videoDecAvgTime(long videoDecAvgTime) {
            this.videoDecAvgTime = videoDecAvgTime;
            return this;
        }

        public Builder videoEncAvgTime(long videoEncAvgTime) {
            this.videoEncAvgTime = videoEncAvgTime;
            return this;
        }

        public VideoParam build() {
            return new VideoParam(videoInWidth, videoInHeight, videoOutWidth, videoOutHeight,
                    bandwidthDownKbps, videoInavgFps, videoDecAvgTime, videoEncAvgTime);
        }
    }
}
