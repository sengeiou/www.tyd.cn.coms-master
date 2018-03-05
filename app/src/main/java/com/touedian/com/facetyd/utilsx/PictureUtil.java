package com.touedian.com.facetyd.utilsx;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class PictureUtil {
    /**
     * 将bitmap转为Base64字符串
     *
     * @param bitmap
     * @return base64字符串
     */
    public static String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
        byte[] bytes = outputStream.toByteArray();
        //Base64算法加密，当字符串过长（一般超过76）时会自动在中间加一个换行符，字符串最后也会加一个换行符。
        // 导致和其他模块对接时结果不一致。所以不能用默认Base64.DEFAULT，而是Base64.NO_WRAP不换行
        L.i(Base64.encodeToString(bytes, Base64.NO_WRAP));

        return Base64.encodeToString(bytes, Base64.NO_WRAP);

    }

    /**
     * 将base64字符串转为bitmap
     * @param base64String
     * @return bitmap
     */
    public static Bitmap base64ToBitmap(String base64String) {

        byte[] bytes = Base64.decode(base64String, Base64.NO_WRAP);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return bitmap;

    }

    /**
     * 根据得到图片字节，获得图片后缀
     *
     * @param photoByte 图片字节
     * @return 图片后缀
     */
    public static String getFileExtendName(byte[] photoByte) {
        String strFileExtendName = ".jpg";
        if ((photoByte[0] == 71) && (photoByte[1] == 73) && (photoByte[2] == 70)
                && (photoByte[3] == 56) && ((photoByte[4] == 55) || (photoByte[4] == 57))
                && (photoByte[5] == 97)) {
            strFileExtendName = ".gif";
        } else if ((photoByte[6] == 74) && (photoByte[7] == 70) && (photoByte[8] == 73)
                && (photoByte[9] == 70)) {
            strFileExtendName = ".jpg";
        } else if ((photoByte[0] == 66) && (photoByte[1] == 77)) {
            strFileExtendName = ".bmp";
        } else if ((photoByte[1] == 80) && (photoByte[2] == 78) && (photoByte[3] == 71)) {
            strFileExtendName = ".png";
        }
        return strFileExtendName;
    }
}