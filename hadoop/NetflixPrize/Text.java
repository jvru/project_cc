public class Text extends org.apache.hadoop.io.Text {
    //TODO to select a has, modify the selectedHash variable
    private static final HashType selectedHash = HashType.NONE;

    public Text() {
    }

    public Text(String s) {
        super(s);
    }

    @Override
    public int hashCode() {
        return HashCode.getHash(toString().getBytes(), selectedHash);
    }
}
