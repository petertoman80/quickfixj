/*******************************************************************************
 * Copyright (c) quickfixj.org  All rights reserved. 
 * 
 * This file is part of the QuickFIX/J FIX Engine 
 * 
 * This file may be distributed under the terms of the quickfixj.org 
 * license as defined by quickfixj.org and appearing in the file 
 * LICENSE included in the packaging of this file. 
 * 
 * This file is provided AS IS with NO WARRANTY OF ANY KIND, INCLUDING 
 * THE WARRANTY OF DESIGN, MERCHANTABILITY AND FITNESS FOR A 
 * PARTICULAR PURPOSE. 
 * 
 * See http://www.quickfixj.org/LICENSE for licensing information. 
 * 
 ******************************************************************************/

package org.quickfixj.jmx.mbean.session;

import java.io.IOException;
import java.util.ArrayList;

import javax.management.MBeanRegistration;
import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import quickfix.Session;
import quickfix.field.converter.UtcTimestampConverter;

public class SessionAdmin implements SessionAdminMBean, MBeanRegistration {

    private final Session session;

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final ObjectName connectorName;

    private MBeanServer mbeanServer;

    public SessionAdmin(Session session, ObjectName connnectorName) {
        this.session = session;
        this.connectorName = connnectorName;
    }

    /* (non-Javadoc)
     * @see quickfix.jmx.SessionMBean#getID()
     */
    public String getID() {
        return session.getSessionID().toString();
    }

    /* (non-Javadoc)
     * @see quickfix.jmx.SessionMBean#getTargetCompID()
     */
    public String getTargetCompID() {
        return session.getSessionID().getTargetCompID();
    }

    /* (non-Javadoc)
     * @see quickfix.jmx.SessionMBean#getSenderCompID()
     */
    public String getSenderCompID() {
        return session.getSessionID().getSenderCompID();
    }

    /* (non-Javadoc)
     * @see quickfix.jmx.SessionMBean#getBeginString()
     */
    public String getBeginString() {
        return session.getSessionID().getBeginString();
    }

    /* (non-Javadoc)
     * @see quickfix.jmx.SessionMBean#isLoggedOn()
     */
    public boolean isLoggedOn() {
        return session.isLoggedOn();
    }

    /* (non-Javadoc)
     * @see quickfix.jmx.SessionMBean#getRemoteIPAddress()
     */
    public String getRemoteIPAddress() {
        if (session.getResponder() != null) {
            return session.getResponder().getRemoteIPAddress();
        } else {
            return "";
        }
    }

    /* (non-Javadoc)
     * @see quickfix.jmx.SessionMBean#reset()
     */
    public void reset() throws IOException {
        logInvocation("reset");
        session.reset();
    }

    /* (non-Javadoc)
     * @see quickfix.jmx.SessionMBean#getNextSenderMsgSeqNum()
     */
    public int getNextSenderMsgSeqNum() throws IOException {
        return session.getExpectedSenderNum();
    }

    /* (non-Javadoc)
     * @see quickfix.jmx.QFJSessionMBean#setNextSenderMsgSeqNum(int)
     */
    public void setNextSenderMsgSeqNum(int next) throws IOException {
        logAttributeChange("NextSenderMsgSeqNum", next);
        session.setNextSenderMsgSeqNum(next);
    }

    /* (non-Javadoc)
     * @see quickfix.jmx.QFJSessionMBean#setNextTargetMsgSeqNum(int)
     */
    public void setNextTargetMsgSeqNum(int next) throws IOException {
        logAttributeChange("NextTargetMsgSeqNum", next);
        session.setNextTargetMsgSeqNum(next);
    }

    /* (non-Javadoc)
     * @see quickfix.jmx.SessionMBean#getNextTargetMsgSeqNum()
     */
    public int getNextTargetMsgSeqNum() throws IOException {
        return session.getExpectedTargetNum();
    }

    /* (non-Javadoc)
     * @see quickfix.jmx.SessionMBean#getMessages(int, int)
     */
    public String[] getMessages(int startSequence, int endSequence) throws IOException {
        ArrayList messages = new ArrayList();
        session.getStore().get(startSequence, endSequence, messages);
        return (String[]) messages.toArray(new String[messages.size()]);
    }

