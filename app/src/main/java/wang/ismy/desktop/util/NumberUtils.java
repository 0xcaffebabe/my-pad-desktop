package wang.ismy.desktop.util;

public class NumberUtils {

    public static int byteArrayToInt(byte[] b)
    {
        int value = 0;
        for (int i = 0; i < 2; i++) {
            int shift = (2 - 1 - i) * 8;
            value += (b[i] & 0x000000FF) << shift;
        }
        return value;
    }
}
