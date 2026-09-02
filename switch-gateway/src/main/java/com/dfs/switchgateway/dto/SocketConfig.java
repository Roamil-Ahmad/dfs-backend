package com.dfs.switchgateway.dto;

import java.math.BigDecimal;

public class SocketConfig {
    private BigDecimal switchClientId;
    private String socketIp;
    private String socketPort;

    public BigDecimal getSwitchClientId() {
        return switchClientId;
    }

    public void setSwitchClientId(BigDecimal switchClientId) {
        this.switchClientId = switchClientId;
    }

    public String getSocketIp() {
        return socketIp;
    }

    public void setSocketIp(String socketIp) {
        this.socketIp = socketIp;
    }

    public String getSocketPort() {
        return socketPort;
    }

    public void setSocketPort(String socketPort) {
        this.socketPort = socketPort;
    }
}
