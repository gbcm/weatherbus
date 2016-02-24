package io.pivotal.service.response;

import lombok.Data;

import java.util.List;

@Data
public class CrimeInfo {
    private int numberOfCrimes;
    private String mostFrequentCrimeType;
    private int numberOfViolentCrimes;
    private List<Offense> offenses;

    public CrimeInfo() {

    }

    public CrimeInfo(int numberOfCrimes, String mostFrequentCrimeType, int numberOfViolentCrimes, List<Offense> offenses) {
        this.numberOfCrimes = numberOfCrimes;
        this.mostFrequentCrimeType = mostFrequentCrimeType;
        this.numberOfViolentCrimes = numberOfViolentCrimes;
        this.offenses = offenses;
    }

    @Data
    static public class Offense {
        private String name;
        private int frequency;

        public Offense() {

        }

        public Offense(String name, int frequency) {
            this.name = name;
            this.frequency = frequency;
        }
    }
}
