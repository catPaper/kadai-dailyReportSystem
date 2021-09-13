package actions;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import actions.views.CommentView;
import actions.views.EmployeeView;
import actions.views.ReportView;
import constants.AttributeConst;
import constants.ForwardConst;
import constants.JpaConst;
import constants.MessageConst;
import services.CommentService;
import services.ReportService;

public class CommentAction extends ActionBase {

    private CommentService commentService;
    private ReportService reportService;

    /**
     * メソッドを実行する
     */
    @Override
    public void process() throws ServletException, IOException {

        commentService = new CommentService();
        reportService = new ReportService();

        invoke();

        commentService.close();
        reportService.close();
    }
    /**
     * 一覧画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void index() throws ServletException,IOException{

        ReportView rv = (ReportView)getSessionScope(AttributeConst.CMT_REPORT);
        //日報一覧から来た場合は、レポートidをもとにセッションスコープにレポートをセットする
        if(rv == null) {
            rv = reportService.findOne(toNumber(getRequestParam(AttributeConst.REP_ID)));
            //見つからない場合エラーページを表示
            if(rv == null) {
                forward(ForwardConst.FW_ERR_UNKNOWN);
                return;
            }else {
                putSessionScope(AttributeConst.CMT_REPORT,rv);
            }
        }

        CommentView cv = new CommentView();
        //指定されたページ数の一覧画面に表示するコメントデータを取得
        int page = getPage();
        List<CommentView> comments = commentService.getAllByReportPerPage(rv, page);
        //全コメントデータを取得
        long commentCount = commentService.countAllByReport(rv);
        long noDeleteCommentCount = commentService.countNoDeleteByReport(rv);

        //閲覧者が日報作成者で、日報がコメント未読状態の場合は閲覧済みに設定する
        EmployeeView ev = getSessionScope(AttributeConst.LOGIN_EMP);
        if(ev.getId() == rv.getEmployee().getId() && !reportService.isReadComment(rv)) {
            reportService.setReadComment(rv);
        }

        putRequestScope(AttributeConst.TOKEN,getTokenId());                                 //CSRF対策用トークン
        putRequestScope(AttributeConst.COMMENTS,comments);                                  //取得したコメントデータ
        putRequestScope(AttributeConst.REP_NODELETE_COMMENT_COUNT,noDeleteCommentCount);    //論理削除されていないコメント数
        putRequestScope(AttributeConst.REP_COMMENT_COUNT,commentCount);                     //全てのコメントデータの件数
        putRequestScope(AttributeConst.PAGE,page);                                          //ページ数
        putRequestScope(AttributeConst.MAX_ROW,JpaConst.ROW_PER_PAGE);                      //1ページに表示するレコードの数
        putRequestScope(AttributeConst.COMMENT,cv);                                         //空のコメントインスタンス

        //セッションにフラッシュメッセージが設定されている場合はリクエストスコープに移しかえ、セッションから削除する
        String flush = getSessionScope(AttributeConst.FLUSH);
        if(flush != null)
        {
            putRequestScope(AttributeConst.FLUSH,flush);
            removeSessionScope(AttributeConst.FLUSH);
        }

        //一覧画面を表示
        forward(ForwardConst.FW_CMT_INDEX);
    }

    /**
     * 新規登録を行う
     * @throws ServletException
     * @throws IOException
     */
    public void create() throws ServletException,IOException{

        //CSRF対策 tokenチェック
        if(checkToken()) {
            //セッションから情報を取得
            EmployeeView ev = (EmployeeView)getSessionScope(AttributeConst.LOGIN_EMP);
            ReportView rv = (ReportView)getSessionScope(AttributeConst.CMT_REPORT);

            //パラメータの値を元にコメント情報のインスタンスを作成する
            CommentView cv = new CommentView(
                    null,
                    rv,
                    ev,
                    getRequestParam(AttributeConst.CMT_CONTENT),
                    null,
                    null,
                    AttributeConst.DEL_FLAG_FALSE.getIntegerValue());

            //指定されたページ数の一覧画面に表示するコメントデータを取得
            int page = getPage();
            List<CommentView> comments = commentService.getAllByReportPerPage(rv, page);
            //全コメントデータを取得
            long commentCount = commentService.countAllByReport(rv);
            long noDeleteCommentCount = commentService.countNoDeleteByReport(rv);

            putRequestScope(AttributeConst.TOKEN,getTokenId());                                 //CSRF対策用トークン
            putRequestScope(AttributeConst.COMMENTS,comments);                                  //取得したコメントデータ
            putRequestScope(AttributeConst.REP_NODELETE_COMMENT_COUNT,noDeleteCommentCount);    //論理削除されていないコメント数
            putRequestScope(AttributeConst.REP_COMMENT_COUNT,commentCount);                     //全てのコメントデータの件数
            putRequestScope(AttributeConst.PAGE,page);                                          //ページ数
            putRequestScope(AttributeConst.MAX_ROW,JpaConst.ROW_PER_PAGE);                      //1ページに表示するレコードの数
            putRequestScope(AttributeConst.COMMENT,cv);                                         //入力されたコメント情報

            //コメント情報の登録
            List<String> errors = commentService.create(cv);
            if(errors.size() > 0) {
                //登録中にエラーがあった場合
                putRequestScope(AttributeConst.ERR,errors);                     //エラーのリスト

                //コメント一覧ページの再表示
                forward(ForwardConst.FW_CMT_INDEX);
            }else {
                //エラーが無かった場合

                //セッションに登録完了のフラッシュメッセージを設定
                putSessionScope(AttributeConst.FLUSH,MessageConst.I_REGISTERED.getMessage());
                //レポートのコメント数を１増やす
                reportService.addCommentCount(rv);
                //コメント主が日報作成者以外の場合は日報にコメント未読をつける
                if(cv.getEmployee().getId() != rv.getEmployee().getId()) {
                    reportService.setUnReadComment(rv);
                }

                //一覧画面にリダイレクト
                redirect(ForwardConst.ACT_CMT,ForwardConst.CMD_INDEX);
            }
        }
    }

