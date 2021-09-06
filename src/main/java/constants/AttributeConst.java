package constants;

/**
 * 画面の項目値等を定義するenumクラス
 * @author ryouta.osada
 *
 */
public enum AttributeConst {

    //フラッシュメッセージ
    FLUSH("flush"),

    //一覧画面共通
    MAX_ROW("maxRow"),
    PAGE("page"),

    //入力フォーム共通
    TOKEN("_token"),
    ERR("errors"),

    //ログイン中の従業員
    LOGIN_EMP("login_employee"),

    //ログイン画面
    LOGIN_ERR("loginError"),

    //従業員管理
    EMPLOYEE("employee"),
    EMPLOYEES("employees"),
    EMP_COUNT("employees_count"),
    EMP_ID("e_id"),
    EMP_CODE("code"),
    EMP_PASS("password"),
    EMP_NAME("name"),
    EMP_ADMIN_FLG("admin_flag"),

    //管理者フラグ
    ROLE_ADMIN(1),
    ROLE_GENERAL(0),

    //削除フラグ
    DEL_FLAG_TRUE(1),
    DEL_FLAG_FALSE(0),


    //日報管理
    REPORT("report"),
    REPORTS("reports"),
    REP_COUNT("reports_count"),
    REP_ID("r_id"),
    REP_DATE("report_date"),
    REP_TITLE("title"),
    REP_CONTENT("content"),
    REP_PUNCH_IN("punch_in"),
    REP_PUNCH_OUT("punch_out"),

    //ユーザーテンプ管理
    TPM("uerTmp"),
    TMPS("userTmps"),
    TMP_ID("t_id"),
    TMP_PUNCH_IN("t_punch_in"),

    //コメント管理
    COMMENT("comment"),
    COMMENTS("comments"),
    CMT_COUNT("comment_count"),
    CMT_REPORT("comment_report"),
    CMT_ID("c_id"),
    CMT_CONTENT("content");


    private final String text;
    private final Integer i;

    private AttributeConst(final String text) {
        this.text = text;
        this.i = null;
    }

    private AttributeConst(final Integer i) {
        this.text = null;
        this.i = i;
    }

    public String getValue() {
        return this.text;
    }

    public Integer getIntegerValue() {
        return this.i;
    }
}
