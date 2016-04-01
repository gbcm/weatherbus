package io.pivotal.view.v1;

import io.pivotal.service.response.CrimeDetail;
import io.pivotal.view.JsonPresenter;

public class CrimePresenter extends JsonPresenter {
    private CrimeDetail data;

    public CrimePresenter(CrimeDetail crimeDetail) {
        this.data = crimeDetail;
    }
}
