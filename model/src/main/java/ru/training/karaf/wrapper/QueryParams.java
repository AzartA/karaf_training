package ru.training.karaf.wrapper;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

public class QueryParams<T> {
    @Valid
    private List<FilterParam> filters;
    @Valid
    private List<SortParam> sorts;
    private int[] pagination = new int[2];
    private Class<T> type;

    private QueryParams() {
    }

    static <T> QueryParams<T> create() {
        return new QueryParams<>();
    }

    public List<FilterParam> getFilters() {
        return filters;
    }

    public void setFilters(List<FilterParam> filters) {
        this.filters = filters;
    }

    public List<SortParam> getSorts() {
        return sorts;
    }

    public void setSorts(List<SortParam> sorts) {
        this.sorts = sorts;
    }

    public Class<T> getType() {
        return type;
    }

    public void setType(Class<T> type) {
        this.type = type;
    }

    public int[] getPagination() {
        return pagination;
    }

    public void setPagination(@Min(0) int pg, @Min(0) int sz) {
        pagination[0] = pg;
        pagination[1] = sz;
    }


}
