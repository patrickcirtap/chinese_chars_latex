import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.io.BufferedReader;
import java.io.Reader;
import java.io.FileReader;
import java.io.IOException;

public class Main
{
    /**
     * Generates a sorted list of all characters parsed in as a text list.
     * After parsing the list, this functions sorts it, before returning.
     * See "CharacterObject.java" for sorting details.
     *
     * @return - populated, sorted list of all characters
     */
    public static List<CharacterObject> populate_list()
    {
        List<CharacterObject> all_chars = new ArrayList<CharacterObject>();
        CharacterObject chr_obj;
        String line;
        int i;

        /* character attributes */
        String chr_sym = "";
        String eng     = "";
        int stk_c      = 0;
        int stk_n      = 0;
        String pin     = "";
        ArrayList<String> defns = new ArrayList<String>();

        try(BufferedReader br = new BufferedReader(new FileReader("list.txt")))
        {
            i = 0;
            while((line = br.readLine()) != null)
            {
                /* empty line means we have a new character to be added */
                if( line.equals("") )
                {
                    /* ignore very firstloop iteration */
                    if( !chr_sym.equals("") )
                    {
                        /* make new character and add to list */
                        chr_obj = new CharacterObject(chr_sym, eng, stk_c, stk_n, pin, defns);
                        all_chars.add(chr_obj);
                        /* reset variable for next character */
                        line    = "";
                        chr_sym = "";
                        eng     = "";
                        stk_c   = 0;
                        stk_n   = 0;
                        pin     = "";
                        defns.clear();
                        i = 0;
                    }
                }
                else if( line.charAt(0) == '/' )
                {
                    /* ignore comment line */
                }
                /* otherwise, grab new character attribute */
                else
                {
                    switch(i)
                    {
                        case 0:
                            chr_sym = line;
                            break;
                        case 1:
                            eng = line;
                            break;
                        case 2:
                            try { stk_c = Integer.parseInt(line); }
                            catch(NumberFormatException e) { System.out.println("str to int error: " + e); System.exit(1); }
                            break;
                        case 3:
                            try { stk_n = Integer.parseInt(line); }
                            catch(NumberFormatException e) { System.out.println("str to int error: " + e); System.exit(1); }
                            break;
                        case 4:
                            pin = line;
                            break;
                        default:
                            defns.add(line);
                            break;
                    }
                    i++;
                }
            }
            /* last character in list */
            if( !chr_sym.equals("") )
            {
                /* make new character and add to list */
                chr_obj = new CharacterObject(chr_sym, eng, stk_c, stk_n, pin, defns);
                all_chars.add(chr_obj);
            }
        } catch(IOException ex) { System.out.println("File Read Error: " + ex); System.exit(1); }
        
        Collections.sort(all_chars);

        return all_chars;
    }


