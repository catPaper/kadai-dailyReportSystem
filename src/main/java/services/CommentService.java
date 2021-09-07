package services;

import java.time.LocalDateTime;
import java.util.List;

import actions.views.CommentConverter;
import actions.views.CommentView;
import actions.views.ReportConverter;
import actions.views.ReportView;
import constants.JpaConst;
import models.Comment;
import models.validators.CommentValidator;

/**
 * コメントテーブルの操作に関わる処理を行うクラス
 * @author ryouta.osada
 *
 */
public class CommentService extends ServiceBase {

    /**
     * 指定した日報についたコメントデータを、指定されたページ数の一覧画面に表示する分取得しCommentViewのリストで返却する
     * @param rv 日報
     * @param page ページ数
     * @return 一覧画面に表示するデータのリスト
     */
    public List<CommentView> getAllByReportPerPage(ReportView report,int page){
        List<Comment> comments = em.createNamedQuery(JpaConst.Q_CMT_GET_ALL_BY_REPORT,Comment.class)
                .setParameter(JpaConst.JPQL_PARM_REPORT, ReportConverter.toModel(report))
                .setFirstResult(JpaConst.ROW_PER_PAGE * (page - 1))
                .setMaxResults(JpaConst.ROW_PER_PAGE)
                .getResultList();
        return CommentConverter.toViewList(comments);
    }

    /**
     * 指定した日報についたコメントデータの件数を取得する
     * @param report
     * @return コメントデータの件数
     */
    public long countAllByReport(ReportView report) {
        long count = (long)em.createNamedQuery(JpaConst.Q_CMT_COUNT_ALL_BY_REPORT,Long.class)
                .setParameter(JpaConst.JPQL_PARM_REPORT, ReportConverter.toModel(report))
                .getSingleResult();

        return count;

    }

    /**
     * 画面から入力されてコメントの登録内容を元にデータを1件作成し、コメントテーブルに登録する
     * @param cv コメントの登録内容
     * @return バリデーションで発生したエラーのリスト
     */
    public List<String> create(CommentView cv){

        //バリデーションを行う
        List<String> errors = CommentValidator.validate(cv);
        if(errors.size() == 0) {
            LocalDateTime ldt = LocalDateTime.now();
            cv.setCreatedAt(ldt);
            cv.setUpdatedAt(ldt);

            createInternal(cv);
        }

        //バリデーションで発生しエラーを返却(エラーがなければ0件の空リスト)
        return errors;
    }

    /**
     * 画面から入力されたコメントの登録内容を元にコメントデータを更新する
     * @param cv コメントのインスタンス
     * @return バリデーションで発生したエラーのリスト
     */
    public List<String> update(CommentView cv){

        //バリデーションを行う
        List<String> errors = CommentValidator.validate(cv);
        if(errors.size() == 0) {
            //更新時刻を現在時刻に設定
            LocalDateTime ldt = LocalDateTime.now();
            cv.setUpdatedAt(ldt);

            updateInternal(cv);
        }

        //バリデーションで発生したエラーを返却(エラーがなければ0件の空リスト)
        return errors;
    }

    /**
     * idを条件にコメントデータを論理削除する
     * @param id
     */
    public void destroy(Integer id) {
        //idを条件に登録済みのコメント情報を取得する
        CommentView savedCmt = findOne(id);
        //更新日時に現在時刻を設定する
        LocalDateTime today = LocalDateTime.now();
        savedCmt.setUpdatedAt(today);

        //論理削除フラグをたてる
        savedCmt.setDeleteFlag(JpaConst.CMT_DEL_TRUE);

        update(savedCmt);
    }

    /**
     * idを条件に取得したデータをCommentViewのインスタンスで返却する
     * @param id
     * @return 取得したデータのインスタンス
     */
    public CommentView findOne(int id) {
        return CommentConverter.toView(findOneInternal(id));
    }

    /**
     * idを条件にデータを1件取得する
     * @param id
     * @return 取得データのインスタンス
     */
    private Comment findOneInternal(int id) {
        return em.find(Comment.class, id);
    }

    /**
     * コメントデータを1件登録する
     * @param cv コメントデータ
     */
    private void createInternal(CommentView cv) {
        em.getTransaction().begin();
        em.persist(CommentConverter.toMedel(cv));
        em.getTransaction().commit();
    }

    /**
     * コメントデータを更新する
     * @param cv コメントデータ
     */
    private void updateInternal(CommentView cv) {
        em.getTransaction().begin();
        Comment c = findOneInternal(cv.getId());
        CommentConverter.copyViewToModel(c, cv);
        em.getTransaction().commit();
    }
}
