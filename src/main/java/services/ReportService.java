package services;

import java.time.LocalDateTime;
import java.util.List;

import actions.views.EmployeeConverter;
import actions.views.EmployeeView;
import actions.views.ReportConverter;
import actions.views.ReportView;
import constants.AttributeConst;
import constants.JpaConst;
import models.Report;
import models.validators.ReportValidator;

/**
 * 日報テーブルの操作に関わる処理を行うクラス
 * @author ryouta.osada
 *
 */
public class ReportService extends ServiceBase {

    /**
     * 指定した従業員が作成した日報データを、指定されたページ数の一覧画面に表示する分取得しReportViewのリストで返却する
     * @param employee 従業員
     * @param page ページ数
     * @return 一覧画面に表示するデータのリスト
     */
    public List<ReportView> getMinePerPage(EmployeeView employee,int page){
        List<Report> reports = em.createNamedQuery(JpaConst.Q_REP_GET_ALL_MINE,Report.class)
                .setParameter(JpaConst.JPQL_PARM_EMPLOYEE, EmployeeConverter.toModel(employee))
                .setFirstResult(JpaConst.ROW_PER_PAGE * (page - 1))
                .setMaxResults(JpaConst.ROW_PER_PAGE)
                .getResultList();
        return ReportConverter.toViewList(reports);
    }

    /**
     * 指定した従業員が作成した日報データ（未読のみ）を、指定されたページ数の一覧画面に表示する分取得しReportViewのリストで返却する
     * @param employee 従業員
     * @param page ページ数
     * @return 一覧画面に表示するデータのリスト
     */
    public List<ReportView> getMineUnReadPerPage(EmployeeView employee,int page){
        List<Report> reports = em.createNamedQuery(JpaConst.Q_REP_GET_ALL_MINE_UNREAD,Report.class)
                .setParameter(JpaConst.JPQL_PARM_EMPLOYEE, EmployeeConverter.toModel(employee))
                .setFirstResult(JpaConst.ROW_PER_PAGE * (page - 1))
                .setMaxResults(JpaConst.ROW_PER_PAGE)
                .getResultList();
        return ReportConverter.toViewList(reports);
    }

    /**
     * 指定した従業員が作成した日報データの件数を取得し、返却する
     * @param employee
     * @return 日報データの件数
     */
    public long countAllMine(EmployeeView employee) {

        long count = (long) em.createNamedQuery(JpaConst.Q_REP_COUNT_ALL_MINE,Long.class)
                .setParameter(JpaConst.JPQL_PARM_EMPLOYEE, EmployeeConverter.toModel(employee))
                .getSingleResult();

        return count;
    }

    /**
     * 指定した従業員が作成した日報データ(未読のみ)の件数を取得し、返却する
     * @param employee
     * @return 日報データ(未読のみ)の件数
     */
    public long countAllMineUnRead(EmployeeView employee) {

        long count = (long)em.createNamedQuery(JpaConst.Q_REP_COUNT_ALL_MINE_UNREAD,Long.class)
                .setParameter(JpaConst.JPQL_PARM_EMPLOYEE, EmployeeConverter.toModel(employee))
                .getSingleResult();

        return count;
    }

    /**
     * 指定されたページ数の一覧画面に表示する日報データを取得し、ReportViewのリストで返却する
     * @param page ページ数
     * @return 一覧画面に表示するデータのリスト
     */
    public List<ReportView> getAllPerPage(int page){
        List<Report> reports = em.createNamedQuery(JpaConst.Q_REP_GET_ALL,Report.class)
                .setFirstResult(JpaConst.ROW_PER_PAGE * (page -1))
                .setMaxResults(JpaConst.ROW_PER_PAGE)
                .getResultList();
        return ReportConverter.toViewList(reports);
    }

    /**
     * 日報テーブルのデータの件数を取得し、返却する
     * @return データの件数
     */
    public long countAll() {
        long reports_count = (long) em.createNamedQuery(JpaConst.Q_REP_COUNT,Long.class)
                .getSingleResult();
        return reports_count;
    }

