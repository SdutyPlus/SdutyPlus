package com.d205.domain.model.common


data class PagingResult<T>(var page: Int, var totalPage: Int, var result: List<T>) {
}
