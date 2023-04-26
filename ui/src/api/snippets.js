import request from '@/utils/request'

export function getAllSnippets() {
    return request({
        url: '/snippets',
        method: 'get'
    })
}

export function getSnippetById(id) {
    return request({
        url: `/snippets/${id}`,
        method: 'get'
    })
}

export function createSnippet(data) {
    return request({
        url: '/snippets',
        method: 'post',
        data: data
    })
}

export function updateSnippet(id, data) {
    return request({
        url: `/snippets/${id}`,
        method: 'put',
        data: data
    })
}

export function deleteSnippet(id) {
    return request({
        url: `/snippets/${id}`,
        method: 'delete'
    })
}
