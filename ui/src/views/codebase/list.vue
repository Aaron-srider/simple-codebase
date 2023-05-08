<template>
    <div>
        <el-button @click="addSnippet">Add Snippet</el-button>
        <search @search-hit="search"></search>
        <el-pagination
            background
            layout="prev, pager, next"
            :total="total"
            @current-change="pageChange"
        ></el-pagination>
        <el-table :data="articles" style="width: 100%">
            <el-table-column
                prop="title"
                label="title"
                width="180"
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
            <el-table-column prop="undefined" label="operateion" width="180">
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
    </div>
</template>
<script>
import { createArticle, deleteArticle, listArticles } from '@/api/article'
import Search from '@/views/codebase/comps/search.vue'
export default {
    components: {
        Search,
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
    created() {
        listArticles().then((resp) => {
            this.articles = resp.data.records
            this.total = resp.data.total
        })
    },
    methods: {
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
            listArticles(this.queryOptions).then((resp) => {
                this.articles = resp.data.records
                this.total = resp.data.total
            })
        },
        search(data) {
            this.queryOptions = data
            this.queryOptions.pageNo = 1
            this.queryOptions.pageSize = this.pageSize
            listArticles(this.queryOptions).then((resp) => {
                this.articles = resp.data.records
                this.total = resp.data.total
            })
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
