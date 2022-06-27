package com.github.didahdx.githubapp.common

object Constants {
    const val DEFAULT_ORDER_BY = "desc"
    const val GITHUB_END_POINT = "https://api.github.com/"
    const val DEFAULT_SORT_GITHUB = "pushed"
    const val PAGE_SIZE = 40
    private const val PAGE_PREFETCHED_SIZE = 3 //should not be lower than 3
    const val PAGE_MAX_SIZE = PAGE_SIZE * PAGE_PREFETCHED_SIZE
    const val FIRST_PAGE = 1

}