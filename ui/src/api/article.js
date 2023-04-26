import request from '@/utils/request'

export function updateConfig(key, value) {
    return request({
        url: '/config',
        method: 'put',
        params: {
            key,
            value
        }
    })
}
