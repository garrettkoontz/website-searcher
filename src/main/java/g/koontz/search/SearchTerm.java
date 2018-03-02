package g.koontz.search;

import java.util.regex.Pattern;

public class SearchTerm {

    private final Pattern term;
    private final String stringTerm;

    public SearchTerm(String term){
        this.stringTerm = term;
        this.term = Pattern.compile(Pattern.quote(term), Pattern.CASE_INSENSITIVE);
    }

    public boolean hasTerm(String searchText){
        return term.matcher(searchText).find();
    }

    public String getTerm(){
        return stringTerm;
    }

}
