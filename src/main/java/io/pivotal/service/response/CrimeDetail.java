package io.pivotal.service.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CrimeDetail {
    private String type;
    private int total;
    private String popular;
    private List<CrimeDetail> offenses;

    public CrimeDetail() {

    }

    public CrimeDetail(String type, int total, String popular,  List<CrimeDetail> offenses) {
        this.type = type;
        this.total = total;
        this.popular = popular;
        this.offenses = offenses;
    }
}
