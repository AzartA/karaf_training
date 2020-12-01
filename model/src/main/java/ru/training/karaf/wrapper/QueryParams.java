package ru.training.karaf.wrapper;

import ru.training.karaf.view.FilterParam;
import ru.training.karaf.view.SortParam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QueryParams {
    private final int[] pagination;
    private List<FilterParam> filters;
    private List<SortParam> sorts;

    private QueryParams() {
        filters = new ArrayList<>();
        sorts = new ArrayList<>();
        pagination = new int[2];
    }

    public static QueryParams create() {
        return new QueryParams();
    }

    public List<FilterParam> getFilters() {
        return filters;
    }

    public List<SortParam> getSorts() {
        return sorts;
    }

    public void setSorts(List<SortParam> sorts) {
        this.sorts = sorts;
    }

    public int[] getPagination() {
        return pagination;
    }

    public void setPagination(int pg, int sz) {
        pagination[0] = pg;
        pagination[1] = sz;
    }

    public void addFilterParams(List<FilterParam> params) {
        filters.addAll(params);
    }

    public void addSortParams(List<SortParam> params) {
        sorts.addAll(params);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QueryParams)) {
            return false;
        }

        QueryParams that = (QueryParams) o;

        if (!Arrays.equals(pagination, that.pagination)) {
            return false;
        }
        if (!filters.equals(that.filters)) {
            return false;
        }
        return sorts.equals(that.sorts);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(pagination);
        result = 31 * result + filters.hashCode();
        result = 31 * result + sorts.hashCode();
        return result;
    }
}
