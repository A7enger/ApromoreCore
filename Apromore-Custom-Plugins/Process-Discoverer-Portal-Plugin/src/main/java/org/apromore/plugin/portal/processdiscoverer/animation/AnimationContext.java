/*-
 * #%L
 * This file is part of "Apromore Core".
 * %%
 * Copyright (C) 2019 - 2021 Apromore Pty Ltd. All Rights Reserved.
 * %%
 * NOTICE:  All information contained herein is, and remains the
 * property of Apromore Pty Ltd and its suppliers, if any.
 * The intellectual and technical concepts contained herein are
 * proprietary to Apromore Pty Ltd and its suppliers and may
 * be covered by U.S. and Foreign Patents, patents in process,
 * and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this
 * material is strictly forbidden unless prior written permission
 * is obtained from Apromore Pty Ltd.
 * #L%
 */
package org.apromore.plugin.portal.processdiscoverer.animation;

import java.util.List;

import org.apromore.service.loganimation.replay.AnimationLog;

/**
 * An <b>AnimationContext</b> captures the global setting for the animation. 
 * 
 * @author Bruce Nguyen
 *
 */
public class AnimationContext {
	private int recordingFrameRate = 60; //frames per second
    private int recordingDuration = 600; //seconds
    private double recordingFrameInterval = 0; //milliseconds between two consecutive frames
    
    private long minLogStartTimestamp = Long.MAX_VALUE;
    private long maxLogEndTimestamp = Long.MIN_VALUE;
    private double logToRecordingTimeRatio = 1; //a second on the animation timeline is converted to actual seconds
    private double logTimeFrameInterval; // frame interval in terms of log time (milliseconds)
    
    private int frameSkip = 0;
    
    public AnimationContext(List<AnimationLog> logs) {
        for (AnimationLog log: logs) {
            if (minLogStartTimestamp > log.getStartDate().getMillis()) minLogStartTimestamp = log.getStartDate().getMillis();
            if (maxLogEndTimestamp < log.getEndDate().getMillis()) maxLogEndTimestamp = log.getEndDate().getMillis();            
        }
        this.setRecordingFrameRate(this.recordingFrameRate);
        this.setRecordingDuration(this.recordingDuration);
    }
    
    public AnimationContext(List<AnimationLog> logs, int recordingFrameRate, int recordingDuration) {
        this(logs);
        this.setRecordingFrameRate(recordingFrameRate);
        this.setRecordingDuration(recordingDuration);
    }
    
    public int getRecordingFrameRate() {
        return this.recordingFrameRate;
    }
    
    public void setRecordingFrameRate(int fps) {
        if (fps > 0) {
            this.recordingFrameRate = fps;
            this.recordingFrameInterval = 1.0/fps*1000;    
            this.logTimeFrameInterval = logToRecordingTimeRatio*recordingFrameInterval;
        }
    }
    
    public int getMaxNumberOfFrames() {
    	return this.recordingFrameRate*this.recordingDuration;
    }
    
    public double getRecordingFrameInterval() {
        return this.recordingFrameInterval;
    }
    
    public double getRecordingLogFrameInterval() {
        return this.logTimeFrameInterval;
    }
    
    public long getMinLogStartTimestamp() {
        return this.minLogStartTimestamp;
    }
    
    public long getMaxLogEndTimestamp() {
        return this.maxLogEndTimestamp;
    }
    
    public int getRecordingDuration() {
        return this.recordingDuration;
    }
    
    //Unit: seconds
    public void setRecordingDuration(int recordingDuration) {
        if (recordingDuration > 0) {
            this.recordingDuration = recordingDuration;
            this.logToRecordingTimeRatio = (maxLogEndTimestamp - minLogStartTimestamp)/(recordingDuration*1000);
            this.logTimeFrameInterval = logToRecordingTimeRatio*recordingFrameInterval;
        }
    }
    
    public double getTimelineRatio() {
        return this.logToRecordingTimeRatio;
    }
    
    /**
     * 
     * @param timestamp: milliseconds since 1/1/1970
     */
    public int getFrameIndexFromLogTimestamp(long timestamp) {
        if (timestamp <= minLogStartTimestamp) {
            return 0; 
        }
        else if (timestamp >= maxLogEndTimestamp) {
            return getMaxNumberOfFrames() - 1;
        }
        else {
            return (int)Math.floor(1.0*(timestamp - minLogStartTimestamp)/logTimeFrameInterval);
        }
    }
    
    public void setFrameSkip(int frameSkip) {
        this.frameSkip = frameSkip;
    }
    
    public int getFrameSkip() {
        return this.frameSkip;
    }
    
}
