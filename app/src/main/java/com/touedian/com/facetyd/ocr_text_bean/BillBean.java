package com.touedian.com.facetyd.ocr_text_bean;

import java.util.List;

/**
 * Created by Administrator on 2018/2/27.
 */

public class BillBean {


    /**
     * direction : 3
     * log_id : 8246800536008123801
     * words_result : [{"location":{"height":124,"left":500,"top":603,"width":16},"words":"发票代码:0110018001"},{"location":{"height":425,"left":473,"top":266,"width":34},"words":"北京增值电杀普通发票发柔号码:116003"},{"location":{"height":160,"left":449,"top":601,"width":17},"words":"开求日期:2018年03月03日"},{"location":{"height":186,"left":425,"top":599,"width":17},"words":"校验码:8528143474H8273478"},{"location":{"height":141,"left":435,"top":20,"width":15},"words":"机5编号:661616325274"},{"location":{"height":60,"left":428,"top":402,"width":21},"words":"局"},{"location":{"height":51,"left":402,"top":112,"width":16},"words":"称:个人"},{"location":{"height":14,"left":404,"top":46,"width":16},"words":"名"},{"location":{"height":311,"left":392,"top":483,"width":18},"words":"密48>83*>5596-5146-8*52<*87>6"},{"location":{"height":268,"left":370,"top":523,"width":20},"words":"4->>5850+34378-73>388><7338"},{"location":{"height":80,"left":381,"top":47,"width":15},"words":"纳税人识别号"},{"location":{"height":81,"left":359,"top":47,"width":15},"words":"地是、电话"},{"location":{"height":278,"left":349,"top":523,"width":18},"words":"<5*45*565/0+->6659*399*12/5"},{"location":{"height":79,"left":337,"top":46,"width":16},"words":"开户行及球号"},{"location":{"height":313,"left":330,"top":478,"width":23},"words":"97-+290>5850+34378-73>3<1/+"},{"location":{"height":32,"left":313,"top":376,"width":11},"words":"我量"},{"location":{"height":25,"left":313,"top":665,"width":11},"words":"税车"},{"location":{"height":293,"left":313,"top":42,"width":19},"words":"货物或应税劳务、三务名称|规格型号单位"},{"location":{"height":70,"left":294,"top":619,"width":10},"words":"5716.24117%"},{"location":{"height":34,"left":293,"top":776,"width":10},"words":"97.76"},{"location":{"height":28,"left":295,"top":497,"width":9},"words":"5716"},{"location":{"height":183,"left":293,"top":15,"width":17},"words":"移动通信设备Applihone8P"},{"location":{"height":179,"left":277,"top":16,"width":15},"words":"hs(AI864)64GB金色移动联通"},{"location":{"height":58,"left":259,"top":28,"width":16},"words":"信4G手机"},{"location":{"height":58,"left":223,"top":140,"width":35},"words":"移动联"},{"location":{"height":66,"left":243,"top":623,"width":11},"words":"5L8I17"},{"location":{"height":113,"left":242,"top":28,"width":16},"words":"多动通信设备精ppp"},{"location":{"height":139,"left":225,"top":17,"width":16},"words":"LsaA1864)61GB金色移"},{"location":{"height":70,"left":207,"top":17,"width":16},"words":"电信4G手机"},{"location":{"height":36,"left":157,"top":770,"width":11},"words":"6.44"},{"location":{"height":64,"left":154,"top":593,"width":15},"words":"4861,43"},{"location":{"height":175,"left":129,"top":235,"width":20},"words":"伍仟陆佰捐拾柒圆角柴分"},{"location":{"height":91,"left":130,"top":68,"widt  h":15},"words":"价税合计(大写)"},{"location":{"height":108,"left":133,"top":614,"width":15},"words":"写￥5687.87"},{"location":{"height":204,"left":103,"top":114,"width":16},"words":"称:北京京东世纪信息技术有限公司"},{"location":{"height":198,"left":108,"top":507,"width":14},"words":"订单号717066316,ME1529790998"},{"location":{"height":11,"left":103,"top":50,"width":13},"words":"名"},{"location":{"height":15,"left":93,"top":22,"width":13},"words":"销"},{"location":{"height":38,"left":87,"top":704,"width":17},"words":"纪信息"},{"location":{"height":328,"left":63,"top":48,"width":17},"words":"地址,电话:北京市北京经济技术开发区科创十四街9号2幢B"},{"location":{"height":18,"left":68,"top":404,"width":10},"words":"tt"},{"location":{"height":310,"left":68,"top":17,"width":33},"words":"纳税人识别号:91110302562134916R"},{"location":{"height":14,"left":53,"top":23,"width":13},"words":"万"},{"location":{"height":7,"left":57,"top":486,"width":9},"words":"E"},{"location":{"height":22,"left":49,"top":721,"width":20},"words":"5"},{"location":{"height":295,"left":40,"top":48,"width":19},"words":"开户行及号:交行北京海淀支行1100605760181500"},{"location":{"height":53,"left":27,"top":707,"width":18},"words":"票专用章"},{"location":{"height":97,"left":21,"top":430,"width":16},"words":"开票人:京东商城"},{"location":{"height":49,"left":24,"top":598,"width":14},"words":"销售方:("},{"location":{"height":101,"left":16,"top":28,"width":16},"words":"收款人:京东商城"},{"location":{"height":26,"left":18,"top":258,"width":15},"words":"复核"}]
     * words_result_num : 51
     */

    private int direction;
    private long log_id;
    private int words_result_num;
    private List<WordsResultBean> words_result;

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public long getLog_id() {
        return log_id;
    }

    public void setLog_id(long log_id) {
        this.log_id = log_id;
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
         * location : {"height":124,"left":500,"top":603,"width":16}
         * words : 发票代码:0110018001
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
             * height : 124
             * left : 500
             * top : 603
             * width : 16
             */

            private int height;
            private int left;
            private int top;
            private int width;

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

            public int getTop() {
                return top;
            }

            public void setTop(int top) {
                this.top = top;
            }

            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }
        }
    }
}
