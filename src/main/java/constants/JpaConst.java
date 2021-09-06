package constants;

/**
 * DB関連の項目値を定義するインターフェース
 * ※インターフェースに定義した変数は public static final 修飾子がついているとみなされる
 * @author ryouta.osada
 *
 */
public interface JpaConst {

    //persistence-unit名
    String PERSISTENCE_UNIT_NAME = "daily_report_system";

    //データ取得件数の最大値
    int ROW_PER_PAGE = 15;  //1ページに表示するレコード数

    //従業員テーブル
    String TABLE_EMP = "employees"; //テーブル名
    //従業員テーブルカラム
    String EMP_COL_ID = "id";                   //id
    String EMP_COL_CODE = "code";               //社員番号
    String EMP_COL_NAME = "name";               //氏名
    String EMP_COL_PASSWORD = "password";       //パスワード
    String EMP_COL_ADMIN_FLAG = "admin_flag";   //管理者権限
    String EMP_COL_CREATED_AT = "created_at";   //登録日時
    String EMP_COL_UPDATED_AT = "updated_at";   //更新日時
    String EMP_COL_DELETE_FLAG = "delete_flag"; //削除フラグ

    int ROLE_ADMIN = 1;     //管理者権限ON(管理者)
    int ROLE_GENERAL = 0;   //管理者権限OFF(一般)
    int EMP_DEL_TRUE = 1;   //削除フラグON(削除済み)
    int EMP_DEL_FALSE = 0;  //削除フラグOFF(現役)
    int CMT_DEL_TRUE = 1;   //削除フラグON(削除済み)
    int CMT_DEL_FALSE = 0;  //削除フラグOFF

    //日報テーブル
    String TABLE_REP = "reports";   //テーブル名
    //日報テーブルカラム
    String REP_COL_ID = "id";                   //id
    String REP_COL_EMP = "employee_id";         //日報を作成した従業員のid
    String REP_COL_REP_DATE = "report_date";    //いつの日報か示す日付
    String REP_COL_TITLE = "title";             //日報のタイトル
    String REP_COL_CONTENT = "content";         //日報の内容
    String REP_COL_CREATED_AT = "created_at";   //登録日時
    String REP_COL_UPDATED_AT = "updated_at";   //更新日時
    String REP_COL_PUNCH_IN = "punch_in";       //出勤時刻
    String REP_COL_PUNCH_OUT = "punch_out";     //退勤時刻

    //ユーザー一時保存テーブル
    String TABLE_TMP = "userTmps";          //テーブル名
    //ユーザー一時保存テーブルカラム
    String TMP_COL_ID = "id";                    //id
    String TMP_COL_EMP = "employee_id";     //一時保存する従業員のid
    String TMP_COL_PUNCH_IN = "punch_in";  //出勤時刻

    //コメントテーブル
    String TABLE_CMT = "comments";   //テーブル名
    //コメントテーブルカラム
    String CMT_COL_ID = "id";                   //id
    String CMT_COL_REP = "report_id";           //コメントをつける日報のid
    String CMT_COL_EMP = "employee_id";         //コメントをつけた従業員のid
    String CMT_COL_CONTENT = "content";         //コメントの内容
    String CMT_COL_CREATED_AT = "created_at";   //登録日時
    String CMT_COL_UPDATED_AT = "updated_at";   //更新日時
    String CMT_COL_DELETE_FLAG = "delete_flg";  //削除フラグ

    //Entity名
    String ENTITY_EMP = "employee"; //従業員
    String ENTITY_REP = "report";   //日報
    String ENTITY_TMP = "userTmp";  //ユーザー一時保存
    String ENTITY_CMT = "comment";  //コメント

    //JPQLパラメータ
    String JPQL_PARM_CODE = "code";          //社員番号
    String JPQL_PARM_PASSWORD = "password"; //パスワード
    String JPQL_PARM_EMPLOYEE = "employee"; //従業員
    String JPQL_PARM_REPORT = "report";     //日報

