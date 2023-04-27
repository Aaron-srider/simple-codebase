<template>
    <div>
        <el-button @click="toAddPage">Add Snippet</el-button>
        <search @search-hit="search"></search>
        <el-pagination
            background
            layout="prev, pager, next"
            :total="10"
            @current-change="pageChange"
        ></el-pagination>
        <el-table :data="snippets" style="width: 100%">
            <el-table-column
                prop="title"
                label="title"
                width="180"
            ></el-table-column>

            <el-table-column
                prop="lang"
                label="lang"
                width="180"
            ></el-table-column>

            <el-table-column
                prop="createTime"
                label="createTime"
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
import { getAllSnippets, deleteSnippet } from '@/api/snippets'
import Search from '@/views/codebase/comps/search.vue'
export default {
    components: {
        Search,
    },
    data() {
        return {
            snippets: [
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
        getAllSnippets().then((resp) => {
            this.snippets = resp.data.records
            this.total = resp.data.total
        })
    },
    methods: {
        toAddPage() {
            this.$router.push({
                path: '/codebase/edit',
                query: { mode: 'create' },
            })
        },
        pageChange(newPageno) {
            this.pageNo = newPageno
            this.queryOptions.pageNo = newPageno
            this.queryOptions.pageSize = this.pageSize
            getAllSnippets(this.queryOptions).then((resp) => {
                this.snippets = resp.data.records
                this.total = resp.data.total
            })
        },
        search(data) {
            this.queryOptions = data
            this.queryOptions.pageNo = this.pageNo
            this.queryOptions.pageSize = this.pageSize
            getAllSnippets(this.queryOptions).then((resp) => {
                this.snippets = resp.data.records
                this.total = resp.data.total
            })
        },
        info(snippet) {
            this.$router.push({
                path: '/codebase/edit',
                query: { snippetId: snippet.id, mode: 'edit' },
            })
        },
        // delete snippet
        remove(snippet) {
            this.$confirm('Are you sure to delete this snippet?', 'Warning', {
                confirmButtonText: 'OK',
                cancelButtonText: 'Cancel',
                type: 'warning',
            })
                .then(() => {
                    deleteSnippet(snippet.id).then((resp) => {
                        this.snippets = this.snippets.filter(
                            (s) => s.id !== snippet.id,
                        )
                        this.$notify({
                            type: 'success',
                            message: 'Delete snippet successfully!',
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
<style lang="scss">@import '~@/styles/common-style.scss';</style>
