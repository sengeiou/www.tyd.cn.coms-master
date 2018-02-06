package com.touedian.tyd.com.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/1/30.
 */

public class CardviewBean {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * mcard_id : 8888
         * mcard_name : 中国银行
         * mcard_type : Credit
         */
        private String mcard_id;
        private String mcard_name;
        private String mcard_type;

        public String getMcard_id() {
            return mcard_id;
        }

        public void setMcard_id(String mcard_id) {
            this.mcard_id = mcard_id;
        }

        public String getMcard_name() {
            return mcard_name;
        }

        public void setMcard_name(String mcard_name) {
            this.mcard_name = mcard_name;
        }

        public String getMcard_type() {
            return mcard_type;
        }

        public void setMcard_type(String mcard_type) {
            this.mcard_type = mcard_type;
        }
    }
}
