/*******************************************************************************
 * Copyrights Inov8
 ******************************************************************************/
package com.dfs.switchgateway.dto.enums;

public enum MessageTypeEnum {
    //TRANSACTION MESSAGES
    MT_0200("0200"),
    MT_0210("0210"),
    //TRANSACTION ADVICE MESSAGE
    MT_0220("0220"),
    MT_0230("0230"),
    //FILE UPLOAD MESSAGES
    MT_0302("0302"),
    MT_0312("0312"),
    //REVERSAL ADVICE MESSAGES
    MT_0420("0420"),
    MT_0430("0430"),
    //NETWORK MESSAGES
    MT_0800("0800"),
    MT_0810("0810");

    MessageTypeEnum( String value ) {
        this.value = value;
    }

    private String value;

    public String getValue() {
        return value;
    }
}
