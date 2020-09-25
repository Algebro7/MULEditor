package core;

import list.UnitListData;

import java.util.ArrayList;
import java.util.List;

public class BVDiscrepancy {
    private String name;
    private int mulBV;
    private int sswBV;
    private String type;
    private String url;

    public BVDiscrepancy(MulUnit mulUnit, UnitListData sswUnit) {
        name = mulUnit.getName();
        mulBV = mulUnit.getBV();
        sswBV = sswUnit.getBV();
        type = sswUnit.getType();
        url = mulUnit.getUrl();
    }

    public List<String> getFields() {
        List<String> fields = new ArrayList<>();
        fields.add(name);
        fields.add(String.valueOf(mulBV));
        fields.add(String.valueOf(sswBV));
        fields.add(String.valueOf(type));
        fields.add(url);
        return fields;
    }
}
