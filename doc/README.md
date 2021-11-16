# 本ソフトウェアの動作
## 大枠
内蔵されている英単語リストから無作為に抽出し、穴埋めをする問題を解くもの。  
一度にたくさん解答することもできるが、連続して回答することも可能。 
## 詳細
### 名前入力
起動した時のみ入力可能で、後で出力されるpdfファイルの名前欄に記載される名前を入力することができる。  
下の欄に名前を入力し、名前入力ボタンを押すと問題数選択画面に遷移する。
### 問題数入力 
解答する問題数を下の欄に入力して、確定ボタンを押すと問題が開始される。
### 問題解答
穴埋めに適切な英単語を入力して、解答ボタンを押すと正解が表示され、次へボタンを押すと次の問題に遷移する。
### 解答終了
指定した問題数に達したら、今までの結果が表示され、次へボタンを押すともう一度問題を解くか終了するか選ぶポップアップ画面が表示される。
### ソフトウェア終了
解答終了ボタンを選択すると今までの間違えた問題が解答した回数ごとに表示されるpdfファイルが生成される。

# 動作保証OS
windows, mac, linux
但し、完全に動作確認できているのはwindowsのみ  
linuxに関しては一部動作確認済み  
macに関しては動作確認していない

# 実行コマンド
```
$ java -jar Tango.jar
```
windowsであればダブルクリックでも起動可  
その他のOSに関しては動作確認していない

# 生成ファイル
## 金のフレーズ730単語間違えたリスト.pdf
間違えた単語のリストをpdfファイルにまとめたものである。
## tango.bat(windows) または tango.sh(linux, mac)
texを実行するためのコマンド群が記載されているシェルスクリプト(但し、削除される)
## tango.tex
pdfファイルを生成するためのtexファイル(但し、削除される)

# 内蔵ファイル
1. tango.txt: 単語リスト  
2. javaソースコード群:  
    2-1. Tango.java : メインクラス, ファイル読み込み ウインドウ起動  
    2-2. TestWindow.java : ウインドウクラス, クリックイベント処理  
    2-3. WriteTex.java : Texファイル作成クラス, 一番最後にのみ呼び出す。  
    2-4. Word.java : 英単語クラス, 英単語を保持し,　問題文,解答例, 単語の意味と日本語のセットの表示などを行う。
3. classファイル群(実行ファイル)
4. backgroud.JPG(背景に使おうとした画像)  
5. tango.bat : 見本のバッチファイル(encodingはShift-JIS)  
6. tango.sh : 見本のシェルファイル(encodingはUTF-8)
7. MANIFEST.MF : メインクラス名が書かれているファイル, jarファイルを起動するために必要なファイル

# 動作確認環境
- Windows10 pro 64bit    
- java
```
$ java --version
java 12.0.1 2019-04-16
Java(TM) SE Runtime Environment (build 12.0.1+12)
Java HotSpot(TM) 64-Bit Server VM (build 12.0.1+12, mixed mode, sharing)
$ javac --version
javac 12.0.1
$ jar --version
jar 12.0.1
```
- Texディストリビューション: Texlive2019(windows用)
- Tex各種バージョン

```
$ platex --version
e-pTeX 3.14159265-p3.8.2-190131-2.6 (utf8.sjis) (TeX Live 2019/W32TeX)
kpathsea version 6.3.1
ptexenc version 1.3.7
Copyright 2019 D.E. Knuth.
There is NO warranty.  Redistribution of this software is
covered by the terms of both the e-pTeX copyright and
the Lesser GNU General Public License.
For more information about these matters, see the file
named COPYING and the e-pTeX source.
Primary author of e-pTeX: Peter Breitenlohner.
$ dvipdfmx --version
This is dvipdfmx Version 20191114 by the DVIPDFMx project team,
modified for TeX Live,
an extended version of dvipdfm-0.13.2c developed by Mark A. Wicks.

Copyright (C) 2002-2019 the DVIPDFMx project team
Copyright (C) 2006-2019 SIL International.

This is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.
```