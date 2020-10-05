package core;

import list.UnitList;
import list.UnitListData;

import java.util.List;

public class SSWLoader {
    public List<UnitListData> load(String path) {
        return new UnitList(path, false).getList();
    }
}
