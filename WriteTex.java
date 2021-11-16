import java.io.BufferedWriter;
import java.io.FileOutputStream;
// import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

class WriteTex {
    private static final String OS_NAME = System.getProperty("os.name").toLowerCase();
    private static final String execTexstring[][] = { { "platex", ".tex" }, { "dvipdfmx", ".dvi" }, { "rm", ".aux" },
            { "rm", ".log" }, { "rm", ".dvi" }, { "rm", ".tex" }, { "mv", ".pdf" }, { "rm", ".sh" } };
    static final String origin = "tango";
    static final int iswindows = 0;
    static final int islinux = 1;
    static final int ismac = 2;
    static final int other = -1;
    private static Tango data;
    static String pdfname = "金のフレーズ730単語間違えたリスト.pdf";
    static int OSname = other;

    public static String replaceString(String target, String from, String to) {
        return target.replace(from, to);
    }

    // judge os
    public static boolean isLinux() {
        return OS_NAME.startsWith("linux");
    }

    public static boolean isMac() {
        return OS_NAME.startsWith("mac");
    }

    public static boolean isWindows() {
        return OS_NAME.startsWith("windows");
    }

    public static int getOSname() {
        if (OSname != other)
            return OSname;
        if (isLinux())
            return OSname = islinux;
        if (isWindows())
            return OSname = iswindows;
        if (isMac())
            return OSname = ismac;
        return other;
    }

    public static int exec(String[] command) {
        Process process = null;
        int exitcode = -1;
        System.out.println("start");
        try {
            Runtime rm = Runtime.getRuntime();
            process = rm.exec(command);
            exitcode = process.waitFor();
            InputStream is = process.getInputStream(); // プロセスの結果を変数に格納する
            BufferedReader br = new BufferedReader(new InputStreamReader(is)); // テキスト読み込みを行えるようにする
            System.out.println("start2");
            while (true) {
                String line = br.readLine();
                if (line == null) {
                    break; // 全ての行を読み切ったら抜ける
                } else {
                    System.out.println("line : " + line); // 実行結果を表示
                }
            }
            br.close();
            is.close();
            System.out.println("sucess!");
        } catch (IOException io) {
            io.printStackTrace();
            exitcode = -1;
        } catch (InterruptedException it) {
            it.printStackTrace();
            exitcode = -1;
        } finally {
            if (process != null && process.isAlive())
            process.destroy();
        }
        return exitcode;
    }

    public static void iniTex() {
        data = Tango.getInstance();
        try {
            OSname = getOSname();
            if (OSname == other)
                throw new IOException();
            if (OSname == iswindows) {
                int len = execTexstring.length;
                for (int i = 0; i < len; ++i) {
                    String command = execTexstring[i][0];
                    String suffix = execTexstring[i][1];
                    switch (command) {
                        case "rm":
                            execTexstring[i][0] = "del";
                            break;
                        case "mv":
                            execTexstring[i][0] = "move /y";
                            break;
                        default:
                            break;
                    }
                    switch (suffix) {
                        case ".sh":
                            execTexstring[i][1] = ".bat";
                            break;
                        default:
                            break;
                    }
                }
            }
        } catch (IOException io) {
            System.out.println("Unsupported OS!");
        }
    }

    public static void createexecFile(PrintWriter pw) {
        int len = execTexstring.length;
        for (int i = 0; i < len; ++i) {
            String cmd = execTexstring[i][0];
            String suffix = execTexstring[i][1];
            pw.print(cmd + " " + origin + suffix);
            if (!suffix.equals(".pdf"))
                pw.println();
            else
                pw.println(" " + pdfname);
        }
    }

    public static int execBat() {
        int exitcode = -1;
        PrintWriter execw = null;
        try {
            execw = new PrintWriter(
                    new BufferedWriter(new OutputStreamWriter(new FileOutputStream(origin + ".bat"), "Shift-JIS")));
            execw.println("@echo off");
            execw.println("cd /d %~dp0");
            createexecFile(execw);
            execw.close();
            exitcode = exec(new String[] { origin + ".bat" });
        } catch (IOException e) {
            e.printStackTrace();
            exitcode = -1;
        } finally {
            if (execw != null) {
                execw.close();
            }
        }
        return exitcode;
    }

