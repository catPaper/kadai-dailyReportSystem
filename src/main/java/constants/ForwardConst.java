package constants;

/**
 * リクエストパラメータの変数名、変数値、jspファイルの名前等画面遷移に関わる値を定義するenumクラス
 * @author ryouta.osada
 *
 */
public enum ForwardConst {

    //action
    ACT("action"),
    ACT_TOP("Top"),
    ACT_EMP("Employee"),
    ACT_REP("Report"),
    ACT_AUTH("Auth"),
    ACT_CMT("Comment"),

    //command
    CMD("command"),
    CMD_NONE(""),
    CMD_INDEX("index"),
    CMD_SHOW("show"),
    CMD_SHOW_LOGIN("showLogin"),
    CMD_LOGIN("login"),
    CMD_LOGOUT("logout"),
    CMD_NEW("entryNew"),
    CMD_CREATE("create"),
    CMD_EDIT("edit"),
    CMD_UPDATE("update"),
    CMD_DESTROY("destroy"),
    CMD_PUNCH_IN("punchIn"),
    CMD_PUNCH_IN_CANCEL("punchIn_cancel"),

    //jsp
    FW_ERR_UNKNOWN("error/unknown"),
    FW_TOP_INDEX("topPage/index"),
    FW_LOGIN("login/login"),
    FW_EMP_INDEX("employees/index"),
    FW_EMP_SHOW("employees/show"),
    FW_EMP_NEW("employees/new"),
    FW_EMP_EDIT("employees/edit"),
    FW_REP_INDEX("reports/index"),
    FW_REP_SHOW("reports/show"),
    FW_REP_NEW("reports/new"),
    FW_REP_EDIT("reports/edit"),
    FW_CMT_INDEX("comments/index"),
    FW_CMT_SHOW("comments/show"),
    FW_CMT_NEW("comments/new"),
    FW_CMT_EDIT("comments/edit");


    /**
     * 文字列
     */
    private final String text;

    /**
     * コンストラクタ
     */
    private ForwardConst(final String text) {
        this.text = text;
    }

    /**
     * 値（文字列）取得
     * @return
     */
    public String getValue() {
        return this.text;
    }

    /**
     * 値（文字列）から、該当する定数を返却する
     * （例："Report"→ForwardConst.ACT_REP)
     * @param key 値（文字列）
     * @return ForwardConst型定数
     */
    public static ForwardConst get(String key) {
        for(ForwardConst c : values()) {
            if(c.getValue().equals(key)){
                return c;
            }
        }
        return CMD_NONE;
    }
}
