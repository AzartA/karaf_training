package ru.training.karaf.view;

import java.util.List;

import ru.training.karaf.wrapper.QueryParams;

public class ViewUtil {

    public ViewUtil() {

    }

    public <T> QueryParams createQueryParams(List<FilterParam> filters, List<SortParam> sorts, int pg, int sz) {
        QueryParams query = QueryParams.create();
        if (!filters.isEmpty()) {
            query.addFilterParams(filters);
        }
        if (!sorts.isEmpty()) {
            query.addSortParams(sorts);
        }
        if (pg > 0 && sz > 0) {
            query.setPagination(pg, sz);
        }
        return query;
    }

    public <T> QueryParams createQueryParams(List<FilterParam> filters, int pg, int sz) {
        QueryParams query = QueryParams.create();
        if (!filters.isEmpty()) {
            query.addFilterParams(filters);
        }
        query.setPagination(pg, sz);
        return query;
    }
}
