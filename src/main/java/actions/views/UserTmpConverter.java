package actions.views;

import models.UserTmp;

/**
 * ユーザーテンプデータのDTOモデル⇔Viewモデルの変換を行うクラス
 * @author ryouta.osada
 *
 */
public class UserTmpConverter {

    /**
     * ViewモデルのインスタンスからDTOモデルのインスタンスを作成する
     * @param uv UserTmpViewのインスタンス
     * @return Reportのインスタンス
     */
    public static UserTmp toModel(UserTmpView uv) {
        return new UserTmp(
               uv.getId(),
               EmployeeConverter.toModel(uv.getEmployee()),
               uv.getPunchIn());
    }

    /**
     * DTOモデルのインスタンスからViewモデルのインスタンを作成する
     * @param u UserTmpのインスタンス
     * @return UserTmpViewのインスタンス
     */
    public static UserTmpView toView(UserTmp u) {
        if(u == null) {
            return null;
        }

        return new UserTmpView(
                u.getId(),
                EmployeeConverter.toView(u.getEmployee()),
                u.getPunchIn());
    }
}
