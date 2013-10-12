package com.clemble.social.service.match;


public enum MatchScore {
    /**
     * Value that indicates that given data can not be matched.
     */
    NO_MATCH(-1),

    /**
     * Value that indicates that detector can not find out whether could be a
     * match or not.
     */
    INCONCLUSIVE(0),

    /**
     * Value that indicates that given data could be of specified format (i.e.
     * it can not be ruled out).
     */
    WEAK_MATCH(1),

    /**
     * Value that indicates that given data conforms to (one of) canonical
     * form(s) of the data format.
     */
    SOLID_MATCH(3),

    /**
     * Value that indicates that given data contains a signature that is deemed
     * specific enough to uniquely indicate data format used.
     */
    FULL_MATCH(5);

    final private int score;

    private MatchScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public static MatchScore valueOf(int score) {
        return valueOf((double) score);
    }

    public static MatchScore valueOf(double score) {
        if (score >= FULL_MATCH.getScore())
            return FULL_MATCH;
        if (score <= NO_MATCH.getScore())
            return NO_MATCH;
        double lowestDifference = Double.MAX_VALUE;
        MatchScore closestMatch = null;
        for (MatchScore matchScore : MatchScore.values()) {
            double currentMatch = Math.abs(matchScore.score - score);
            if (currentMatch < lowestDifference) {
                closestMatch = matchScore;
                lowestDifference = currentMatch;
            }
        }
        return closestMatch;
    }
}
