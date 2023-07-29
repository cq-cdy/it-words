package com.example.ITwords;

import android.content.Context;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.BankCardParams;
import com.baidu.ocr.sdk.model.BankCardResult;
import com.baidu.ocr.sdk.model.GeneralBasicParams;
import com.baidu.ocr.sdk.model.GeneralParams;
import com.baidu.ocr.sdk.model.GeneralResult;
import com.baidu.ocr.sdk.model.IDCardParams;
import com.baidu.ocr.sdk.model.IDCardResult;
import com.baidu.ocr.sdk.model.OcrRequestParams;
import com.baidu.ocr.sdk.model.OcrResponseResult;
import com.baidu.ocr.sdk.model.ResponseResult;

import java.io.File;

public class OCRManager {

    /**
     * 通用文字识别接口
     *
     * @param context     上下文
     * @param filePath    图片文件路径
     * @param ocrCallBack 请求回调
     */
    public static void recognizeGeneralBasic(Context context, String filePath, final OCRCallBack<GeneralResult> ocrCallBack) {
        // 通用文字识别参数设置
        GeneralBasicParams param = new GeneralBasicParams();
        param.setDetectDirection(true);
        param.setImageFile(new File(filePath));

        // 调用通用文字识别服务
        OCR.getInstance(context).recognizeGeneralBasic(param, new OnResultListener<GeneralResult>() {
            @Override
            public void onResult(GeneralResult result) {
                ocrCallBack.succeed(result);
            }

            @Override
            public void onError(OCRError ocrError) {
                ocrCallBack.failed(ocrError);
            }
        });
    }

    /**
     * 通用文字识别接口（高精度版）
     *
     * @param context     上下文
     * @param filePath    图片文件路径
     * @param ocrCallBack 请求回调
     */
    public static void recognizeAccurateBasic(Context context, String filePath, final OCRCallBack<GeneralResult> ocrCallBack) {
        // 通用文字识别参数设置
        GeneralBasicParams param = new GeneralBasicParams();
        param.setDetectDirection(true);
        param.setImageFile(new File(filePath));

        // 调用通用文字识别服务
        OCR.getInstance(context).recognizeAccurateBasic(param, new OnResultListener<GeneralResult>() {
            @Override
            public void onResult(GeneralResult result) {
                ocrCallBack.succeed(result);
            }

            @Override
            public void onError(OCRError ocrError) {
                ocrCallBack.failed(ocrError);
            }
        });
    }

    /**
     * 通用文字识别接口（含位置信息版）
     *
     * @param context     上下文
     * @param filePath    图片文件路径
     * @param ocrCallBack 请求回调
     */
    public static void recognizeGeneral(Context context, String filePath, final OCRCallBack<GeneralResult> ocrCallBack) {
        // 通用文字识别参数设置
        GeneralParams param = new GeneralParams();
        param.setDetectDirection(true);
        param.setImageFile(new File(filePath));

        // 调用通用文字识别服务
        OCR.getInstance(context).recognizeGeneral(param, new OnResultListener<GeneralResult>() {
            @Override
            public void onResult(GeneralResult result) {
                ocrCallBack.succeed(result);
            }

            @Override
            public void onError(OCRError ocrError) {
                ocrCallBack.failed(ocrError);
            }
        });
    }

    /**
     * 通用文字识别接口（高精度含位置信息版）
     *
     * @param context     上下文
     * @param filePath    图片文件路径
     * @param ocrCallBack 请求回调
     */
    public static void recognizeAccurate(Context context, String filePath, final OCRCallBack<GeneralResult> ocrCallBack) {
        // 通用文字识别参数设置
        GeneralParams param = new GeneralParams();
        param.setDetectDirection(true);
        param.setImageFile(new File(filePath));

        // 调用通用文字识别服务
        OCR.getInstance(context).recognizeAccurate(param, new OnResultListener<GeneralResult>() {
            @Override
            public void onResult(GeneralResult result) {
                ocrCallBack.succeed(result);
            }

            @Override
            public void onError(OCRError ocrError) {
                ocrCallBack.failed(ocrError);
            }
        });
    }

