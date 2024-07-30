package com.openclassrooms.tourguide.constant;

/**
 * A class that holds the quantity of internal users used for testing in the TourGuide application.
 */
public class InternalUserQuantity {

	/**
	 * The default number of internal users used for testing.
	 * This value can be adjusted as needed.
	 */
	private static int INTERNAL_USER_QUANTITY = 1000;

	/**
	 * Sets the quantity of internal users for testing.
	 *
	 * @param internalUserQuantity the number of internal users to set
	 */
	public static void setInternalUserQuantity(int internalUserQuantity) {
		InternalUserQuantity.INTERNAL_USER_QUANTITY = internalUserQuantity;
	}

	/**
	 * Gets the current quantity of internal users for testing.
	 *
	 * @return the number of internal users
	 */
	public static int getInternalUserQuantity() {
		return INTERNAL_USER_QUANTITY;
	}
}