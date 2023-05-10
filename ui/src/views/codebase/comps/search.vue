<!--  -->
<template>
    <div class="flex">
        <el-input v-model="title" placeholder="title"></el-input>
        <el-select v-model="lang">
            <el-option value="" label="All"></el-option>
            <el-option value="javascript" label="javascript">
                javascript
            </el-option>
            <el-option value="java" label="java">java</el-option>
            <el-option value="kotlin" label="kotlin">kotlin</el-option>
        </el-select>
        <el-button @click="search">search</el-button>
    </div>
</template>

<script>
export default {
    components: {},
    data() {
        return { title: '', lang: '' }
    },
    computed: {},
    watch: {
        /**
         * handle changes when re-entering the page, mainly to obtain new paths
         */
        $route: {
            handler(new_route) {
                this.populateQueryParam(new_route)
            },
        },
    },
    created() {
        this.populateQueryParam(this.$route)
    },
    mounted() {},
    beforeCreate() {},
    beforeMount() {},
    beforeUpdate() {},
    updated() {},
    beforeDestroy() {},
    destroyed() {},
    activated() {},
    methods: {
        populateQueryParam(route) {
            var query = route.query
            var title = query.title
            if (title != undefined) {
                this.title = title
            }
        },
        search() {
            if (this.title === '') {
                this.title = undefined
            }

            if (this.lang === '') {
                this.lang = undefined
            }

            this.$emit('search-hit', { title: this.title, lang: this.lang })
        },
    },
}
</script>
<style lang="scss" scoped>
@import '~@/styles/common-style.scss';
</style>
