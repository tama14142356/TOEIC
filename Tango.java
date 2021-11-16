import java.util.List;
import java.util.ArrayList;
// import java.util.LinkedHashMap;
import java.util.Scanner;
import java.util.Random;
import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;
// import java.io.IOException;
// import java.io.File;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

class Tango {
    static Tango instance = null;
    static final int islevel600 = 0;
    static final int islevel730 = 1;
    static final int islevel860 = 2;
    static final int islevel990 = 3;
    static final int islevelall = 4;
    static final Map<Integer, Integer> wordnum = new HashMap<Integer, Integer>() {
        {
            put(600, 1);
            put(730, 401);
            put(860, 701);
            put(990, 901);
        }
    };
    TestWindow window;
    List<Word> wronganswerlist = new ArrayList<Word>();
    List<Word> wordlist = new ArrayList<Word>();
    Map<Integer, List<Word>> wordlists = new TreeMap<Integer, List<Word>>();
    String keylist[] = null;
    int loopcount = 0;
    int curpromnum = 0;
    int level = islevel730;
    Random random;
    boolean flag[];
    boolean ansflag[];
    String user;
    boolean israndom;
    boolean isadmin;

    public boolean setWordlist(int level) {
        if (!wordlists.containsKey(level))
            return false;
        wordlist.addAll(wordlists.get(level));
        System.out.println(wordlist.size() + " size " + wordlists.get(level).size() + " size ");
        return true;
    }

    public boolean clearWordlist() {
        if (wordlist == null)
            return false;
        wordlist.clear();
        return true;
    }

    public boolean setLoopcount() {
        loopcount = window.getNumber();
        if (loopcount < 1)
            return false;
        return true;
    }

    public boolean setName() {
        user = window.username;
        // Scanner sc = new Scanner(user);
        // String admintest = "";
        System.out.println(user);
        if (user.length() <= 0)
            return false;
        return true;
    }

    public void judge(Word prob) {
        String answer = window.getUserAnswer();
        // int index = prob.idnum;
        // boolean iscorrect = true;
        window.ReceiveMessage("\nyour answer\n");
        window.ReceiveMessage(answer + "\n");
        window.ReceiveMessage(prob.judge(answer) + "\n");
        if (!prob.answerword.equals(answer)) {
            wronganswerlist.add(prob);
            // iscorrect = false;
        }
        // ansflag[index] = iscorrect;
        window.setName("第" + String.valueOf(curpromnum + 1) + "問　解答結果");
        window.ReceiveMessage(prob.showAnswer());
        curpromnum++;
    }

    public void createProblem() {
        int size = wordlist.size();
        int rnd = 0;
        if (israndom) {
            rnd = random.nextInt(size);
            while (flag[rnd]) {
                rnd = random.nextInt(size);
            }
            System.out.println("random " + rnd);
        } else {
            while (flag[rnd]) {
                rnd++;
            }
            System.out.println("current num " + rnd);
        }
        flag[rnd] = true;
        Word prob = new Word(wordlist.get(rnd));
        window.showQuestion(prob, curpromnum + 1);
    }

    public void showResult() {
        window.setName("テスト結果");
        window.clearchatRoom();
        window.showResult(wronganswerlist);
    }

    public void resetGUI() {
        loopcount = 0;
        curpromnum = 0;
        if (wronganswerlist != null)
            wronganswerlist.clear();
        // wronganswerlist = new ArrayList<Word>();
        int size = wordlist.size();
        flag = new boolean[size];
        window.clearchatRoom();
        window.setName("問題数決定");
        window.ReceiveMessage("enter the number of words (1 ~" + size + ") : ");
    }

    private int initGUI(String filename) {
        // String filename = "tango.txt";
        // String filename = "test.txt";
        List<Word> tmplist = new ArrayList<Word>();
        Scanner sc = null;
        int tmplevel = -1, ids = -1;
        try {
            // sc = new Scanner(new File(filename));
            sc = new Scanner(new InputStreamReader(getClass().getResourceAsStream(filename), "UTF-8"));
            if (sc.hasNext()) {
                tmplevel = Integer.valueOf(sc.nextLine());
                if (!wordnum.containsKey(tmplevel))
                    throw new NumberFormatException();
            }
            ids = wordnum.get(tmplevel);
            while (sc.hasNextLine()) {
                Word word = new Word();
                String input = sc.nextLine();
                word.answerword = input.substring(0);
                word.word = input.substring(0);
                Scanner in = new Scanner(input);
                String[] tmp = new String[2];
                int i = 0;
                while (in.hasNext())
                    tmp[i++] = in.next();
                word.mainword = tmp[0];
                in.close();
                if (i >= 2)
                    word.answerword = tmp[1];
                if (sc.hasNextLine())
                    word.meaning = sc.nextLine();
                if (sc.hasNextLine())
                    word.japanese = sc.nextLine();
                if (sc.hasNextLine()) {
                    word.question = sc.nextLine();
                    int id = tmplist.size();
                    word.idnum = ids + id;
                    tmplist.add(word);
                }
            }
            sc.close();
            // ansflag = new boolean[tmplist.size()];
            wordlists.put(tmplevel, tmplist);
            // resetGUI();
        } catch (NullPointerException e) {
            System.out.println("File is not found");
            tmplevel = -1;
        } catch (UnsupportedEncodingException en) {
            System.out.println("unsupported encoding");
            tmplevel = -1;
        } catch (NumberFormatException nume) {
            System.out.println("unsupported level or unsupported format type");
            tmplevel = -1;
        } finally {
            if (sc != null)
                sc.close();
        }
        return tmplevel;
    }

    private void initGUI() {
        // initGUI("tango.txt");
        // wordlist.addAll(wordlists.get(730));
        String filenamelist[] = { "tango.txt", "tango2.txt", "tango3.txt", "tango4.txt" };
        int tmplevel = -1;
        for (String filename : filenamelist) {
            tmplevel = initGUI(filename);
            if (tmplevel != -1)
                wordlist.addAll(wordlists.get(tmplevel));
        }
        if (wordlists.isEmpty())
            System.exit(1);
        int size = wordlists.keySet().size();
        if (size > 1) {
            size++;
            keylist = new String[size];
            int i = 0;
            for (int key : wordlists.keySet()) {
                keylist[i] = String.valueOf(key);
                ++i;
            }
            keylist[i] = "全部";
        }
    }

    public static Tango getInstance() {
        if (instance == null) {
            instance = new Tango();
        }
        return instance;
    }

    private Tango() {
        israndom = true;
        isadmin = false;
        random = new Random();
        initGUI();
    }

    public static void main(String args[]) {
        instance = new Tango();
        instance.window = TestWindow.getInstance();
        instance.window.setName("名前登録");
        instance.window.ReceiveMessage("Enter your name\n");
        instance.window.Showwindow();
    }
}