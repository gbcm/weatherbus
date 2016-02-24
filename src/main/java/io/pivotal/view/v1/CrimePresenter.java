package io.pivotal.view.v1;

import io.pivotal.service.response.CrimeInfo;
import io.pivotal.view.JsonPresenter;

public class CrimePresenter extends JsonPresenter {
    private CrimeInfo data;

    public CrimePresenter(CrimeInfo crimeInfo) {
        this.data = crimeInfo;
    }
}
