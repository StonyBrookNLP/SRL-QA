 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProcessFrame {

    private String processName;
    private String rawText;
    private String[] tokenizedText;
    private String underGoer;
    private String enabler;
    private String trigger;
    private String result;
    private String underSpecified;

    public ArrayList<Integer> getTriggerIdx() {
        ArrayList<Integer> matchIdx = new ArrayList<Integer>();
        String[] triggerItem = trigger.split("\\|");
        String[] tokenized = tokenizedText;
        for (int i = 0; i < triggerItem.length; i++) {
            List<String> ls = StanfordTokenizerSingleton.getInstance().tokenize(triggerItem[i].trim());
            String[] triggerValues = ls.toArray(new String[ls.size()]);
            if (getIdxMatches(triggerValues, tokenized) != null) {
                matchIdx.addAll(getIdxMatches(triggerValues, tokenized));
            }
        }

        return matchIdx;
    }

    public ArrayList<Integer> getEnablerIdx()
    {
        ArrayList<Integer> matchIdx = new ArrayList<Integer>();
        String[] enablerItem= enabler.split("\\|");
        String[] tokenized = tokenizedText;
        for (int i = 0; i < enablerItem.length; i++) {
            List<String> ls = StanfordTokenizerSingleton.getInstance().tokenize(enablerItem[i].trim());
            String[] enablerValues = ls.toArray(new String[ls.size()]);
            if (getIdxMatches(enablerValues, tokenized) != null) {
                matchIdx.addAll(getIdxMatches(enablerValues, tokenized));
            }
        }

        return matchIdx;
    }
    
    public ArrayList<Integer> getResultIdx()
    {
        ArrayList<Integer> matchIdx = new ArrayList<Integer>();
        String[] resultItem= enabler.split("\\|");
        String[] tokenized = tokenizedText;
        for (int i = 0; i < resultItem.length; i++) {
            List<String> ls = StanfordTokenizerSingleton.getInstance().tokenize(resultItem[i].trim());
            String[] resultValues = ls.toArray(new String[ls.size()]);
            if (getIdxMatches(resultValues, tokenized) != null) {
                matchIdx.addAll(getIdxMatches(resultValues, tokenized));
            }
        }

        return matchIdx;
    }
    public ArrayList<Integer> getUndergoerIdx() {
        ArrayList<Integer> matchIdx = new ArrayList<Integer>();
        String[] undergoerItem = underGoer.split("\\|");
        String[] tokenized = tokenizedText;
        for (int i = 0; i < undergoerItem.length; i++) {
            List<String> ls = StanfordTokenizerSingleton.getInstance().tokenize(undergoerItem[i].trim());
            String[] undergoerValues = ls.toArray(new String[ls.size()]);
            if (getIdxMatches(undergoerValues, tokenized) != null) {
                matchIdx.addAll(getIdxMatches(undergoerValues, tokenized));
            }
        }

        return matchIdx;
    }

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

    public ArrayList<Integer> getIdxMatches(String[] targetPattern, String[] tokenizedSentence) {
        boolean inRegion = false;
        int matchStart = 0;
        int matchEnd = targetPattern.length;
        ArrayList<Integer> idx = new ArrayList<Integer>();
        for (int i = 0; i < tokenizedSentence.length && matchStart < matchEnd; i++) {
            if (tokenizedSentence[i].equalsIgnoreCase(targetPattern[matchStart])) {
                idx.add(i + 1); // because ConLL index starts from 1 
                if (!inRegion) {
                    inRegion = true;
                }
                matchStart++;
            } else {
                if (inRegion) {
                    inRegion = false;
                    idx.clear();
                    matchStart--;
                }
            }
        }
        if (matchStart == matchEnd) {
            return idx;
        } else {
            if (targetPattern[0].length() > 0) {
                System.out.println(Arrays.toString(tokenizedSentence));
                System.out.println("ERROR : CANNOT FIND \"" + Arrays.toString(targetPattern) + "\" IN THE SENTENCE");
            }
            return null;
        }
    }

}
