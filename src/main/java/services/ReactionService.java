package services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;

import actions.views.EmployeeConverter;
import actions.views.EmployeeView;
import actions.views.ReactionConverter;
import actions.views.ReactionView;
import actions.views.ReportConverter;
import actions.views.ReportView;
import constants.AttributeConst;
import constants.JpaConst;
import models.Reaction;

/**
 * リアクションテーブルの操作に関わる処理を行うクラス
 * @author ryouta.osada
 *
 */
public class ReactionService extends ServiceBase {

    /**
     * 日報のリアクションフラグをtrueにする
     * @param rv 日報データ
     * @param ev 従業員データ
     * @return 処理が完了した場合true
     */
    public boolean doReact(ReportView rv,EmployeeView ev) {
        try {
            Reaction r = em.createNamedQuery(JpaConst.Q_RCT_GET_MINE_REACT_TO_REPORT,Reaction.class)
                .setParameter(JpaConst.JPQL_PARM_REPORT, ReportConverter.toModel(rv))
                .setParameter(JpaConst.JPQL_PARM_EMPLOYEE, EmployeeConverter.toModel(ev))
                .getSingleResult();
            em.getTransaction().begin();
            r.setReactFlag(JpaConst.RCT_REP_TRUE);
            r.setReactedAt(LocalDateTime.now());
            em.getTransaction().commit();

            return true;
        }catch(NoResultException e) {
            e.printStackTrace();

            return false;
        }
    }

    /**
     * 日報のリアクションフラグをfalseにする
     * @param rv 日報データ
     * @param ev 従業員データ
     * @return 処理が完了した場合true
     */
    public boolean cancelReact(ReportView rv,EmployeeView ev) {
        try {
            Reaction r = em.createNamedQuery(JpaConst.Q_RCT_GET_MINE_REACT_TO_REPORT,Reaction.class)
                .setParameter(JpaConst.JPQL_PARM_REPORT, ReportConverter.toModel(rv))
                .setParameter(JpaConst.JPQL_PARM_EMPLOYEE, EmployeeConverter.toModel(ev))
                .getSingleResult();
            em.getTransaction().begin();
            r.setReactFlag(JpaConst.RCT_REP_FALSE);
            r.setReactedAt(LocalDateTime.now());
            em.getTransaction().commit();

            return true;
        }catch(NoResultException e) {
            e.printStackTrace();

            return false;
        }
    }

    /**
     * 指定した日報についたリアクションの件数を取得し、返却する
     * @param rv ReportView
     * @return 「いいね」の件数
     */
    public long countAllReactToReport(ReportView rv) {

        long count = (long)em.createNamedQuery(JpaConst.Q_RCT_ALL_REACT_COUNT_TO_REPORT,Long.class)
                .setParameter(JpaConst.JPQL_PARM_REPORT, ReportConverter.toModel(rv))
                .getSingleResult();

        return count;
    }

    /**
     * 日報のリストをもとにリアクション数のリストを作成し、返却する
     * @param reportViewList 日報のリスト
     * @return リアクション数のリスト
     */
    public List<Long> getAllCountReactToReport(List<ReportView> reportViewList){
        List<Long> reactCount = new ArrayList<Long>();
        for(ReportView rv : reportViewList){
            reactCount.add(countAllReactToReport(rv));
        }
        return reactCount;


    }

    /**
     * 日報に紐づく自分のリアクションデータの件数を取得する
     * @param rv 日報データ
     * @param ev 従業員データ
     * @return 自分のリアクションデータの件数
     */
    public long countCreatedMineReactDataToReport(ReportView rv,EmployeeView ev) {

        long count = (long)em.createNamedQuery(JpaConst.Q_RCT_COUNT_CREATED_MINE_REACT_DATA_TO_REPORT,Long.class)
                .setParameter(JpaConst.JPQL_PARM_REPORT, ReportConverter.toModel(rv))
                .setParameter(JpaConst.JPQL_PARM_EMPLOYEE, EmployeeConverter.toModel(ev))
                .getSingleResult();

        return count;
    }

    /**
     * 日報に従業員がリアクションした件数を取得する
     * @param rv 日報データ
     * @param ev 従業員データ
     * @return 自分のリアクションの件数
     */
    public long countMineReactToReport(ReportView rv,EmployeeView ev) {

        long count = (long)em.createNamedQuery(JpaConst.Q_RCT_COUNT_MINE_REACT_TO_REPORT,Long.class)
                .setParameter(JpaConst.JPQL_PARM_REPORT, ReportConverter.toModel(rv))
                .setParameter(JpaConst.JPQL_PARM_EMPLOYEE, EmployeeConverter.toModel(ev))
                .getSingleResult();

        return count;
    }

    /**
     * 従業員用の日報リアクションテーブルを作成する(既にある場合は作成しない)
     * @param rv 日報データ
     * @param ev 従業員データ
     */
    public void create(ReportView rv,EmployeeView ev) {
        if(countCreatedMineReactDataToReport(rv,ev) == 0) {
            ReactionView reactionView = new ReactionView(
                    null,
                    ReportConverter.toModel(rv),
                    EmployeeConverter.toModel(ev),
                    AttributeConst.REACT_FLAG_FALSE.getIntegerValue(),
                    LocalDateTime.now());
            createInternal(ReactionConverter.toModel(reactionView));
        }
    }

    /**
     * リアクションデータを作成する
     * @param rv リアクションデータ
     */
    private void createInternal(Reaction r) {
        em.getTransaction().begin();
        em.persist(r);
        em.getTransaction().commit();
    }

}