    /**
     * Print sorted list of characters WITHOUT definitions
     *
     * @param  - all_chars: sorted list of character objects
     *
     * @return - prints sorted list of characters
    */
    public static void print_list_without_defns(List<CharacterObject> all_chars)
    {
        /* START: preceeding LaTeX code */
        System.out.println("\\documentclass[12pt]{article}");
        /* user flags */
        System.out.println();
        System.out.println("% toggles for extra information:");
        System.out.println("% letters");
        System.out.println("\\newif\\ifletter");
        System.out.println("\\lettertrue");
        System.out.println("%\\letterfalse");
        System.out.println("% characters");
        System.out.println("\\def\\rowchr#1\\\\{#1\\\\} % on");
        System.out.println("%\\def\\rowchr#1\\\\{} % off");
        System.out.println("% pinyin");
        System.out.println("\\def\\rowpin#1\\\\{#1\\\\} % on");
        System.out.println("%\\def\\rowpin#1\\\\{} % off");
        System.out.println("% meaning(s) / definition(s)");
        System.out.println("%\\def\\rowdef#1\\\\{#1\\\\} % on");
        System.out.println("\\def\\rowdef#1\\\\{} % off");
        /* usepackages */
        System.out.println();
        System.out.println("\\usepackage[a4paper, margin = 15mm, tmargin = 20mm]{geometry}");
        System.out.println("\\usepackage{changepage}");
        System.out.println("\\usepackage{xeCJK}");
        System.out.println("\\usepackage{array}");
        System.out.println("\\newcolumntype{C}[1]{>{\\centering\\let\\newline\\\\\\arraybackslash\\hspace{0pt}}m{#1}}");
        System.out.println();
        System.out.println("\\pagenumbering{gobble}");
        System.out.println("\\setCJKmainfont[Scale=1.5]{Noto Sans CJK SC}");
        System.out.println();
        System.out.println("\\newcommand{\\nl}{\\newline}");
        System.out.println();
        System.out.println("\\begin{document}");
        System.out.println();
        /* END: preceeding LaTeX code */

        int chars_per_line  = 10;
        int indent_size     = 4;
        int number_of_chars = all_chars.size();
        int i, j, k;
        /* number of spaces between each "table column" */
        int column_spacing  = 0;
        String SPACE_FILLER = " ";
        int max_defn_length = 0;
        int max_pin_length  = 0;
        ArrayList<String> defns = new ArrayList<String>();

        /* part 1: find longest: definition OR pinyin to decide COLUMN_SPACING value */
        for(i = 0; i < number_of_chars; i++)
        {
            /* first - check pinyin length */
            if( all_chars.get(i).get_pinyin().length() > max_pin_length )
            {
                max_pin_length = all_chars.get(i).get_pinyin().length();
            }
            
            /* second - check max defn length */
            defns = all_chars.get(i).get_definitions();
            for(j = 0; j < defns.size(); j++)
            {
                if(max_defn_length < defns.get(j).length())
                {
                    max_defn_length = defns.get(j).length();
                }
            }
        }
        
        if( (max_defn_length*2) >= max_pin_length )
        {
            column_spacing = (max_defn_length * 2) + 1;
        }
        else
        {
            column_spacing = max_pin_length + 1;
        }

        /* part 2: produce list with even column-spacing */
        /* handfull of characters for current table row - will have up to chars_per_line characters */
        List<CharacterObject> chars_bunch = new ArrayList<CharacterObject>();
        /* in a character bunch: the max number of defns sentences */
        int max_number_of_defns;
        String[][] defns_bunch;
        int chars_bunch_size;
        String current_print_string;
        /* current index of all characters */
        int char_index = 0;
        int current_letter;
        /* for each letter; {A, B, C, ..., X, Y, Z} */
        for(current_letter = 0; current_letter < 26; current_letter++)
        {
            System.out.print("\\begin{tabular}{ ");
            for(i = 0; i < chars_per_line; i++)
            {
                System.out.print("C{7ex} ");
            }
            System.out.println("}");
            System.out.println("\\hspace{-13ex} \\ifletter {\\large " + (char)(65 + current_letter) + "} \\else \\fi \\\\");

            /* for each char that starts with the current letter (for each char_bunch) */
            while( (char_index < number_of_chars) &&
                   (all_chars.get(char_index).get_english().charAt(0) == (char)(97 + current_letter)) )
            {
                chars_bunch.clear();
                max_number_of_defns = 0;
                /* grab the next [1 to chars_per_line] amount of characters */
                i = 0;
                while( (char_index < number_of_chars) &&
                       (i < chars_per_line) &&
                       (all_chars.get(char_index).get_english().charAt(0) == (char)(97 + current_letter)) )
                {
                    chars_bunch.add(all_chars.get(char_index));
                    if( all_chars.get(char_index).get_definitions().size() > max_number_of_defns )
                    {
                        max_number_of_defns = all_chars.get(char_index).get_definitions().size();
                    }
                    i++;
                    char_index++;
                }
                chars_bunch_size = chars_bunch.size();

                /* fill defns array with either defns or nothing */
                defns_bunch = new String[max_number_of_defns][chars_bunch_size];
                /* for each character in bunch */
                for(i = 0; i < chars_bunch_size; i++)
                {
                    /* for each defn in character */
                    for(j = 0; j < max_number_of_defns; j++)
                    {
                        /* if this character still has more defns */
                        if( j < (chars_bunch.get(i).get_definitions().size()) )
                        {
                            defns_bunch[j][i] = chars_bunch.get(i).get_definitions().get(j);
                        }
                        /* else, no defns left - add nothing */
                        else
                        {
                            defns_bunch[j][i] = "";
                        }
                    }
                }

                /* print the current bunch of characters */
                /* 1. print character symbol */
                for(i = 0; i < indent_size; i++)
                {
                    System.out.print(SPACE_FILLER);
                }
                System.out.print("\\rowchr ");
                for(i = 0; i < chars_bunch_size; i++)
                {
                    System.out.print(chars_bunch.get(i).get_chr_sym());
                    for(j = 0; j < column_spacing - 2; j++)
                    {
                        System.out.print(SPACE_FILLER);
                    }
                    if( i < (chars_bunch_size - 1) )
                    {
                        System.out.print("&" + SPACE_FILLER);
                    }
                }
                for(j = 0; j < ((chars_per_line - chars_bunch_size) * (column_spacing + 2)); j++)
                {
                    System.out.print(SPACE_FILLER);
                }
                System.out.println("\\\\");
                for(i = 0; i < indent_size; i++)
                {
                    System.out.print(SPACE_FILLER);
                }
                System.out.print("\\rowpin ");
                /* 2. print pinyin */
                for(i = 0; i < chars_bunch_size; i++)
                {
                    current_print_string = chars_bunch.get(i).get_pinyin();
                    System.out.print(current_print_string);
                    for(j = 0; j < (column_spacing - current_print_string.length()); j++)
                    {
                        System.out.print(SPACE_FILLER);
                    }
                    if( i < (chars_bunch_size - 1) )
                    {
                        System.out.print("&" + SPACE_FILLER);
                    }
                }
                for(j = 0; j < ((chars_per_line - chars_bunch_size) * (column_spacing + 2)); j++)
                {
                    System.out.print(SPACE_FILLER);
                }
                System.out.println("\\\\");
                /* 3. print defns */
                /* this is opposite to filling the array above because printing must go: left to right, top to down */
                /*
                for(i = 0; i < max_number_of_defns; i++)
                {
                    for(j = 0; j < indent_size; j++)
                    {
                        System.out.print(SPACE_FILLER);
                    }
                    System.out.print("\\rowdef ");
                    for(j = 0; j < chars_bunch_size; j++)
                    {
                        current_print_string = defns_bunch[i][j];
                        System.out.print(current_print_string);
                        for(k = 0; k < (column_spacing - (current_print_string.length()*2)); k++)
                        {
                            System.out.print(SPACE_FILLER);
                        }
                        if( j < (chars_bunch_size - 1) )
                        {
                            System.out.print("&" + SPACE_FILLER);
                        }
                    }
                    for(j = 0; j < ((chars_per_line - chars_bunch_size) * (column_spacing + 2)); j++)
                    {
                        System.out.print(SPACE_FILLER);
                    }
                    System.out.println("\\\\");
                }
                */

                if( (char_index < number_of_chars) && (all_chars.get(char_index).get_english().charAt(0) == (char)(97 + current_letter)) )
                {
                    for(i = 0; i < indent_size; i++)
                    {
                        System.out.print(SPACE_FILLER);
                    }
                    System.out.println("\\ \\\\");
                }
            }

            System.out.println("\\end{tabular}");
            System.out.println("\\ \\\\");
            System.out.println("\\ \\\\");
            System.out.println();
        }

        System.out.println("总数： {\\Large " + all_chars.size() + "}");
        System.out.println();

        /* proceeding LaTeX code: */
        System.out.println("\\end{document}");
        System.out.println();
    }


