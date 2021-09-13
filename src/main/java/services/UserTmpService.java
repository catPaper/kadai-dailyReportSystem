package services;

import java.sql.Time;

import actions.views.EmployeeConverter;
import actions.views.EmployeeView;
import actions.views.UserTmpConverter;
import actions.views.UserTmpView;
import constants.JpaConst;
import models.UserTmp;

/**
 * ユーザーテンプテーブルの操作に関わる処理を行うクラス
 * @author ryouta.osada
 *
 */
public class UserTmpService extends ServiceBase {



    /**
     * 従業員用の一時データを作成する(既にデータがある場合は作成しない）
     * @param ev 従業員データ
     */
    public void create(EmployeeView ev) {
        if(countAllMine(ev) == 0) {
            UserTmp tmp = new UserTmp();
            tmp.setEmployee(EmployeeConverter.toModel(ev));
            createInternal(tmp);
        }
    }

    /**
     * 出勤時刻を取得する
     * @param ev
     * @return
     */
    public Time getPuncIn(EmployeeView ev) {
        if(countAllMine(ev) != 0) {
            UserTmpView tmp = findViewByEmployee(ev);
            return tmp.getPunchIn();
        }
        return null;
    }

    /**
     * 出勤時刻を設定する
     * @param ev 従業員データのインスタンス
     * @param punchIn 出勤時刻
     */
    public void setPunchIn(EmployeeView ev,Time punchIn) {
        UserTmp tmp = (UserTmp) findByEmployeeInternal(ev);
        em.getTransaction().begin();
        tmp.setPunchIn(punchIn);
        em.getTransaction().commit();
    }
    /**
     * 指定した従業員が作成したユーザーテンプデータの件数を取得し、返却する
     * @param employee 従業員データ
     * @return データの件数
     */
    public long countAllMine(EmployeeView employee) {
        long count = (long)em.createNamedQuery(JpaConst.Q_TMP_COUNT_ALL_MINE,Long.class)
                .setParameter(JpaConst.JPQL_PARM_EMPLOYEE,EmployeeConverter.toModel(employee))
                .getSingleResult();
        return count;
    }

    /**
     * 指定した従業員のビュー用ユーザーテンプを取得する
     * @param employee 従業員データ
     * @return 取得したデータのインスタンス
     */
    private UserTmpView findViewByEmployee(EmployeeView employee) {

        return UserTmpConverter.toView(findByEmployeeInternal(employee));
    }
    /**
     * 従業員データをもとにデータを取得する
     * @param employee 従業員データ
     * @return ユーザーテンプデータのインスタンス
     */
    private UserTmp findByEmployeeInternal(EmployeeView employee) {
        UserTmp tmp = (UserTmp) em.createNamedQuery(JpaConst.Q_TMP_GET_MINE,UserTmp.class)
                .setParameter(JpaConst.JPQL_PARM_EMPLOYEE,EmployeeConverter.toModel(employee))
                .getSingleResult();
        return tmp;
    }

    /**
     * 従業員用の一時データを作成する
     * @param tmp ユーザーテンプデータ
     */
    private void createInternal(UserTmp tmp) {
        em.getTransaction().begin();
        em.persist(tmp);
        em.getTransaction().commit();
    }
}
