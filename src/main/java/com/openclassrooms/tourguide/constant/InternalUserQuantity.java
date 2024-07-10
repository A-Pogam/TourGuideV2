package com.openclassrooms.tourguide.constant;

public class InternalUserQuantity {

	// Set this default up to 100,000 for testing
	private static int INTERNAL_USER_QUANTITY = 1000;

	public static void setInternalUserQuantity(int internalUserQuantity) {
		InternalUserQuantity.INTERNAL_USER_QUANTITY = internalUserQuantity;
	}

	public static int getInternalUserQuantity() {
		return INTERNAL_USER_QUANTITY;
	}
}