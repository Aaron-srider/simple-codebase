import router, { asyncRoutes, resetRouter } from './router'
import store from './store'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
import getPageTitle from '@/utils/get-page-title'

NProgress.configure({ showSpinner: false }) // NProgress Configuration

const whiteList = ['/login', '/auth-redirect'] // no redirect whitelist

let user_info

function need_build_routes() {
    if (user_info) {
        return false
    } else {
        return true
    }
}
function fetch_user_info() {
    user_info = {}
}

router.beforeEach(async(to, from, next) => {
    console.log(`global before each, from ${from.path} to ${to.path}`)
    // start progress bar
    NProgress.start()

    // set page title
    document.title = getPageTitle(to.meta.title)

    if (need_build_routes()) {
        fetch_user_info()
        resetRouter()

        store.commit('permission/SET_ROUTES', asyncRoutes)

        router.addRoutes(asyncRoutes)
    }

    if (to.matched.length === 0) {
        next({ path: to.path })
    } else {
        next()
    }

    // determine whether the user has logged in
    // const hasToken = getToken()

    // if (hasToken) {
    //   if (to.path === '/login') {
    //     // if is logged in, redirect to the home page
    //     next({ path: '/' })
    //     NProgress.done() // hack: https://github.com/PanJiaChen/vue-element-admin/pull/2939
    //   } else {
    //     // determine whether the user has obtained his permission roles through getInfo
    //     const hasRoles = store.getters.roles && store.getters.roles.length > 0
    //     if (hasRoles) {
    //       next()
    //     } else {
    //       try {
    //         // get user info
    //         // note: roles must be a object array! such as: ['admin'] or ,['developer','editor']
    //         const { roles } = await store.dispatch('user/getInfo')

    //         // generate accessible routes map based on roles
    //         const accessRoutes = await store.dispatch('permission/generateRoutes', roles)

    //         // dynamically add accessible routes
    //         router.addRoutes(accessRoutes)

    //         // hack method to ensure that addRoutes is complete
    //         // set the replace: true, so the navigation will not leave a history record
    //         next({ ...to, replace: true })
    //       } catch (error) {
    //         // remove token and go to login page to re-login
    //         await store.dispatch('user/resetToken')
    //         Message.error(error || 'Has Error')
    //         next(`/login?redirect=${to.path}`)
    //         NProgress.done()
    //       }
    //     }
    //   }
    // } else {
    //   /* has no token*/

    //   if (whiteList.indexOf(to.path) !== -1) {
    //     // in the free login whitelist, go directly
    //     next()
    //   } else {
    //     // other pages that do not have permission to access are redirected to the login page.
    //     next(`/login?redirect=${to.path}`)
    //     NProgress.done()
    //   }
    // }
})

router.afterEach(() => {
    // finish progress bar
    NProgress.done()
})
