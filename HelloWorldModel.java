package com.myproject.core.models;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class HelloWorldModel {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Inject
    private SlingHttpServletRequest slingHttpServletRequest;

    private String userAgent;

    // User-Agent and Accept HTTP request headers
    private String httpAccept = "";

    // Initialize some initial smartphone string variables.
    public static final String engineWebKit = "webkit";
    public static final String deviceAndroid = "android";
    public static final String deviceIphone = "iphone";
    public static final String deviceIpod = "ipod";
    public static final String deviceSymbian = "symbian";
    public static final String deviceS60 = "series60";
    public static final String deviceS70 = "series70";
    public static final String deviceS80 = "series80";
    public static final String deviceS90 = "series90";
    public static final String deviceWinMob = "windows ce";
    public static final String deviceWindows = "windows"; 
    public static final String deviceIeMob = "iemobile";
    public static final String enginePie = "wm5 pie"; //An old Windows Mobile
    public static final String deviceBB = "blackberry";   
    public static final String vndRIM = "vnd.rim"; //Detectable when BB devices emulate IE or Firefox
    public static final String deviceBBStorm = "blackberry95";  //Storm 1 and 2 
    public static final String devicePalm = "palm";
    public static final String deviceWebOS = "webos"; //For Palm's new WebOS devices

    public static final String engineBlazer = "blazer"; //Old Palm
    public static final String engineXiino = "xiino"; //Another old Palm

    //Initialize variables for mobile-specific content.
    public static final String vndwap = "vnd.wap";
    public static final String wml = "wml";   

    public static final String engineOpera = "opera"; //Popular browser
    public static final String engineNetfront = "netfront"; //Common embedded OS browser
    public static final String engineUpBrowser = "up.browser"; //common on some phones
    public static final String engineOpenWeb = "openweb"; //Transcoding by OpenWave server
    public static final String deviceMidp = "midp"; //a mobile Java technology
    public static final String uplink = "up.link";

    public static final String devicePda = "pda"; //some devices report themselves as PDAs
    public static final String mini = "mini";  //Some mobile browsers put "mini" in their names.
    public static final String mobile = "mobile"; //Some mobile browsers put "mobile" in their user agent strings.
    public static final String mobi = "mobi"; //Some mobile browsers put "mobi" in their user agent strings.
    
    public String getUserAgent() {
        return userAgent;
    }

    @PostConstruct
    public void init() {
        try{
        if (slingHttpServletRequest != null) {
            userAgent = slingHttpServletRequest.getHeader("User-Agent").toLowerCase();
            logger.debug("User-Agent ; {}", userAgent);
        }
    } catch (Exception e) {
        logger.error(e.getMessage(), e);
    }
    }

    /**
     * Detects if the current device is an iPhone.
     */
    public boolean detectIphone() {
        // The iPod touch says it's an iPhone! So let's disambiguate.
        return userAgent.indexOf(deviceIphone) != -1 && !detectIpod();
    }

    /**
     * Detects if the current device is an iPod Touch.
     */
    public boolean detectIpod() {
        return userAgent.indexOf(deviceIpod) != -1;
    }

    /**
     * Detects if the current device is an iPhone or iPod Touch.
     */
    public boolean detectIphoneOrIpod() {
        //We repeat the searches here because some iPods may report themselves as an iPhone, which would be okay.
        return userAgent.indexOf(deviceIphone) != -1 || userAgent.indexOf(deviceIpod) != -1;
    }

    /**
     * Detects if the current device is an Android OS-based device.
     */
    public boolean detectAndroid() {
        return userAgent.indexOf(deviceAndroid) != -1;
    }

    /**
     * Detects if the current device is an Android OS-based device and
     * the browser is based on WebKit.
     */
    public boolean detectAndroidWebKit() {
        return detectAndroid() && detectWebkit();
    }

    /**
     * Detects if the current browser is based on WebKit.
     */
    public boolean detectWebkit() {
        return userAgent.indexOf(engineWebKit) != -1;
    }

    /**
     * Detects if the current browser is the S60 Open Source Browser.
     */
    public boolean detectS60OssBrowser() {
        //First, test for WebKit, then make sure it's either Symbian or S60.
        return detectWebkit() && (userAgent.indexOf(deviceSymbian) != -1 || userAgent.indexOf(deviceS60) != -1);
    }

    /**
     *
     * Detects if the current device is any Symbian OS-based device,
     *   including older S60, Series 70, Series 80, Series 90, and UIQ, 
     *   or other browsers running on these devices.
     */
    public boolean detectSymbianOS() {
        return userAgent.indexOf(deviceSymbian) != -1 || userAgent.indexOf(deviceS60) != -1 ||
                userAgent.indexOf(deviceS70) != -1 || userAgent.indexOf(deviceS80) != -1 ||
                userAgent.indexOf(deviceS90) != -1;
    }

    /**
     * Detects if the current browser is a Windows Mobile device.
     */
    public boolean detectWindowsMobile() {
        //Most devices use 'Windows CE', but some report 'iemobile' 
        //  and some older ones report as 'PIE' for Pocket IE. 
        return userAgent.indexOf(deviceWinMob) != -1 ||
                userAgent.indexOf(deviceIeMob) != -1 ||
                userAgent.indexOf(enginePie) != -1 ||
                (detectWapWml() && userAgent.indexOf(deviceWindows) != -1);
    }

    /**
     * Detects if the current browser is a BlackBerry of some sort.
     */
    public boolean detectBlackBerry() {
        return userAgent.indexOf(deviceBB) != -1 || httpAccept.indexOf(vndRIM) != -1;
    }

    /**
     * Detects if the current browser is a BlackBerry Touch
     * device, such as the Storm
     */
    public boolean detectBlackBerryTouch() {
        return userAgent.indexOf(deviceBBStorm) != -1;
    }

    /**
     * Detects if the current browser is on a PalmOS device.
     */
    public boolean detectPalmOS() {
        //Most devices nowadays report as 'Palm', but some older ones reported as Blazer or Xiino.
        if (userAgent.indexOf(devicePalm) != -1 || userAgent.indexOf(engineBlazer) != -1 ||
                userAgent.indexOf(engineXiino) != -1 && !detectPalmWebOS()) {
            //Make sure it's not WebOS first
            if (detectPalmWebOS()) { return false; }
            else { return true; }
        }
        return false;
    }

    /**
     * Detects if the current browser is on a Palm device
     *    running the new WebOS.
     */
    public boolean detectPalmWebOS() {
        return userAgent.indexOf(deviceWebOS) != -1;
    }

    /**
     * Check to see whether the device is any device
     *   in the 'smartphone' category.
     */
    public boolean detectSmartphone() {
        return (detectIphoneOrIpod() ||
                detectS60OssBrowser() ||
                detectSymbianOS() ||
                detectWindowsMobile() ||
                detectBlackBerry() ||
                detectPalmOS() ||
                detectPalmWebOS() ||
                detectAndroid());
    }

     /**
     * Detects Opera Mobile or Opera Mini.
     * Added by AHand
     */
    public boolean detectOperaMobile() {
        return userAgent.indexOf(engineOpera) != -1 && (userAgent.indexOf(mini) != -1 || userAgent.indexOf(mobi) != -1);
    }

    /**
     * Detects whether the device supports WAP or WML.
     */
    public boolean detectWapWml() {
        return httpAccept.indexOf(vndwap) != -1 || httpAccept.indexOf(wml) != -1;
    }

}
