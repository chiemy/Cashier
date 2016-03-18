package com.chiemy.app.cashier.bean;

import java.util.List;

/**
 * Created by chiemy on 16/3/4.
 */
public class BookCategoriesResponse {


    /**
     * list : [{"createdate":"2016-01-15 00:00:00","id":1,"imgurl":"book/image/icon/fl_yishu.png","sort":0,"title":"艺术"},{"createdate":"2016-01-15 00:00:00","id":2,"imgurl":"book/image/icon/fl_huiben.png","sort":0,"title":"绘本"},{"createdate":"2016-01-15 00:00:00","id":3,"imgurl":"book/image/icon/fl_renzhi.png","sort":0,"title":"认知"}]
     * status : {"msg":"ok","ret":1}
     */

    private ContentEntity content;

    public void setContent(ContentEntity content) {
        this.content = content;
    }

    public ContentEntity getContent() {
        return content;
    }

    public static class ContentEntity {
        /**
         * msg : ok
         * ret : 1
         */

        private StatusEntity status;
        /**
         * createdate : 2016-01-15 00:00:00
         * id : 1
         * imgurl : book/image/icon/fl_yishu.png
         * sort : 0
         * title : 艺术
         */

        private List<ListEntity> list;

        public void setStatus(StatusEntity status) {
            this.status = status;
        }

        public void setList(List<ListEntity> list) {
            this.list = list;
        }

        public StatusEntity getStatus() {
            return status;
        }

        public List<ListEntity> getList() {
            return list;
        }

        public static class StatusEntity {
            private String msg;
            private int ret;

            public void setMsg(String msg) {
                this.msg = msg;
            }

            public void setRet(int ret) {
                this.ret = ret;
            }

            public String getMsg() {
                return msg;
            }

            public int getRet() {
                return ret;
            }
        }

        public static class ListEntity {
            private String createdate;
            private int id;
            private String imgurl;
            private int sort;
            private String title;

            public void setCreatedate(String createdate) {
                this.createdate = createdate;
            }

            public void setId(int id) {
                this.id = id;
            }

            public void setImgurl(String imgurl) {
                this.imgurl = imgurl;
            }

            public void setSort(int sort) {
                this.sort = sort;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getCreatedate() {
                return createdate;
            }

            public int getId() {
                return id;
            }

            public String getImgurl() {
                return imgurl;
            }

            public int getSort() {
                return sort;
            }

            public String getTitle() {
                return title;
            }
        }
    }
}
