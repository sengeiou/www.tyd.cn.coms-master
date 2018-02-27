package com.touedian.com.facetyd.ocr_text_bean;

import java.util.List;

/**
 * Created by Administrator on 2018/2/27.
 */

public class BillBean {

    /**
     * log_id : 5220490402245480099
     * direction : 3
     * words_result_num : 29
     * words_result : [{"location":{"width":39,"top":617,"height":33,"left":415},"words":"2"},{"location":{"width":28,"top":681,"height":138,"left":433},"words":"00100083"},{"location":{"width":47,"top":296,"height":205,"left":421},"words":"商业承兑汇票"},{"location":{"width":29,"top":248,"height":347,"left":393},"words":"田期贰零堂陆年肆月登推扮图"},{"location":{"width":28,"top":692,"height":120,"left":403},"words":"22349114"},{"location":{"width":27,"top":563,"height":169,"left":346},"words":"海朴豉实业有限公司"},{"location":{"width":25,"top":428,"height":23,"left":345},"words":"收"},{"location":{"width":32,"top":9,"height":326,"left":349},"words":"付全称上海赋银贸易有限公司"},{"location":{"width":39,"top":10,"height":283,"left":314},"words":"款账号121919427410803"},{"location":{"width":33,"top":427,"height":266,"left":316},"words":"款[盖12913045094"},{"location":{"width":23,"top":12,"height":23,"left":302},"words":"大"},{"location":{"width":27,"top":137,"height":180,"left":286},"words":"招行上海澜江镇支行"},{"location":{"width":32,"top":426,"height":253,"left":286},"words":"招行人民意场交"},{"location":{"width":23,"top":36,"height":41,"left":294},"words":"开户"},{"location":{"width":35,"top":11,"height":305,"left":248},"words":"出票金额入民币伍佰陆抬方元整"},{"location":{"width":23,"top":608,"height":235,"left":236},"words":"￥560000000托"},{"location":{"width":18,"top":121,"height":44,"left":241},"words":"太导"},{"location":{"width":44,"top":39,"height":308,"left":199},"words":"真到日贰子陆年伍月壹抬捌日"},{"location":{"width":27,"top":11,"height":102,"left":168},"words":"交易合号码"},{"location":{"width":23,"top":50,"height":106,"left":138},"words":"本汇票已轻承总"},{"location":{"width":16,"top":663,"height":27,"left":146},"words":"日款"},{"location":{"width":17,"top":212,"height":73,"left":143},"words":"条注付票畝"},{"location":{"width":15,"top":494,"height":16,"left":146},"words":"本"},{"location":{"width":29,"top":49,"height":110,"left":96},"words":"财多"},{"location":{"width":25,"top":50,"height":110,"left":79},"words":"有海"},{"location":{"width":28,"top":46,"height":111,"left":57},"words":"专"},{"location":{"width":27,"top":46,"height":109,"left":38},"words":"公银"},{"location":{"width":88,"top":239,"height":611,"left":35},"words":"赵伟一-「风"},{"location":{"width":16,"top":207,"height":17,"left":33},"words":"承"}]
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
         * location : {"width":39,"top":617,"height":33,"left":415}
         * words : 2
         */

        private LocationBean location;
        private String words;

        public LocationBean getLocation() {
            return location;
        }

        public void setLocation(LocationBean location) {
            this.location = location;
        }

        public String getWords() {
            return words;
        }

        public void setWords(String words) {
            this.words = words;
        }

        public static class LocationBean {
            /**
             * width : 39
             * top : 617
             * height : 33
             * left : 415
             */

            private int width;
            private int top;
            private int height;
            private int left;

            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }

            public int getTop() {
                return top;
            }

            public void setTop(int top) {
                this.top = top;
            }

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }

            public int getLeft() {
                return left;
            }

            public void setLeft(int left) {
                this.left = left;
            }
        }
    }
}
