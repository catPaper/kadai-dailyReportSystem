package actions.views;

import constants.AttributeConst;
import constants.JpaConst;
import models.Reaction;

/**
 * リアクションデータのDTOモデル⇔Viewモデルの変換を行うクラス
 * @author ryouta.osada
 *
 */
public class ReactionConverter {

    /**
     * ViewモデルのインスタンスからDTOモデルのインスタンスを作成する
     * @param rv ReactionViewのインスタンス
     * @return Reactionのインスタンス
     */
    public static Reaction toModel(ReactionView rv) {
        return new Reaction(
                rv.getId(),
                rv.getReport(),
                rv.getEmployee(),
                rv.getReactFlag() == null
                    ? null
                    : rv.getReactFlag() == AttributeConst.REACT_FLAG_TRUE.getIntegerValue()
                        ? JpaConst.RCT_REP_TRUE
                        : JpaConst.RCT_REP_FALSE,
                rv.getReactedAt());
    }

    /**
     * DTOモデルのインスタンスからViewモデルのインスタンスを作成する
     * @param r Reactionのインスタンス
     * @return ReactionViewのインスタンス
     */
    public static ReactionView toView(Reaction r) {
        return new ReactionView(
                r.getId(),
                r.getReport(),
                r.getEmployee(),
                r.getReactFlag() == null
                    ? null
                    : r.getReactFlag() == JpaConst.RCT_REP_TRUE
                        ? AttributeConst.REACT_FLAG_TRUE.getIntegerValue()
                        : AttributeConst.REACT_FLAG_FALSE.getIntegerValue(),
                r.getReactedAt());
    }

    /**
     * Viewモデルの全フィールドの内容をDTOモデルのフィールドにコピーする
     * @param r DTOモデル(コピー先)
     * @param rv Viewモデル(コピー元)
     */
    public static void copyViewToModel(Reaction r,ReactionView rv) {
        r.setId(rv.getId());
        r.setReport(rv.getReport());
        r.setEmployee(rv.getEmployee());
        r.setReactFlag(rv.getReactFlag());
        r.setReactedAt(rv.getReactedAt());
    }
}
