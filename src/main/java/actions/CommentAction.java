package actions;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import actions.views.CommentView;
import actions.views.ReportView;
import constants.AttributeConst;
import constants.ForwardConst;
import constants.JpaConst;
import services.CommentService;

public class CommentAction extends ActionBase {

    private CommentService commentService;

    /**
     * メソッドを実行する
     */
    @Override
    public void process() throws ServletException, IOException {

        commentService = new CommentService();

        invoke();

        commentService.close();
    }
    /**
     * 一覧画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void index() throws ServletException,IOException{

        ReportView report = (ReportView)getSessionScope(AttributeConst.CMT_REPORT);
        CommentView comment = new CommentView();
        //指定されたページ数の一覧画面に表示するコメントデータを取得
        int page = getPage();
        List<CommentView> comments = commentService.getAllByReportPerPage(report, page);
        //全コメントデータを取得
        long commentCount = commentService.countAllByReport(report);

        putRequestScope(AttributeConst.TOKEN,getTokenId());             //CSRF対策用トークン
        putRequestScope(AttributeConst.COMMENTS,comments);              //取得したコメントデータ
        putRequestScope(AttributeConst.CMT_COUNT,commentCount);         //全てのコメントデータの件数
        putRequestScope(AttributeConst.PAGE,page);                      //ページ数
        putRequestScope(AttributeConst.MAX_ROW,JpaConst.ROW_PER_PAGE);  //1ページに表示するレコードの数
        putRequestScope(AttributeConst.COMMENT,comment);                //空のコメントインスタンス

        //一覧画面を表示
        forward(ForwardConst.FW_CMT_INDEX);
    }

}
