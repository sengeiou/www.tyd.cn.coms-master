package com.touedian.com.facetyd.ocr_text_bean;

import java.util.List;

/**
 * Created by Administrator on 2018/2/26.
 */

public class NetWorkCardBean {

    /**
     * log_id : 7909590018893978012
     * direction : 3
     * words_result_num : 5
     * words_result : [{"words":"宝龙律师,找邓律师"},{"words":"电话:13798385305"},{"words":"十年经验,办案千件。"},{"words":"勤勉,专业,敬业。"},{"words":"更多信息登陆:WWW.lsb360.com"}]
     */

    private long log_id;
    private int direction;
    private int words_result_num;
    private List<WordsResultBean> words_result;

    public long getLog_id() {
        return log_id;
    }

    public void setLog_id(long log_id) {
        this.log_id = log_id;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getWords_result_num() {
        return words_result_num;
    }

    public void setWords_result_num(int words_result_num) {
        this.words_result_num = words_result_num;
    }

    public List<WordsResultBean> getWords_result() {
        return words_result;
    }

    public void setWords_result(List<WordsResultBean> words_result) {
        this.words_result = words_result;
    }

    public static class WordsResultBean {
        /**
         * words : 宝龙律师,找邓律师
         */

        private String words;

        public String getWords() {
            return words;
        }

        public void setWords(String words) {
            this.words = words;
        }
    }
}
