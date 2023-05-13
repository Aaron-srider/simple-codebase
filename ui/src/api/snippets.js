import request from '@/utils/request'

export function exchangeOrder(exchangeOrderRequest) {
    return request({
        url: '/snippet/order/exchange',
        method: 'put',
        data: exchangeOrderRequest
    })
}

export function updateLanguageForSnippet(snippetId, lang) {
    return request({
        url: `/snippet/${snippetId}`,
        method: 'put',
        data: { lang }
    })
}
