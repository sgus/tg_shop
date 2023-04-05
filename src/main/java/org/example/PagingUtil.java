package org.example;

import java.util.List;

public class PagingUtil<T> {
    private List<T> items;

    public PagingUtil(List<T> items) {
        this.items = items;
    }

    public List<T> getPage(int pageNumber, int pageSize) {
        int startIndex = (pageNumber - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, items.size());
        return items.subList(startIndex, endIndex);
    }

    public int getTotalPages(int pageSize) {
        return (int) Math.ceil((double) items.size() / pageSize);
    }
}
