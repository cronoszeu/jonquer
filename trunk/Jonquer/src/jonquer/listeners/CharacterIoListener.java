package jonquer.listeners;

import jonquer.model.Character;
/**
 * 
 * @author xEnt
 *
 */
public interface CharacterIoListener {
    
    public Character loadCharacter(String username);
    
    public void saveCharacter(Character character);
    
    public String getIoName();
}
