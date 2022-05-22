# Chinese Characters to LaTeX

Convert a list of Chinese (Mandarin) characters and their corresponding meaning(s) to a formatted PDF list.

The list is a `.txt` file where each character is formatted in the following structure:

```
<empty line>
<chinese character>
<English spelling>
<stroke count>
<tone number>
<LaTeX pinyin>
<definition>
...
<definition>
<empty line>
```

Example with annotations:

```
          (empty line)
上        (the character)
shang     (the English spelling)
3         (how many strokes it takes to write)
4         (tone number 4)
sh\`ang   (the pinyin in LaTeX form; will produce an accented 'a')
上个星期   (defn. 1 - 上 can mean previous; as in "last week")
在桌子上   (defn. 2 - 上 can mean on top; as in "on top of the table")
          (empty line) 
```

Execute `./make.sh` to run.

`Main.java` will produce LaTeX output, which is piped into a `.tex` file. The `.tex` file is then compiled using `xelatex` to produce the PDFs.

2 PDFs are produced; one with definitions and one with just the characters.
