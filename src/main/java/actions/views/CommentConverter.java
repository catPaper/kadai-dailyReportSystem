package actions.views;

import java.util.ArrayList;
import java.util.List;

import constants.AttributeConst;
import constants.JpaConst;
import models.Comment;

/**
 * コメントデータのDTOモデル⇔Viewモデルの変換を行うクラス
 * @author ryouta.osada
 *
 */
public class CommentConverter {

    /**
     * ViewモデルのインスタンスからDTOモデルのインスタンスを作成する
     * @param cv CommentViewのインスタンス
     * @return Commentのインスタンス
     */
    public static Comment toMedel(CommentView cv) {
        return new Comment(
                cv.getId(),
                ReportConverter.toModel(cv.getReport()),
                EmployeeConverter.toModel(cv.getEmployee()),
                cv.getContent(),
                cv.getCreatedAt(),
                cv.getUpdatedAt(),
                cv.getDeleteFlag() == null
                    ? null
                    : cv.getDeleteFlag() == AttributeConst.DEL_FLAG_TRUE.getIntegerValue()
                        ? JpaConst.CMT_DEL_TRUE
                        : JpaConst.CMT_DEL_FALSE);
    }


    /**
     * DTOモデルのインスタンスからViewモデルのインスタンスを作成する
     * @param c Commentのインスタンス
     * @return CommentViewのインスタンス
     */
    public static CommentView toView(Comment c) {
        if(c == null) {
            return null;
        }

        return new CommentView(
                c.getId(),
                ReportConverter.toView(c.getReport()),
                EmployeeConverter.toView(c.getEmployee()),
                c.getContent(),
                c.getCreatedAt(),
                c.getUpdatedAt(),
                c.getDeleteFlag() == null
                    ? null
                    : c.getDeleteFlag() == JpaConst.EMP_DEL_TRUE
                        ? AttributeConst.DEL_FLAG_TRUE.getIntegerValue()
                        : AttributeConst.DEL_FLAG_FALSE.getIntegerValue());
    }

    public static List<CommentView> toViewList(List<Comment> list){
        List<CommentView> evs = new ArrayList<>();

        for(Comment c:list) {
            evs.add(toView(c));
        }

        return evs;
    }

    /**
     * Viewモデルの全フィールドの内容をDTOモデルのフィールドにコピーする
     * @param c DTOモデル（コピー先）
     * @param cv Viewモデル（コピー元）
     */
    public static void copyViewToModel(Comment c,CommentView cv) {
        c.setId(cv.getId());
        c.setReport(ReportConverter.toModel(cv.getReport()));
        c.setEmployee(EmployeeConverter.toModel(cv.getEmployee()));
        c.setContent(cv.getContent());
        c.setCreatedAt(cv.getCreatedAt());
        c.setUpdatedAt(cv.getUpdatedAt());
        c.setDeleteFlag(cv.getDeleteFlag());
    }
}