    /* (non-Javadoc)
     * @see quickfix.jmx.QFJSessionMBean#disconnect()
     */
    public void disconnect() throws IOException {
        logInvocation("disconnect");
        session.disconnect();
    }

    /* (non-Javadoc)
     * @see quickfix.jmx.QFJSessionMBean#logon()
     */
    public void logon() {
        logInvocation("logon");
        session.logon();
    }

    /* (non-Javadoc)
     * @see quickfix.jmx.SessionMBean#logoff()
     */
    public void logoff() {
        logInvocation("logout");
        session.logout();
    }

    /* (non-Javadoc)
     * @see quickfix.jmx.QFJSessionMBean#isReconnectEnabled()
     */
    public boolean isReconnectEnabled() {
        return session.isEnabled();
    }

    /* (non-Javadoc)
     * @see quickfix.jmx.SessionMBean#getHost()
     */
    public String getHost() {
        try {
            return java.net.InetAddress.getLocalHost().getHostName();
        } catch (java.net.UnknownHostException uhe) {
            log.error(uhe.getMessage(), uhe);
            return "N/A";
        }
    }

    /* (non-Javadoc)
     * @see quickfix.jmx.SessionMBean#getProcessID()
     */
    public String getProcessID() {
        return System.getProperty("java.pid");
    }


    public ObjectName getConnectorName() {
        return connectorName;
    }

    public boolean getCheckCompID() {
        return session.getCheckCompID();
    }

    public String getLogClassName() {
        return session.getLog().getClass().getName();
    }

    public int getLogonTimeout() {
        return session.getLogonTimeout();
    }

    public int getLogoutTimeout() {
        return session.getLogoutTimeout();
    }

    public String getMessageFactoryClassName() {
        return session.getMessageFactory().getClass().getName();
    }

    public String getMessageStoreClassName() {
        return session.getStore().getClass().getName();
    }

    public boolean getRedundantResendRequestsAllowed() {
        return session.getRedundantResentRequestsAllowed();
    }

    public boolean getRefreshOnLogon() {
        return session.getRefreshOnLogon();
    }

    public boolean getResetOnDisconnect() {
        return session.getResetOnDisconnect();
    }

    public boolean getResetOnLogout() {
        return session.getResetOnLogout();
    }

    public boolean isLogonAlreadySent() {
        return session.isLogonAlreadySent();
    }

    public boolean isLogonReceived() {
        return session.isLogonReceived();
    }

    public boolean isLogonSendNeeded() {
        return session.isLogonSendNeeded();
    }

    public boolean isLogonSent() {
        return session.isLogonSent();
    }

    public boolean isLogonTimedOut() {
        return session.isLogonTimedOut();
    }

    public boolean isLogoutReceived() {
        return session.isLogoutReceived();
    }

    public boolean isLogoutSent() {
        return session.isLogoutSent();
    }

    public boolean isLogoutTimedOut() {
        return session.isLogoutTimedOut();
    }

    public void setLogonTimeout(int seconds) {
        logAttributeChange("LogonTimeout", seconds);
        session.setLogonTimeout(seconds);
    }

    public void setLogoutTimeout(int seconds) {
        logAttributeChange("LogoutTimeout", seconds);
        session.setLogoutTimeout(seconds);
    }

    public boolean isUsingDataDictionary() {
        return session.isUsingDataDictionary();
    }

    public String getSessionID() {
        return session.getSessionID().toString();
    }

    public boolean getEnabled() {
        return session.isEnabled();
    }

    public String getStartTime() {
        try {
            return UtcTimestampConverter.convert(session.getStartTime(), true);
        } catch (IOException e) {
            return "[ERROR]";
        }
    }

    public String getConnectionRole() {
        try {
            return mbeanServer.getAttribute(connectorName, "Role").toString();
        } catch (Exception e) {
            return "[ERROR: " + e.getMessage() + "]";
        }
    }

    private void logAttributeChange(String attributeName, int value) {
        session.getLog().onEvent("JMX: setting " + attributeName + " to " + value);
    }

    private void logInvocation(String operation) {
        session.getLog().onEvent("JMX: "+operation+" invoked");
    }

    public void postDeregister() {
    }

    public void postRegister(Boolean registrationDone) {
    }

    public void preDeregister() throws Exception {
    }

    public ObjectName preRegister(MBeanServer server, ObjectName name) throws Exception {
        mbeanServer = server;
        return name;
    }
}