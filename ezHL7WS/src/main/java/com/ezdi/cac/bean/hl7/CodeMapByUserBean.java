package com.ezdi.cac.bean.hl7;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.ezdi.cac.constant.table.CodeMapByUserTableConstant;

@Entity
@Table(name = CodeMapByUserTableConstant.TABLE_NAME)
public class CodeMapByUserBean
{
	@Id
	@GeneratedValue
	@Column(name = CodeMapByUserTableConstant.ID)
	private Integer id;

	@Column(name = CodeMapByUserTableConstant.ENCOUNER_ID)
	private Integer encounterId;

	@Column(name = CodeMapByUserTableConstant.DOCUMENT_ID)
	private Integer documentId;

	@Column(name = CodeMapByUserTableConstant.DOCUMENT_TYPE)
	private String documentType;

	@Column(name = CodeMapByUserTableConstant.DOCUMENT_NAME)
	private String documentName;

	@Column(name = CodeMapByUserTableConstant.MASTER_ID)
	private Long masterId;

	@Column(name = CodeMapByUserTableConstant.ACCEPT_REJECT_FLAG)
	private Integer acceptRejectFlag;

	@Column(name = CodeMapByUserTableConstant.CODE_ID)
	private Integer codeId;

	@Column(name = CodeMapByUserTableConstant.CODE)
	private String code;

	@Column(name = CodeMapByUserTableConstant.CODE_DESC)
	private String codeDesc;

	@Column(name = CodeMapByUserTableConstant.CODING_SYSTEM)
	private String codingSystem;

	@Column(name = CodeMapByUserTableConstant.CODE_ORDINALITY)
	private Integer codeOrdinality;

	@Column(name = CodeMapByUserTableConstant.CODE_STATUS_CODE)
	private String codeStatusCode;

	@Column(name = CodeMapByUserTableConstant.IS_PRINCIPAL)
	private Integer isPrincipal;

	@Column(name = CodeMapByUserTableConstant.IS_ADMITTING)
	private Integer isAdmitting;

	@Column(name = CodeMapByUserTableConstant.IS_ACTIVE)
	private Integer isActive;

	@Column(name = CodeMapByUserTableConstant.PROCEDURE_DATE)
	private Date procedureDate;

	@Column(name = CodeMapByUserTableConstant.ANESTHESIA_TYPE)
	private String anesthesiaType;

	@Column(name = CodeMapByUserTableConstant.ANESTHESIA_PROVIDER)
	private String anesthesiaProvider;

	@Column(name = CodeMapByUserTableConstant.ANESTHESIA_TIME)
	private String anesthesiaTime;

	@Column(name = CodeMapByUserTableConstant.CREATOR_USER_ID)
	private Integer creatorUserId;

	@Column(name = CodeMapByUserTableConstant.CREATION_TIME)
	private Date creationTime;

	@Column(name = CodeMapByUserTableConstant.UPDATION_TIME)
	private Date updationTime;

	@Column(name = CodeMapByUserTableConstant.CODE_STATUS)
	private String codeStatus;

	@Column(name = CodeMapByUserTableConstant.ORIGIN_CODE)
	private String originCode;

	@Column(name = CodeMapByUserTableConstant.REASON_ID)
	private int reasonId;

	@Column(name = CodeMapByUserTableConstant.REASON)
	private String reason;

	@Column(name = CodeMapByUserTableConstant.REASON_DETAIL)
	private String reasonDetail;


    @Column(name = CodeMapByUserTableConstant.ROLE_ID)
    private Integer roleId;

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public Integer getEncounterId()
	{
		return encounterId;
	}

	public void setEncounterId(Integer encounterId)
	{
		this.encounterId = encounterId;
	}

	public Integer getDocumentId()
	{
		return documentId;
	}

	public void setDocumentId(Integer documentId)
	{
		this.documentId = documentId;
	}

	public String getDocumentType()
	{
		return documentType;
	}

	public void setDocumentType(String documentType)
	{
		this.documentType = documentType;
	}

	public String getDocumentName()
	{
		return documentName;
	}

