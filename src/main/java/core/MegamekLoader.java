package core;

import megamek.common.EquipmentType;
import megamek.common.MechSummary;

import java.io.*;

import java.util.Vector;

public class MegamekLoader {
    public Vector<MechSummary> load(String cachePath) throws Exception {
        Vector<MechSummary> vMechs = new Vector<>();
        EquipmentType.initializeTypes();
        InputStream istream = new BufferedInputStream(
                new FileInputStream(cachePath));
        ObjectInputStream fin = new ObjectInputStream(istream);
        Integer num_units = (Integer) fin.readObject();
        for (int i = 0; i < num_units; i++) {
            MechSummary ms = (MechSummary) fin.readObject();
            vMechs.addElement(ms);
        }
        fin.close();
        istream.close();

        return vMechs;
    }
}
