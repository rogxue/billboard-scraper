package billboardscraper;

import billboardscraper.Rankings.SongPair;
import java.text.DecimalFormat;

public class BillboardScraper {

    public static void main(String[] args) {

//        getNewChartData();
        DecimalFormat fmt = new DecimalFormat("0.0000");
        Chart chart = new Chart("hot-100", "2016-12-03");

        int rank = 1, trueRank = 1;
        double prevPoint = -1;
        Rankings rankings = new Rankings();

        for (int i = 0; i < 51; i++) {
            for (Song s : chart.getDataFromLocal()) {
                if (!rankings.isSongRanked(s)) {
                    s.setPeakPos(s.getRank());
                    rankings.getSongList().add(new SongPair(s, s.getPoints()));
                }
            }
            chart = chart.getNextWeek();
        }
        rankings.sortByArtist();
        rankings.sortByPoints();
        System.out.println("Rank\t\tPoints\t  Artist - Song Title (Peak) (Eligible Weeks)");
        for (SongPair sp : rankings.getSongList()) {
            if (prevPoint != sp.getPoints()) {
                System.out.print(rank++ + ".\t(" + trueRank++ + ")\t");
            } else {
                System.out.print("\t(" + trueRank++ + ")\t");
            }
            prevPoint = sp.getPoints();
            System.out.println(fmt.format(sp.getPoints()) + " " + sp.getSong().toShortString() + " (" + sp.getSong().getPeakPos() + ")" + "(" + sp.getSong().getWeeks() + ")");
        }
    }

    private static void getNewChartData() {
        Chart chart = new Chart("hot-100", "2017-03-25");
        while (!chart.getDateStr().equalsIgnoreCase("2017-11-18")) {
            chart = chart.getNextWeek();
            chart.dataToText();
        }
    }
}
