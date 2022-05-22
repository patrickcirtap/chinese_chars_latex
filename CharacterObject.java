import java.util.*;

/* character object */
public class CharacterObject implements Comparable<CharacterObject>
{
    private String chr_sym;
    private String english;
    private int stroke_count;
    private int stroke_number;
    private String pinyin;
    private ArrayList<String> definitions;
    
    /* create new char obj */
    public CharacterObject(String chr, String eng, int stk_c, int stk_n, String pin, ArrayList<String> defns)
    {
        chr_sym       = chr;
        english       = eng;
        stroke_count  = stk_c;
        stroke_number = stk_n;
        pinyin        = pin;
        definitions   = new ArrayList<String>(defns);
    }

    /* comparison definition */
    public int compareTo(CharacterObject other)
    {
        /* compare based on english */
        if( (english.compareTo(other.english)) != 0 )
        {
            return english.compareTo(other.english);
        }
        /* compare based on number of strokes */
        else if( (Integer.compare(stroke_count, other.stroke_count)) != 0 )
        {
            return Integer.compare(stroke_count, other.stroke_count);
        }
        /* compare based on stroke (tone) number (0, 1, 2, 3, 4); _ / V \ */
        else
        {
            return Integer.compare(stroke_number, other.stroke_number);
        }
    }
    
    /* get'ers */
    public String get_chr_sym()
    {
        return chr_sym;
    }
    
    public String get_english()
    {
        return english;
    }
    
    public String get_pinyin()
    {
        return pinyin;
    }
    
    /* convert ArrayList to array of strings and then return array */
    public ArrayList<String> get_definitions()
    {
        return definitions;
    }
}
