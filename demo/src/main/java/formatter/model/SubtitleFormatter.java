package formatter.model;

public class SubtitleFormatter {

    private final FormatterSettings settings;

    private final char[] src;
    private final StringBuilder sb;

    public SubtitleFormatter(String originalText, FormatterSettings fs) {
        settings = fs;
        src = originalText.toCharArray();
        sb = new StringBuilder();

    }

    public String getFormatted() {
        return sb.toString();
    }

    public void format() {

        int beginOfSentence = 0;
        boolean sHasPrefSPoint = false;
        boolean isNameOfChar = false;

        for (int i = 0; i < src.length; i++) {
            // if coma
            if (isOneOf(src[i], settings.preferedSplitPoints())) {
                sHasPrefSPoint = true;
            }

            // if point, ?, ! or ]
            else if (isOneOf(src[i], settings.endOfSentence())) {
                if (i == src.length - 1 || isOneOf(src[i + 1], settings.nextToSentence())) {

                    if (isNameOfChar) {
                        manageDialog();
                    } else {
                        manageSentence(beginOfSentence, i, sHasPrefSPoint);
                    }

                    sHasPrefSPoint = false;
                    i = nextBeginningChar(i);
                    i--; // get into the space

                    beginOfSentence = i + 1; // next to space
                    isNameOfChar = false;
                }
            }

            // if it needs a -
            else if (src[i] == '\t') {
                isNameOfChar = true;
            }

        }

    }

    private int nextBeginningChar(int current) {
        current++;
        while (current < src.length && isOneOf(src[current], settings.nextToSentence())) {
            current++;
        }
        return current;
    }

    private boolean isOneOf(char c, char... set) {
        for (char d : set) {
            if (c == d) {
                return true;
            }
        }
        return false;
    }

    private void manageDialog() {
        copyToBuilder("-");
    }

    /**
     * 
     * @param start Inclusive.
     * @param end   Inclusive: This is the point or symbol that ends the sentence.
     * 
     */
    private void manageSentence(int start, int end, boolean hasCommasOP) {
        int len = end - start + 1;
        int splittingPoint = splittingPoint(start, end, hasCommasOP);

        // cut long subtitles into smaller ones
        if (len > settings.maxLenSubtitle()) {
            // Create new sentence that ends in the char before the splitting point
            manageSentence(start, splittingPoint - 1, hasCommasOP);
            // Create new sentence that starts in the next char to the splitting point
            manageSentence(splittingPoint + 1, end, hasCommasOP);
            return; // Recursivly managed
        }

        // split into more lines
        if (len > settings.maxLenLine()) {
            copyToBuilder(start, splittingPoint - 1);
            sb.append('\n');
            copyToBuilder(splittingPoint + 1, end);
        }

        // the sentence is small enough
        else {
            copyToBuilder(start, end);
        }

        sb.append('\n');
        sb.append('\n');
    }

    /**
     * 
     * @param start Inclusive
     * @param end   Inclusive
     */
    private void copyToBuilder(int start, int end) {
        for (int i = start; i <= end; i++) {
            sb.append(src[i]);
        }
    }

    private void copyToBuilder(String s) {
        sb.append(s);
    }

    /**
     * 
     * @param start
     * @param end
     * @param hasCommasOP
     * @return The index of the next space to the center. If it has a comma or ')'
     *         near it, it will prefer that over the space.
     */
    private int splittingPoint(int start, int end, boolean hasCommasOP) {

        int center = (start + end) / 2;
        int nextSpace = nextCharOf(center, ' ', end);

        if (nextSpace == -1) { // No space found
            return center;
        }

        int bestPoint = nextSpace;

        if (hasCommasOP) {

            for (char c : settings.preferedSplitPoints()) {
                int nearOfCur = nearestCharOf(start, end, c);

                if (Math.abs(nearOfCur - center) <= settings.nearSplittpointThreshold()) {
                    bestPoint = nearOfCur + 1; // The space that is next to it
                }
            }

        }
        return bestPoint;
    }

    private int nearestCharOf(int start, int end, char ch) {
        int refPoint = (start + end) / 2;
        int nextChar = nextCharOf(refPoint, ',', end);
        int lastChar = lastCharOf(refPoint, ',', start);

        int distanceToNextChar = Math.abs(nextChar - refPoint);
        int distanceToLastChar = Math.abs(lastChar - refPoint);

        int nearestChar = lastChar;
        if (distanceToNextChar <= distanceToLastChar) {
            nearestChar = nextChar;
        }

        return nearestChar;
    }

    private int nextCharOf(int refPoint, char ch, int limit) {
        return nearCharOf(refPoint, ch, limit, 1);
    }

    private int lastCharOf(int refPoint, char ch, int limit) {
        return nearCharOf(refPoint, ch, limit, -1);
    }

    /**
     * 
     * @param refPoint
     * @param ch
     * @param limit
     * @param increment
     * @return -1 if not found. The index of the next ch in some direction when
     */
    private int nearCharOf(int refPoint, char ch, int limit, int increment) {
        int nextChar = refPoint;
        while (src[nextChar] != ch && nextChar != limit) { // until it is a space
            nextChar += increment;
        }
        if (nextChar == limit) {
            return -1;
        }

        return nextChar;
    }
}
