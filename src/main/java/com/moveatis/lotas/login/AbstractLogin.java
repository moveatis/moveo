package com.moveatis.lotas.login;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
public abstract class AbstractLogin {
    
    protected String loginOutcome = "success";
    
    public abstract void init();
    
    public String getLoginOutcome() {
        return loginOutcome;
    }

    public void setLoginOutcome(String loginOutcome) {
        this.loginOutcome = loginOutcome;
    }
    
}
