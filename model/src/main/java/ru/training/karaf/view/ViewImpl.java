package ru.training.karaf.view;

import ru.training.karaf.wrapper.FilterParamImpl;
import ru.training.karaf.wrapper.QueryParams;
import ru.training.karaf.wrapper.SortParamImpl;

import java.util.List;

public class ViewImpl {

    public ViewImpl() {

    }

    public <T> QueryParams createQueryParams(
            List<FilterParam> filters, List<SortParam> sorts, int pg, int sz
    ) {
        QueryParams query = QueryParams.create();
        if(!filters.isEmpty()){
            query.addFilterParams(filters);
        }
        if(!sorts.isEmpty()){
            query.addSortParams(sorts);
        }
        if(pg>0 && sz>0)
        query.setPagination(pg,sz);
        return query;
    }
    public <T> QueryParams createQueryParams(
            List<FilterParam> filters, int pg, int sz
    ) {
        QueryParams query = QueryParams.create();
        if(!filters.isEmpty()){
            query.addFilterParams(filters);
        }
        //if(pg>=0 && sz>0) {
            query.setPagination(pg, sz);
        //}
        return query;
    }
}
