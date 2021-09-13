package utils;

import java.sql.Time;
import java.time.LocalTime;

import constants.ParameterConst;

/**
 * 計算に関するユーティリティクラス
 * @author ryouta.osada
 *
 */
public class CalculationUtil {

    /**
     * 指定の時間間隔に合わせてフォーマットした現在時刻を取得する
     * 指定間隔は 60秒<= time < 3600秒
     * (exp. step=10m in=10:22:00 out=10:30:00
     */
    public static Time NowFormatTime(){
        LocalTime lt = LocalTime.now();
        int stepMinutes = (ParameterConst.STEP_REP_TIME.getIntegerValue() / 60);
        lt = lt.withSecond(0);
        lt = NowFormatTimeInternal(lt,stepMinutes,stepMinutes);
        return Time.valueOf(lt);
    }

    /**
     * 受け取った時間を指定の時間間隔で表示する
     * @param lt 取得時間
     * @param STEP_MINUTES 時間の間隔
     * @param inStepMinutes ループ内の時間の間隔
     * @return
     */
    private static LocalTime NowFormatTimeInternal(LocalTime lt,final int STEP_MINUTES,int inStepMinutes) {
        //余りが０の場合はフォーマット通りなので返却する
        if(( lt.getMinute() % inStepMinutes )== 0) {
            return lt;
        }else {
            if(lt.getMinute() < inStepMinutes) {
                if(inStepMinutes >= 60) {
                    //フォーマット間隔が分を超えた場合は時間を繰り上げ
                    lt = lt.plusHours(1);
                    lt = lt.withMinute(0);
                }else {
                    lt = lt.withMinute(inStepMinutes);
                }
            }else {
                inStepMinutes += STEP_MINUTES;
                lt = NowFormatTimeInternal(lt,STEP_MINUTES,inStepMinutes);
            }
        }
        return lt;
    }
}
