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
import mgsa.GraphicsUtils;

public class DisplayElection {

    public static void displayElection(int numseats, String[] candidates, List<ElectionState> record) throws IOException {
        String timestring = "Spring 2022";
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
            BufferedImage img = displayElectionState(reorderedcandidates, states, votes, maxvote, quota);
            ImageIO.write(img, "png", new File("C:\\Users\\thoma\\website\\election\\round" + round + ".png"));
            round++;
        }

        List<String> sb = new ArrayList<>();
        Path file = Paths.get("C:\\Users\\thoma\\website\\election\\election.html");
        sb.add("<html>");
        sb.add("<head>");
        sb.add("<title>" + timestring + " MGSA Election</title>");
        sb.add("<style>");
        sb.add("body {font-family: sans-serif; background-color: #003262;}");
        sb.add("</style>");
        sb.add("</head>");
        sb.add("<body>");
        sb.add("<h1 style=\"text-align:center;color:#FDB515;\">" + timestring + " MGSA Election</h1>");
        sb.add("</body>");
        sb.add("</html>");
        Files.write(file, sb, StandardCharsets.UTF_8);

        System.exit(0);
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
                        return record.get(i+1).getVote(id2).compareTo(record.get(i+1).getVote(id1));
                    } else {
                        throw new IllegalArgumentException("Defeated candidate was elected");
                    }
                }
                return id1 - id2;
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

    private static BufferedImage displayElectionState(String[] candidates, CandidateState[] states, Decimal[] votes, Decimal maxvote, Decimal quota) {
        int n = candidates.length;
        FontMetrics metrics = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB).getGraphics().getFontMetrics(font);
        int maxlen = 0;
        for (String candidate : candidates) {
            int len = metrics.stringWidth(candidate);
            if (len > maxlen) {
                maxlen = len;
            }
        }
        int w = barwidth + maxlen + 3 * (padding + 1);//one+padding+maxlen+padding+one+barwidth+padding+one
        int h = n * barheight + (n + 1) * barsep + 2 * (padding + 1);//one+padding+[bars]+padding+one
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics g = img.getGraphics();
        g.setFont(font);
        g.setColor(GraphicsUtils.Grey);
        g.fillRect(0, 0, w, h);
        g.setColor(GraphicsUtils.SoyBean);
        int pos = quota.divideup(maxvote).multiplyup(new Decimal(BigInteger.valueOf(barwidth))).val().intValue();
        g.fillRect(maxlen + 2 * padding + pos + 1, padding + 1, 2, h - 2 * padding - 2);
        g.setColor(GraphicsUtils.Black);
        g.drawRect(0, 0, w - 1, h - 1);
        g.drawLine(maxlen + 2 * padding + 1, padding + 1, maxlen + 2 * padding + 1, h - padding - 2);
        for (int i = 0; i < n; i++) {
            if (states[i] == CandidateState.ELECTED) {
                g.setColor(GraphicsUtils.CaliforniaGolf);
            } else if (states[i] == CandidateState.DEFEATED) {
                g.setColor(GraphicsUtils.GoldenGate);
            } else {
                g.setColor(GraphicsUtils.Lawrence);
            }
            int len = votes[i].divideup(maxvote).multiplyup(new Decimal(BigInteger.valueOf(barwidth))).val().intValue();
            g.fillRect(maxlen + 2 * padding + 1, i * barheight + (i + 1) * barsep + padding + 1, len + 1, barheight);
        }
        g.setColor(GraphicsUtils.Black);
        for (int i = 0; i < n; i++) {
            int len = votes[i].divideup(maxvote).multiplyup(new Decimal(BigInteger.valueOf(barwidth))).val().intValue();
            g.drawRect(maxlen + 2 * padding + 1, i * barheight + (i + 1) * barsep + padding + 1, len, barheight - 1);
        }
        for (int i = 0; i < n; i++) {
            int len = metrics.stringWidth(candidates[i]);
            Rectangle rect = new Rectangle(maxlen + padding - len + 1, i * barheight + (i + 1) * barsep + padding + 1, len, barheight);
            GraphicsUtils.drawCenteredString(g, candidates[i], rect, false);
        }
        return img;
    }
}
