package com.seva.towerdefence.models;

public enum TowerType {
	RED(100, 200, 300);

	private int price;
	private int upgradePrice;
	private int finalUpgradePrice;

	TowerType(int price, int upgradePrice, int finalUpgradePrice) {
		this.price = price;
		this.upgradePrice = upgradePrice;
		this.finalUpgradePrice = finalUpgradePrice;
	}

	public int getPrice() {
		return price;
	}

	public int getUpgradePrice() {
		return upgradePrice;
	}

	public int getFinalUpgradePrice() {
		return finalUpgradePrice;
	}
}
