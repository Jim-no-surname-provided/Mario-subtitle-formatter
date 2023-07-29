package formatter.model;

import com.google.gson.Gson;

import formatter.inout.*;

public class FormatterSettings {

    private int maxLenSubtitle;
    private int maxLenLine;
    private final int nearSplittpointThreshold;

    /** It will prefer this characters over whitespaces to split lines. */
    private char[] preferedSplitPoints; //// = { ',', ')' };
    /**
     * This characters define de end of a sentence if next to these come one of the
     * {@code endOfSentence} variables.
     */
    private char[] endOfSentence; //// = { ':', '.', '?', '!', ']' };

    public void maxLenSubtitle(final int maxLenSubtitle) {
        this.maxLenSubtitle = maxLenSubtitle;
    }

    public void maxLenLine(final int maxLenLine) {
        this.maxLenLine = maxLenLine;
    }

    public void preferedSplitPoints(final char[] preferedSplitPoints) {
        this.preferedSplitPoints = preferedSplitPoints;
    }

    public void endOfSentence(final char[] endOfSentence) {
        this.endOfSentence = endOfSentence;
    }

    /**
     * One of these has to come next to the {@code preferedSplitPoints} for it to
     * truly be considered a split point.
     */
    private final char[] nextToSentence = { ' ', '\r', '\n' };

    /** Default name for the Input file. */
    private final String defInName; //// "resources/original.txt";

    /** Default name for the Output file. */
    private final String defOutName; //// "resources/result.txt";

    /** The path to where the files are. */
    private static final String PATH_PREFIX = "demo/src/main/resources/";

    private static final Gson gson = new Gson();

    private static final String SETTINGS_FILE_PATH = "settings.json";

    /** Default name for the Input file. */
    public String defInName() {
        return defInName;
    }

    /** Default name for the Output file. */
    public String defOutName() {
        return defOutName;
    }

    public FormatterSettings(final int maxLenSubtitle, final int maxLenLine, final int nearSplittpointThreshold) {
        this(maxLenSubtitle, maxLenLine, nearSplittpointThreshold,
                ",)".toCharArray(),
                ":.?!]".toCharArray(),
                "data/original.txt",
                "data/result.txt");
    }

    public FormatterSettings(final int maxLenSubtitle, final int maxLenLine, final int nearSplittpointThreshold,
            final char[] preferedSplitPoints, final char[] endOfSentence, final String defInName, final String defOutName) {
        this.maxLenSubtitle = maxLenSubtitle;
        this.maxLenLine = maxLenLine;
        this.nearSplittpointThreshold = nearSplittpointThreshold;
        this.preferedSplitPoints = preferedSplitPoints;
        this.endOfSentence = endOfSentence;

        this.defInName = defInName;
        this.defOutName = defOutName;

    }

    public int maxLenSubtitle() {
        return maxLenSubtitle;
    }

    public int maxLenLine() {
        return maxLenLine;
    }

    public int nearSplittpointThreshold() {
        return nearSplittpointThreshold;
    }

    /** It will prefer this characters over whitespaces to split lines. */
    public char[] preferedSplitPoints() {
        return preferedSplitPoints;
    }

    /**
     * This characters define de end of a sentence if next to these come one of the
     * {@code endOfSentence} variables.
     */
    public char[] endOfSentence() {
        return endOfSentence;
    }

    /**
     * One of these has to come next to the {@code preferedSplitPoints} for it to
     * truly be considered a split point.
     */
    public char[] nextToSentence() {
        return nextToSentence;
    }

    public static FormatterSettings fromJson() {
        final String s = InOut.readFile(PATH_PREFIX + SETTINGS_FILE_PATH);
        return gson.fromJson(s, FormatterSettings.class);
    }

    public static void toJson(final FormatterSettings settings) {
        final String s = gson.toJson(settings);
        InOut.writeFile(PATH_PREFIX + SETTINGS_FILE_PATH, s);
    }
}
