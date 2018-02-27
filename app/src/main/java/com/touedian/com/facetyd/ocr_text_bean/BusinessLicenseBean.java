package com.touedian.com.facetyd.ocr_text_bean;

/**
 * Created by Administrator on 2018/2/27.
 */

public class BusinessLicenseBean {

    /**
     * log_id : 1437398720357589571
     * direction : 0
     * words_result_num : 7
     * words_result : {"社会信用代码":{"location":{"width":136,"top":220,"height":13,"left":441},"words":"91110114MA009EFJ52"},"单位名称":{"location":{"width":268,"top":260,"height":20,"left":154},"words":"投易点(北京)信息技术有限公司"},"法人":{"location":{"width":50,"top":347,"height":18,"left":156},"words":"胡小彬"},"证件编号":{"location":{"width":0,"top":0,"height":0,"left":0},"words":"无"},"成立日期":{"location":{"width":122,"top":403,"height":19,"left":157},"words":"2016年11月03日"},"地址":{"location":{"width":368,"top":315,"height":22,"left":155},"words":"北京市昌平区科技园区振兴路2号院2号楼4层2401-2413"},"有效期":{"location":{"width":124,"top":429,"height":18,"left":316},"words":"2046年11月02日"}}
     */

    private long log_id;
    private int direction;
    private int words_result_num;
    private WordsResultBean words_result;

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

    public WordsResultBean getWords_result() {
        return words_result;
    }

    public void setWords_result(WordsResultBean words_result) {
        this.words_result = words_result;
    }

    public static class WordsResultBean {
        /**
         * 社会信用代码 : {"location":{"width":136,"top":220,"height":13,"left":441},"words":"91110114MA009EFJ52"}
         * 单位名称 : {"location":{"width":268,"top":260,"height":20,"left":154},"words":"投易点(北京)信息技术有限公司"}
         * 法人 : {"location":{"width":50,"top":347,"height":18,"left":156},"words":"胡小彬"}
         * 证件编号 : {"location":{"width":0,"top":0,"height":0,"left":0},"words":"无"}
         * 成立日期 : {"location":{"width":122,"top":403,"height":19,"left":157},"words":"2016年11月03日"}
         * 地址 : {"location":{"width":368,"top":315,"height":22,"left":155},"words":"北京市昌平区科技园区振兴路2号院2号楼4层2401-2413"}
         * 有效期 : {"location":{"width":124,"top":429,"height":18,"left":316},"words":"2046年11月02日"}
         */

        private 社会信用代码Bean 社会信用代码;
        private 单位名称Bean 单位名称;
        private 法人Bean 法人;
        private 证件编号Bean 证件编号;
        private 成立日期Bean 成立日期;
        private 地址Bean 地址;
        private 有效期Bean 有效期;

        public 社会信用代码Bean get社会信用代码() {
            return 社会信用代码;
        }

        public void set社会信用代码(社会信用代码Bean 社会信用代码) {
            this.社会信用代码 = 社会信用代码;
        }

        public 单位名称Bean get单位名称() {
            return 单位名称;
        }

        public void set单位名称(单位名称Bean 单位名称) {
            this.单位名称 = 单位名称;
        }

        public 法人Bean get法人() {
            return 法人;
        }

        public void set法人(法人Bean 法人) {
            this.法人 = 法人;
        }

        public 证件编号Bean get证件编号() {
            return 证件编号;
        }

        public void set证件编号(证件编号Bean 证件编号) {
            this.证件编号 = 证件编号;
        }

        public 成立日期Bean get成立日期() {
            return 成立日期;
        }

        public void set成立日期(成立日期Bean 成立日期) {
            this.成立日期 = 成立日期;
        }

        public 地址Bean get地址() {
            return 地址;
        }

        public void set地址(地址Bean 地址) {
            this.地址 = 地址;
        }

        public 有效期Bean get有效期() {
            return 有效期;
        }

