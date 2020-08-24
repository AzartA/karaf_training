package ru.training.karaf.rest.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;
import java.util.List;

@SupportedValidationTarget(ValidationTarget.PARAMETERS)
public class ParamsCountValidatorImpl implements ConstraintValidator<CountParams, Object[]> {
    @Override
    public void initialize(CountParams countParams) {

    }

    @Override
    public boolean isValid(Object[] params, ConstraintValidatorContext constraintValidatorContext) {
        List<?> p0 = (List<?>)params[0];
        List<?> p1 = (List<?>)params[1];
        List<?> p2 = (List<?>)params[2];

        if(params.length == 7) {
            List<?> p3 = (List<?>) params[3];
            List<?> p4 = (List<?>) params[4];
            return p0.size() == p1.size() && (p2.size() == p3.size() || p2.size() == p4.size());
        }else{
            return p0.size() == p1.size() || p0.size() == p2.size();
        }
    }
}
