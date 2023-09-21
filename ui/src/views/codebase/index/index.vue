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

<script lang="ts">
import PageHeader from '@/views/common/page-header.vue';
import PageContent from '@/views/common/page-content.vue';
import PageFooter from '@/views/common/page-footer.vue';
import ToolBar from '@/views/common/tool-bar.vue';
import Search from '@/views/codebase/comps/search.vue';
import { Component, Vue, Watch } from 'vue-property-decorator';
import Client from '@/request/client';

@Component({
    components: {
        Search,
        PageContent,
        PageHeader,
        PageFooter,
        ToolBar,
    },
})
export default class IndexView extends Vue {
    private articles: any[] = [
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
    ];

    private queryOptions: Record<string, any> = {};
    private pageNo = 1;
    private pageSize = 10;
    private total = 0;

    @Watch('$route')
    onRouteChanged(new_route: any) {
        this.populateQueryParam(new_route);
        this.fetchListAccordingToQueryOptions();
    }

    mounted() {
        // Add the event listener to the document when the component is mounted
        document.addEventListener('keydown', this.handleKeyDown);
    }

    handleKeyDown(event) {
        // Check if 'Ctrl' (or 'Cmd' for Mac) and 'K' are pressed together
        if ((event.ctrlKey || event.metaKey) && event.key === 'k') {
            event.preventDefault();

            let searchInput = document.getElementById(
                'searchInput',
            ) as HTMLElement;
            searchInput.focus();
        }
    }

    beforeDestroy() {
        // Remove the event listener from the document before the component is destroyed
        document.removeEventListener('keydown', this.handleKeyDown);
    }

    created() {
        this.populateQueryParam(this.$route);
        this.fetchListAccordingToQueryOptions();
    }

    fetchListAccordingToQueryOptions() {
        Client.listArticles({
            pageNo: this.queryOptions.pageNo,
            pageSize: this.queryOptions.pageSize,
            title: this.queryOptions.title,
        }).then((resp) => {
            this.articles = resp.data.records;
            this.total = resp.data.total;
        });
    }

    rerenderQueryParamAndReenterThePage() {
        let query = this.concatQuerys();
        this.$router.push(`/index${query}`);
    }

    populateQueryParam(route) {
        var query = route.query;
        var title = query.title;
        if (title != undefined) {
            this.queryOptions.title = title;
        }
        var pageNo = query.pageNo;
        if (pageNo != undefined) {
            this.queryOptions.pageNo = pageNo;
        }

        var pageSize = query.pageSize;

        if (pageSize != undefined) {
            this.queryOptions.pageSize = pageSize;
        }
    }

    concatQuerys() {
        var arr: any[] = [];
        var title = this.queryOptions.title;
        if (title != undefined) {
            arr.push(`title=${title}`);
        }
        var pageNo = this.queryOptions.pageNo;
        if (pageNo != undefined) {
            arr.push(`pageNo=${pageNo}`);
        }

        var pageSize = this.queryOptions.pageSize;

        if (pageSize != undefined) {
            arr.push(`pageSize=${pageSize}`);
        }

        if (arr.length != 0) {
            let querys = `?${arr.join('&')}`;
            return querys;
        }

        return '';
    }

    addSnippet() {
        Client.createArticle({ title: 'Untitled' }).then((resp) => {
            var articlehandle = resp.data;
            // @ts-ignore
            this.$notify({
                type: 'success',
                message: 'Create article successfully!',
            });
            this.$router.push({
                path: '/index/edit',
                query: { mode: 'edit', articleId: articlehandle },
            });
        });
    }

    pageChange(newPageno) {
        this.pageNo = newPageno;
        this.queryOptions.pageNo = newPageno;
        this.queryOptions.pageSize = this.pageSize;
        this.rerenderQueryParamAndReenterThePage();
    }

    search(data) {
        this.queryOptions = data;
        this.queryOptions.pageNo = 1;
        this.queryOptions.pageSize = this.pageSize;
        this.rerenderQueryParamAndReenterThePage();
    }

    info(article) {
        this.$router.push({
            path: '/index/edit',
            query: { articleId: article.id, mode: 'edit' },
        });
    }

    // delete article
    remove(article) {
        this.$confirm('Are you sure to delete this article?', 'Warning', {
            confirmButtonText: 'OK',
            cancelButtonText: 'Cancel',
            type: 'warning',
        })
            .then(() => {
                Client.deleteArticle(article.id).then((resp) => {
                    this.articles = this.articles.filter(
                        (s) => s.id !== article.id,
                    );
                    // @ts-ignore
                    this.$notify({
                        type: 'success',
                        message: 'Delete article successfully!',
                    });
                });
            })
            .catch(() => {
                this.$message({
                    type: 'info',
                    message: 'Delete canceled',
                });
            });
    }
}
</script>

<style lang="scss">
@import '~@/style/common-style';
</style>
