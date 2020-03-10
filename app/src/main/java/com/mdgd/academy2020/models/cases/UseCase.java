package com.mdgd.academy2020.models.cases;

public interface UseCase<PARAMS, RESULT> {
    RESULT exec(PARAMS params);
}
