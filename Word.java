class Word {
    String question, japanese, word, meaning;
    String mainword, answerword;
    int idnum;
    static final String judgestring[] = { "correct!", "it's wrong." };

    public boolean equals(Object obj) {
        Word tc = (Word)obj;
        String tcmainword = tc.mainword;
        return mainword.equals(tcmainword);
    }

    public String judge(String ans) {
        int a = 1;
        if (answerword.equals(ans))
            a = 0;
        return judgestring[a];
    }

    public String showQuestion() {
        StringBuffer ans = new StringBuffer("fill in the blanck.\n");
        ans.append(japanese + "\n");
        ans.append(question + "\n");
        return ans.toString();
    }

    public String showAnswer() {
        StringBuffer ans = new StringBuffer("No." + String.valueOf(idnum) + " " +  mainword + "\n");
        if (!mainword.equals(answerword))
            ans.append("(" + answerword + ")\n");
        ans.append(meaning);
        return ans.toString();
    }

    public String showWord() {
        StringBuffer ans = new StringBuffer("No." + String.valueOf(idnum) + " " +  mainword + "\n");
        ans.append(meaning + "\n");
        return ans.toString();
    }

    public Word(Word word) {
        question = word.question.substring(0);
        answerword = word.answerword.substring(0);
        japanese = word.japanese.substring(0);
        mainword = word.mainword.substring(0);
        meaning = word.meaning.substring(0);
        idnum = word.idnum;
        if (word.word != null)
            this.word = word.word.substring(0);
    }

    public Word(){}
}