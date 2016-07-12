
package com.patrykkrawczyk.pogodynka.json.wunder;

public class Temp {

    private String english;
    private Double metric;

    /**
     * 
     * @return
     *     The english
     */
    public String getEnglish() {
        return english;
    }

    /**
     * 
     * @param english
     *     The english
     */
    public void setEnglish(String english) {
        this.english = english;
    }

    /**
     * 
     * @return
     *     The metric
     */
    public Double getMetric() {
        return metric;
    }

    /**
     * 
     * @param metric
     *     The metric
     */
    public void setMetric(Double metric) {
        this.metric = metric;
    }

}
