package com.trackandtrail.model.ecommercev2;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.trackandtrail.common.AuditableBase;
import com.trackandtrail.model.common.StaticParamModel;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "ecomm_product")
@Data
@EqualsAndHashCode(callSuper = false)
public class EcomProduct extends AuditableBase {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_id")
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name = "category_id", nullable = true)
	private StaticParamModel category;

	@Column(name = "product_name")
	private String productName;
	
	private Long vid;
	
//	private Long id;

}
