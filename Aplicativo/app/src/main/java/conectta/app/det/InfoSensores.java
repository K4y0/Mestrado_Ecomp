package conectta.app.det;

import java.util.ArrayList;
import java.util.List;

public class InfoSensores {

    private List<double[]> lista = new ArrayList<double[]>();
    private long timestamp;

    public InfoSensores(List<double[]> lista, long timestamp) {

        this.lista = lista;
        this.timestamp = timestamp;
    }

    public InfoSensores() {

    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public List<double[]> getLista() {
        return lista;
    }

    public void setLista(List<double[]> lista) {
        this.lista = lista;
    }
}
