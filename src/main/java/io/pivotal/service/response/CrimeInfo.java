package io.pivotal.service.response;

import lombok.Data;

@Data
public class CrimeInfo {
    private int numberOfCrimes;
    private String mostFrequentCrimeType;
    private int numberOfViolentCrimes;

    public CrimeInfo() {

    }

    public CrimeInfo(int numberOfCrimes, String mostFrequentCrimeType, int numberOfViolentCrimes) {
        this.numberOfCrimes = numberOfCrimes;
        this.mostFrequentCrimeType = mostFrequentCrimeType;
        this.numberOfViolentCrimes = numberOfViolentCrimes;
    }
}
