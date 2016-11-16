package billboardscraper;

public class Song {

    private String title, artist, spotify, date;
    private int peakPos, lastPos, weeks, change, rank;

    /**
     * Creates song object
     */
    public Song() {
        title = "";
        artist = "";
        peakPos = 0;
        lastPos = 101;
        weeks = 1;
        change = 0;
        rank = 0;
    }

    /**
     * Returns song title
     *
     * @return title of song
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets song title
     *
     * @param title
     */
    public void setTitle(String title) {
        this.title = cleanString(title);
    }

    /**
     * Returns artist name
     *
     * @return name of artist
     */
    public String getArtist() {
        return artist;
    }

    /**
     * Sets song artist
     *
     * @param artist
     */
    public void setArtist(String artist) {
        this.artist = cleanString(artist);
    }

    /**
     * Returns peak position
     *
     * @return peaks position
     */
    public int getPeakPos() {
        return peakPos;
    }

    /**
     * Sets peak position
     *
     * @param peakPos
     */
    public void setPeakPos(int peakPos) {
        this.peakPos = peakPos;
    }

    /**
     * Returns Spotify ID
     *
     * @return Spotify ID
     */
    public String getSpotify() {
        return spotify;
    }

    /**
     * Sets Spotify ID
     *
     * @param spotify
     */
    public void setSpotify(String spotify) {
        this.spotify = spotify;
    }

    /**
     * Returns date
     *
     * @return date
     */
    public String getDate() {
        return date;
    }

    /**
     * Sets date
     *
     * @param date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Get previous position
     *
     * @return Previous position
     */
    public int getLastPos() {
        return lastPos;
    }

    /**
     * Sets last position
     *
     * @param lastPos
     */
    public void setLastPos(int lastPos) {
        this.lastPos = lastPos;
    }

    /**
     * Increments week
     */
    public void addWeek() {
        weeks++;
    }

    /**
     * Gets weeks on chart
     *
     * @return weeks on chart
     */
    public int getWeeks() {
        return weeks;
    }

    /**
     * Sets weeks on chart
     *
     * @param weeks
     */
    public void setWeeks(int weeks) {
        this.weeks = weeks;
    }

    /**
     * Gets change in position
     *
     * @return change in position
     */
    public int getChange() {
        return change;
    }

    /**
     * Sets change in position
     *
     * @param change
     */
    public void setChange(int change) {
        this.change = change;
    }

    /**
     * Gets rank
     *
     * @return rank
     */
    public int getRank() {
        return rank;
    }

    /**
     * Sets rank
     *
     * @param rank
     */
    public void setRank(int rank) {
        this.rank = rank;
    }

    /**
     * Returns generic song data string
     *
     * @return song data string
     */
    @Override
    public String toString() {
        String s = rank + ". " + artist + " - " + title + "\n";
        if (lastPos == 101) {
            s += "Last Week: -- Peak: " + peakPos + " Weeks: " + weeks + " Change: --";
        } else {
            s += "Last Week: " + lastPos + " Peak: " + peakPos + " Weeks: " + weeks + " Change: " + change;
        }
        return s;
    }

    /**
     * Returns short string with just song artist and title
     *
     * @return Song artist and title
     */
    public String toShortString() {
        return artist + " - " + title;
    }

    /**
     * String cleaning
     *
     * @param s
     * @return clean string
     */
    private String cleanString(String s) {
        s = s.replace("&amp;", "&");
        s = s.replace("&#039;", "'");
        s = s.replace("&#39;", "'");
        s = s.replace("   ", " & ");
        s = s.replace("_and_", "&");
        s = s.replace("l  ", "l & ");
        s = s.replace("Z  ", "Z & ");
        s = s.replace("  ", "' ");
        s = s.replace("&quot;", "\"");
        s = s.replace(" d ", "'d ");
        s = s.replace(" s ", "'s ");
        s = s.replace(" t ", "'t ");
        s = s.replace(" m ", "'m ");
        s = s.replace(" ll ", "'ll ");
        s = s.replace(" re ", "'re ");
        s = s.replace(" ve ", "'ve ");
        return s;
    }

    /**
     * Returns points based on inverse points system
     *
     * @return points
     */
    public int getInversePoints() {
        return 101 - rank;
    }

    /**
     * Returns points based on square root points system
     *
     * @return points
     */
    public double getRootPoints() {
        return (-1 * Math.sqrt(rank) + 11) * 10;
    }

    /**
     * Returns points based on multiplicative inverse system and weighted years
     *
     * @return points
     */
    public double getPoints() {
        int year = Integer.parseInt(date.substring(0, 4));
        double multiplier;
        if (year <= 1991) {
            multiplier = 1.9;
        } else {
            multiplier = 1;
        }
        return multiplier / (rank * 1.1 + 9.6) * 1000;
    }

}
