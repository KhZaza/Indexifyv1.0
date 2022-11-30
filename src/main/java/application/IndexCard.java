package application;

public class IndexCard {
    private String front;
    private String back;
    private boolean learned;

    private boolean flipped;

    public IndexCard(String front, String back, boolean learned){
        this.front = front;
        this.back = back;
        this.learned = learned;
        flipped = false;
    }

    public String getFront(){
        return front;
    }

    public String getBack(){
        return back;
    }

    public boolean isLearned(){
        return learned;
    }

    public void updateTerm(String front){
        this.front = front;
    }

    public void updateDefinition(String back){
        this.back = back;
    }

    public void setLearned(boolean learned){
        this.learned = learned;
    }

    public String toString(){
        return "Term: " + front + "\n" + "Definition: " + back;
    }

    public void flip(){
        flipped = !flipped;
    }

    public boolean isFlipped(){
        return flipped;
    }

    public void updateCard(String term, String definition, boolean learned){
        this.front = term;
        this.back = definition;
        this.learned = learned;
    }

}
