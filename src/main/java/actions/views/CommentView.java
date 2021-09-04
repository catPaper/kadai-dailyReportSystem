package actions.views;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * コメント情報について画面の入力値・出力値を扱うViewモデル
 * @author ryouta.osada
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentView {

    /**
     * id
     */
    private Integer id;

    /**
     * コメントをつける日報
     */
    private ReportView report;

    /**
     * コメントをつけた従業員
     */
    private EmployeeView employee;

    /**
     * コメントの内容
     */
    private String content;

    /**
     * 登録日時
     */
    private LocalDateTime createdAt;

    /**
     * 更新日時
     */
    private LocalDateTime updatedAt;

    /**
     * 削除されたコメントかどうか(0:false,1:true)
     */
    private Integer deleteFlag;
}
