package jonathanfinerty.once;

@SuppressWarnings("WeakerAccess")
public class Amount {

    public static CountChecker exactly(final int numberOfTimes) {
        return count -> numberOfTimes == count;
    }

    public static CountChecker moreThan(final int numberOfTimes) {
        return count -> count > numberOfTimes;
    }

    public static CountChecker lessThan(final int numberOfTimes) {
        return count -> count < numberOfTimes;
    }
}
