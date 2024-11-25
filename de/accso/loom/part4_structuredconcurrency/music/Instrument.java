package de.accso.loom.part4_structuredconcurrency.music;

public record Instrument (String name) {
    public enum AllInstruments{ Piano, Guitar, DoubleBass, Tuba, Drums, TenorSaxophone, AltoSaxophone, SopranoSaxophone,
                                Clarinet, BassClarinet, Flute, AltoFlute, Trumpet, PiccoloTrumpet, BassTrombone }

    @Override
    public String toString() {
        return name;
    }
}
