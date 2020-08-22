package ru.training.karaf.view;

import ru.training.karaf.validation.OSGIServiceDiscover;
import ru.training.karaf.wrapper.FilterParam;
import ru.training.karaf.wrapper.QueryParams;
import ru.training.karaf.wrapper.SortParam;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Set;

public class ViewImpl {

    public ViewImpl() {

    }

    public <T> QueryParams createQueryParams(
            List<String> by, List<String> order, List<String> field, List<String> cond, List<String> value,
            int pg, int sz, FilterParam auth, Class<T> type
    ) {
        if (field.size() != cond.size() && field.size() != value.size() || by.size() != order.size()) {
            throw new IllegalArgumentException("The count of parameters is different");
        }
        QueryParams query = QueryParams.create();
        if(auth!=null){
            query.addFilterParam(auth);
        }
        for (int i = 0; i <field.size(); i++) {
            query.addFilterParam(new FilterParam(field.get(i),cond.get(i),value.get(i),type));
        }
        for (int i = 0; i <by.size(); i++) {
            query.addSortParam(new SortParam(by.get(i), order.get(i),type));
        }
        query.setPagination(pg,sz);
        /*ValidatorFactory factory = Validation.byDefaultProvider().providerResolver(new OSGIServiceDiscover()).configure().buildValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<QueryParams>> constraintViolations =
                validator.validate( query, "p" );
        throw new ConstraintViolationException(constraintViolations);*/
        return query;
    }
    public <T> QueryParams createQueryParams(
           List<String> field, List<String> cond, List<String> value,
            int pg, int sz, FilterParam auth, Class<T> type
    ) {
        if (field.size() != cond.size() && field.size() != value.size()) {
            throw new IllegalArgumentException("The count of parameters is different");
        }
        QueryParams query = QueryParams.create();
        if(auth!=null){
            query.addFilterParam(auth);
        }
        for (int i = 0; i <field.size(); i++) {
            query.addFilterParam(new FilterParam(field.get(i),cond.get(i),value.get(i),type));
        }
        query.setPagination(pg,sz);
        return query;
    }
}
