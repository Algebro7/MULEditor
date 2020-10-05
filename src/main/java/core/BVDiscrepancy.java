package core;

import list.UnitListData;
import megamek.common.MechSummary;

import java.util.ArrayList;
import java.util.List;

public class BVDiscrepancy {
    private String name;
    private int mulBV;
    private int sswBV;
    private int mmBV;
    private String type;
    private String url;
    private String mulIntro;
    private String sswIntro;

    public BVDiscrepancy(MulUnit mulUnit, UnitListData sswUnit) {
        name = mulUnit.getName();
        mulBV = mulUnit.getBV();
        sswBV = sswUnit.getBV();
        mmBV = 0;
        type = sswUnit.getType();
        url = mulUnit.getUrl();
        mulIntro = mulUnit.getIntro();
        sswIntro = String.valueOf(sswUnit.getYear());
    }

    public BVDiscrepancy(MulUnit mulUnit, UnitListData sswUnit, MechSummary mmUnit) {
        name = mulUnit.getName();
        mulBV = mulUnit.getBV();
        sswBV = sswUnit.getBV();
        mmBV = mmUnit.getBV();
        type = sswUnit.getType();
        url = mulUnit.getUrl();
        mulIntro = mulUnit.getIntro();
        sswIntro = String.valueOf(sswUnit.getYear());
    }

    public List<String> getFields() {
        List<String> fields = new ArrayList<>();
        fields.add(name);
        fields.add(String.valueOf(mulBV));
        fields.add(String.valueOf(sswBV));
        fields.add(String.valueOf(mmBV));
        fields.add(String.valueOf(type));
        fields.add(url);
        fields.add(mulIntro);
        fields.add(sswIntro);
        return fields;
    }
}
