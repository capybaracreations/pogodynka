
package com.patrykkrawczyk.pogodynka.json.wunder;


public class HourlyForecast {

    private FCTTIME FCTTIME;
    private Temp temp;
    private Dewpoint dewpoint;
    private String condition;
    private String icon;
    private String icon_url;
    private String fctcode;
    private String sky;
    private Wspd wspd;
    private Wdir wdir;
    private String wx;
    private String uvi;
    private String humidity;
    private Windchill windchill;
    private Heatindex heatindex;
    private Feelslike feelslike;
    private Qpf qpf;
    private Snow snow;
    private String pop;
    private Mslp mslp;

    /**
     * 
     * @return
     *     The FCTTIME
     */
    public FCTTIME getFCTTIME() {
        return FCTTIME;
    }

    /**
     * 
     * @param fCTTIME
     *     The FCTTIME
     */
    public void setFCTTIME(FCTTIME fCTTIME) {
        this.FCTTIME = fCTTIME;
    }

    /**
     * 
     * @return
     *     The temp
     */
    public Temp getTemp() {
        return temp;
    }

    /**
     * 
     * @param temp
     *     The temp
     */
    public void setTemp(Temp temp) {
        this.temp = temp;
    }

    /**
     * 
     * @return
     *     The dewpoint
     */
    public Dewpoint getDewpoint() {
        return dewpoint;
    }

    /**
     * 
     * @param dewpoint
     *     The dewpoint
     */
    public void setDewpoint(Dewpoint dewpoint) {
        this.dewpoint = dewpoint;
    }

    /**
     * 
     * @return
     *     The condition
     */
    public String getCondition() {
        return condition;
    }

    /**
     * 
     * @param condition
     *     The condition
     */
    public void setCondition(String condition) {
        this.condition = condition;
    }

    /**
     * 
     * @return
     *     The icon
     */
    public String getIcon() {
        return icon;
    }

    /**
     * 
     * @param icon
     *     The icon
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * 
     * @return
     *     The icon_url
     */
    public String getIcon_url() {
        return icon_url;
    }

    /**
     * 
     * @param icon_url
     *     The icon_url
     */
    public void setIcon_url(String icon_url) {
        this.icon_url = icon_url;
    }

    /**
     * 
     * @return
     *     The fctcode
     */
    public String getFctcode() {
        return fctcode;
    }

    /**
     * 
     * @param fctcode
     *     The fctcode
     */
    public void setFctcode(String fctcode) {
        this.fctcode = fctcode;
    }

    /**
     * 
     * @return
     *     The sky
     */
    public String getSky() {
        return sky;
    }

    /**
     * 
     * @param sky
     *     The sky
     */
    public void setSky(String sky) {
        this.sky = sky;
    }

    /**
     * 
     * @return
     *     The wspd
     */
    public Wspd getWspd() {
        return wspd;
    }

    /**
     * 
     * @param wspd
     *     The wspd
     */
    public void setWspd(Wspd wspd) {
        this.wspd = wspd;
    }

    /**
     * 
     * @return
     *     The wdir
     */
    public Wdir getWdir() {
        return wdir;
    }

    /**
     * 
     * @param wdir
     *     The wdir
     */
    public void setWdir(Wdir wdir) {
        this.wdir = wdir;
    }

    /**
     * 
     * @return
     *     The wx
     */
    public String getWx() {
        return wx;
    }

    /**
     * 
     * @param wx
     *     The wx
     */
    public void setWx(String wx) {
        this.wx = wx;
    }

    /**
     * 
     * @return
     *     The uvi
     */
    public String getUvi() {
        return uvi;
    }

    /**
     * 
     * @param uvi
     *     The uvi
     */
    public void setUvi(String uvi) {
        this.uvi = uvi;
    }

    /**
     * 
     * @return
     *     The humidity
     */
    public String getHumidity() {
        return humidity;
    }

    /**
     * 
     * @param humidity
     *     The humidity
     */
    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    /**
     * 
     * @return
     *     The windchill
     */
    public Windchill getWindchill() {
        return windchill;
    }

    /**
     * 
     * @param windchill
     *     The windchill
     */
    public void setWindchill(Windchill windchill) {
        this.windchill = windchill;
    }

    /**
     * 
     * @return
     *     The heatindex
     */
    public Heatindex getHeatindex() {
        return heatindex;
    }

    /**
     * 
     * @param heatindex
     *     The heatindex
     */
    public void setHeatindex(Heatindex heatindex) {
        this.heatindex = heatindex;
    }

    /**
     * 
     * @return
     *     The feelslike
     */
    public Feelslike getFeelslike() {
        return feelslike;
    }

    /**
     * 
     * @param feelslike
     *     The feelslike
     */
    public void setFeelslike(Feelslike feelslike) {
        this.feelslike = feelslike;
    }

    /**
     * 
     * @return
     *     The qpf
     */
    public Qpf getQpf() {
        return qpf;
    }

    /**
     * 
     * @param qpf
     *     The qpf
     */
    public void setQpf(Qpf qpf) {
        this.qpf = qpf;
    }

    /**
     * 
     * @return
     *     The snow
     */
    public Snow getSnow() {
        return snow;
    }

    /**
     * 
     * @param snow
     *     The snow
     */
    public void setSnow(Snow snow) {
        this.snow = snow;
    }

    /**
     * 
     * @return
     *     The pop
     */
    public String getPop() {
        return pop;
    }

    /**
     * 
     * @param pop
     *     The pop
     */
    public void setPop(String pop) {
        this.pop = pop;
    }

    /**
     * 
     * @return
     *     The mslp
     */
    public Mslp getMslp() {
        return mslp;
    }

    /**
     * 
     * @param mslp
     *     The mslp
     */
    public void setMslp(Mslp mslp) {
        this.mslp = mslp;
    }

}
