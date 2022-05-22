import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.io.BufferedReader;
import java.io.Reader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.Random;
import java.util.Scanner;

public class Definitions
{
    /**
     * Generates a list of all character definitions
     *
     * @return - list of all character definitions
     */
    public static List<String> populate_definition_list()
    {
        String line;
        int i;
        ArrayList<String> all_defns = new ArrayList<String>();

        try(BufferedReader br = new BufferedReader(new FileReader("list.txt")))
        {
            i = 0;
            while((line = br.readLine()) != null)
            {
                /* empty line means we have a new character to be added */
                if( line.equals("") )
                {
                    i = 0;
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
                        /* ignore character */
                        case 0:
                            break;
                        /* ignore english */
                        case 1:
                            break;
                        /* ignore stroke count */
                        case 2:
                            break;
                        /* ignore stroke number */
                        case 3:
                            break;
                        /* ignore pinyin */
                        case 4:
                            break;
                        default:
                            all_defns.add(line);
                            break;
                    }
                    i++;
                }
            }
        } catch(IOException ex) { System.out.println("File Read Error: " + ex); System.exit(1); }

        return all_defns;
    }


    /**
     * Use populated list to randomly select sentences for practice
     *
     * @param - populated list of all characters
     */
    public static void random_sentences(List<String> all_defns)
    {
        String curr_defn;
        String line;
        Scanner scanner = new Scanner(System.in);

        System.out.println();
        for(;;)
        {
            curr_defn = all_defns.get(new Random().nextInt(all_defns.size()));
            
            System.out.println(curr_defn);

            /* try{ TimeUnit.SECONDS.sleep(1); } catch(Exception e) {} */
            line = scanner.nextLine();
        }
    }


    public static void main(String[] args)
    {
        /* SIGINT (Ctrl+C) handler */
        Runtime.getRuntime().addShutdownHook(new Thread()
        {
            @Override
            public void run()
            {
                System.out.println("");
            }
        });

        List<String> all_defns = populate_definition_list();

        random_sentences(all_defns);
        
    } /* main() */

} /* class */
