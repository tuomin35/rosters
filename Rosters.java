package com.github.tuomin35.rosters;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Rosters {

    private static final char COMMENT_LINE = '#';
    private static final char LINE_DELIMITER = ':';

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("d.M.yyyy HH:mm:ss");
    private static final Charset ENCODING = StandardCharsets.UTF_8;
    private static final String PLAYERS_FILE_NAME = "players.txt";
    private static final String RANKING_FILE_NAME = "ranking.txt";
    private static final String ROSTERS_FILE_NAME = "rosters.txt";

    private static final String ARGUMENT_DEBUG1 = "-d";
    private static final String ARGUMENT_DEBUG2 = "--debug";
    private static final String ARGUMENT_RANDOM1 = "-r";
    private static final String ARGUMENT_RANDOM2 = "--random";
    private static final int RANDOM_DELTA_DEFAULT = 5;

    public static void main(String[] args) {

        boolean debug = false;
        boolean random = false;
        int random_delta = RANDOM_DELTA_DEFAULT;
        if ( args.length > 0 ) {
            for (String arg : args) {
                if ( arg.equals(ARGUMENT_DEBUG1) || arg.equals(ARGUMENT_DEBUG2) ) {
                    debug = true;
                }
                if ( arg.equals(ARGUMENT_RANDOM1) || arg.equals(ARGUMENT_RANDOM2) ) {
                    random = true;
                }
                else if ( arg.matches("\\d+") ) {
                    random_delta = Integer.parseInt(arg);
                }
            }
        }

        try {
            List<String> players = readPlayersFile();
            Map<String, Integer> ranking = readRankingFile();
            sortPlayers(players, ranking, random, random_delta);
            writeRostersFile(players, random, random_delta, debug);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        System.exit(0);
    }

    private static List<String> readPlayersFile() throws IOException {
        List<String> players = readTextFile(PLAYERS_FILE_NAME);
        Iterator<String> it = players.iterator();
        while (it.hasNext()) {
            if ( it.next().trim().isEmpty() ) {
                it.remove();
            }
        }
        for (int i = 0; i < players.size(); i++) {
            players.set(i, players.get(i).trim());
        }
        return players;
    }

    private static Map<String, Integer> readRankingFile() throws IOException {
        List<String> lines = readTextFile(RANKING_FILE_NAME);
        Map<String, Integer> ranking = new HashMap<String, Integer>();
        lines.forEach((line) -> {
            if ( !line.trim().isEmpty() && line.trim().charAt(0) != COMMENT_LINE ) {
                int delimiter = line.indexOf(LINE_DELIMITER);
                String key = line.substring(0, delimiter).trim();
                int value = Integer.valueOf(line.substring(delimiter + 1).trim());
                ranking.put(key, value);
            }
        });
        return ranking;
    }

    private static List<String> readTextFile(String fileName) throws IOException {
        Path path = Paths.get(fileName);
        return Files.readAllLines(path, ENCODING);
    }

    private static void sortPlayers(List<String> players, Map<String, Integer> ranking, boolean random,
            int random_delta) {

        players.sort(new Comparator<String>() {

            @Override
            public int compare(String o1, String o2) {
                int r1 = ranking.containsKey(o1) ? ranking.get(o1) : 50;
                int r2 = ranking.containsKey(o2) ? ranking.get(o2) : 50;

                if ( random && Math.abs(r2 - r1) <= random_delta ) {
                    return (Math.random() < 0.5 ? 1 : -1);
                }
                else {
                    return r2 - r1;
                }
            }

        });
    }

    private static void swap(List<String> list1, List<String> list2) {
        List<String> temp = new ArrayList<String>();
        temp.addAll(list1);
        list1.clear();
        list1.addAll(list2);
        list2.clear();
        list2.addAll(temp);
    }

    private static int teamAverageRanking(List<String> team) throws IOException {
        Map<String, Integer> ranking = readRankingFile();

        int cumulativeRanking = 0;
        for (String player : team) {
            cumulativeRanking += ranking.containsKey(player) ? ranking.get(player) : 50;
        }

        return cumulativeRanking / team.size();
    }

    private static void writeRostersFile(List<String> players, boolean random, int random_delta, boolean debug)
            throws IOException {

        // rankingin parilliset pelaajat punaiselle
        List<String> punainen = new ArrayList<String>();
        for (int i = 0; i < players.size(); i = i + 2) {
            punainen.add(players.get(i));
        }

        // rankingin parittomat pelaajat valkoiselle
        List<String> valkoinen = new ArrayList<String>();
        for (int i = 1; i < players.size(); i = i + 2) {
            valkoinen.add(players.get(i));
        }

        // järjestetään aakkosjärjestykseen
        Collections.sort(punainen);
        Collections.sort(valkoinen);
        // tarvittaessa vaihdetaan joukkueet keskenään
        if ( punainen.contains("Tepi") ) {
            swap(punainen, valkoinen);
        }
        if ( debug ) {
            System.out.println("[DEBUG] Team average ranking: punainen = " + teamAverageRanking(punainen)
                    + ", valkoinen = " + teamAverageRanking(valkoinen) + "\n");
        }

        // muodostetaan tiedoston sisältö
        final StringBuilder builder = new StringBuilder();
        builder.append(String.format("# File '%s' created %s", ROSTERS_FILE_NAME,
                DATE_FORMAT.format(Calendar.getInstance().getTime())));
        builder.append(random ? String.format(" (random_delta = %d)", random_delta) : "");

        String template = "\n\n%s (%d):\n%s";
        builder.append(String.format(template, "Punainen", punainen.size(), getAsString(punainen)));
        builder.append(String.format(template, "Valkoinen", valkoinen.size(), getAsString(valkoinen)));

        writeTextFile(Arrays.asList(builder.toString()), ROSTERS_FILE_NAME);
    }

    private static void writeTextFile(List<String> lines, String fileName) throws IOException {
        Path path = Paths.get(fileName);
        Files.write(path, lines, ENCODING);
    }

    private static String getAsString(List<?> list) {
        final StringBuilder builder = new StringBuilder();
        list.forEach((item) -> {
            if ( builder.length() > 0 ) {
                builder.append(", ");
            }
            builder.append(item);
        });
        return builder.toString();
    }

    private static String getAsString(Map<?, ?> map) {
        final StringBuilder builder = new StringBuilder();
        map.forEach((k, v) -> {
            if ( builder.length() > 0 ) {
                builder.append(", ");
            }
            builder.append(k + "=" + v);
        });
        return builder.toString();
    }
}
