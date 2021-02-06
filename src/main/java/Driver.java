import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;

class Driver {
    @org.jetbrains.annotations.NotNull
    public static CqlSession getSession(){
        return CqlSession.builder().build();
    }
}
