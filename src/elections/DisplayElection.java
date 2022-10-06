package elections;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.imageio.ImageIO;

public class DisplayElection {

    public static void displayElection(int numseats, String[] candidates, List<ElectionState> record) throws IOException {
        String timestring = "Fall 2022";
        int numcandidates = candidates.length;
        Decimal maxvote = Decimal.ZERO;
        for (ElectionState electionstate : record) {
            for (int candidate = 0; candidate < numcandidates; candidate++) {
                Decimal vote = electionstate.getVote(candidate);
                if (vote.compareTo(maxvote) > 0) {
                    maxvote = vote;
                }
            }
        }
        FontMetrics metrics = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB).getGraphics().getFontMetrics(font);
        int maxlen = 0;
        for (String candidate : candidates) {
            int len = metrics.stringWidth(candidate);
            if (len > maxlen) {
                maxlen = len;
            }
        }
        int w = barwidth + maxlen + 3 * (padding + 1);
        int[] ordering = candidateOrdering(numcandidates, record);
        String[] reorderedcandidates = new String[numcandidates];
        for (int i = 0; i < numcandidates; i++) {
            reorderedcandidates[i] = candidates[ordering[i]];
        }
        int round = 1;
        for (ElectionState electionstate : record) {
            Decimal[] votes = new Decimal[numcandidates];
            CandidateState[] states = new CandidateState[numcandidates];
            for (int i = 0; i < numcandidates; i++) {
                votes[i] = electionstate.getVote(ordering[i]);
                states[i] = electionstate.getCandidateState(ordering[i]);
            }
            Decimal quota = electionstate.getQuota();
            BufferedImage img = displayElectionState(maxlen, reorderedcandidates, states, votes, maxvote, quota);
            ImageIO.write(img, "png", new File("C:\\Users\\thoma\\website\\election\\round" + round + ".png"));
            round++;
        }
        List<String> sb = new ArrayList<>();
        sb.add("<html>");
        sb.add("<head>");
        sb.add("<title>" + timestring + " MGSA Election</title>");
        sb.add("<style>");
        sb.add("body {text-align:center; font-family: sans-serif; color:#FDB515; background-color: #003262;}");
        sb.add(".infobox {margin: auto; box-sizing: border-box; -moz-box-sizing: border-box; -webkit-box-sizing: border-box; "
                + "width: " + w + "px; border: 1px solid black; padding: " + cssinpadding + "px; background-color: #EEEEEE; "
                + "text-align:left; color:#000000;}");
        sb.add("img {display: block; margin-left: auto; margin-right: auto;}");
        sb.add("</style>");
        sb.add("</head>");
        sb.add("<body>");
        sb.add("<h1>" + timestring + " MGSA Election</h1>");
        sb.add("<h2>There were " + numcandidates + " candidates and " + numseats + " open seats</h2>");
        sb.add("<h2>The election used Meek's STV algorithm</h2>");
        List<String> elected = new ArrayList<>();
        List<String> defeated = new ArrayList<>();
        for (round = 0; round < record.size(); round++) {
            String text = "After " + (round == 0 ? "distributing the initial" : "reallocating") + " votes, ";
            if (elected.size() >= numseats || numcandidates - defeated.size() <= numseats) {
                text = "";
            }
            List<String> newelected = new ArrayList<>();
            List<String> newdefeated = new ArrayList<>();
            ElectionState electionstate = record.get(round);
            for (int i = 0; i < numcandidates; i++) {
                int candidate = ordering[i];
                String candidatename = candidates[candidate];
                CandidateState candidatestate = electionstate.getCandidateState(candidate);
                if (candidatestate == CandidateState.ELECTED && !elected.contains(candidatename)) {
                    elected.add(candidatename);
                    newelected.add(candidatename);
                } else if (candidatestate == CandidateState.DEFEATED && !defeated.contains(candidatename)) {
                    defeated.add(candidatename);
                    newdefeated.add(candidatename);
                }
            }
            if (newelected.isEmpty() && newdefeated.size() == 1) {
                text += newdefeated.get(0) + " is defeated:";
            } else if (!newelected.isEmpty() && newdefeated.isEmpty()) {
                text += winners(newelected) + ":";
            } else {
                throw new IllegalArgumentException("Incorrect pattern of elections and defeats");
            }
            sb.add("<div class=\"infobox\">" + text + "</div>");
            sb.add("<div style=\"height: " + cssoutpadding + "px;\">&nbsp;</div>");
            sb.add("<img src=\"round" + (round + 1) + ".png\">");
            sb.add("<div style=\"height: " + cssoutpadding + "px;\">&nbsp;</div>");
        }
        sb.add("<div class=\"infobox\">" + winners(elected) + "!</div>");
        sb.add("</body>");
        sb.add("</html>");
        Path file = Paths.get("C:\\Users\\thoma\\website\\election\\election.html");
        Files.write(file, sb, StandardCharsets.UTF_8);
        System.exit(0);
    }

    private static String winners(List<String> names) {
        int len = names.size();
        if (len == 0) {
            throw new IllegalArgumentException("No winners");
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            sb.append(names.get(i));
            if (i != len - 1 && len > 2) {
                sb.append(",");
            }
            sb.append(" ");
            if (i == len - 2) {
                sb.append("and ");
            }
        }
        sb.append(len == 1 ? "is" : "are");
        sb.append(" elected");
        return sb.toString();
    }

    private static int[] candidateOrdering(int numcandidates, List<ElectionState> record) {
        Integer[] Intordering = new Integer[numcandidates];
        for (int i = 0; i < numcandidates; i++) {
            Intordering[i] = i;
        }
        Arrays.sort(Intordering, (id1, id2) -> {
            CandidateState state1 = record.get(record.size() - 1).getCandidateState(id1);
            CandidateState state2 = record.get(record.size() - 1).getCandidateState(id2);
            if (state1 == CandidateState.ELECTED && state2 == CandidateState.DEFEATED) {
                return -1;
            } else if (state1 == CandidateState.DEFEATED && state2 == CandidateState.ELECTED) {
                return 1;
            } else if (state1 == CandidateState.ELECTED && state2 == CandidateState.ELECTED) {
                for (int i = record.size() - 2; i >= 0; i--) {
                    state1 = record.get(i).getCandidateState(id1);
                    state2 = record.get(i).getCandidateState(id2);
                    if (state1 == CandidateState.ELECTED && state2 == CandidateState.ELECTED) {
                        continue;
                    } else if (state1 == CandidateState.ELECTED && state2 == CandidateState.HOPEFUL) {
                        return -1;
                    } else if (state1 == CandidateState.HOPEFUL && state2 == CandidateState.ELECTED) {
                        return 1;
                    } else if (state1 == CandidateState.HOPEFUL && state2 == CandidateState.HOPEFUL) {
                        return record.get(i + 1).getVote(id2).compareTo(record.get(i + 1).getVote(id1));
                    } else {
                        throw new IllegalArgumentException("Defeated candidate was elected");
                    }
                }
                return record.get(0).getVote(id2).compareTo(record.get(0).getVote(id1));
            } else if (state1 == CandidateState.DEFEATED && state2 == CandidateState.DEFEATED) {
                for (int i = record.size() - 2; i >= 0; i--) {
                    state1 = record.get(i).getCandidateState(id1);
                    state2 = record.get(i).getCandidateState(id2);
                    if (state1 == CandidateState.DEFEATED && state2 == CandidateState.DEFEATED) {
                        continue;
                    } else if (state1 == CandidateState.DEFEATED && state2 == CandidateState.HOPEFUL) {
                        return 1;
                    } else if (state1 == CandidateState.HOPEFUL && state2 == CandidateState.DEFEATED) {
                        return -1;
                    } else if (state1 == CandidateState.HOPEFUL && state2 == CandidateState.HOPEFUL) {
                        throw new IllegalArgumentException("Defeated more than one candidate in a single round");
                    } else {
                        throw new IllegalArgumentException("Elected candidate was defeated");
                    }
                }
                throw new IllegalArgumentException("Started with more than one defeated candidate");
            } else {
                throw new IllegalArgumentException("Finished with a hopeful candidate");
            }
        });
        int[] intordering = new int[numcandidates];
        for (int i = 0; i < numcandidates; i++) {
            intordering[i] = Intordering[i];
        }
        return intordering;
    }

    private static final Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 16);
    private static final int barwidth = 500; // width of each horizontal bar
    private static final int barheight = 16; // height of each horizontal bar
    private static final int barsep = 8; // space between horizontal bars
    private static final int padding = 4; // padding
    private static final int cssinpadding = 6; // space inside div
    private static final int cssoutpadding = 12; // space between div and img

    private static BufferedImage displayElectionState(int maxlen, String[] candidates, CandidateState[] states, Decimal[] votes, Decimal maxvote, Decimal quota) {
        int n = candidates.length;
        int w = barwidth + maxlen + 3 * (padding + 1);//one+padding+maxlen+padding+one+barwidth+padding+one
        int h = n * barheight + (n + 1) * barsep + 2 * (padding + 1);//one+padding+[bars]+padding+one
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics g = img.getGraphics();
        g.setFont(font);
        FontMetrics metrics = g.getFontMetrics();
        g.setColor(mgsa.GraphicsUtils.Grey);
        g.fillRect(0, 0, w, h);
        g.setColor(mgsa.GraphicsUtils.SoyBean);
        int pos = quota.divideup(maxvote).multiplyup(new Decimal(BigInteger.valueOf(barwidth))).val().intValue();
        g.fillRect(maxlen + 2 * padding + pos + 1, padding + 1, 2, h - 2 * padding - 2);
        g.setColor(mgsa.GraphicsUtils.Black);
        g.drawRect(0, 0, w - 1, h - 1);
        g.drawLine(maxlen + 2 * padding + 1, padding + 1, maxlen + 2 * padding + 1, h - padding - 2);
        for (int i = 0; i < n; i++) {
            if (states[i] == CandidateState.ELECTED) {
                g.setColor(mgsa.GraphicsUtils.CaliforniaGolf);
            } else if (states[i] == CandidateState.DEFEATED) {
                g.setColor(mgsa.GraphicsUtils.GoldenGate);
            } else {
                g.setColor(mgsa.GraphicsUtils.Lawrence);
            }
            int len = votes[i].divideup(maxvote).multiplyup(new Decimal(BigInteger.valueOf(barwidth))).val().intValue();
            g.fillRect(maxlen + 2 * padding + 1, i * barheight + (i + 1) * barsep + padding + 1, len + 1, barheight);
        }
        g.setColor(mgsa.GraphicsUtils.Black);
        for (int i = 0; i < n; i++) {
            int len = votes[i].divideup(maxvote).multiplyup(new Decimal(BigInteger.valueOf(barwidth))).val().intValue();
            g.drawRect(maxlen + 2 * padding + 1, i * barheight + (i + 1) * barsep + padding + 1, len, barheight - 1);
        }
        for (int i = 0; i < n; i++) {
            int len = metrics.stringWidth(candidates[i]);
            Rectangle rect = new Rectangle(maxlen + padding - len + 1, i * barheight + (i + 1) * barsep + padding + 1, len, barheight);
            mgsa.GraphicsUtils.drawCenterString(g, candidates[i], rect, false);
        }
        return img;
    }
}
