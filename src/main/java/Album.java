class Album {

    private final String artist_name;
    private final String album_name;
    private final String city;
    private final int year;

    public Album(String artist_name, String album_name, String city, int year) {
        this.artist_name = artist_name;
        this.album_name = album_name;
        this.city = city;
        this.year = year;
    }

    public String getArtist_name() {
        return artist_name;
    }

    public String getAlbum_name() {
        return album_name;
    }

    public String getCity() {
        return city;
    }

    public int getYear() {
        return year;
    }

}
