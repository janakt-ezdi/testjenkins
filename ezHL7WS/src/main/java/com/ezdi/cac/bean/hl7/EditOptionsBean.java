package com.ezdi.cac.bean.hl7;

import com.ezdi.cac.constant.table.EditOptionsTableConstant;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Satyam.S on 7/10/2015.
 */
@Entity
@Table(name = EditOptionsTableConstant.TABLE_NAME)
public class EditOptionsBean {


    @Id
    @GeneratedValue
    @Column(name = EditOptionsTableConstant.EDITOPTIONSID)
    private Integer editOptionsId;

    @Column(name = EditOptionsTableConstant.APPLY_DRG_EDITS)
    private Integer applyDRGEdits;

    @Column(name = EditOptionsTableConstant.APPLY_MUE_EDITS)
    private Integer applyMUEEdits;

    @Column(name = EditOptionsTableConstant.APPLY_RAC_EDITS)
    private Integer applyRACEdits;

    @Column(name = EditOptionsTableConstant.APPLY_TRUCODE_EDITS)
    private Integer applyTruCodeEdits;

    @Column(name = EditOptionsTableConstant.APPLY_E_CODE_EDITS)
    private Integer applyECodeEdits;

    @Column(name = EditOptionsTableConstant.APPLY_E_CODE_PLACE_OF_OCCURRENCE_EDITS)
    private Integer applyECodePlaceOfOccurrenceEdits;

    @Column(name = EditOptionsTableConstant.ENCOUNTERID)
    private Integer encounterId;

    @Column(name = EditOptionsTableConstant.UPDATEDTS)
    private Date updateDTS;

    @Column(name = EditOptionsTableConstant.CREATORDTS)
    private Date creatorDTS;

    @Column(name = EditOptionsTableConstant.UPDATEUSERID)
    private Integer updateUserId;

    @Column(name = EditOptionsTableConstant.PATIENTCLASSID)
    private Integer patientClassId;

    @Column(name = EditOptionsTableConstant.CREATORUSERID)
    private Integer creatorUserId;

    @Column(name = EditOptionsTableConstant.ACTIVERECORD)
    private Integer activeRecord;


    @Column(name = EditOptionsTableConstant.PATIENTCLASSCODE)
    private String patientClassCode;

    @Column(name = EditOptionsTableConstant.PATIENTCLASSDESC)
    private String patientClassDesc;

    @Column(name = EditOptionsTableConstant.GROUPER_TYPE)
    private String grouperType;

    @Column(name = EditOptionsTableConstant.ISDEFAULT, nullable = false, columnDefinition = "int default 0")
    private Integer isDefault = 0;


    public Integer getEditOptionsId() {
        return editOptionsId;
    }

    public void setEditOptionsId(Integer editOptionsId) {
        this.editOptionsId = editOptionsId;
    }

    public Integer getApplyDRGEdits() {
        return applyDRGEdits;
    }

    public void setApplyDRGEdits(Integer applyDRGEdits) {
        this.applyDRGEdits = applyDRGEdits;
    }

    public Integer getApplyMUEEdits() {
        return applyMUEEdits;
    }

    public void setApplyMUEEdits(Integer applyMUEEdits) {
        this.applyMUEEdits = applyMUEEdits;
    }

    public Integer getApplyRACEdits() {
        return applyRACEdits;
    }

    public void setApplyRACEdits(Integer applyRACEdits) {
        this.applyRACEdits = applyRACEdits;
    }

    public Integer getApplyTruCodeEdits() {
        return applyTruCodeEdits;
    }

    public void setApplyTruCodeEdits(Integer applyTruCodeEdits) {
        this.applyTruCodeEdits = applyTruCodeEdits;
    }

    public Integer getApplyECodeEdits() {
        return applyECodeEdits;
    }

    public void setApplyECodeEdits(Integer applyECodeEdits) {
        this.applyECodeEdits = applyECodeEdits;
    }

    public Integer getApplyECodePlaceOfOccurrenceEdits() {
        return applyECodePlaceOfOccurrenceEdits;
    }

    public void setApplyECodePlaceOfOccurrenceEdits(Integer applyECodePlaceOfOccurrenceEdits) {
        this.applyECodePlaceOfOccurrenceEdits = applyECodePlaceOfOccurrenceEdits;
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
