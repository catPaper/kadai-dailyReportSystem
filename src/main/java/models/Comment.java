package models;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import constants.JpaConst;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * コメント機能管理のDTOモデル
 * @author ryouta.osada
 *
 */
@Table(name = JpaConst.TABLE_CMT)
@NamedQueries({
    @NamedQuery(
            name = JpaConst.Q_CMT_GET_ALL_BY_REPORT,
            query = JpaConst.Q_CMT_GET_ALL_BY_REPORT_DEF),
    @NamedQuery(
            name = JpaConst.Q_CMT_COUNT_ALL_BY_REPORT,
            query = JpaConst.Q_CMT_COUNT_ALL_BY_REPORT_DEF),
    @NamedQuery(
            name = JpaConst.Q_CMT_COUNT_NODELETE_BY_REPORT,
            query = JpaConst.Q_CMT_COUNT_NODELETE_BY_REPORT_DEF)
})
@Getter //全てのクラスフィールドについてgetterを自動生成する(Lombok)
@Setter //全てのクラスフィールドについてsetterを自動生成する(Lombok)
@NoArgsConstructor //引数なしのコンストラクタを自動生成する(Lombok)
@AllArgsConstructor
@Entity
public class Comment {

    /**
     * id
     */
    @Id
    @Column(name=JpaConst.CMT_COL_ID)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    /**
     * コメントをつける日報
     */
    @ManyToOne
    @JoinColumn(name=JpaConst.CMT_COL_REP,nullable = false)
    private Report report;

    /**
     * コメントをつけた従業員
     */
    @ManyToOne
    @JoinColumn(name=JpaConst.CMT_COL_EMP,nullable = false)
    private Employee employee;

    /**
     * コメントの内容
     */
    @Lob
    @Column(name=JpaConst.CMT_COL_CONTENT,nullable = false)
    private String content;

    /**
     * 登録日時
     */
    @Column(name=JpaConst.CMT_COL_CREATED_AT,nullable = false)
    private LocalDateTime createdAt;

    /**
     * 更新日時
     */
    @Column(name=JpaConst.CMT_COL_UPDATED_AT,nullable = false)
    private LocalDateTime updatedAt;

    /**
     * 削除されたコメントかどうか(0:false,1:true)
     */
    @Column(name=JpaConst.CMT_COL_DELETE_FLAG,nullable = false)
    private Integer deleteFlag;

}
