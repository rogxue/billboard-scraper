package billboardscraper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Rankings {

    private final List<SongPair> songList = new ArrayList<>();

    /**
     * Initializes ranking object which is just a list of songs
     */
    public Rankings() {

    }

    /**
     * Returns song list
     *
     * @return song list
     */
    public List<SongPair> getSongList() {
        return songList;
    }

    /**
     * Determines if song is already included in rankings, if true, adds song
     * and data
     *
     * @param s
     * @return was song in rankings
     */
    public boolean isSongRanked(Song s) {
        //
        // Some double checking for coincidences where separate songs have similar
        // artists and names and/or slight misspellings in the data
        //
        int p = (s.getArtist().length() > 4) ? 5 : s.getArtist().length();
        int q, t;
        for (SongPair sp : songList) {
            q = (sp.getSong().getArtist().length() > 4) ? 5 : sp.getSong().getArtist().length();
            t = Math.min(p, q);
            if (sp.getSong().getTitle().equalsIgnoreCase(s.getTitle())
                    && sp.getSong().getArtist().equalsIgnoreCase(s.getArtist())) {
                if (s.getRank() < sp.getSong().getPeakPos()) {
                    sp.getSong().setPeakPos(s.getRank());
                }
                sp.getSong().addWeek();
                sp.addPoints(s.getPoints());
                return true;
            }
        }
        return false;
    }

    /**
     * Sorts rankings by points
     */
    public void sortByPoints() {
        Collections.sort(songList, (SongPair c1, SongPair c2) -> Double.compare(c2.getPoints(), c1.getPoints()));
    }

    /**
     * Sorts rankings by artist name, then song title
     */
    public void sortByArtist() {
        Collections.sort(songList, (SongPair c1, SongPair c2) -> c1.getSong().getTitle().toLowerCase().compareTo(c2.getSong().getTitle().toLowerCase()));
        Collections.sort(songList, (SongPair c1, SongPair c2) -> c1.getSong().getArtist().toLowerCase().compareTo(c2.getSong().getArtist().toLowerCase()));
    }

    /**
     * Sorts rankings by song title, then artist
     */
    public void sortByTitle() {
        Collections.sort(songList, (SongPair c1, SongPair c2) -> c1.getSong().getArtist().toLowerCase().compareTo(c2.getSong().getArtist().toLowerCase()));
        Collections.sort(songList, (SongPair c1, SongPair c2) -> c1.getSong().getTitle().toLowerCase().compareTo(c2.getSong().getTitle().toLowerCase()));
    }

    /**
     * Song pair object that pairs a song with its accumulated points
     */
    public static class SongPair {

        private final Song song;
        private double points;

        /**
         * Song Pair constructor
         *
         * @param song
         */
        public SongPair(Song song) {
            this.song = song;
            points = 0;
        }

        /**
         * Song Pair constructor
         *
         * @param song
         * @param points
         */
        public SongPair(Song song, double points) {
            this.song = song;
            this.points = points;
        }

        /**
         * Returns song object
         *
         * @return song
         */
        public Song getSong() {
            return song;
        }

        /**
         * Returns points
         *
         * @return points
         */
        public double getPoints() {
            return points;
        }

        /**
         * Adds amount to points total
         *
         * @param add
         */
        public void addPoints(double add) {
            points += add;
        }
    }
}
