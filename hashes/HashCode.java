import java.util.Arrays;

public class HashCode { 
    private static Simhash shInstance = new Simhash(new BinaryWordSeg());
    private static Adler32 adlerInstance = new Adler32();
    private static Fletcher32 fletcherInstance = new Fletcher32();

    public static int getHash(String string, HashType type) {
        return getHash(string.getBytes(), type);
    }

    public static int getHash(byte[] bytes, HashType type) {
        switch(type) {
            case NONE:
                return noHash(bytes);
            case JENKINS:
                return jenkins(bytes);
            case FLETCHER32:
                return fletcher32(bytes);
            case BLAKE_2S:
                return blake2s(bytes);
            case XX_HASH:
                return xxHash(bytes);
            case ADLER32:
                return adler32(bytes);
            case SIM_HASH:
                return simHash(bytes);
            case XOR8:
                return xor(bytes);
            case CRC32:
                return crc32(bytes);
            case WHIRLPOOL:
                return whirlpool(bytes);
            case MURMUR:
                return murmur(bytes);
            case SHA256:
                return sha256(bytes);
            case MD5:
                return md5(bytes);
            case HASH_CODE:
            default:
                return standard(bytes);
        }
    }

    private static int noHash(byte[] bytes) {
        return NoHash.noHash(bytes);
    }

    private static int jenkins(byte[] bytes) {
        return JenkinsHash.getInstance().hash32(bytes);
    }

    private static int fletcher32(byte[] bytes) {
        fletcherInstance.reset();
        fletcherInstance.update(bytes);
        return (int) fletcherInstance.getValue();
    }

    private static int blake2s(byte[] bytes) {
        Blake2s hash = new Blake2s(null, null);
        hash.update(bytes);
        return first32Bit(hash.digest());
    }

    private static int xxHash(byte[] bytes) {
        return XxHash.hash32(bytes, 0, bytes.length, 0);
    }

    private static int adler32(byte[] bytes) {
        adlerInstance.reset();
        adlerInstance.update(bytes);
        return (int) adlerInstance.getValue();
    }

    private static int simHash(byte[] bytes) {
        return (int) shInstance.simhash32(new String(bytes));
    }

    private static int xor(byte[] bytes) {
        return Xor.xor(bytes);
    }

    private static int crc32(byte[] bytes) {
        return Crc.crc32(bytes);
    }

    private static int standard(byte[] bytes) {
        return Arrays.hashCode(bytes);
    }

    private static int whirlpool(byte[] bytes) {
        bytes = Whirlpool.whirlpool(bytes, 0, bytes.length);
        return first32Bit(bytes);
    }

    private static int murmur(byte[] bytes) {
        return MurmurHash.hash32(bytes, bytes.length);
    }

    private static int sha256(byte[] bytes) {
        String result = Sha.sha256(bytes);
        return first32Bit(result.getBytes());
    }

    private static int md5(byte[] bytes) {
        byte[] result = MD5.computeMD5(bytes);
        return first32Bit(result);
    }

    private static int first32Bit(byte[] bytes) {
        int result = 0;
        result = bytes[0];
        result <<= 4;
        result += bytes[1];
        result <<= 4;
        result += bytes[2];
        result <<= 4;
        result += bytes[3];
        return result;
    }
}
