package models.validators;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import actions.views.ReportView;
import constants.MessageConst;

/**
 * 日報インスタンスに設定されている値のバリデーションを行うクラス
 * @author ryouta.osada
 *
 */
public class ReportValidator {


    /**
     * 日報インスタンスの各項目についてバリデーションを行う
     * @param rv 日報インスタンス
     * @return エラーのリスト
     */
    public static List<String> validate(ReportView rv){
        List<String> errors = new ArrayList<String>();

        //入力文字前後の空白文字等を削除
        removeSpace(rv);

        //タイトルのチェック
        String titleError = validateTitle(rv.getTitle());
        if(!titleError.equals("")) {
            errors.add(titleError);
        }

        //内容のチェック
        String contentError = validateContent(rv.getContent());
        if(!contentError.equals("")) {
            errors.add(contentError);
        }

        //出勤時刻のチェック
        String punchInError = validatePunchIn(rv.getPunchIn());
        if(!punchInError.equals("")) {
            errors.add(punchInError);
        }

        //退勤時刻のチェック
        String punchOutError = validatePunchOut(rv.getPunchOut());
        if(!punchOutError.equals("")) {
            errors.add(punchOutError);
        }

        return errors;
    }

    /**
     * 入力内容の前後のスペース文字列を取り除くことでスペース文字や改行のみの項目もエラーの対象内とする
     * @param rv 日報のインスタンス
     */
    private static void removeSpace(ReportView rv) {
        rv.setTitle(rv.getTitle().strip());
        rv.setContent(rv.getContent().strip());
    }

    /**
     * タイトルに入力値があるかをチェックし、入力値がなければエラーメッセージを返す
     * @param title タイトル
     * @return エラーメッセージ
     */
    private static String validateTitle(String title) {
        if(title ==  null || title.equals("")) {
            return MessageConst.E_NOTITLE.getMessage();
        }

        //入力値がある場合は空文字を返却
        return "";
    }

    /**
     * 内容に入力値があるかをチェックし、入力値がなければエラーメッセージを返却
     * @param content 内容
     * @return エラーメッセージ
     */
    private static String validateContent(String content) {
        if(content == null || content.equals("")) {
            return MessageConst.E_NOCONTENT.getMessage();
        }

        //入力値がある場合は空文字を返却
        return "";
    }

    /**
     * 出勤時刻に入力値があるかをチェックし、入力値がなけらばエラーメッセージを返却
     * @param punchIn 出勤時刻
     * @return エラーメッセージ
     */
    private static String validatePunchIn(Time punchIn) {
        if(punchIn == null) {
            return MessageConst.E_NOPUNCH_IN.getMessage();
        }

        //入力値がある場合は空文字を返却
        return "";
    }

    /**
     * 退勤時刻に入力値があるかをチェックし、入力値がなけらばエラーメッセージを返却
     * @param punchOut 退勤時刻
     * @return エラーメッセージ
     */
    private static String validatePunchOut(Time punchOut) {
        if(punchOut == null) {
            return MessageConst.E_NOPUNCH_IN.getMessage();
        }

        //入力値がある場合は空文字を返却
        return "";
    }
}
