package de.accso.loom.part4_structuredconcurrency.music;

public record Musician(String name) {
    public enum AllMusicians { John, Paul, Peter, Ringo, Justin, Robert, Anne, Bono, Larry,
                               Adam, Jamie, Bruce, Jim, Freddie, George, Sam, Kurt }
    
    @Override
    public String toString() {
        return name;
    }
}