	public void setDocumentName(String documentName)
	{
		this.documentName = documentName;
	}

	public Long getMasterId()
	{
		return masterId;
	}

	public void setMasterId(Long masterId)
	{
		this.masterId = masterId;
	}

	public Integer getAcceptRejectFlag()
	{
		return acceptRejectFlag;
	}

	public void setAcceptRejectFlag(Integer acceptRejectFlag)
	{
		this.acceptRejectFlag = acceptRejectFlag;
	}

	public Integer getCodeId()
	{
		return codeId;
	}

	public void setCodeId(Integer codeId)
	{
		this.codeId = codeId;
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public String getCodeDesc()
	{
		return codeDesc;
	}

	public void setCodeDesc(String codeDesc)
	{
		this.codeDesc = codeDesc;
	}

	public String getCodingSystem()
	{
		return codingSystem;
	}

	public void setCodingSystem(String codingSystem)
	{
		this.codingSystem = codingSystem;
	}

	public Integer getCodeOrdinality()
	{
		return codeOrdinality;
	}

	public void setCodeOrdinality(Integer codeOrdinality)
	{
		this.codeOrdinality = codeOrdinality;
	}

	public String getCodeStatusCode()
	{
		return codeStatusCode;
	}

	public void setCodeStatusCode(String codeStatusCode)
	{
		this.codeStatusCode = codeStatusCode;
	}

	public Integer getIsPrincipal()
	{
		return isPrincipal;
	}

	public void setIsPrincipal(Integer isPrincipal)
	{
		this.isPrincipal = isPrincipal;
	}

	public Integer getIsAdmitting()
	{
		return isAdmitting;
	}

	public void setIsAdmitting(Integer isAdmitting)
	{
		this.isAdmitting = isAdmitting;
	}

	public Integer getIsActive()
	{
		return isActive;
	}

	public void setIsActive(Integer isActive)
	{
		this.isActive = isActive;
	}

	public Date getProcedureDate()
	{
		return procedureDate;
	}

	public void setProcedureDate(Date procedureDate)
	{
		this.procedureDate = procedureDate;
	}

	public String getAnesthesiaType()
	{
		return anesthesiaType;
	}

	public void setAnesthesiaType(String anesthesiaType)
	{
		this.anesthesiaType = anesthesiaType;
	}

	public String getAnesthesiaProvider()
	{
		return anesthesiaProvider;
	}

	public void setAnesthesiaProvider(String anesthesiaProvider)
	{
		this.anesthesiaProvider = anesthesiaProvider;
	}

	public String getAnesthesiaTime()
	{
		return anesthesiaTime;
	}

	public void setAnesthesiaTime(String anesthesiaTime)
	{
		this.anesthesiaTime = anesthesiaTime;
	}

	public Integer getCreatorUserId()
	{
		return creatorUserId;
	}

	public void setCreatorUserId(Integer creatorUserId)
	{
		this.creatorUserId = creatorUserId;
	}

	public Date getCreationTime()
	{
		return creationTime;
	}

	public void setCreationTime(Date creationTime)
	{
		this.creationTime = creationTime;
	}

	public Date getUpdationTime()
	{
		return updationTime;
	}

	public void setUpdationTime(Date updationTime)
	{
		this.updationTime = updationTime;
	}

	public String getCodeStatus()
	{
		return codeStatus;
	}

	public void setCodeStatus(String codeStatus)
	{
		this.codeStatus = codeStatus;
	}

	public String getOriginCode()
	{
		return originCode;
	}

	public void setOriginCode(String originCode)
	{
		this.originCode = originCode;
	}

	public int getReasonId()
	{
		return reasonId;
	}

	public void setReasonId(int reasonId)
	{
		this.reasonId = reasonId;
	}

	public String getReason()
	{
		return reason;
	}

	public void setReason(String reason)
	{
		this.reason = reason;
	}

	public String getReasonDetail()
	{
		return reasonDetail;
	}

	public void setReasonDetail(String reasonDetail)
	{
		this.reasonDetail = reasonDetail;
	}

}