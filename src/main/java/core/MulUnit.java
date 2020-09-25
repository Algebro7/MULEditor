package core;

import java.util.ArrayList;
import java.util.List;

public class MulUnit {
    private String name;
    private int bv;
    private int pv;
    private String rules;
    private String url;

    public MulUnit(String name, String BV, String PV, String rules, String URL) {
        this.name = name;
        try {
            this.bv = Integer.parseInt(BV);
        } catch (NumberFormatException e) {
            this.bv = 0;
        }
        try {
            this.pv = Integer.parseInt(PV);
        } catch (NumberFormatException e) {
            this.pv = 0;
        }
        this.rules = rules;
        this.url = URL;
    }

    public MulUnit(List<String> fields) {
        this(fields.get(0), fields.get(1), fields.get(2), fields.get(3), fields.get(4));
    }

    public String getName() {
        return name;
    }

    public int getBV() {
        return bv;
    }

    public String getUrl() {
        return url;
    }

    public List<String> getFields() {
        List<String> fields = new ArrayList<>();
        fields.add(name);
        fields.add(String.valueOf(bv));
        fields.add(String.valueOf(pv));
        fields.add(rules);
        fields.add(url);
        return fields;
    }

    @Override
    public String toString() {
        return name + " " + bv + " " + pv + " " + rules + " " + url;
    }
}
