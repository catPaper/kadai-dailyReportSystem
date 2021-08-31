package constants;

/**
 * デフォルト値などの設定値を管理する
 * @author ryouta.osada
 *
 */
public enum ParameterConst {

    //日報管理
    //デフォルト値
    DEF_REP_PUNCH_IN("09:00:00"),
    DEF_REP_PUNCH_OUT("18:00:00"),
    //入力時刻の刻み値（秒）
    STEP_REP_PUNCH_IN("600"),
    STEP_REP_PUNCH_OUT("600");

    private final String text;

    private ParameterConst(final String text) {
        this.text = text;
    }

    public String getValue() {
        return this.text;
    }
}
