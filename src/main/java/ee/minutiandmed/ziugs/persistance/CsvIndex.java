package ee.minutiandmed.ziugs.persistance;

        import ee.minutiandmed.ziugs.DownloaderData;

        import java.util.List;

public enum CsvIndex {

    INDEX_OF_VISIBILITY(20) {
        @Override
        public List<String> getList(DownloaderData data) {
            return data.getVisibility();
        }
    },
    INDEX_OF_WIND_DIRECTION(11) {
        @Override
        public List<String> getList(DownloaderData data) {
            return data.getWindDirection();
        }
    },
    INDEX_OF_WIND_SPEED(7) {
        @Override
        public List<String> getList(DownloaderData data) {
            return data.getWindSpeed();
        }
    },
    INDEX_OF_WEATHER_FENOMENON(15) {
        @Override
        public List<String> getList(DownloaderData data) {
            return data.getWeatherFenomenon();
        }
    },
    INDEX_OF_CLOUDBASE(22) {
        @Override
        public List<String> getList(DownloaderData data) {
            return data.getCloudBase();
        }
    },
    INDEX_OF_OKTA(23) {
        @Override
        public List<String> getList(DownloaderData data) {
            return data.getOkta();
        }
    },
    INDEX_OF_UPDATE_TIME_ON_SERVER(0) {
        @Override
        public List<String> getList(DownloaderData data) {
            return data.getUpdateTimeOnServer();
        }
    };

    private int index;

    private CsvIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public abstract List<String> getList(DownloaderData data);
}
