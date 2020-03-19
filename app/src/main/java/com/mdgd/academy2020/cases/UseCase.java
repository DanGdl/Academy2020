package com.mdgd.academy2020.cases;

public interface UseCase<PARAMS, RESULT> {
    RESULT exec(PARAMS params);
}
