package com.smallrhino.junruyi;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

public class Generator {
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Schema schema = new Schema(1, "com.junruyi.entities");
		schema.setDefaultJavaPackageDao("com.junruyi.dao");
		schema.enableKeepSectionsByDefault();
		addData(schema);
		new DaoGenerator().generateAll(schema, "../JunRuYi/src-models");
	}

	private static void addData(Schema schema) {
		/****添加设备*******/
		Entity equipMentEntity = schema.addEntity("EquipMent");
		equipMentEntity.addIdProperty().autoincrement();
		equipMentEntity.addStringProperty("equipMentAddress");
		equipMentEntity.addStringProperty("equipMentName");
		equipMentEntity.addIntProperty("equipMentLogo");

		/****添加Wifi*******/
		Entity wifiEntity = schema.addEntity("Wifi");
		wifiEntity.addIdProperty().autoincrement();
		wifiEntity.addStringProperty("wifiName");
		wifiEntity.addStringProperty("bssid");

		/****添加丢失信息*******/
		Entity locationEntity = schema.addEntity("Location");
		locationEntity.addIdProperty().autoincrement();
		locationEntity.addStringProperty("name");//设备名称
		locationEntity.addStringProperty("latitude");//纬度
		locationEntity.addStringProperty("longtitude");//经度
		locationEntity.addStringProperty("address");
		locationEntity.addDateProperty("losetime");
		//设备外键
//		Property location_equipment_id = locationEntity.addStringProperty("EquipMentID").notNull().getProperty();
//		locationEntity.addToOne(equipMentEntity, location_equipment_id);
//		ToMany equipMentToLocation = equipMentEntity.addToMany(locationEntity, location_equipment_id);
//		equipMentToLocation.setName("locationList");
	}
}
