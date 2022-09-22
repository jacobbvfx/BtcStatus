package net.boybacks.btcstatus;

public enum VariablesManager {
    //https://colorhunt.co/palette/bedbbb8db59692817a707070
    LIGHT_GREEN("#BEDBBB"),
    DARK_GREEN("#8DB596"),
    BROWN("#92817A"),
    LIGHT_GREY("#707070"),
    MAINTITLETEXT("        BTCStatus v2   "),
    EXITBUTTONTEXT("X"),
    REFRESHBUTTONTEXT("Refresh"),
    BTCPRRICECOINSTEXT("[0] - [1]"),

    TEST("");

    private String value;

    private VariablesManager(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
