package core;

public class SearchResult {
    private String name;
    private String bv;
    private String pv;
    private String rules;

    public SearchResult(String name, String BV, String PV, String rules) {
        this.name = name;
        this.bv = BV;
        this.pv = PV;
        this.rules = rules;
    }

    @Override
    public String toString() {
        return name + " " + bv + " " + pv + " " + rules;
    }
}
