package billboardscraper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Chart {

    private final String billboard = "http://www.billboard.com/charts/";
    private String type, dateStr, url;

    /**
     * Chart object constructor, I don't think it's used anymore
     *
     * @param type
     */
    public Chart(String type) {
        this(type, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
    }

    /**
     * Chart object constructor
     *
     * @param type
     * @param dateInput
     */
    public Chart(String type, String dateInput) {
        this.type = type;
        try {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            Date date = fmt.parse(dateInput);

            //
            // Makes sure date is valid Billboard chart Saturday
            //
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int weekday = cal.get(Calendar.DAY_OF_WEEK);
            if (weekday != Calendar.SATURDAY) {
                int days = (Calendar.SATURDAY - weekday) % 7;
                cal.add(Calendar.DAY_OF_YEAR, days);
            }
            date = cal.getTime();
            dateStr = fmt.format(date);
            url = billboard + type + "/" + dateStr;

        } catch (ParseException ex) {
            Logger.getLogger(Chart.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Gets list of songs from data scraped from Billboard's website, works for
     * Billboard Hot 100, not sure about other charts
     *
     * @return List of songs from particular chart
     */
    public List<Song> getData() {
        List<Song> chart = new ArrayList<>();
        String line;
        Song song = new Song();
        int count = 1;

        try {
            InputStream is = new URL(url).openStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                if (line.contains("\"Song Hover-")) {
                    song = new Song();
                    song.setDate(dateStr);
                    song.setRank(count++);
                } else if (line.contains("chart-row__song")) {
                    String title = line.substring(line.indexOf("chart-row__song") + 17, line.indexOf("</h2"));
                    title = title.replaceAll("&#039;", "'");
                    song.setTitle(title);
                } else if (line.contains("chart-row__artist")) {
                    line = br.readLine();
                    if (line.contains("a-tracklabel=\"Artist Name\">")) {
                        line = br.readLine();
                        if (!line.contains("</a>			</h3>")) {
                            song.setArtist(line.trim());
                        }
                    } else {
                        song.setArtist(line.trim());
                    }
                } else if (line.contains("<span class=\"chart-row__label\">Last Week</span>")) {
                    line = br.readLine();
                    String prevPos = line.substring(line.indexOf("\"chart-row__value\">") + 19, line.indexOf("</span>"));
                    if (!prevPos.equalsIgnoreCase("--")) {
                        song.setLastPos(Integer.parseInt(prevPos));
                        song.setChange(song.getLastPos() - song.getRank());
                    }

                } else if (line.contains("<span class=\"chart-row__label\">Peak Position</span>")) {
                    line = br.readLine();
                    song.setPeakPos(Integer.parseInt(line.substring(line.indexOf("\"chart-row__value\">") + 19, line.indexOf("</span>"))));
                } else if (line.contains("<span class=\"chart-row__label\">Wks on Chart</span>")) {
                    line = br.readLine();
                    song.setWeeks(Integer.parseInt(line.substring(line.indexOf("\"chart-row__value\">") + 19, line.indexOf("</span>"))));
                    chart.add(song);
                }
            }
        } catch (MalformedURLException ex) {
            Logger.getLogger(Chart.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Chart.class.getName()).log(Level.SEVERE, null, ex);
        }
        return chart;
    }

    /**
     * Writes active chart data to txt file
     */
    public void dataToText() {
        System.out.println(dateStr);
        String line;
        Song song = new Song();
        int count = 1;
        String delimiter = "@@@";
        try {
            FileWriter fw = new FileWriter(System.getProperty("user.dir") + "\\data\\" + dateStr + ".txt");
            InputStream is = new URL(url).openStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                if (line.contains("\"Song Hover-")) {
                    song = new Song();
                    song.setDate(dateStr);
                    song.setRank(count++);
                } else if (line.contains("chart-row__song")) {
                    String title = line.substring(line.indexOf("chart-row__song") + 17, line.indexOf("</h2"));
                    title = title.replaceAll("&#039;", "'");
                    song.setTitle(title);
                } else if (line.contains("chart-row__artist")) {
                    line = br.readLine();
                    if (line.contains("a-tracklabel=\"Artist Name\">")) {
                        line = br.readLine();
                        if (!line.contains("</a>			</h3>")) {
                            song.setArtist(line.trim());
                        }
                    } else {
                        song.setArtist(line.trim());
                    }
                } else if (line.contains("<span class=\"chart-row__label\">Last Week</span>")) {
                    line = br.readLine();
                    String prevPos = line.substring(line.indexOf("\"chart-row__value\">") + 19, line.indexOf("</span>"));
                    if (!prevPos.equalsIgnoreCase("--")) {
                        song.setLastPos(Integer.parseInt(prevPos));
                    }
                    fw.write(song.getRank() + delimiter + song.getLastPos() + delimiter + song.getArtist() + delimiter + song.getTitle());
                    fw.write(System.getProperty("line.separator"));
                }
            }
            fw.close();
        } catch (MalformedURLException ex) {
            Logger.getLogger(Chart.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Chart.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Gets data from local files
     *
     * @return List of songs
     */
    public List<Song> getDataFromLocal() {
        List<Song> chart = new ArrayList<>();
        String line;
        Song song;

        try {
            FileReader fr = new FileReader(System.getProperty("user.dir") + "\\data\\" + dateStr + ".txt");
            BufferedReader br = new BufferedReader(fr);
            while ((line = br.readLine()) != null) {
                String[] data = line.split("@@@");
                song = new Song();
                song.setDate(dateStr);
                song.setRank(Integer.parseInt(data[0]));
                song.setLastPos(Integer.parseInt(data[1]));
                song.setArtist(data[2]);
                song.setTitle(data[3]);
                chart.add(song);
            }
        } catch (MalformedURLException ex) {
            Logger.getLogger(Chart.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Chart.class.getName()).log(Level.SEVERE, null, ex);
        }
        return chart;
    }

    /**
     * Gets previous week's chart
     *
     * @return Previous week chart
     */
    public Chart getPreviousWeek() {
        try {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            Date date = fmt.parse(dateStr);

            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.DAY_OF_YEAR, -7);
            date = cal.getTime();
            dateStr = fmt.format(date);
            url = billboard + type + "/" + dateStr;

        } catch (ParseException ex) {
            Logger.getLogger(Chart.class.getName()).log(Level.SEVERE, null, ex);
        }
        return this;
    }

    /**
     * Gets next week's chart
     *
     * @return Next week chart
     */
    public Chart getNextWeek() {
        try {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            Date date = fmt.parse(dateStr);

            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.DAY_OF_YEAR, 7);
            date = cal.getTime();
            dateStr = fmt.format(date);
            url = billboard + type + "/" + dateStr;

        } catch (ParseException ex) {
            Logger.getLogger(Chart.class.getName()).log(Level.SEVERE, null, ex);
        }
        return this;
    }

    /**
     * Returns active date
     *
     * @return Date
     */
    public String getDateStr() {
        return dateStr;
    }

    /**
     * Returns active year
     *
     * @return Year
     */
    public String getYear() {
        return dateStr.substring(0, 4);
    }
}
