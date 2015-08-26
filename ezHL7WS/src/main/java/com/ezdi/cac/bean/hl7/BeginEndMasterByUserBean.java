package com.ezdi.cac.bean.hl7;


import com.ezdi.cac.constant.table.BeginEndMasterByUserTableConstant;

import javax.persistence.*;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = BeginEndMasterByUserTableConstant.TABLE_NAME)
public class BeginEndMasterByUserBean {
    /**
     *
     */
    protected static final long serialVersionUID = 1L;

    @Column(name = BeginEndMasterByUserTableConstant.TEXT)
    protected String text;

	/*@Column( name = BeginEndForICD9TableConstant.MASTERID)
    protected Integer	masterId ;*/

    @Column(name = BeginEndMasterByUserTableConstant.MASTERID)
    protected Long masterId;

    @Column(name = BeginEndMasterByUserTableConstant.SECDOCID)
    protected Integer secDocId;

    @Column(name = BeginEndMasterByUserTableConstant.SENTID)
    protected Integer sentId;

    @Column(name = BeginEndMasterByUserTableConstant.END)
    protected Integer end;

    @Id
    @GeneratedValue
    @Column(name = BeginEndMasterByUserTableConstant.BEGINEND_USERID)
    protected Integer beginEndUserId;

    @Column(name = BeginEndMasterByUserTableConstant.BEGIN)
    protected Integer begin;

    @Column(name = BeginEndMasterByUserTableConstant.ACTIVE_RECORD)
    protected Integer activeRecord;

    @Column(name = BeginEndMasterByUserTableConstant.CREATOR_USER_ID)
    protected Integer creatorUserId;

    @Column(name = BeginEndMasterByUserTableConstant.REMOVER_USER_ID)
    protected Integer removerUserId;

    @Column(name = BeginEndMasterByUserTableConstant.DOCID)
    protected Integer docId;

