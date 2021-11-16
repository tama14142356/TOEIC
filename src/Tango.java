import java.util.*;
import java.io.*;

class Tango {
    static Tango instance = null;
    TestWindow window;
    List<Word> wronganswerlist;
    List<Word> wordlist = new ArrayList<Word>();
    int loopcount = 0;
    int curpromnum = 0;
    Random random;
    boolean flag[];
    boolean ansflag[];
    String user;

    public boolean setLoopcount() {
        loopcount = window.getNumber();
        if (loopcount < 1)
            return false;
        return true;
    }

    public boolean setName() {
        user = window.username;
        if (user.length() <= 0)
            return false;
        return true;
    }

    public void judge(Word prob) {
        String answer = window.getUserAnswer();
        int index = wordlist.indexOf(prob);
        boolean iscorrect = true;
        window.ReceiveMessage("your answer\n");
        window.ReceiveMessage(answer + "\n");
        window.ReceiveMessage(prob.judge(answer) + "\n");
        if (!prob.answerword.equals(answer)) {
            wronganswerlist.add(prob);
            iscorrect = false;
        }
        ansflag[index] = iscorrect;
        window.setName("第" + String.valueOf(curpromnum + 1) + "問　解答結果");
        window.ReceiveMessage(prob.showAnswer());
        curpromnum++;
    }

    public void createProblem() {
        int size = wordlist.size();
        int rnd = random.nextInt(size);
        while (flag[rnd]) {
            rnd = random.nextInt(size);
        }
        System.out.println("random " + rnd);
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
        wronganswerlist = new ArrayList<Word>();
        int size = wordlist.size();
        flag = new boolean[size];
        window.clearchatRoom();
        window.setName("問題数決定");
        window.ReceiveMessage("enter the number of words (1 ~" + size + ") : ");
    }

    public void initGUI() {
        String filename = "tango.txt";
        // String filename = "test.txt";
        Scanner sc = null;
        try {
            // sc = new Scanner(new File(filename));
            sc = new Scanner(new InputStreamReader(getClass().getResourceAsStream(filename), "UTF-8"));
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
                    int id = wordlist.size();
                    word.idnum = 401 + id;
                    wordlist.add(word);
                }
            }
            sc.close();
            ansflag = new boolean[wordlist.size()];
            // resetGUI();
        } catch (NullPointerException e) {
            System.out.println("File is not found");
        } catch (UnsupportedEncodingException en) {
            System.out.println("unsupported encoding");
        } finally {
            sc.close();
        }
    }

    public static Tango getInstance() {
        if (instance == null) {
            instance = new Tango();
        }
        return instance;
    }

    private Tango() {
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