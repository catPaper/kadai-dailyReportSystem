package models;

import java.sql.Time;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import constants.JpaConst;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 従業員ごとの一時情報を格納しておくDTOモデル
 * @author ryouta.osada
 *
 */
@Table(name = JpaConst.TABLE_TMP)
@NamedQueries({
    @NamedQuery(
            name = JpaConst.Q_TMP_GET_MINE,
            query = JpaConst.Q_TMP_GET_MINE_DEF),
    @NamedQuery(
            name = JpaConst.Q_TMP_COUNT_ALL_MINE,
            query = JpaConst.Q_TMP_COUNT_ALL_MINE_DEF)
})
@Getter //全てのクラスフィールドについてgetterを自動生成する(Lombok)
@Setter //全てのクラスフィールドについてsetterを自動生成する(Lombok)
@NoArgsConstructor //引数なしのコンストラクタを自動生成する(Lombok)
@AllArgsConstructor
@Entity
public class UserTmp {

    /**
     * id
     */
    @Id
    @Column(name = JpaConst.TMP_COL_ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 登録した従業員
     */
    @OneToOne
    @JoinColumn(name= JpaConst.TMP_COL_EMP,nullable = false,unique = true)
    private Employee employee;

    /*
     * 出勤時刻
     */
    @Column(name = JpaConst.TMP_COL_PUNCH_IN,nullable = true)
    private Time punchIn;
}
