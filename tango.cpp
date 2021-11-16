#include <bits/stdc++.h>
using namespace std;

struct Word
{
	string question, japanese, word, meaning;
	string mainword, answerword;
};

string replaceOtherStr(string &replaceStr, string from, string to)
{
	const unsigned int pos = replaceStr.find(from);
	const int len = from.length();

	if (pos == string::npos || from.empty())
	{
		return replaceStr;
	}
	return replaceStr.replace(pos, len, to);
}

int main()
{
	string filename = "./tango.txt";
	string ansname = "./correctanswer.txt";
	string texname = "./tango";
	ifstream ifs;
	ofstream ofs;
	ofstream texofs;
	ifstream ansifs;

	ifs.open(filename, ios::in);
	ansifs.open(ansname, ios::in);
	if (ifs.fail())
	{
		cerr << "Failed to open " << filename << endl;
		return -1;
	}
	if (ifs.fail())
	{
		cerr << "Failed to open " << ansname << endl;
		return -1;
	}
	Word str;
	string answer;
	vector<Word> vecstr;
	vector<Word> wronganswer;
	while (getline(ifs, str.word))
	{
		str.mainword = str.word;
		str.answerword = str.word;
		for (int i = 0; i < str.word.length(); i++)
		{
			if (str.word[i] == ' ')
			{
				str.mainword = str.word[0];
				int j;
				for (j = 1; str.word[j] != ' '; j++)
				{
					str.mainword += str.word[j];
				}
				j++;
				str.answerword = str.word[j];
				j++;
				for (; j < str.word.length(); j++)
				{
					str.answerword += str.word[j];
				}
			}
		}
		getline(ifs, str.meaning);
		getline(ifs, str.japanese);
		getline(ifs, str.question);
		if (str.question != " ")
			vecstr.push_back(str);
	}
	bool flag[vecstr.size()];
	bool ansflag[vecstr.size()];
	for (int i = 0; i < vecstr.size(); i++)
	{
		flag[i] = true;
		ansflag[i] = false;
	}
	cout << "Include correctly answered question?(y/n) : ";
	string yn;
	string num;
	int number;
	cin >> yn;
	while (yn != "y" && yn != "n")
	{
		cout << "Please answer y or n : ";
		cin >> yn;
	}
	int loopcounter = vecstr.size();
	if (yn == "y")
	{
		while (getline(ansifs, num))
		{
			number = atoi(num.c_str());
			flag[number] = false;
			ansflag[number] = true;
			loopcounter--;
		}
	}
	else
	{
		while (getline(ansifs, num))
		{
			number = atoi(num.c_str());
			ansflag[number] = true;
		}
	}
	srand(time(NULL));
	int rnd;
	string loop;
	int loopcount = 0;
	while (loopcount == 0)
	{
		cout << "enter the number of words (1 ~" << loopcounter << ") : ";
		cin >> loop;
		for (int i = 0; i < loop.length(); i++)
		{
			if (loop[i] < 48 || loop[i] > 57)
				loopcount = -1;
		}
		if (loopcount == -1)
			loopcount = 0;
		else
			loopcount = atoi(loop.c_str());
	}
	while (loopcount > loopcounter)
	{
		cout << "number is too large" << endl;
		loopcount = 0;
		while (loopcount == 0)
		{
			cout << "enter the number of words again (1 ~" << loopcounter << ") : ";
			cin >> loop;
			for (int i = 0; i < loop.length(); i++)
			{
				if (loop[i] < 48 || loop[i] > 57)
					loopcount = -1;
			}
			if (loopcount == -1)
				loopcount = 0;
			else
				loopcount = atoi(loop.c_str());
		}
	}
	for (int i = 0; i < loopcount; i++)
	{
		rnd = rand() % vecstr.size();
		while (!flag[rnd])
		{
			rnd = rand() % (vecstr.size());
		}
		flag[rnd] = false;
		str = vecstr[rnd];
		cout << "fill in the blank." << endl;
		cout << str.japanese << endl;
		cout << str.question << endl;
		cout << "answer is : ";
		cin >> answer;
		cout << "\x1b[31m";
		if (answer == str.answerword)
		{
			cout << endl
				 << "correct!" << endl
				 << endl;
			ansflag[rnd] = true;
		}
		else
		{
			cout << endl
				 << "it's wrong." << endl
				 << endl;
			wronganswer.push_back(str);
			ansflag[rnd] = false;
		}
		cout << "\x1b[39m";
		if (str.answerword == str.mainword)
			cout << str.word << endl;
		else
			cout << str.mainword << "  (" << str.answerword << ")" << endl;
		cout << str.meaning << endl
			 << endl
			 << endl
			 << endl;
	}
	if (wronganswer.size() != 0)
		cout << "---------------------------" << endl;
	if (wronganswer.size() != 0)
		cout << "show wrong words below." << endl;
	for (int i = 0; i < wronganswer.size(); i++)
	{
		cout << wronganswer[i].mainword << endl;
		cout << wronganswer[i].meaning << endl
			 << endl;
	}
	cout << "The accuracy rate is " << (double)100 * (loopcount - wronganswer.size()) / loopcount << "%" << endl;
	ofs.open(ansname, ios::trunc);
	for (int i = 0; i < vecstr.size(); i++)
	{
		if (ansflag[i])
			ofs << i << endl;
	}
	cout << "いままでに間違えた(解いていない)問題の番号一覧" << endl;
	for (int i = 0; i < vecstr.size(); i++)
	{
		if (!ansflag[i])
			cout << i + 401 << ", ";
	}
	cout << endl
		 << endl;
	cout << "間違えた問題一覧を表示しますか？(y/n) : ";
	cin >> yn;
	while (yn != "y" && yn != "n")
	{
		cout << "Please answer y or n : ";
		cin >> yn;
	}
	if (yn == "y")
	{
		string changestr;
		string firststr;
		cout << endl;
		texofs.open(texname + ".tex", ios::trunc);
		texofs << "\\documentclass{jsarticle}" << endl;
		texofs << "\\begin{document}" << endl;
		texofs << "\\noindent {\\Large 間違えた単語リスト}\\" << endl;
		texofs << "\\begin{itemize}" << endl;
		for (int i = 0; i < vecstr.size(); i++)
		{
			if (!ansflag[i])
			{
				for (int j = 0; j < vecstr[i].question.length(); j++)
				{
					if (vecstr[i].question[j] == '(')
					{
						changestr = "(";
						changestr += vecstr[i].question[j + 1];
						firststr = "";
						firststr += vecstr[i].question[j + 1];
						changestr += "   )";
						break;
					}
				}
				cout << vecstr[i].word << " " << vecstr[i].meaning << endl
					 << vecstr[i].japanese << endl
					 << vecstr[i].question << endl
					 << endl;
				replaceOtherStr(vecstr[i].question, changestr, "(" + firststr + "\\hspace{20pt} )");
				texofs << "\\item[" << i + 401 << "]"
					   << "{\\bf " << vecstr[i].word << "} \\hspace{20pt} {\\bf " << vecstr[i].meaning << "} \\\\" << endl;
				texofs << vecstr[i].japanese << "\\\\" << endl
					   << vecstr[i].question << "\\\\[1cm]" << endl;
			}
		}
		texofs << "\\end{itemize}" << endl;
		texofs << "\\end{document}";
		texofs.close();
		system("platex tango.tex");
		system("dvipdfmx tango.dvi");
		system("rm tango.tex");
		system("rm tango.aux");
		system("rm tango.log");
		system("rm tango.dvi");
		system("mv tango.pdf 金のフレーズ730単語間違えたリスト.pdf");
	}
	return 0;
}
