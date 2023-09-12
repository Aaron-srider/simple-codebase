import Vue from 'vue';
import VueRouter, { RouteConfig } from 'vue-router';
import HelloWorld from '@/views/HelloWorld.vue';
import About from '@/views/About.vue';
import EditView from "@/views/codebase/edit/edit.vue";
import IndexView from "@/views/codebase/index/index.vue";
import IndexIndexView from "@/views/codebase/index.vue";
Vue.use(VueRouter);

const routes: Array<RouteConfig> = [
    {
        path: '/',
        redirect: '/index',
    },

    {
        path: '/index',
        component: IndexIndexView,
        name: 'shell',
        children: [
            {
                path: '',
                component: IndexView,
                name: 'codebase_index',
            },
            {
                path: 'edit',
                component: EditView,
                name: 'codebase_edit',
            },
        ],
    },
];

const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes,
});

export default router;
