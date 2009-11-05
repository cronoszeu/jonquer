package jonquer.model;

import java.io.Serializable;

public class Equipment implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Item left_hand = null;
    private Item right_hand = null;
    private Item head = null;
    private Item neck = null;
    private Item ring = null;
    private Item armor = null;
    private Item boots = null;
    
    public Item getLeft_hand() {
        return left_hand;
    }
    public void setLeft_hand(Item leftHand) {
        left_hand = leftHand;
    }
    public Item getRight_hand() {
        return right_hand;
    }
    public void setRight_hand(Item rightHand) {
        right_hand = rightHand;
    }
    public Item getHead() {
        return head;
    }
    public void setHead(Item head) {
        this.head = head;
    }
    public Item getNeck() {
        return neck;
    }
    public void setNeck(Item neck) {
        this.neck = neck;
    }
    public Item getRing() {
        return ring;
    }
    public void setRing(Item ring) {
        this.ring = ring;
    }
    public Item getArmor() {
        return armor;
    }
    public void setArmor(Item armor) {
        this.armor = armor;
    }
    public Item getBoots() {
        return boots;
    }
    public void setBoots(Item boots) {
        this.boots = boots;
    }

}
