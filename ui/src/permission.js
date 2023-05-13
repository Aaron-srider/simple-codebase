import router, { asyncRoutes, resetRouter } from './router'
import store from './store'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
import getPageTitle from '@/utils/get-page-title'

NProgress.configure({ showSpinner: false }) // NProgress Configuration

router.beforeEach(async(to, from, next) => {
    console.log(`global before each, from ${from.path} to ${to.path}`)
    // start progress bar
    NProgress.start()

    // set page title
    document.title = getPageTitle(to.meta.title)

    store.commit('permission/SET_ROUTES', asyncRoutes)
    if (to.matched.length === 0) {
        next({ path: to.path })
    } else {
        next()
    }
})

router.afterEach(() => {
    // finish progress bar
    NProgress.done()
})
