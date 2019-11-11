package org.gobiiproject.gobiimodel.dto.entity.noaudit;

import org.gobiiproject.gobiimodel.entity.Marker;

import javax.persistence.ColumnResult;
import javax.persistence.SqlResultSetMapping;
import java.math.BigDecimal;

@SqlResultSetMapping(name="mapMarkerStartStop",
        columns={@ColumnResult(name="start"), @ColumnResult(name="stop")})
public class MarkerStartStopDTO extends Marker {

    private BigDecimal start;

    private BigDecimal stop;

    public BigDecimal getStart() {
        return start;
    }

    public void setStart(BigDecimal start) {
        this.start = start;
    }

    public BigDecimal getStop() {
        return stop;
    }

    public void setStop(BigDecimal stop) {
        this.stop = stop;
    }
}