    public static int execBash() {
        PrintWriter execw = null;
        int exitcode = -1;
        try {
            execw = new PrintWriter(
                    new BufferedWriter(new OutputStreamWriter(new FileOutputStream(origin + ".sh"), "UTF-8")));
            execw.println("#!/bin/bash");
            createexecFile(execw);
            execw.close();
            exitcode = exec(new String[] { "bash", origin + ".sh" });
        } catch (IOException e) {
            e.printStackTrace();
            exitcode = -1;
        } finally {
            if (execw != null) {
                execw.close();
            }
        }
        return exitcode;
    }

    public static int execDirectly() {
        int exitcode = -1;
        int len = execTexstring.length;
        for (int i = 0; i < len; ++i) {
            String command = execTexstring[i][0];
            String suffix = execTexstring[i][1];
            if (!suffix.equals(".pdf"))
                exitcode = exec(new String[] { command, origin + suffix });
            else
                exitcode = exec(new String[] { command, origin + suffix, pdfname });
            System.out.println(exitcode);
        }
        return exitcode;
    }

    public static int execLinux() {
        // return execBash();
        return execDirectly();
    }

    public static int execMac() {
        return execBash();
    }

    public static int execWindows() {
        return execBat();
    }

    public static int execCreateTex() {
        if (OSname == iswindows)
            return execWindows();
        if (OSname == islinux)
            return execLinux();
        if (OSname == ismac)
            return execMac();
        return -1;
    }

    public static int createTex(List<Map<Double, List<Word>>> wordlist) {
        iniTex();
        int exitcode = -1;
        PrintWriter bw = null;
        try {
            bw = new PrintWriter(
                    new BufferedWriter(new OutputStreamWriter(new FileOutputStream(origin + ".tex"), "UTF-8")));
            bw.println("\\documentclass{jsarticle}");
            bw.println("\\renewcommand{\\thesection}{第\\arabic{section}回目}");
            bw.println("\n\n\\title{\\Large 間違えた単語リスト}");
            bw.println("\\author{" + data.user + "}");
            bw.println("\n\\begin{document}");
            bw.println("\n\\maketitle\n");
            int cnt = wordlist.size();
            for (int i = 0; i < cnt; ++i) {
                bw.println("\\section{}");
                Map<Double, List<Word>> list = wordlist.get(i);
                double accuracy = list.keySet().toArray(new Double[0])[0];
                bw.println("The accuracy is " + String.valueOf(accuracy) + "\\%");
                List<Word> wronglist = list.get(accuracy);
                String blank = "(";
                String blanktex = "(";
                int wrongnum = wronglist.size();
                if (wrongnum > 0)
                    bw.println("\t\\begin{itemize}");
                for (Word word : wronglist) {
                    int index = word.idnum;
                    int size = word.question.length();
                    int start = 0, end = 0;
                    for (int j = 0; j < size; ++j) {
                        if (word.question.charAt(j) == '(')
                            start = j;
                        else if (word.question.charAt(j) == ')') {
                            end = j;
                            break;
                        }
                    }
                    blank = word.question.substring(start, end + 1);
                    blanktex = blank.substring(0, 2);
                    System.out.println(word.question + " | " + blanktex + " | " + blank + " |www");
                    blanktex += "\\hspace{20pt} )";
                    System.out.println(word.question + " | " + blanktex + " | " + blank);
                    String question = replaceString(word.question, blank, blanktex);
                    System.out.println(word.word + " " + word.meaning + " " + word.japanese + " " + word.question);
                    System.out.println(question);
                    bw.print("\t\t\\item[" + String.valueOf(index) + "]");
                    bw.println(" {\\bf " + word.word + "} \\hspace{20pt} {\\bf " + word.meaning + "} \\\\");
                    bw.println("\t\t" + word.japanese + "\\\\");
                    bw.println("\t\t" + question + "\\\\[1cm]\n");
                }
                if (wrongnum > 0)
                    bw.println("\t\\end{itemize}");
            }
            bw.println("\\end{document}");
            bw.close();

            // execution tex
            exitcode = execCreateTex();
            System.out.println(exitcode);
        } catch (IOException io) {
            io.printStackTrace();
            exitcode = -1;
        } finally {
            if (bw != null)
                bw.close();
        }
        return exitcode;
    }
}