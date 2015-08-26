package com.ezdi.cac.bean.hl7;

import com.ezdi.cac.constant.table.ResequenceOptionsTableConstant;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Satyam.S on 7/10/2015.
 */
@Entity
@Table(name = ResequenceOptionsTableConstant.TABLE_NAME)
public class ResequenceOptionsBean {

    @Id
    @GeneratedValue
    @Column(name = ResequenceOptionsTableConstant.RESEQUENCEOPTIONSID)
    private Integer resequenceOptionsId;

    @Column(name = ResequenceOptionsTableConstant.NEED_DIAGNOSES_RESEQUENCE)
    private Integer needDiagnosisResequence;

    @Column(name = ResequenceOptionsTableConstant.NEED_PROCEDURES_RESEQUENCE)
    private Integer needProcedureResequence;

    @Column(name = ResequenceOptionsTableConstant.ONLY_CHECK_DIAGNOSES)
    private Integer onlyCheckDiagnoses;

    @Column(name = ResequenceOptionsTableConstant.ONLY_CHECK_PROCEDURES)
    private Integer onlyCheckProcedures;

    @Column(name = ResequenceOptionsTableConstant.DIAGNOSES_RESEQUENCE_COUNT)
    private Integer diagnosesResequenceCount;

    @Column(name = ResequenceOptionsTableConstant.PROCEDURES_RESEQUENCE_COUNT)
    private Integer proceduresResequeceCount;

    @Column(name = ResequenceOptionsTableConstant.RESEQUENCE_ALL)
    private Integer resequenceAll;

    @Column(name = ResequenceOptionsTableConstant.ENCOUNTERID)
    private Integer encounterId;

    @Column(name = ResequenceOptionsTableConstant.UPDATEDTS)
    private Date updateDTS;

    @Column(name = ResequenceOptionsTableConstant.CREATORDTS)
    private Date creatorDTS;

    @Column(name = ResequenceOptionsTableConstant.UPDATEUSERID)
    private Integer updateUserId;

    @Column(name = ResequenceOptionsTableConstant.PATIENTCLASSID)
    private Integer patientClassId;

    @Column(name = ResequenceOptionsTableConstant.CREATORUSERID)
    private Integer creatorUserId;

    @Column(name = ResequenceOptionsTableConstant.ACTIVERECORD)
    private Integer activeRecord;

    @Column(name = ResequenceOptionsTableConstant.PATIENTCLASSCODE)
    private String patientClassCode;

    @Column(name = ResequenceOptionsTableConstant.PATIENTCLASSDESC)
    private String patientClassDesc;

    @Column(name = ResequenceOptionsTableConstant.GROUPER_TYPE)
    private String grouperType;

    @Column(name = ResequenceOptionsTableConstant.ISDEFAULT, nullable = false, columnDefinition = "int default 0")
    private Integer isDefault = 0;


    public Integer getResequenceOptionsId() {
        return resequenceOptionsId;
    }

    public void setResequenceOptionsId(Integer resequenceOptionsId) {
        this.resequenceOptionsId = resequenceOptionsId;
    }

    public Integer getNeedDiagnosisResequence() {
        return needDiagnosisResequence;
    }

    public void setNeedDiagnosisResequence(Integer needDiagnosisResequence) {
        this.needDiagnosisResequence = needDiagnosisResequence;
    }

    public Integer getNeedProcedureResequence() {
        return needProcedureResequence;
    }

    public void setNeedProcedureResequence(Integer needProcedureResequence) {
        this.needProcedureResequence = needProcedureResequence;
    }

    public Integer getOnlyCheckDiagnoses() {
        return onlyCheckDiagnoses;
    }

    public void setOnlyCheckDiagnoses(Integer onlyCheckDiagnoses) {
        this.onlyCheckDiagnoses = onlyCheckDiagnoses;
    }

    public Integer getOnlyCheckProcedures() {
        return onlyCheckProcedures;
    }

    public void setOnlyCheckProcedures(Integer onlyCheckProcedures) {
        this.onlyCheckProcedures = onlyCheckProcedures;
    }

    public Integer getDiagnosesResequenceCount() {
        return diagnosesResequenceCount;
    }

    public void setDiagnosesResequenceCount(Integer diagnosesResequenceCount) {
        this.diagnosesResequenceCount = diagnosesResequenceCount;
    }

    public Integer getProceduresResequeceCount() {
        return proceduresResequeceCount;
    }

    public void setProceduresResequeceCount(Integer proceduresResequeceCount) {
        this.proceduresResequeceCount = proceduresResequeceCount;
    }

    public Integer getResequenceAll() {
        return resequenceAll;
    }

    public void setResequenceAll(Integer resequenceAll) {
        this.resequenceAll = resequenceAll;
    }

    public Integer getEncounterId() {
        return encounterId;
    }

    public void setEncounterId(Integer encounterId) {
        this.encounterId = encounterId;
    }

    public Date getUpdateDTS() {
        return updateDTS;
    }

    public void setUpdateDTS(Date updateDTS) {
        this.updateDTS = updateDTS;
    }

    public Date getCreatorDTS() {
        return creatorDTS;
    }

    public void setCreatorDTS(Date creatorDTS) {
        this.creatorDTS = creatorDTS;
    }

    public Integer getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(Integer updateUserId) {
        this.updateUserId = updateUserId;
    }

    public Integer getPatientClassId() {
        return patientClassId;
    }

    public void setPatientClassId(Integer patientClassId) {
        this.patientClassId = patientClassId;
    }

    public Integer getCreatorUserId() {
        return creatorUserId;
    }

    public void setCreatorUserId(Integer creatorUserId) {
        this.creatorUserId = creatorUserId;
    }

    public Integer getActiveRecord() {
        return activeRecord;
    }

    public void setActiveRecord(Integer activeRecord) {
        this.activeRecord = activeRecord;
    }

    public String getPatientClassCode() {
        return patientClassCode;
    }

    public void setPatientClassCode(String patientClassCode) {
        this.patientClassCode = patientClassCode;
    }

    public String getPatientClassDesc() {
        return patientClassDesc;
    }

    public void setPatientClassDesc(String patientClassDesc) {
        this.patientClassDesc = patientClassDesc;
    }

    public String getGrouperType() {
        return grouperType;
    }

    public void setGrouperType(String grouperType) {
        this.grouperType = grouperType;
    }

    public Integer getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
    }
}
