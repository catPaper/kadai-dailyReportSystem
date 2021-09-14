package actions;

import java.io.IOException;
import java.sql.Time;
import java.time.LocalDate;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import actions.views.CommentView;
import actions.views.EmployeeView;
import actions.views.ReportView;
import constants.AttributeConst;
import constants.ForwardConst;
import constants.JpaConst;
import constants.MessageConst;
import constants.ParameterConst;
import services.CommentService;
import services.ReactionService;
import services.ReportService;
import services.UserTmpService;

/**
 * 日報に関する処理を行うActionクラス
 * @author ryouta.osada
 *
 */
public class ReportAction extends ActionBase {

    private ReportService reportService;
    private UserTmpService tmpService;
    private CommentService commentService;
    private ReactionService reactionService;

    /**
     * メソッドを実行する
     */
    @Override
    public void process() throws ServletException, IOException {

        reportService = new ReportService();
        tmpService = new UserTmpService();
        commentService = new CommentService();
        reactionService = new ReactionService();

        invoke();

        reportService.close();
        tmpService.close();
        commentService.close();
        reactionService.close();
    }

    /**
     * 一覧画面を表示する
     * @throws Servletexception
     * @throws IOException
     */
    public void index() throws ServletException,IOException{

        //セッションからログイン中の従業員情報を取得
        EmployeeView loginEmployee = (EmployeeView)getSessionScope(AttributeConst.LOGIN_EMP);

        //未読メッセージのみを表示するかどうかのパラメータ値を受け取り
        //nullまたは未読メッセージがない場合はデフォルトで全件表示するよう設定する
        Integer exist_unread = (reportService.countAllMineUnRead(loginEmployee) > 0)
                                ? AttributeConst.EXIST_FLAG_TRUE.getIntegerValue()
                                : AttributeConst.EXIST_FLAG_FALSE.getIntegerValue();
        Integer isShow;
        if(getRequestParam(AttributeConst.REP_SHOW_UNREAD) == null
                || exist_unread == AttributeConst.EXIST_FLAG_FALSE.getIntegerValue()) {
            isShow = AttributeConst.SHOW_FLAG_FALSE.getIntegerValue();
        }else {
            isShow = toNumber(getRequestParam(AttributeConst.REP_SHOW_UNREAD));
        }

        //指定されたページ数の一覧画面に表示する日報データを取得
        int page = getPage();
        List<ReportView> reports;
        //全日報データの件数を取得
        long reportsCount;
        if(isShow == AttributeConst.SHOW_FLAG_TRUE.getIntegerValue()) {
            //ログイン中の従業員の未読コメントのついた日報のみを表示する
            reports = reportService.getMineUnReadPerPage(loginEmployee, page);
            reportsCount = reportService.countAllMineUnRead(loginEmployee);
        }else {
            //全日報を表示する
            reports = reportService.getAllPerPage(page);
            reportsCount = reportService.countAll();
        }

        //リアクション数のリストを作成する
        List<Long> reactions = reactionService.getAllCountReactToReport(reports);

        putRequestScope(AttributeConst.REPORTS,reports);                //取得した日報データ
        putRequestScope(AttributeConst.REP_COUNT,reportsCount);         //全ての日報データの件数
        putRequestScope(AttributeConst.PAGE,page);                      //ページ数
        putRequestScope(AttributeConst.MAX_ROW,JpaConst.ROW_PER_PAGE);  //1ページに表示するレコードの数
        putRequestScope(AttributeConst.REP_SHOW_UNREAD,isShow);         //未読コメントのある日報のみを表示するかどうか
        putRequestScope(AttributeConst.REP_EXIST_UNREAD,exist_unread);  //未読コメントがついた日報が存在するかどうか
        putRequestScope(AttributeConst.REACTIONS,reactions);            //リアクション数のリスト

        //セッションスコープ内の日報データを削除
        removeSessionScope_report();
        //セッションにフラッシュメッセージが設定されている場合はリクエストスコープに移し替え
        String flush = getSessionScope(AttributeConst.FLUSH);
        if(flush != null) {
            putRequestScope(AttributeConst.FLUSH,flush);
            removeSessionScope(AttributeConst.FLUSH);
        }

        //一覧画面を表示
        forward(ForwardConst.FW_REP_INDEX);
    }

