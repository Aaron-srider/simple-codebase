<template>
    <div>
        <page-header :title="'CodeBase'"></page-header>
        <page-content>
            <tool-bar>
                <el-button @click="addSnippet" type="primary" class="mgr20">
                    <i class="el-icon-plus"></i>
                </el-button>
                <search @search-hit="search" class="mgr20"></search>
                <el-pagination
                    layout="prev, pager, next"
                    :total="total"
                    :current-page="queryOptions.pageNo"
                    @current-change="pageChange"
                ></el-pagination>
            </tool-bar>

            <el-table :data="articles" style="width: 100%">
                <el-table-column
                    prop="title"
                    label="title"
                    width="360"
                ></el-table-column>

                <el-table-column
                    prop="createTime"
                    label="createTime"
                    width="180"
                ></el-table-column>

                <el-table-column
                    prop="updateTime"
                    label="updateTime"
                    width="180"
                ></el-table-column>
                <el-table-column prop="undefined" label="operateion" width="">
                    <template #default="scope">
                        <el-button
                            class="el-icon-info"
                            @click="info(scope.row)"
                        ></el-button>
                        <el-button
                            class="el-icon-delete"
                            @click="remove(scope.row)"
                        ></el-button>
                    </template>
                </el-table-column>
            </el-table>
        </page-content>
        <page-footer></page-footer>
    </div>
</template>
<script>
import PageHeader from '@/views/common/page-header.vue'
import PageContent from '@/views/common/page-content.vue'
import PageFooter from '@/views/common/page-footer.vue'
import ToolBar from '@/views/common/tool-bar.vue'
import { createArticle, deleteArticle, listArticles } from '@/api/article'
import Search from '@/views/codebase/comps/search.vue'
export default {
    components: {
        Search,
        PageContent,
        PageHeader,
        PageFooter,
        ToolBar,
    },
    data() {
        return {
            articles: [
                {
                    id: 1,
                    title: 'title1',
                    lang: 'asdfasdf',
                    createTime: '2020-20-20 11:11:11',
                },
                {
                    id: 2,
                    title: 'title2',
                    lang: 'java',
                    createTime: '2020-20-20 11:11:11',
                },
                {
                    id: 3,
                    title: 'title3',
                    lang: 'java',
                    createTime: '2020-20-20 11:11:11',
                },
            ],
            queryOptions: {},
            pageNo: 1,
            pageSize: 10,
            total: 0,
        }
    },
    watch: {
        /**
         * handle changes when re-entering the page, mainly to obtain new paths
         */
        $route: {
            handler(new_route) {
                this.populateQueryParam(new_route)
                this.fetchListAccordingToQueryOptions()
            },
        },
    },

    created() {
        this.populateQueryParam(this.$route)
        this.fetchListAccordingToQueryOptions()
    },
    methods: {
        fetchListAccordingToQueryOptions() {
            listArticles({
                pageNo: this.queryOptions.pageNo,
                pageSize: this.queryOptions.pageSize,
                title: this.queryOptions.title,
            }).then((resp) => {
                this.articles = resp.data.records
                this.total = resp.data.total
            })
        },
        rerenderQueryParamAndReenterThePage() {
            let query = this.concatQuerys()
            this.$router.push(`/codebase${query}`)
        },
        populateQueryParam(route) {
            var query = route.query
            var title = query.title
            if (title != undefined) {
                this.queryOptions.title = title
            }
            var pageNo = query.pageNo
            if (pageNo != undefined) {
                this.queryOptions.pageNo = pageNo
            }

            var pageSize = query.pageSize

            if (pageSize != undefined) {
                this.queryOptions.pageSize = pageSize
            }
        },
        concatQuerys() {
            var arr = []
            var title = this.queryOptions.title
            if (title != undefined) {
                arr.push(`title=${title}`)
            }
            var pageNo = this.queryOptions.pageNo
            if (pageNo != undefined) {
                arr.push(`pageNo=${pageNo}`)
            }

            var pageSize = this.queryOptions.pageSize

            if (pageSize != undefined) {
                arr.push(`pageSize=${pageSize}`)
            }

            if (arr.length != 0) {
                let querys = `?${arr.join('&')}`
                return querys
            }

            return ''
        },
        addSnippet() {
            createArticle({ title: 'Untitled' }).then((resp) => {
                var articlehandle = resp.data
                this.$notify({
                    type: 'success',
                    message: 'Create article successfully!',
                })
                this.$router.push({
                    path: '/codebase/edit',
                    query: { mode: 'edit', articleId: articlehandle },
                })
            })
        },
        pageChange(newPageno) {
            this.pageNo = newPageno
            this.queryOptions.pageNo = newPageno
            this.queryOptions.pageSize = this.pageSize
            this.rerenderQueryParamAndReenterThePage()
        },
        search(data) {
            this.queryOptions = data
            this.queryOptions.pageNo = 1
            this.queryOptions.pageSize = this.pageSize
            this.rerenderQueryParamAndReenterThePage()
        },
        info(article) {
            this.$router.push({
                path: '/codebase/edit',
                query: { articleId: article.id, mode: 'edit' },
            })
        },
        // delete article
        remove(article) {
            this.$confirm('Are you sure to delete this article?', 'Warning', {
                confirmButtonText: 'OK',
                cancelButtonText: 'Cancel',
                type: 'warning',
            })
                .then(() => {
                    deleteArticle(article.id).then((resp) => {
                        this.articles = this.articles.filter(
                            (s) => s.id !== article.id,
                        )
                        this.$notify({
                            type: 'success',
                            message: 'Delete article successfully!',
                        })
                    })
                })
                .catch(() => {
                    this.$message({
                        type: 'info',
                        message: 'Delete canceled',
                    })
                })
        },
    },
}
</script>
<style lang="scss">
@import '~@/styles/common-style.scss';
</style>
