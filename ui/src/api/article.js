import request from '@/utils/request'

export function createArticle(createArticleRequest) {
    return request({
        url: '/article',
        method: 'post',
        data: createArticleRequest,
    })
}

export function deleteArticle(articleId) {
    return request({
        url: `/article/${articleId}`,
        method: 'delete',
    })
}

export function updateArticle(articleId, updateArticleRequest) {
    return request({
        url: `/article/${articleId}`,
        method: 'put',
        data: updateArticleRequest,
    })
}

export function listArticles(queryArticle) {
    return request({
        url: '/articles',
        method: 'get',
        params: queryArticle,
    })
}

export function listSnippetsForArticle(articleId) {
    return request({
        url: `/article/snippets/${articleId}`,
        method: 'get',
    })
}

export function createSnippet(articleId, createArticleRequest) {
    return request({
        url: `/article/${articleId}/snippet`,
        method: 'post',
        data: createArticleRequest,
    })
}

export function deleteSnippet(snippetId) {
    return request({
        url: `/article/snippet/${snippetId}`,
        method: 'delete',
    })
}