    /**
     * 新規登録画面を表示する
     * @throws ServletContext
     * @throws IOException
     */
    public void entryNew() throws ServletException,IOException{

        putRequestScope(AttributeConst.TOKEN,getTokenId()); //CSRF対策用トークン

        ReportView rv = new ReportView();
        EmployeeView ev = (EmployeeView)getSessionScope(AttributeConst.LOGIN_EMP);
        //日報情報の空インスタンスに、日報の日付＝今日の日付を設定する
        rv.setReportDate(LocalDate.now());
        //日報の出勤時刻と退勤時刻にデフォルト値を設定する(出勤登録済みの場合は出勤時刻を設定)
        Time punchIn = tmpService.getPuncIn(ev);
        if(punchIn == null) {
        rv.setPunchIn(Time.valueOf(ParameterConst.DEF_REP_PUNCH_IN.getValue()));
        }else {
            rv.setPunchIn(punchIn);
        }
        rv.setPunchOut(Time.valueOf(ParameterConst.DEF_REP_PUNCH_OUT.getValue()));
        putRequestScope(AttributeConst.REPORT,rv);

        //新規登録画面を表示
        forward(ForwardConst.FW_REP_NEW);
    }

    /**
     * 新規登録を行う1
     * @throws ServletException
     * @throws IOException
     */
    public void create() throws ServletException,IOException{

        //CSRF対策 tokenのチェック
        if(checkToken()) {

            //日報の日付が入力されていなければ、今日の日付を設定
            LocalDate day = null;
            if(getRequestParam(AttributeConst.REP_DATE) == null
                || getRequestParam(AttributeConst.REP_DATE).equals("")){
                day = LocalDate.now();
            }else {
                day = LocalDate.parse(getRequestParam(AttributeConst.REP_DATE));
            }

            //セッションから出勤時刻、退勤時刻情報を取得
            //もし入力が無ければエラーメッセージを出すため、null値を設定
            String chkPunchIn = getRequestParam(AttributeConst.REP_PUNCH_IN);
            Time punchIn = null;
            if(!(chkPunchIn == null || chkPunchIn.equals(""))) {
                punchIn = Time.valueOf(chkPunchIn + ":00");   //秒情報の付与
            }
            String chkPunchOut = getRequestParam(AttributeConst.REP_PUNCH_OUT);
            Time punchOut = null;
            if(!(chkPunchOut == null || chkPunchOut.equals(""))){
                punchOut = Time.valueOf(chkPunchOut + ":00");   //秒情報の付与
            }


            //セッションからログイン中の従業員情報を取得
            EmployeeView ev = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);

            //パラメータの値をもとに日報情報のインスタンスを作成する
            ReportView rv = new ReportView(
                    null,
                    ev, //ログインしている従業員を、日報作成者として登録する
                    day,
                    getRequestParam(AttributeConst.REP_TITLE),
                    getRequestParam(AttributeConst.REP_CONTENT),
                    null,
                    null,
                    punchIn,
                    punchOut,
                    AttributeConst.DEL_FLAG_FALSE.getIntegerValue(),
                    AttributeConst.READ_FLAG_TRUE.getIntegerValue());

            //日報情報の登録
            List<String> errors = reportService.create(rv);

            if(errors.size() > 0) {
                //登録中にエラーがあった場合

                putRequestScope(AttributeConst.TOKEN,getTokenId()); //CSRF対策用トークン
                putRequestScope(AttributeConst.REPORT,rv);          //入力されたレポート情報
                putRequestScope(AttributeConst.ERR,errors);         //エラーのリスト

                //新規登録画面の再表示
                forward(ForwardConst.FW_REP_NEW);
            }else {
                //登録中にエラーがなかった場合

                //出勤時刻の一時データをnullにする
                tmpService.setPunchIn(ev, null);

                //セッションに登録完了のフラッシュメッセージを設定
                putSessionScope(AttributeConst.FLUSH,MessageConst.I_REGISTERED.getMessage());

                //一覧画面にリダイレクト
                redirect(ForwardConst.ACT_REP,ForwardConst.CMD_INDEX);
            }
        }
    }

    /**
     * 詳細画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void show() throws ServletException,IOException{

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
        putRequestScope(AttributeConst.REPORT,rv);

        CommentView comment = new CommentView();
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

        //日報閲覧時にリアクションテーブルが作られていない場合は作成する
        if(reactionService.countCreatedMineReactDataToReport(rv, ev) != 1) {
            reactionService.create(rv, ev);
        }
        long reactCount = reactionService.countAllReactToReport(rv);
        long myReactCount = reactionService.countMineReactToReport(rv, ev);

        putRequestScope(AttributeConst.TOKEN,getTokenId());                                 //CSRF対策用トークン
        putRequestScope(AttributeConst.COMMENTS,comments);                                  //取得したコメントデータ
        putRequestScope(AttributeConst.REP_NODELETE_COMMENT_COUNT,noDeleteCommentCount);    //論理削除されていないコメント数
        putRequestScope(AttributeConst.REP_COMMENT_COUNT,commentCount);                     //全てのコメントデータの件数
        putRequestScope(AttributeConst.PAGE,page);                                          //ページ数
        putRequestScope(AttributeConst.MAX_ROW,JpaConst.ROW_PER_PAGE);                      //1ページに表示するレコードの数
        putRequestScope(AttributeConst.COMMENT,comment);                                    //空のコメントインスタンス
        putRequestScope(AttributeConst.RCT_REACT_COUNT,reactCount);                         //日報についたリアクション数
        putRequestScope(AttributeConst.RCT_MY_REACT_COUNT,myReactCount);                    //自分が日報につけたリアクションの数

        //詳細画面を表示
        forward(ForwardConst.FW_REP_SHOW);
    }

    /**
     * 編集画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void edit() throws ServletException,IOException{

        //idを条件に日報データを取得する
        ReportView rv = reportService.findOne(toNumber(getRequestParam(AttributeConst.REP_ID)));

        //セッションからログイン中の従業員情報を取得
        EmployeeView ev = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);

        if(rv == null || ev.getId() != rv.getEmployee().getId()) {
            //該当の日報データが存在しない、または
            //ログインしている従業員が日報の作成者でない場合はエラー画面を表示
            forward(ForwardConst.FW_ERR_UNKNOWN);
        }else {

            putRequestScope(AttributeConst.TOKEN,getTokenId()); //CSRF対策用トークン
            putRequestScope(AttributeConst.REPORT,rv);          //取得した日報データ

            //編集画面を表示
            forward(ForwardConst.FW_REP_EDIT);
        }
    }

    /**
     * 更新を行う
     * @throws ServletException
     * @throws IOException
     */
    public void update() throws ServletException,IOException{

        //CSRF対策用 tokenのチェック
        if(checkToken()) {

            //idを条件に日報データを取得する
            ReportView rv = reportService.findOne(toNumber(getRequestParam(AttributeConst.REP_ID)));

            //入力された日報内容を設定する
            rv.setReportDate(toLocalDate(getRequestParam(AttributeConst.REP_DATE)));
            rv.setTitle(getRequestParam(AttributeConst.REP_TITLE));
            rv.setContent(getRequestParam(AttributeConst.REP_CONTENT));

            //日報データを更新する
            List<String> errors = reportService.update(rv);

            if(errors.size() > 0) {
                //更新中にエラーが発生した場合

                putRequestScope(AttributeConst.TOKEN,getTokenId()); //CSFR対策用トークン
                putRequestScope(AttributeConst.REPORT,rv);          //入力された日報データ
                putRequestScope(AttributeConst.ERR,errors);         //エラーのリスト

                //編集画面の再表示
                forward(ForwardConst.FW_REP_EDIT);
            }else {
                //更新中にエラーがなかった場合

                //セッションに更新完了のフラッシュメッセージを設定
                putSessionScope(AttributeConst.FLUSH,MessageConst.I_UPDATED.getMessage());

                //一覧画面にリダイレクト
                redirect(ForwardConst.ACT_REP,ForwardConst.CMD_INDEX);
            }
        }
    }

    /**
     * 日報に「いいね」する
     * @throws ServletException
     * @throws IOException
     */
    public void doReact() throws ServletException,IOException{

        //CSRF対策
        if(checkToken()) {
            int id = Integer.parseInt(getRequestParam(AttributeConst.REP_ID));
            ReportView rv = reportService.findOne(id);
            EmployeeView ev = getSessionScope(AttributeConst.LOGIN_EMP);
            //リアクションを行い、完了した場合フラッシュメッセージを設定
            if(reactionService.doReact(rv, ev)) {
                putRequestScope(AttributeConst.FLUSH,MessageConst.I_ADD_GOOD.getMessage());
            }

            //詳細画面の呼び出し処理
            show();
        }
    }

    /**
     * 日報についた「いいね」を取り消す
     * @throws ServletException
     * @throws IOException
     */
    public void cancelReact() throws ServletException,IOException{

        //CSRF対策
        if(checkToken()) {
            int id = Integer.parseInt(getRequestParam(AttributeConst.REP_ID));
            ReportView rv = reportService.findOne(id);
            EmployeeView ev = getSessionScope(AttributeConst.LOGIN_EMP);
            //リアクションを取り消し、完了した場合フラッシュメッセージを設定
            if(reactionService.cancelReact(rv, ev)) {
                putRequestScope(AttributeConst.FLUSH,MessageConst.I_SUB_GOOD.getMessage());
            }

            //詳細画面の呼び出し処理
            show();
        }
    }
}
