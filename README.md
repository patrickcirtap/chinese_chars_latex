# Chinese Characters to LaTeX

This program converts a list of Chinese (Mandarin) characters and their corresponding meaning(s) to a formatted PDF list.

The list is a `.txt` file where each character is formatted in the following structure:

```
<chinese character>
<English spelling>
<stroke count>
<tone number>
<LaTeX pinyin>
<definition>
...
<definition>
```

Example with annotations:

```
上       (the actual character)
shang    (the English spelling)
3        (how many strokes it takes to write)
4        (tone number 4)
sh\`ang  (the pinyin in LaTeX form)
上个星期  (defn. 1 - 上 can mean previous; as in "last week")
在桌子上  (defn. 2 - 上 can mean on top; as in "on top of the table")
```

Chinese has 