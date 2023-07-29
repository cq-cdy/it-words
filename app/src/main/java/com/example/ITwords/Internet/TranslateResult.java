package com.example.ITwords.Internet;

import java.util.List;

public class TranslateResult {


    /**
     * from : zh
     * to : en
     * trans_result : [{"src":"null要就找一个让你衣食无忧的人","dst":"Null wants to find someone who will make you comfortable with food and clothing"},{"src":"要么就找一个爱你爱到骨子里的","dst":"Or find someone who loves you to the bone."},{"src":"要么就一个人","dst":"Or just one person."},{"src":"不然为什么要谈恋爱","dst":"Otherwise why to fall in love?"},{"src":"是酒不好喝","dst":"It's a bad wine."},{"src":"还是手机不好玩","dst":"Or mobile phones are not fun"}]
     */

    private String from;
    private String to;
    private List<TransResultBean> trans_result;

    @Override
    public String toString() {
        return "TranslateResult{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", trans_result=" + trans_result +
                '}';
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public List<TransResultBean> getTrans_result() {
        return trans_result;
    }

    public void setTrans_result(List<TransResultBean> trans_result) {
        this.trans_result = trans_result;
    }

    public static class TransResultBean {
        /**
         * src : null要就找一个让你衣食无忧的人
         * dst : Null wants to find someone who will make you comfortable with food and clothing
         */

        private String src;
        private String dst;

        @Override
        public String toString() {
            return "TransResultBean{" +
                    "src='" + src + '\'' +
                    ", dst='" + dst + '\'' +
                    '}';
        }

        public String getSrc() {
            return src;
        }

        public void setSrc(String src) {
            this.src = src;
        }

        public String getDst() {
            return dst;
        }

        public void setDst(String dst) {
            this.dst = dst;
        }
    }
}
