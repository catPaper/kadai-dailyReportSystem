package models;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
 * 日報に対するリアクション管理のDTOモデル
 * @author ryouta.osada
 *
 */
@Table(name = JpaConst.TABLE_RCT)
@NamedQueries({
    @NamedQuery(
            name = JpaConst.Q_RCT_ALL_REACT_COUNT_TO_REPORT,
            query = JpaConst.Q_RCT_ALL_REACT_COUNT_TO_REPORT_DEF),
    @NamedQuery(
            name = JpaConst.Q_RCT_COUNT_CREATED_MINE_REACT_DATA_TO_REPORT,
            query = JpaConst.Q_RCT_COUNT_CREATED_MINE_REACT_DATA_TO_REPORT_DEF),
    @NamedQuery(
            name = JpaConst.Q_RCT_COUNT_MINE_REACT_TO_REPORT,
            query = JpaConst.Q_RCT_COUNT_MINE_REACT_TO_REPORT_DEF),
    @NamedQuery(
            name = JpaConst.Q_RCT_GET_MINE_REACT_TO_REPORT,
            query = JpaConst.Q_RCT_GET_MINE_REACT_TO_REPORT_DEF)
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Reaction {

    /**
     * id
     */
    @Id
    @Column(name = JpaConst.RCT_COL_ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 「いいね」をした日報
     */
    @ManyToOne
    @JoinColumn(name = JpaConst.RCT_COL_REP,nullable = false)
    private Report report;

    /**
     * 「いいね」をした従業員
     */
    @ManyToOne
    @JoinColumn(name = JpaConst.RCT_COL_EMP,nullable = false)
    private Employee employee;

    /**
     * 日報に「いいね」したか
     */
    @Column(name = JpaConst.RCT_COL_REACT_FLAG,nullable = false,columnDefinition = "integer default 0")
    private Integer reactFlag;

    /**
     * リアクションした日時
     */
    @Column(name = JpaConst.RCT_COL_REACTED_AT,nullable = false)
    private LocalDateTime reactedAt;
}