    /**
     * Print sorted list of characters WITH definitions
     *
     * @param  - all_chars: sorted list of character objects
     *
     * @return - prints sorted list of characters
    */
    public static void print_list_with_defns(List<CharacterObject> all_chars)
    {
        /* START: preceeding LaTeX code */
        System.out.println("\\documentclass[12pt]{article}");
        /* user flags */
        System.out.println();
        System.out.println("% toggles for extra information:");
        System.out.println("% letters");
        System.out.println("\\newif\\ifletter");
        System.out.println("\\lettertrue");
        System.out.println("%\\letterfalse");
        System.out.println("% characters");
        System.out.println("\\def\\rowchr#1\\\\{#1\\\\} % on");
        System.out.println("%\\def\\rowchr#1\\\\{} % off");
        System.out.println("% pinyin");
        System.out.println("\\def\\rowpin#1\\\\{#1\\\\} % on");
        System.out.println("%\\def\\rowpin#1\\\\{} % off");
        System.out.println("% meaning(s) / definition(s)");
        System.out.println("\\def\\rowdef#1\\\\{#1\\\\} % on");
        System.out.println("%\\def\\rowdef#1\\\\{} % off");
        /* usepackages */
        System.out.println();
        System.out.println("\\usepackage[a4paper, margin = 15mm, tmargin = 20mm]{geometry}");
        System.out.println("\\usepackage{changepage}");
        System.out.println("\\usepackage{xeCJK}");
        System.out.println("\\usepackage{array}");
        System.out.println("\\newcolumntype{C}[1]{>{\\centering\\let\\newline\\\\\\arraybackslash\\hspace{0pt}}m{#1}}");
        System.out.println();
        System.out.println("\\pagenumbering{gobble}");
        System.out.println("\\setCJKmainfont[]{Noto Sans CJK SC}");
        System.out.println();
        System.out.println("\\newcommand{\\nl}{\\newline}");
        System.out.println();
        System.out.println("\\begin{document}");
        System.out.println();
        /* END: preceeding LaTeX code */

        int chars_per_line  = 5;
        int indent_size     = 4;
        int number_of_chars = all_chars.size();
        int i, j, k;
        /* number of spaces between each "table column" */
        int column_spacing  = 0;
        String SPACE_FILLER = " ";
        int max_defn_length = 0;
        int max_pin_length  = 0;
        ArrayList<String> defns = new ArrayList<String>();

        /* part 1: find longest: definition OR pinyin to decide COLUMN_SPACING value */
        for(i = 0; i < number_of_chars; i++)
        {
            /* first - check pinyin length */
            if( all_chars.get(i).get_pinyin().length() > max_pin_length )
            {
                max_pin_length = all_chars.get(i).get_pinyin().length();
            }
            
            /* second - check max defn length */
            defns = all_chars.get(i).get_definitions();
            for(j = 0; j < defns.size(); j++)
            {
                if(max_defn_length < defns.get(j).length())
                {
                    max_defn_length = defns.get(j).length();
                }
            }
        }
        
        if( (max_defn_length*2) >= max_pin_length )
        {
            column_spacing = (max_defn_length * 2) + 1;
        }
        else
        {
            column_spacing = max_pin_length + 1;
        }

        /* part 2: produce list with even column-spacing */
        /* handfull of characters for current table row - will have up to chars_per_line characters */
        List<CharacterObject> chars_bunch = new ArrayList<CharacterObject>();
        /* in a character bunch: the max number of defns sentences */
        int max_number_of_defns;
        String[][] defns_bunch;
        int chars_bunch_size;
        String current_print_string;
        /* current index of all characters */
        int char_index = 0;
        int current_letter;
        /* for each letter; {A, B, C, ..., X, Y, Z} */
        for(current_letter = 0; current_letter < 26; current_letter++)
        {
            System.out.println("\\begin{adjustwidth}{-4ex}{0ex}");
            System.out.print("\\begin{tabular}{ ");
            for(i = 0; i < chars_per_line; i++)
            {
                System.out.print("C{19ex} ");
            }
            System.out.println("}");
            System.out.println("\\hspace{-13ex} \\ifletter " + (char)(65 + current_letter) + " \\else \\fi \\\\");

            /* for each char that starts with the current letter (for each char_bunch) */
            while( (char_index < number_of_chars) &&
                   (all_chars.get(char_index).get_english().charAt(0) == (char)(97 + current_letter)) )
            {
                chars_bunch.clear();
                max_number_of_defns = 0;
                /* grab the next [1 to chars_per_line] amount of characters */
                i = 0;
                while( (char_index < number_of_chars) &&
                       (i < chars_per_line) &&
                       (all_chars.get(char_index).get_english().charAt(0) == (char)(97 + current_letter)) )
                {
                    chars_bunch.add(all_chars.get(char_index));
                    if( all_chars.get(char_index).get_definitions().size() > max_number_of_defns )
                    {
                        max_number_of_defns = all_chars.get(char_index).get_definitions().size();
                    }
                    i++;
                    char_index++;
                }
                chars_bunch_size = chars_bunch.size();

                /* fill defns array with either defns or nothing */
                defns_bunch = new String[max_number_of_defns][chars_bunch_size];
                /* for each character in bunch */
                for(i = 0; i < chars_bunch_size; i++)
                {
                    /* for each defn in character */
                    for(j = 0; j < max_number_of_defns; j++)
                    {
                        /* if this character still has more defns */
                        if( j < (chars_bunch.get(i).get_definitions().size()) )
                        {
                            defns_bunch[j][i] = chars_bunch.get(i).get_definitions().get(j);
                        }
                        /* else, no defns left - add nothing */
                        else
                        {
                            defns_bunch[j][i] = "";
                        }
                    }
                }

                /* print the current bunch of characters */
                /* 1. print character symbol */
                for(i = 0; i < indent_size; i++)
                {
                    System.out.print(SPACE_FILLER);
                }
                System.out.print("\\rowchr ");
                for(i = 0; i < chars_bunch_size; i++)
                {
                    System.out.print(chars_bunch.get(i).get_chr_sym());
                    for(j = 0; j < column_spacing - 2; j++)
                    {
                        System.out.print(SPACE_FILLER);
                    }
                    if( i < (chars_bunch_size - 1) )
                    {
                        System.out.print("&" + SPACE_FILLER);
                    }
                }
                for(j = 0; j < ((chars_per_line - chars_bunch_size) * (column_spacing + 2)); j++)
                {
                    System.out.print(SPACE_FILLER);
                }
                System.out.println("\\\\");
                for(i = 0; i < indent_size; i++)
                {
                    System.out.print(SPACE_FILLER);
                }
                System.out.print("\\rowpin ");
                /* 2. print pinyin */
                for(i = 0; i < chars_bunch_size; i++)
                {
                    current_print_string = chars_bunch.get(i).get_pinyin();
                    System.out.print(current_print_string);
                    for(j = 0; j < (column_spacing - current_print_string.length()); j++)
                    {
                        System.out.print(SPACE_FILLER);
                    }
                    if( i < (chars_bunch_size - 1) )
                    {
                        System.out.print("&" + SPACE_FILLER);
                    }
                }
                for(j = 0; j < ((chars_per_line - chars_bunch_size) * (column_spacing + 2)); j++)
                {
                    System.out.print(SPACE_FILLER);
                }
                System.out.println("\\\\");
                /* 3. print defns */
                /* this is opposite to filling the array above because printing must go: left to right, top to down */
                for(i = 0; i < max_number_of_defns; i++)
                {
                    for(j = 0; j < indent_size; j++)
                    {
                        System.out.print(SPACE_FILLER);
                    }
                    System.out.print("\\rowdef ");
                    for(j = 0; j < chars_bunch_size; j++)
                    {
                        current_print_string = defns_bunch[i][j];
                        System.out.print(current_print_string);
                        for(k = 0; k < (column_spacing - (current_print_string.length()*2)); k++)
                        {
                            System.out.print(SPACE_FILLER);
                        }
                        if( j < (chars_bunch_size - 1) )
                        {
                            System.out.print("&" + SPACE_FILLER);
                        }
                    }
                    for(j = 0; j < ((chars_per_line - chars_bunch_size) * (column_spacing + 2)); j++)
                    {
                        System.out.print(SPACE_FILLER);
                    }
                    System.out.println("\\\\");
                }

                if( (char_index < number_of_chars) && (all_chars.get(char_index).get_english().charAt(0) == (char)(97 + current_letter)) )
                {
                    for(i = 0; i < indent_size; i++)
                    {
                        System.out.print(SPACE_FILLER);
                    }
                    System.out.println("\\ \\\\");
                }
            }

            System.out.println("\\end{tabular}");
            System.out.println("\\end{adjustwidth}");
            System.out.println("\\ \\\\");
            System.out.println();
        }

        System.out.println("总数： " + all_chars.size());
        System.out.println();

        /* proceeding LaTeX code: */
        System.out.println("\\end{document}");
        System.out.println();
    }


    public static void main(String[] args)
    {
        List<CharacterObject> all_chars = populate_list();
        if(args.length != 1)
        {
            System.out.println("Argument error");
            System.exit(1);
        }

        if( args[0].equals("a") )
        {
            print_list_without_defns(all_chars);
        }
        else if( args[0].equals("b") )
        {
            print_list_with_defns(all_chars);
        }
        else
        {
            System.out.println("Argument error");
            System.exit(1);
        }
    }

}
