import java.io.Serializable;
import java.util.Arrays;

public class MessageTuple implements Serializable, Comparable<MessageTuple>{
    private static final long serialVersionUID = 1234L;
    public String message;
    public long[] intArray;

    public MessageTuple(String message, long[] intArray) {
        this.message = message;
        this.intArray = intArray;
    }

    public String getMessage() {
        return this.message;
    }
    public long[] getArray(){
        return this.intArray;
    }

    @Override
    public int compareTo(MessageTuple other) {
        if (this.intArray[0] < other.intArray[0]) {
            return -1;
        } else if (this.intArray[0] > other.intArray[0]) {
            return 1;
        } else if (this.intArray[1] < other.intArray[1]) {
            return -1;
        } else {
            return 0;
        }
    }
}
