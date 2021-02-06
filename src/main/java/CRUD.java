import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

class CRUD {

    static String music_album_path = "data/music_library";

    public static void main(String[] args) {
        CqlSession session = Driver.getSession();
        String table = "music_library";
        String keyspace = "java_test";
        //printInputStream(is);
        List<Album> albums = loadAlbums();
//        System.out.println(albums);
        try{
            createKeyspace(session, keyspace);
            createAlbumTable(session, keyspace, table);
            PreparedStatement preparedStatement = session.prepare("INSERT INTO java_Test.music_library " +
                    "(year, artist_name, album_name, city) VALUES( ?,?,?,?)");
            for (Album album : albums
                 ) {
                addAlbum(preparedStatement, session, album);
            }

            dropAlbumTable(session, keyspace ,table);
        }
        catch(Exception e){
            System.out.println(e);
        }
        finally {
            session.close();
        }

    }

    private static List<Album> loadAlbums(){
        InputStream is = new CRUD().getFileFromResourceAsStream(music_album_path);
        return loadInputStream(is);

    }

    private static List<Album> loadInputStream(InputStream is) {
        List<Album> albums = new LinkedList<>();
        try (InputStreamReader streamReader =
                     new InputStreamReader(is, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(streamReader)) {

            String line;

            while ((line = reader.readLine()) != null) {
                String[] albumDataLine = line.split(",");

                albums.add(new Album(albumDataLine[1]
                                ,albumDataLine[2]
                                ,albumDataLine[3]
                                ,Integer.parseInt(albumDataLine[0])
                ));

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return albums;
    }

    private InputStream getFileFromResourceAsStream(String fileName) {

        // The class loader that loaded the class
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        // the stream holding the file content
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return inputStream;
        }

    }

    private static void dropAlbumTable(CqlSession session, String keyspace, String table){
        session.execute("DROP TABLE IF EXISTS "+ keyspace +"."+table );
        System.out.println("DROPPED TABLE "+ keyspace +"." +table);
    }

    private static void createKeyspace(CqlSession session,String keyspace){
        session.execute("CREATE KEYSPACE IF NOT EXISTS "+ keyspace +" WITH REPLICATION = {'class':" +
                "'SimpleStrategy','replication_factor':1 }");
        System.out.println("KEYSPACE CREATED "+keyspace);
    }

    private static void createAlbumTable(CqlSession session, String keyspace, String table){
        session.execute("CREATE TABLE IF NOT EXISTS " + keyspace +"." + table +
                "(year int, artist_name text, album_name text, city text,PRIMARY KEY(artist_name,album_name,city))");
        System.out.println("CREATED TABLE " + keyspace + "." + table);
    }

    private static void addAlbum(PreparedStatement preparedStatement, CqlSession session, Album album){
        session.execute(preparedStatement.bind(album.getYear()
                ,album.getArtist_name()
                ,album.getAlbum_name()
                ,album.getCity()));
        System.out.println("INSERTED "+album.getAlbum_name());
    }
}
