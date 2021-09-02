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
    STEP_REP_TIME(600);

    private final String text;
    private final Integer i;

    private ParameterConst(final String text) {
        this.text = text;
        this.i = null;
    }

    private ParameterConst(final Integer i) {
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
