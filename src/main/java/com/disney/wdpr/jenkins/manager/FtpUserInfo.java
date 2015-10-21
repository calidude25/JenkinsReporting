package com.disney.wdpr.jenkins.manager;

import com.jcraft.jsch.UserInfo;

public class FtpUserInfo implements UserInfo {

    private final String password;
    
    public FtpUserInfo(final String password) {
        super();
        this.password = password;
    }

    @Override
    public String getPassphrase() {
        return null;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean promptPassphrase(final String arg0) {
        return false;
    }

    @Override
    public boolean promptPassword(final String arg0) {
        return false;
    }

    @Override
    public boolean promptYesNo(final String arg0) {
        return false;
    }

    @Override
    public void showMessage(final String arg0) {
    }

}