    /**
     * 論理削除を行う
     * @throws ServletException
     * @throws IOException
     */
    public void destroy() throws ServletException,IOException{

        //CSRF対策 tokenチェック
        if(checkToken()){

            //idを条件にコメントデータを論理削除する
            commentService.destroy(toNumber(getRequestParam(AttributeConst.CMT_ID)));
            //日報についたコメント数を１減らす
            reportService.subtractCommentCount((ReportView)getSessionScope(AttributeConst.CMT_REPORT));

            //セッションに削除官僚のフラッシュメッセージを設定
            putSessionScope(AttributeConst.FLUSH,MessageConst.I_DELETED.getMessage());

            //一覧画面にリダイレクト
            redirect(ForwardConst.ACT_CMT,ForwardConst.CMD_INDEX);
        }
    }

    /**
     * 編集画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void edit() throws ServletException,IOException{

        //idを条件にコメントデータを取得する
        CommentView cv = commentService.findOne(toNumber(getRequestParam(AttributeConst.CMT_ID)));

        //セッションからログイン中の従業員情報を取得
        EmployeeView ev = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);

        if(cv == null || ev.getId() != cv.getEmployee().getId() || cv.getDeleteFlag() == AttributeConst.DEL_FLAG_TRUE.getIntegerValue()) {
            //該当のコメントデータが存在しない、または
            //ログインしている従業員が日報の作成者でない場合または
            //コメントが論理削除されている場合はエラー画面を表示
            forward(ForwardConst.FW_ERR_UNKNOWN);
        }else {

            putRequestScope(AttributeConst.TOKEN,getTokenId()); //CSRF対策用トークン
            putRequestScope(AttributeConst.COMMENT,cv);          //取得したコメントデータ

            //編集画面を表示
            forward(ForwardConst.FW_CMT_EDIT);
        }
    }

    /**
     * 更新を行う
     * @throws ServletException
     * @throws IOException
     */
    public void update() throws ServletException,IOException{
        //CSRF対策 tokenチェック
        if(checkToken()) {
            //idを条件にコメントデータを取得する
            CommentView cv = commentService.findOne(toNumber(getRequestParam(AttributeConst.CMT_ID)));

            //入力されたコメント内容を確定する
            cv.setContent(getRequestParam(AttributeConst.CMT_CONTENT));

            //コメントデータを更新する
            List<String> errors = commentService.update(cv);

            if(errors.size() > 0) {
                //更新中にエラーが発生した場合

                putRequestScope(AttributeConst.TOKEN,getTokenId()); //CSRF対策用トークン
                putRequestScope(AttributeConst.COMMENT,cv);         //入力されたコメントデータ
                putRequestScope(AttributeConst.ERR,errors);         //エラーのリスト
            }else {
                //更新中にエラーなかった場合

                //セッションに更新完了のフラッシュメッセージを設定
                putSessionScope(AttributeConst.FLUSH,MessageConst.I_UPDATED.getMessage());

                //一覧画面にリダイレクト
                redirect(ForwardConst.ACT_CMT,ForwardConst.CMD_INDEX);
            }
        }
    }

}
