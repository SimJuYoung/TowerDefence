package com.seva.towerdefence.models.tower;

public enum TowerType {
	RED(100, 200, 300),	YELLOW(150, 250, 400), BLUE(150, 250, 400), BLACK(200, 350, 500);

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
