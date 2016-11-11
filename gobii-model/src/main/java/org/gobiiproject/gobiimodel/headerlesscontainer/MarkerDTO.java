package org.gobiiproject.gobiimodel.headerlesscontainer;
// Generated Mar 31, 2016 1:44:38 PM by Hibernate Tools 3.2.2.GA



import java.util.List;

/** This class is not used yet. It was created in anticipation of a markers service.
 * However, this services not necessary yet. This class still needs annotations. There are
 * no ResultSet (RS), marker, or service classes. However, RsMarkerGroupsImpl does retrieve
 * markers for platform groups.
 */
public class MarkerDTO extends  DTOBase {


    private Integer markerId = 0;
    private Integer platformId;
    private Integer variantId;
    private String name;
    private String code;
    private String ref;
    private List<Integer> alts;
    private String sequence;
    private Integer referenceId;
    private Integer strandId;
    private Integer status;



    public MarkerDTO() {
    }

    @Override
    public Integer getId() {
        return this.markerId;
    }

    @Override
    public void setId(Integer id) {
        this.markerId = id;
    }

    public void setPlatformId(Integer platformId) {
        this.platformId = platformId;
    }

    public List<Integer> getAlts() {
        return alts;
    }

    public void setAlts(List<Integer> alts) {
        this.alts = alts;
    }

    public Integer getStrandId() {
        return strandId;
    }

    public void setStrandId(Integer strandId) {
        this.strandId = strandId;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getMarkerId() {
        return this.markerId;
    }

    public void setMarkerId(Integer markerId) {
        this.markerId = markerId;
    }

    public int getPlatformId() {
        return this.platformId;
    }

    public void setPlatformId(int platformId) {
        this.platformId = platformId;
    }

    public Integer getVariantId() {
        return this.variantId;
    }

    public void setVariantId(Integer variantId) {
        this.variantId = variantId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRef() {
        return this.ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getSequence() {
        return this.sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public Integer getReferenceId() {
        return this.referenceId;
    }

    public void setReferenceId(Integer referenceId) {
        this.referenceId = referenceId;
    }

    public Integer getStrand() {
        return this.strandId;
    }

    public void setStrand(Integer strandId) {
        this.strandId = strandId;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


}


