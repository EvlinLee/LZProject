package com.by.lizhiyoupin.app.loader;

import java.util.List;

public interface IPageStrategy<Data, Page> {
    int DEFAULT_PAGE_SIZE = 15;

    /**
     * 分页开始页
     */
    Page begin();

    /**
     * 分页加载数量
     */
    default int pageSize() {
        return DEFAULT_PAGE_SIZE;
    }

    /**
     * 分页增加策略
     */
    Page pageStrategy(List<Data> data, Page current);

    class IndexPageStrategy<Data> implements IPageStrategy<Data, Integer> {
        private int pageBegin;
        private int pageSize;

        public IndexPageStrategy() {
            this(0);
        }

        public IndexPageStrategy(int pageBegin) {
            this(pageBegin, DEFAULT_PAGE_SIZE);
        }

        public IndexPageStrategy(int pageBegin, int pageSize) {
            this.pageBegin = pageBegin;
            this.pageSize = pageSize;
        }

        @Override
        public Integer begin() {
            return pageBegin;
        }

        @Override
        public Integer pageStrategy(List<Data> data, Integer current) {
            return current == null ? begin() + 1 : ++current;
        }

        @Override
        public int pageSize() {
            return pageSize;
        }
    }
}
