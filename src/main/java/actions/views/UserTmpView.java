package actions.views;

import java.sql.Time;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ユーザーテンプ情報について画面の入力値・出力値を扱うViewモデル
 * @author ryouta.osada
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserTmpView {

    /**
     * id
     */
    private Integer id;

    /**
     * ユーザーテンプを作成した従業員
     */
    private EmployeeView employee;

    /**
     * 出勤時刻
     */
    private Time punchIn;
}
