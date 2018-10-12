package org.fkjava.mbao.domain;

import java.util.List;

public class IndexPage {


    private List<Article> articles;
    /**
     * 页码
     */
    private Integer number;
    /**
     * 数据库记录是从第几条开始被读取出来
     */
    private Integer offset;
    private Integer pageSize;
    private Integer totalPages;
    private Integer totalRows;

    private List<ArticleType> topTypes;
    private List<ArticleType> types;

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(Integer totalRows) {
        this.totalRows = totalRows;
    }

    public List<ArticleType> getTopTypes() {
        return topTypes;
    }

    public void setTopTypes(List<ArticleType> topTypes) {
        this.topTypes = topTypes;
    }

    public List<ArticleType> getTypes() {
        return types;
    }

    public void setTypes(List<ArticleType> types) {
        this.types = types;
    }
}
