public class Xor { 
    public static int xor(byte[] bytes) { 
        int crc = 0;
        for (byte b : bytes) {
            crc = crc ^ b;
        }

        return crc;
    }
}