    //NamedQueryのnameとquery
    //全ての従業員をidの降順に取得する
    String Q_EMP_GET_ALL = ENTITY_EMP + ".getAll";                                  //name
    String Q_EMP_GET_ALL_DEF = "SELECT e FROM Employee AS e ORDER BY e.id DESC";    //query
    //全ての従業員件数を取得する
    String Q_EMP_COUNT = ENTITY_EMP + ".count";
    String Q_EMP_COUNT_DEF = "SELECT COUNT(e) FROM Employee AS e";
    //社員番号とハッシュ化済パスワードを条件に未削除の従業員を取得する
    String Q_EMP_GET_BY_CODE_AND_PASS = ENTITY_EMP + "getByCodeAndPass";
    String Q_EMP_GET_BY_CODE_AND_PASS_DEF = "SELECT e FROM Employee AS e WHERE e.deleteFlag = 0 AND e.code = :" + JPQL_PARM_CODE + " AND e.password = :" + JPQL_PARM_PASSWORD;
    //指定した社員番号を保持する従業員の件数を取得する
    String Q_EMP_COUNT_RESISTERED_BY_CODE = ENTITY_EMP + ".countRegisteredByCode";
    String Q_EMP_COUNT_RESISTERED_BY_CODE_DEF = "SELECT COUNT(e) FROM Employee AS e WHERE e.code = :" + JPQL_PARM_CODE;
    //全ての日報をidの降順に取得する
    String Q_REP_GET_ALL = ENTITY_REP + ".getAll";
    String Q_REP_GET_ALL_DEF = "SELECT r FROM Report AS r ORDER BY r.id DESC";
    //全ての日報の件数を取得する
    String Q_REP_COUNT = ENTITY_REP + ".count";
    String Q_REP_COUNT_DEF = "SELECT COUNT(r) FROM Report AS r";
    //指定して従業員が作成した日報を全件idの降順で取得する
    String Q_REP_GET_ALL_MINE = ENTITY_REP + ".getAllMine";
    String Q_REP_GET_ALL_MINE_DEF = "SELECT r FROM Report AS r WHERE r.employee = :" + JPQL_PARM_EMPLOYEE + " ORDER BY r.id DESC";
    //指定した従業員が作成した日報の件数を取得する
    String Q_REP_COUNT_ALL_MINE = ENTITY_REP + ".countAllMine";
    String Q_REP_COUNT_ALL_MINE_DEF = "SELECT COUNT(r) FROM Report AS r WHERE r.employee = :" + JPQL_PARM_EMPLOYEE;
    //指定した従業員のユーザー一時情報を取得する
    String Q_TMP_GET_MINE = ENTITY_TMP + ".getMine";
    String Q_TMP_GET_MINE_DEF = "SELECT t FROM UserTmp AS t WHERE t.employee = :" + JPQL_PARM_EMPLOYEE;
    //指定した従業員が作成したユーザー一時情報の件数を取得する
    String Q_TMP_COUNT_ALL_MINE = ENTITY_TMP + ".countAllMine";
    String Q_TMP_COUNT_ALL_MINE_DEF = "SELECT COUNT(t) FROM UserTmp AS t WHERE t.employee = :" + JPQL_PARM_EMPLOYEE;
    //指定した日報についたコメントデータを全件作成日時の降順で取得する
    String Q_CMT_GET_ALL_BY_REPORT = ENTITY_CMT + ".getAllByReport";
    String Q_CMT_GET_ALL_BY_REPORT_DEF = "SELECT c FROM Comment AS c WHERE c.report = :" + JPQL_PARM_REPORT + " ORDER BY c.createdAt DESC";
    //指定した日報についたコメントデータの件数を取得す
    String Q_CMT_COUNT_ALL_BY_REPORT = ENTITY_CMT + ".countAllByReport";
    String Q_CMT_COUNT_ALL_BY_REPORT_DEF = "SELECT COUNT(c) FROM Comment As c WHERE c.report = :" + JPQL_PARM_REPORT;
}
