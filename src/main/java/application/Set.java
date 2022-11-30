package application;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;

public class Set {

    private ArrayList<IndexCard> set;
    private String course;
    private int index;
    private Label text;

    private CheckBox checkBox;

    private TextField counterBox;

    public Set(String course, Label text, CheckBox checkBox, TextField counterBox){
        set = new ArrayList<>();
        set.add(new IndexCard("EDIT THIS CARD", "Definition", false));
        this.course = course;
        index = 0;
        this.text = text;
        this.counterBox = counterBox;
        this.checkBox = checkBox;
    }

    public ArrayList<IndexCard> getSetList()
    {
        return set;
    }
    public int size(){return set.size();}

    public String getCourse(){
        return course;
    }

    public void setCourse(String course){
        this.course = course;
    }

    public String toString(){
        String str = "";
        for(int i = 0; i < set.size(); i++){
            str += set.get(i) + "\n";
        }
        return str;
    }

    public void nextCard() {
        if(index + 1 >= size()){
            return;
        }
        IndexCard currentCard = set.get(index);
        if(currentCard.isFlipped()){
            flipCurrentCard();
        }
        IndexCard card = set.get(++index);
        updateCard(card.getFront(), card.getBack(), card.isLearned());
        counterBox.setText((index + 1) + "/" + size());
    }

    public void previousCard() {
        if(index - 1 < 0){
            return;
        }
        IndexCard currentCard = set.get(index);

        if(currentCard.isFlipped()){
            flipCurrentCard();
        }
        IndexCard card = set.get(--index);
        updateCard(card.getFront(), card.getBack(), card.isLearned());
        counterBox.setText((index + 1) + "/" + size());
    }


    public void flipCurrentCard(){
        IndexCard card = set.get(index);
        card.flip();
        updateCard(card.getFront(), card.getBack(), card.isLearned());
    }

    public boolean updateCard(String term, String definition, boolean learned) {
        IndexCard card = set.get(index);
        card.updateCard(term, definition, learned);
        if(card.isFlipped()){
            //text.setFont(new Font(17));
            text.setText(card.getBack());
        } else {
            //text.setFont(new Font(23));
            text.setText(card.getFront());
        }
        text.setTextAlignment(TextAlignment.CENTER);
        checkBox.setSelected(card.isLearned());
        return card.isLearned();
    }

    public IndexCard getCard(){
            return set.get(index);
    }

    public void addCard(String term, String definition, boolean selected) {
        set.add(new IndexCard(term, definition, selected));
        counterBox.setText((index + 1) + "/" + size());
    }

    public void deleteCard(IndexCard card) {
        if(size() == 1){
            System.out.println("One card needed for a set!");
            return;
        }
        set.remove(card);
        if(index == set.size())
        {
            index-=1;
            card = set.get(index);
        }
        else
        {
            card = set.get(index);
        }
        updateCard(card.getFront(), card.getBack(), card.isLearned());
        counterBox.setText((index + 1) + "/" + size());
    }
}
