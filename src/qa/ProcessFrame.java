/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qa;

public class ProcessFrame {
    private String processName;
    private String rawText;
    private String[] tokenizedText;
    private String underGoer;
    private String enabler;
    private String trigger;
    private String result;
    private String underSpecified;

    public String getRawText() {
        return rawText;
    }

    public String getUnderGoer() {
        return underGoer;
    }

    public String getEnabler() {
        return enabler;
    }

    public String getTrigger() {
        return trigger;
    }

    public String getResult() {
        return result;
    }

    public void setRawText(String rawText) {
        this.rawText = rawText;
    }

    public void setUnderGoer(String underGoer) {
        this.underGoer = underGoer;
    }

    public void setEnabler(String enabler) {
        this.enabler = enabler;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public void setTrigger(String trigger) {
        this.trigger = trigger;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getUnderSpecified() {
        return underSpecified;
    }

    public void setUnderSpecified(String underSpecified) {
        this.underSpecified = underSpecified;
    }

    public String[] getTokenizedText() {
        return tokenizedText;
    }

    public void setTokenizedText(String[] tokenizedText) {
        this.tokenizedText = tokenizedText;
    }

}
