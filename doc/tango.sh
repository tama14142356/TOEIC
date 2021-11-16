#!/bin/bash
platex tango.tex
dvipdfmx tango.dvi
rm tango.aux
rm tango.log
rm tango.dvi
rm tango.tex
mv tango.pdf 金のフレーズ730単語間違えたリスト.pdf
rm tango.sh