    /**
     * 通用文字识别接口(含生僻字版)
     *
     * @param context     上下文
     * @param filePath    图片文件路径
     * @param ocrCallBack 请求回调
     */
    public static void recognizeGeneralEnhanced(Context context, String filePath, final OCRCallBack<GeneralResult> ocrCallBack) {
        // 通用文字识别参数设置
        GeneralBasicParams param = new GeneralBasicParams();
        param.setDetectDirection(true);
        param.setImageFile(new File(filePath));

        // 调用通用文字识别服务
        OCR.getInstance(context).recognizeGeneralEnhanced(param, new OnResultListener<GeneralResult>() {
            @Override
            public void onResult(GeneralResult result) {
                ocrCallBack.succeed(result);
            }

            @Override
            public void onError(OCRError ocrError) {
                ocrCallBack.failed(ocrError);
            }
        });
    }

    /**
     * 网络图片文字识别
     *
     * @param context     上下文
     * @param filePath    图片路径
     * @param ocrCallBack 请求回调
     */
    public static void recognizeWebimage(Context context, String filePath, final OCRCallBack<GeneralResult> ocrCallBack) {
        // 网络图片识别参数设置
        GeneralBasicParams param = new GeneralBasicParams();
        param.setDetectDirection(true);
        param.setImageFile(new File(filePath));

        // 调用网络图片识别服务
        OCR.getInstance(context).recognizeWebimage(param, new OnResultListener<GeneralResult>() {
            @Override
            public void onResult(GeneralResult result) {
                ocrCallBack.succeed(result);
            }

            @Override
            public void onError(OCRError ocrError) {
                ocrCallBack.failed(ocrError);
            }
        });
    }

    /**
     * 银行卡识别
     *
     * @param context     上下文
     * @param filePath    图片文件路径
     * @param ocrCallBack 请求回调
     */
    public static void recognizeBankCard(Context context, String filePath, final OCRCallBack<BankCardResult> ocrCallBack) {
        // 银行卡识别参数设置
        BankCardParams param = new BankCardParams();
        param.setImageFile(new File(filePath));

        // 调用银行卡识别服务
        OCR.getInstance(context).recognizeBankCard(param, new OnResultListener<BankCardResult>() {
            @Override
            public void onResult(BankCardResult result) {
                // 调用成功，返回BankCardResult对象
                ocrCallBack.succeed(result);
            }

            @Override
            public void onError(OCRError error) {
                // 调用失败，返回OCRError对象
                ocrCallBack.failed(error);
            }
        });
    }

    /**
     * 身份证识别
     *
     * @param context     上下文
     * @param filePath    图片文件路径
     * @param ocrCallBack 请求回调
     */
    public static void recognizeIDCard(Context context, String filePath, final OCRCallBack<IDCardResult> ocrCallBack) {
        // 身份证识别参数设置
        IDCardParams param = new IDCardParams();
        param.setImageFile(new File(filePath));

        // 调用身份证识别服务
        OCR.getInstance(context).recognizeIDCard(param, new OnResultListener<IDCardResult>() {
            @Override
            public void onResult(IDCardResult result) {
                // 调用成功，返回BankCardResult对象
                ocrCallBack.succeed(result);
            }

            @Override
            public void onError(OCRError error) {
                // 调用失败，返回OCRError对象
                ocrCallBack.failed(error);
            }
        });
    }

    /**
     * 行驶证识别
     *
     * @param context     上下文
     * @param filePath    图片文件路径
     * @param ocrCallBack 请求回调
     */
    public static void recognizeVehicleLicense(Context context, String filePath, final OCRCallBack<OcrResponseResult> ocrCallBack) {
        // 行驶证识别参数设置
        OcrRequestParams param = new OcrRequestParams();
        // 设置image参数
        param.setImageFile(new File(filePath));
        // 设置其他参数
        param.putParam("detect_direction", true);
        // 调用行驶证识别服务
        OCR.getInstance(context).recognizeVehicleLicense(param, new OnResultListener<OcrResponseResult>() {
            @Override
            public void onResult(OcrResponseResult result) {
                // 调用成功，返回OcrResponseResult对象
                ocrCallBack.succeed(result);
            }

            @Override
            public void onError(OCRError error) {
                // 调用失败，返回OCRError对象
                ocrCallBack.failed(error);
            }
        });
    }

