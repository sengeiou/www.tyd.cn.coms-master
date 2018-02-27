package com.touedian.com.facetyd.ocr_text_bean;

/**
 * Created by Administrator on 2018/2/27.
 */

public class LicencePlateBean {


    /**
     * log_id : 8875787305595901754
     * words_result : {"color":"blue","number":"苏AUA888"}
     */

    private long log_id;
    private WordsResultBean words_result;

    public long getLog_id() {
        return log_id;
    }

    public void setLog_id(long log_id) {
        this.log_id = log_id;
    }

    public WordsResultBean getWords_result() {
        return words_result;
    }

    public void setWords_result(WordsResultBean words_result) {
        this.words_result = words_result;
    }

    public static class WordsResultBean {
        /**
         * color : blue
         * number : 苏AUA888
         */

        private String color;
        private String number;

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }
    }
}
