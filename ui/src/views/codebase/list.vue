<template>
    <div>
        hello
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
                </template>
            </el-table-column>
        </el-table>
    </div>
</template>
<script>
import { getAllSnippets } from '@/api/snippets'
export default {
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
        }
    },
    created() {
        getAllSnippets().then((resp) => {
            this.snippets = resp.data
        })
    },
    methods: {
        info(snippet) {
            this.$router.push({
                path: '/codebase/edit',
                query: { snippetId: snippet.id },
            })
        },
    },
}
</script>
<style lang="scss"></style>