        public void set有效期(有效期Bean 有效期) {
            this.有效期 = 有效期;
        }

        public static class 社会信用代码Bean {
            /**
             * location : {"width":136,"top":220,"height":13,"left":441}
             * words : 91110114MA009EFJ52
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
                 * width : 136
                 * top : 220
                 * height : 13
                 * left : 441
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

        public static class 单位名称Bean {
            /**
             * location : {"width":268,"top":260,"height":20,"left":154}
             * words : 投易点(北京)信息技术有限公司
             */

            private LocationBeanX location;
            private String words;

            public LocationBeanX getLocation() {
                return location;
            }

            public void setLocation(LocationBeanX location) {
                this.location = location;
            }

            public String getWords() {
                return words;
            }

            public void setWords(String words) {
                this.words = words;
            }

            public static class LocationBeanX {
                /**
                 * width : 268
                 * top : 260
                 * height : 20
                 * left : 154
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

        public static class 法人Bean {
            /**
             * location : {"width":50,"top":347,"height":18,"left":156}
             * words : 胡小彬
             */

            private LocationBeanXX location;
            private String words;

            public LocationBeanXX getLocation() {
                return location;
            }

            public void setLocation(LocationBeanXX location) {
                this.location = location;
            }

            public String getWords() {
                return words;
            }

            public void setWords(String words) {
                this.words = words;
            }

            public static class LocationBeanXX {
                /**
                 * width : 50
                 * top : 347
                 * height : 18
                 * left : 156
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

        public static class 证件编号Bean {
            /**
             * location : {"width":0,"top":0,"height":0,"left":0}
             * words : 无
             */

            private LocationBeanXXX location;
            private String words;

            public LocationBeanXXX getLocation() {
                return location;
            }

            public void setLocation(LocationBeanXXX location) {
                this.location = location;
            }

            public String getWords() {
                return words;
            }

            public void setWords(String words) {
                this.words = words;
            }

            public static class LocationBeanXXX {
                /**
                 * width : 0
                 * top : 0
                 * height : 0
                 * left : 0
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

        public static class 成立日期Bean {
            /**
             * location : {"width":122,"top":403,"height":19,"left":157}
             * words : 2016年11月03日
             */

            private LocationBeanXXXX location;
            private String words;

            public LocationBeanXXXX getLocation() {
                return location;
            }

            public void setLocation(LocationBeanXXXX location) {
                this.location = location;
            }

            public String getWords() {
                return words;
            }

            public void setWords(String words) {
                this.words = words;
            }

            public static class LocationBeanXXXX {
                /**
                 * width : 122
                 * top : 403
                 * height : 19
                 * left : 157
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

        public static class 地址Bean {
            /**
             * location : {"width":368,"top":315,"height":22,"left":155}
             * words : 北京市昌平区科技园区振兴路2号院2号楼4层2401-2413
             */

            private LocationBeanXXXXX location;
            private String words;

            public LocationBeanXXXXX getLocation() {
                return location;
            }

            public void setLocation(LocationBeanXXXXX location) {
                this.location = location;
            }

            public String getWords() {
                return words;
            }

            public void setWords(String words) {
                this.words = words;
            }

            public static class LocationBeanXXXXX {
                /**
                 * width : 368
                 * top : 315
                 * height : 22
                 * left : 155
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

        public static class 有效期Bean {
            /**
             * location : {"width":124,"top":429,"height":18,"left":316}
             * words : 2046年11月02日
             */

            private LocationBeanXXXXXX location;
            private String words;

            public LocationBeanXXXXXX getLocation() {
                return location;
            }

            public void setLocation(LocationBeanXXXXXX location) {
                this.location = location;
            }

            public String getWords() {
                return words;
            }

            public void setWords(String words) {
                this.words = words;
            }

            public static class LocationBeanXXXXXX {
                /**
                 * width : 124
                 * top : 429
                 * height : 18
                 * left : 316
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
}
