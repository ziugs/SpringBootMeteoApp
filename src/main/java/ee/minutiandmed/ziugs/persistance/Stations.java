package ee.minutiandmed.ziugs.persistance;

public enum Stations {

    S1("Jõgeva", "Jõgeva.csv"),
    S2("Jõhvi", "Jõhvi.csv"),
    S3("Kihnu", "Kihnu.csv"),
    S4("Kunda", "Kunda.csv"),
    S5("Kuusiku", "Kuusiku.csv"),
    S6("Lääne-Nigula", "Lääne-Nigula.csv"),
    S7("Narva", "Narva.csv"),
    S8("Pärnu", "Pärnu.csv"),
    S9("Ristna", "Ristna.csv"),
    S10("Ruhnu", "Ruhnu.csv"),
    S11("Sõrve", "Sõrve.csv"),
    S12("Tallinn-Harku", "Tallinn-Harku.csv"),
    S13("Tartu-Tõravere", "Tartu-Tõravere.csv"),
    S14("Tiirikoja", "Tiirikoja.csv"),
    S15("Türi", "Türi.csv"),
    S16("Väike-Maarja", "Väike-Maarja.csv"),
    S17("Viljandi", "Viljandi.csv"),
    S18("Vilsandi", "Vilsandi.csv"),
    S19("Võru", "Võru.csv");

    private String stationName;
    private String csvFileName;

    Stations(String stationName, String csvFileName) {
        this.stationName = stationName;
        this.csvFileName = csvFileName;
    }

    public String getStationName() {
        return stationName;
    }

    public String getCsvFileName() {
        return csvFileName;
    }

}


