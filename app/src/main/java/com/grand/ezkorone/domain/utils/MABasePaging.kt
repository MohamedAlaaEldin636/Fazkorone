package com.grand.ezkorone.domain.utils

import com.google.gson.annotations.SerializedName

/*
        "data": [
            {
                "id": 1,
                "provider_id": 3,
                "order_number": "620bb25b1d031",
                "date": "25 February",
                "time": "12:00 AM",
                "created_at": "2022-02-15T14:02:03.000000Z",
                "past_24_hours": false,
                "cancellation_fees": 0.12,
                "provider": {
                    "id": 3,
                    "name": "Eman",
                    "average_rate": 0,
                    "image": "https://samee.my-staff.net/uploads/users/1644930048620ba4005680c.jpg"
                }
            }
        ],
 */
data class MABasePaging<T>(
	val data: List<T>?,
	
	@SerializedName("current_page") val currentPage: Int,
	
	@SerializedName("next_page_url") val nextPageUrl: String?,
	/** "first_page_url": "https://samee.my-staff.net/api/v1/orders?page=1" */
	@SerializedName("first_page_url") val firstPageUrl: String?,
	
	@SerializedName("last_page") val lastPage: Int,
	
	/** 1 is the start number ex. index + 1 as doesn't start from 0 */
	@SerializedName("from") val fromInPage: Int,
	/** 1 */
	@SerializedName("to") val toInPage: Int,
	/** 1 */
	@SerializedName("total") val totalNumberOfItems: Int,
	/** 20 */
	@SerializedName("per_page") val numOfItemsPerPage: Int,
)
