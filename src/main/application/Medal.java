package main.application;

public class Medal implements java.io.Serializable{
    public enum Value{
        GOLD,
        SILVER,
        BRONZE;

        @Override
        public String toString(){
            switch(this){
                case GOLD -> {
                    return "Gold";
                }
                case SILVER -> {
                    return "Silver";
                }
                case BRONZE -> {
                    return "Bronze";
                }
                default -> {
                    return this.name();
                }
            }
        }
    }

    private Value value;
    private Event event;

    public Medal(Value value, Event event){
        setValue(value);
        setEvent(event);
    }

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
