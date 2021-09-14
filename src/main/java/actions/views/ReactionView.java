package actions.views;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import models.Employee;
import models.Report;

/**
 * リアクション情報について画面の入力値・出力地を扱うViewモデル
 * @author ryouta.osada
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReactionView {

    /**
     * id
     */
    private Integer id;

    /**
     * 「いいね」をつけた日報
     */
    private Report report;

    /**
     * 「いいね」をつけた従業員
     */
    private Employee employee;

    /**
     * 日報に「いいね」をしたか
     */
    private Integer reactFlag;

    /**
     * リアクションした日時
     */
    private LocalDateTime reactedAt;
}