    /**
     * idを条件に取得したデータをReportViewのインスタンスで返却する
     * @param id
     * @return 取得データのインスタンス
     */
    public ReportView findOne(int id) {
        return ReportConverter.toView(findOneInternal(id));
    }

    /**
     * 画面から入力された日報の登録内容を元にデータを1件作成し、日報テーブルに登録する
     * @param rv 日報の登録内容
     * @return バリデーションで発生したエラーのリスト
     */
    public List<String> create(ReportView rv){

        //バリデーションを行う
        List<String> errors = ReportValidator.validate(rv);
        if(errors.size() == 0) {
            LocalDateTime ldt = LocalDateTime.now();
            rv.setCreatedAt(ldt);
            rv.setUpdatedAt(ldt);
            createInternal(rv);
        }

        //バリデーションで発生しエラーを返却(エラーがなければ0件の空リスト)
        return errors;
    }

    /**
     * 画面から入力された日報の登録内容を元に日報データを更新する
     * @param rv 日報の更新内容
     * @return バリデーションで発生したエラーのリスト
     */
    public List<String> update(ReportView rv){

        //バリデーションを行う
        List<String> errors = ReportValidator.validate(rv);
        if(errors.size() == 0) {
            //更新日時を現在時刻に設定
            LocalDateTime ldt = LocalDateTime.now();
            rv.setUpdatedAt(ldt);

            updateInternal(rv);
        }

        //バリデーションで発生したエラーを返却(エラーがなければ0件の空リスト)
        return errors;
    }

    /**
     * 日報についたコメント数を１増やす
     */
    public void addCommentCount(ReportView rv) {
        rv.setCommentCount(rv.getCommentCount() + 1);
        updateInternal(rv);
    }

    /**
     * 日報についたコメント数を1減らす
     */
    public void subtractCommentCount(ReportView rv) {
        rv.setCommentCount(rv.getCommentCount() - 1);
        updateInternal(rv);
    }

    /**
     * 日報の「いいね」数を１増やす
     * @param rv
     */
    public void addGood(ReportView rv) {
        rv.setGoodCount(rv.getGoodCount() + 1);
        updateInternal(rv);
    }

    /**
     * 日報についた「いいね」数を１減らす
     * @param rv
     */
    public void subtractGood(ReportView rv) {
        rv.setGoodCount(rv.getGoodCount() - 1);
        updateInternal(rv);
    }

    /**
     * コメントが閲覧済みかどうか
     * @param rv
     * @return true:閲覧済み false:未読
     */
    public boolean isReadComment(ReportView rv) {
        boolean isRead = (rv.getIsReadComment() == AttributeConst.READ_FLAG_TRUE.getIntegerValue());
        return isRead;
    }

    /**
     * コメントを閲覧済みにする
     * @param rv
     */
    public void setReadComment(ReportView rv) {
        rv.setIsReadComment(AttributeConst.READ_FLAG_TRUE.getIntegerValue());
        updateInternal(rv);
    }

    /**
     * コメントを未読にする
     * @param rv
     */
    public void setUnReadComment(ReportView rv) {
        rv.setIsReadComment(AttributeConst.READ_FLAG_FALSE.getIntegerValue());
        updateInternal(rv);
    }

    /**
     * idを条件にデータを1件取得する
     * @param id
     * @return 取得データのインスタンス
     */
    private Report findOneInternal(int id) {
        return em.find(Report.class, id);
    }

    /**
     * 日報データを1件登録する
     * @param rv 日報データ
     */
    private void createInternal(ReportView rv) {
        em.getTransaction().begin();
        em.persist(ReportConverter.toModel(rv));
        em.getTransaction().commit();
    }

    /**
     * 日報データを更新する
     * @param rv 日報データ
     */
    private void updateInternal(ReportView rv) {

        em.getTransaction().begin();
        Report r = findOneInternal(rv.getId());
        ReportConverter.copyViewToModel(r, rv);
        em.getTransaction().commit();
    }
}
