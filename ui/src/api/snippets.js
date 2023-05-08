import request from '@/utils/request'

export function exchangeOrder(exchangeOrderRequest) {
    return request({
        url: '/snippet/order/exchange',
        method: 'put',
        data: exchangeOrderRequest
    })
}
