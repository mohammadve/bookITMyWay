package com.virtual.customervendor.model.response


class FollowUnfollowResponse (
        val message: String,
        val status: Int) {
    override fun toString(): String {
        return "EventListingResponse(message='$message', status=$status)"
    }
}
