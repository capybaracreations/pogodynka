
package com.patrykkrawczyk.pogodynka.json.wunder;

public class Response {

    private String version;
    private String termsofService;
    private Features features;

    /**
     * 
     * @return
     *     The version
     */
    public String getVersion() {
        return version;
    }

    /**
     * 
     * @param version
     *     The version
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * 
     * @return
     *     The termsofService
     */
    public String getTermsofService() {
        return termsofService;
    }

    /**
     * 
     * @param termsofService
     *     The termsofService
     */
    public void setTermsofService(String termsofService) {
        this.termsofService = termsofService;
    }

    /**
     * 
     * @return
     *     The features
     */
    public Features getFeatures() {
        return features;
    }

    /**
     * 
     * @param features
     *     The features
     */
    public void setFeatures(Features features) {
        this.features = features;
    }

}
