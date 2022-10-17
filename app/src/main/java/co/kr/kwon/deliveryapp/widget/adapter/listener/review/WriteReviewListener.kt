package co.kr.kwon.deliveryapp.widget.adapter.listener.review

import co.kr.kwon.deliveryapp.model.review.UnWrittenReviewModel
import co.kr.kwon.deliveryapp.widget.adapter.listener.AdapterListener

interface WriteReviewListener : AdapterListener {

    fun onClick(item: UnWrittenReviewModel)
}