    @Column(name = BeginEndMasterByUserTableConstant.ROLE_ID)
    protected Integer roleId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = BeginEndMasterByUserTableConstant.MASTERID, nullable = false, insertable = false, updatable = false)
    protected MasterMapBean masterMap2ByUser;


    @Column(name = BeginEndMasterByUserTableConstant.ISSYSTEMSUGGESTED)
    protected boolean isSystemSuggested;
    @Column(name = BeginEndMasterByUserTableConstant.SENTBEGIN)
    protected Integer sentBegin;
    @Column(name = BeginEndMasterByUserTableConstant.SENTEND)
    protected Integer sentEnd;
    @Column(name = BeginEndMasterByUserTableConstant.SECNAME, length = 100)
    protected String secName;
    @Column(name = BeginEndMasterByUserTableConstant.SECBEGIN)
    protected Integer secBegin;
    @Column(name = BeginEndMasterByUserTableConstant.SECEND)
    protected Integer secEnd;
    @Column(name = BeginEndMasterByUserTableConstant.DOCTYPE, length = 100)
    protected String docType;
    @Column(name = BeginEndMasterByUserTableConstant.DOCNAME, length = 300)
    protected String docName;
    @Column(name = BeginEndMasterByUserTableConstant.USERROLENAME)
    protected String userRoleName;
    @Column(name = BeginEndMasterByUserTableConstant.SENTENCE)
    protected String sentence;
    @Column(name = BeginEndMasterByUserTableConstant.ISUPDATED)
    //@Transient
    protected Integer isUpdated;
    @Column(name = BeginEndMasterByUserTableConstant.EVIDENCE_GROUP_ID)
    private Integer evidenceGroupId;

    @Column(name = BeginEndMasterByUserTableConstant.CREATOR_DTS)
    private Date creatorDTS;

    /**
     * @return the isSystemSuggested
     */
    public boolean isSystemSuggested() {
        return isSystemSuggested;
    }

    /**
     * @param isSystemSuggested the isSystemSuggested to set
     */
    public void setSystemSuggested(boolean isSystemSuggested) {
        this.isSystemSuggested = isSystemSuggested;
    }

    /**
     * @return the sentBegin
     */
    public Integer getSentBegin() {
        return sentBegin;
    }

    /**
     * @param sentBegin the sentBegin to set
     */
    public void setSentBegin(Integer sentBegin) {
        this.sentBegin = sentBegin;
    }

    /**
     * @return the sentEnd
     */
    public Integer getSentEnd() {
        return sentEnd;
    }

    /**
     * @param sentEnd the sentEnd to set
     */
    public void setSentEnd(Integer sentEnd) {
        this.sentEnd = sentEnd;
    }

    /**
     * @return the secName
     */
    public String getSecName() {
        return secName;
    }

    /**
     * @param secName the secName to set
     */
    public void setSecName(String secName) {
        this.secName = secName;
    }

    /**
     * @return the secBegin
     */
    public Integer getSecBegin() {
        return secBegin;
    }

    /**
     * @param secBegin the secBegin to set
     */
    public void setSecBegin(Integer secBegin) {
        this.secBegin = secBegin;
    }

    /**
     * @return the secEnd
     */
    public Integer getSecEnd() {
        return secEnd;
    }

    /**
     * @param secEnd the secEnd to set
     */
    public void setSecEnd(Integer secEnd) {
        this.secEnd = secEnd;
    }

    /**
     * @return the docType
     */
    public String getDocType() {
        return docType;
    }

    /**
     * @param docType the docType to set
     */
    public void setDocType(String docType) {
        this.docType = docType;
    }

    /**
     * @return the docName
     */
    public String getDocName() {
        return docName;
    }

    /**
     * @param docName the docName to set
     */
    public void setDocName(String docName) {
        this.docName = docName;
    }

    public String getText() {
        return text;
    }

    public void setText(final String text) {
        this.text = text;
    }

    /*public Integer getMasterId()
    {
        return masterId;
    }*/
    public Long getMasterId() {
        return masterId;
    }

    public void setMasterId(final Long masterId) {
        this.masterId = masterId;
    }

    public Integer getSecDocId() {
        return secDocId;
    }

    public void setSecDocId(final Integer secDocId) {
        this.secDocId = secDocId;
    }

    public Integer getSentId() {
        return sentId;
    }

    public void setSentId(final Integer sentId) {
        this.sentId = sentId;
    }

    public Integer getEnd() {
        return end;
    }

    public void setEnd(final Integer end) {
        this.end = end;
    }

    public Integer getBegin() {
        return begin;
    }

    public void setBegin(final Integer begin) {
        this.begin = begin;
    }


    public Integer getRemoverUserId() {
        return removerUserId;
    }

    public void setRemoverUserId(final Integer removerUserId) {
        this.removerUserId = removerUserId;
    }

    public Integer getCreatorUserId() {
        return creatorUserId;
    }

    public void setCreatorUserId(final Integer creatorUserId) {
        this.creatorUserId = creatorUserId;
    }

    public Integer getActiveRecord() {
        return activeRecord;
    }

    public void setActiveRecord(final Integer activeRecord) {
        this.activeRecord = activeRecord;
    }


    /**
     * @return the docId
     */
    public Integer getDocId() {
        return docId;
    }

    /**
     * @param docId the docId to set
     */
    public void setDocId(final Integer docId) {
        this.docId = docId;
    }


    public Integer getBeginEndUserId() {
        return beginEndUserId;
    }

    public void setBeginEndUserId(final Integer beginEndUserId) {
        this.beginEndUserId = beginEndUserId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(final Integer roleId) {
        this.roleId = roleId;
    }


    /**
     * @return the masterMap2ByUser
     */
    public MasterMapBean getMasterMap2ByUser() {
        return masterMap2ByUser;
    }

    /**
     * @param masterMap2ByUser the masterMap2ByUser to set
     */
    public void setMasterMap2ByUser(final MasterMapBean masterMap2ByUser) {
        this.masterMap2ByUser = masterMap2ByUser;
    }


    public String getUserRoleName() {
        return userRoleName;
    }

    public void setUserRoleName(String userRoleName) {
        this.userRoleName = userRoleName;
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public Integer getIsUpdated() {
        return isUpdated;
    }

    public void setIsUpdated(Integer isUpdated) {
        this.isUpdated = isUpdated;
    }

    public Integer getEvidenceGroupId() {
        return evidenceGroupId;
    }

    public void setEvidenceGroupId(Integer evidenceGroupId) {
        this.evidenceGroupId = evidenceGroupId;
    }

    @Override
    public int hashCode() {
        return getBegin() + getEnd() + getDocId();
    }

    @Override
    public boolean equals(Object obj) {
        boolean isEqual = false;
        if (obj != null && obj instanceof BeginEndMasterByUserBean) {
            isEqual = this.getBegin().intValue() == ((BeginEndMasterByUserBean) obj).getBegin().intValue() && this.getEnd().intValue() == ((BeginEndMasterByUserBean) obj).getEnd().intValue() && this.getDocId().intValue() == ((BeginEndMasterByUserBean) obj).getDocId().intValue();
        }
        return isEqual;
    }

    public Date getCreatorDTS() {
        return creatorDTS;
    }

    public void setCreatorDTS(Date creatorDTS) {
        this.creatorDTS = creatorDTS;
    }
}