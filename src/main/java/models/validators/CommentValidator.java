package models.validators;

import java.util.ArrayList;
import java.util.List;

import actions.views.CommentView;
import constants.MessageConst;

/**
 * コメントインスタンスに設定されている値のバリデーションを行うクラス
 * @author ryouta.osada
 *
 */
public class CommentValidator {

    /**
     * コメントインスタンスの各項目についてバリデーションを行う
     * @param cv コメントインスタンス
     * @return エラーのリスト
     */
    public static List<String> validate(CommentView cv){
        List<String> errors = new ArrayList<String>();

        //入力文字前後の空白文字を削除
        removeSpace(cv);

        //内容のチェック
        String contentError = validateContent(cv.getContent());
        if(!contentError.equals("")) {
            errors.add(contentError);
        }

        return errors;
    }

    /**
     * 入力内容の前後のスペース文字列を取り除くことでスペース文字や改行のみの項目もエラーの対象内とする
     * @param cv コメントのインスタンス
     */
    private static void removeSpace(CommentView cv) {
        cv.setContent(cv.getContent().strip());
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
}
