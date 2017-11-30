/**
 * @author dimav
 *         Date: 4/7/15
 *         Time: 10:33 AM
 */
public class Fletcher32 {

    private int sum1 = 0;
    private int sum2 = 0;

    /**
     * Creates a new Fletcher-32 object.
     */
    public Fletcher32() {
    }

    /**
     * Returns Fletcher-32 value.
     *
     * @since Commons Checksum 1.0
     */
    public long getValue() {
        return (sum2 << 16) | sum1;
    }

    /**
     * Resets Fletcher-32 to initial value.
     *
     * @since Commons Checksum 1.0
     */
    public void reset() {
        sum1 = 0;
        sum2 = 0;
    }

    /**
     * Updates checksum with specified array of bytes.
     *
     * @param b the array of bytes to update the checksum with
     * @since Commons Checksum 1.0
     */
    public void update(byte[] b) {
        update(b, 0, b.length);
    }

    /**
     * Updates Fletcher-32 with specified array of bytes.
     *
     * @since Commons Checksum 1.0
     */
    public void update(byte[] b, int off, int len) {
        if (b == null) {
            throw new NullPointerException();
        }
        if (off < 0 || len < 0 || off > b.length - len) {
            throw new ArrayIndexOutOfBoundsException();
        }
        updateBytes(b, off, len);
    }

    /**
     * Updates Fletcher-32 with specified array of bytes.
     *
     * @since Commons Checksum 1.0
     */
    private void updateBytes(byte[] b, int off, int len) {
        for (int i = off; i < len; i++) {
            sum1 = (sum1 + (b[i] & 0xff)) % 65535;
            sum2 = (sum2 + sum1) % 65535;
        }
    }
}