    /**
     * 驾驶证识别
     *
     * @param context     上下文
     * @param filePath    图片文件路径
     * @param ocrCallBack 请求回调
     */
    public static void recognizeDrivingLicense(Context context, String filePath, final OCRCallBack<OcrResponseResult> ocrCallBack) {
        // 驾驶证识别参数设置
        OcrRequestParams param = new OcrRequestParams();
        // 设置image参数
        param.setImageFile(new File(filePath));
        // 设置其他参数
        param.putParam("detect_direction", true);
        // 调用驾驶证识别服务
        OCR.getInstance(context).recognizeDrivingLicense(param, new OnResultListener<OcrResponseResult>() {
            @Override
            public void onResult(OcrResponseResult result) {
                // 调用成功，返回OcrResponseResult对象
                ocrCallBack.succeed(result);
            }

            @Override
            public void onError(OCRError error) {
                // 调用失败，返回OCRError对象
                ocrCallBack.failed(error);
            }
        });
    }

    /**
     * 车牌识别
     *
     * @param context     上下文
     * @param filePath    图片文件路径
     * @param ocrCallBack 请求回调
     */
    public static void recognizeLicensePlate(Context context, String filePath, final OCRCallBack<OcrResponseResult> ocrCallBack) {
        // 车牌识别参数设置
        OcrRequestParams param = new OcrRequestParams();
        // 设置image参数
        param.setImageFile(new File(filePath));
        // 设置其他参数
        param.putParam("detect_direction", true);
        // 调用车牌识别服务
        OCR.getInstance(context).recognizeLicensePlate(param, new OnResultListener<OcrResponseResult>() {
            @Override
            public void onResult(OcrResponseResult result) {
                // 调用成功，返回OcrResponseResult对象
                ocrCallBack.succeed(result);
            }

            @Override
            public void onError(OCRError error) {
                // 调用失败，返回OCRError对象
                ocrCallBack.failed(error);
            }
        });
    }

    /**
     * 营业执照识别
     *
     * @param context     上下文
     * @param filePath    图片文件路径
     * @param ocrCallBack 请求回调
     */
    public static void recognizeBusinessLicense(Context context, String filePath, final OCRCallBack<OcrResponseResult> ocrCallBack) {
        // 营业执照识别参数设置
        OcrRequestParams param = new OcrRequestParams();
        // 设置image参数
        param.setImageFile(new File(filePath));
        // 设置其他参数
        param.putParam("detect_direction", true);
        // 调用营业执照识别服务
        OCR.getInstance(context).recognizeBusinessLicense(param, new OnResultListener<OcrResponseResult>() {
            @Override
            public void onResult(OcrResponseResult result) {
                // 调用成功，返回OcrResponseResult对象
                ocrCallBack.succeed(result);
            }

            @Override
            public void onError(OCRError error) {
                // 调用失败，返回OCRError对象
                ocrCallBack.failed(error);
            }
        });
    }

    /**
     * 通用票据识别
     *
     * @param context     上下文
     * @param filePath    图片文件路径
     * @param ocrCallBack 请求回调
     */
    public static void recognizeReceipt(Context context, String filePath, final OCRCallBack<OcrResponseResult> ocrCallBack) {
        // 通用票据识别参数设置
        OcrRequestParams param = new OcrRequestParams();
        // 设置image参数
        param.setImageFile(new File(filePath));
        // 设置其他参数
        param.putParam("detect_direction", true);
        // 调用通用票据识别服务
        OCR.getInstance(context).recognizeReceipt(param, new OnResultListener<OcrResponseResult>() {
            @Override
            public void onResult(OcrResponseResult result) {
                // 调用成功，返回OcrResponseResult对象
                ocrCallBack.succeed(result);
            }

            @Override
            public void onError(OCRError error) {
                // 调用失败，返回OCRError对象
                ocrCallBack.failed(error);
            }
        });
    }


    /**
     * 从返回内容中提取识别出的信息
     *
     * @param result
     * @return
     */
    public static String getResult(ResponseResult result) {
        String sb = result.getJsonRes();
        return sb;
    }

    /**
     * 图片识别统一回调接口
     */
    public interface OCRCallBack<T> {
        void succeed(T data);

        void failed(OCRError error);
    }
}
