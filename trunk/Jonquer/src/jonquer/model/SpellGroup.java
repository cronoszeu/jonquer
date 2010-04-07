package jonquer.model;

import java.util.HashMap;

import jonquer.model.def.COSpellDef;

public class SpellGroup {
    
    private HashMap<Integer, COSpellDef> spellDefs = new HashMap<Integer, COSpellDef>();

    public void setSpellDefs(HashMap<Integer, COSpellDef> spellDefss) {
	spellDefs = spellDefss;
    }

    public HashMap<Integer, COSpellDef> getSpellDefs() {
	return spellDefs;
    }

}
